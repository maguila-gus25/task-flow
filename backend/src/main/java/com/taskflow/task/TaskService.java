package com.taskflow.task;

import com.taskflow.task.dto.CreateTaskRequest;
import com.taskflow.task.dto.TaskResponse;
import com.taskflow.task.dto.UpdateTaskRequest;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regra de negócio do CRUD de tarefas. Converte entre entidade e DTOs.
 */
@Service
@Transactional
public class TaskService {

    private static final int DAYS_PER_WEEK = 7;

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return repository.findAll().stream().map(TaskResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse findById(Long id) {
        return TaskResponse.from(getOrThrow(id));
    }

    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task(request.title(), request.description(), false);
        applyRecurrence(task, request.dueDate(), request.recurrenceFrequency(), request.recurrenceInterval(),
                request.recurrenceDaysOfWeek(), request.recurrenceEndDate());
        return TaskResponse.from(repository.save(task));
    }

    public TaskResponse update(Long id, UpdateTaskRequest request) {
        Task task = getOrThrow(id);
        boolean wasPending = !task.isDone();

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDone(request.done());
        applyRecurrence(task, request.dueDate(), request.recurrenceFrequency(), request.recurrenceInterval(),
                request.recurrenceDaysOfWeek(), request.recurrenceEndDate());

        Task saved = repository.save(task);

        if (wasPending && saved.isDone() && saved.isRecurring()) {
            createNextOccurrence(saved);
        }

        return TaskResponse.from(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private void applyRecurrence(Task task, Instant dueDate, RecurrenceFrequency frequency, Integer interval,
            Set<DayOfWeek> daysOfWeek, Instant endDate) {
        task.setDueDate(dueDate);
        task.setRecurrenceFrequency(frequency == null ? RecurrenceFrequency.NONE : frequency);
        task.setRecurrenceInterval(interval == null ? 1 : interval);
        task.setRecurrenceDaysOfWeek(daysOfWeek);
        task.setRecurrenceEndDate(endDate);
    }

    private void createNextOccurrence(Task completed) {
        Instant nextDueDate = calculateNextDueDate(completed);
        if (nextDueDate == null) {
            return;
        }
        if (completed.getRecurrenceEndDate() != null && nextDueDate.isAfter(completed.getRecurrenceEndDate())) {
            return;
        }

        Task next = new Task(completed.getTitle(), completed.getDescription(), false);
        next.setDueDate(nextDueDate);
        next.setRecurrenceFrequency(completed.getRecurrenceFrequency());
        next.setRecurrenceInterval(completed.getRecurrenceInterval());
        next.setRecurrenceDaysOfWeek(completed.getRecurrenceDaysOfWeek());
        next.setRecurrenceEndDate(completed.getRecurrenceEndDate());
        repository.save(next);
    }

    /**
     * Calcula a próxima data de vencimento a partir da recorrência configurada na tarefa concluída.
     */
    private Instant calculateNextDueDate(Task task) {
        Instant base = task.getDueDate() != null ? task.getDueDate() : Instant.now();
        int interval = Math.max(task.getRecurrenceInterval(), 1);

        return switch (task.getRecurrenceFrequency()) {
            case DAILY -> base.plus(interval, ChronoUnit.DAYS);
            case WEEKLY -> nextWeeklyOccurrence(base, task.getRecurrenceDaysOfWeek(), interval);
            case MONTHLY -> base.atZone(ZoneOffset.UTC).plusMonths(interval).toInstant();
            case YEARLY -> base.atZone(ZoneOffset.UTC).plusYears(interval).toInstant();
            case NONE -> null;
        };
    }

    /**
     * Com mais de um dia da semana marcado (padrão do Apple Lembretes, ex: seg/qua/sex), avança para o
     * próximo dia marcado dentro da semana em vez de aplicar o intervalo em semanas cheias.
     */
    private Instant nextWeeklyOccurrence(Instant base, Set<DayOfWeek> daysOfWeek, int interval) {
        if (daysOfWeek == null || daysOfWeek.size() <= 1) {
            return base.plus((long) DAYS_PER_WEEK * interval, ChronoUnit.DAYS);
        }

        ZonedDateTime current = base.atZone(ZoneOffset.UTC);
        for (int offset = 1; offset <= DAYS_PER_WEEK; offset++) {
            ZonedDateTime candidate = current.plusDays(offset);
            if (daysOfWeek.contains(candidate.getDayOfWeek())) {
                return candidate.toInstant();
            }
        }
        return base.plus((long) DAYS_PER_WEEK * interval, ChronoUnit.DAYS);
    }

    private Task getOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }
}
