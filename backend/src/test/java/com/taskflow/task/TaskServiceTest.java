package com.taskflow.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.taskflow.task.dto.CreateTaskRequest;
import com.taskflow.task.dto.TaskResponse;
import com.taskflow.task.dto.UpdateTaskRequest;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    void create_dadosValidos_salvaERetornaPendente() {
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse result = service.create(
                new CreateTaskRequest("Estudar", "Spring Boot", null, null, null, null, null));

        assertThat(result.title()).isEqualTo("Estudar");
        assertThat(result.done()).isFalse();
        assertThat(result.recurrenceFrequency()).isEqualTo(RecurrenceFrequency.NONE);
        verify(repository).save(any(Task.class));
    }

    @Test
    void findAll_comTarefas_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new Task("A", null, false), new Task("B", null, true)));

        List<TaskResponse> result = service.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_idInexistente_lancaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void update_existente_atualizaCampos() {
        Task existing = new Task("Antigo", "desc", false);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse result = service.update(1L,
                new UpdateTaskRequest("Novo", "nova desc", true, null, null, null, null, null));

        assertThat(result.title()).isEqualTo("Novo");
        assertThat(result.done()).isTrue();
    }

    @Test
    void delete_idInexistente_lancaNotFoundENaoApaga() {
        when(repository.existsById(42L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(42L))
                .isInstanceOf(TaskNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void update_tarefaDiariaConcluida_geraProximaOcorrenciaComUmDiaAMais() {
        Instant dueDate = Instant.parse("2026-07-01T10:00:00Z");
        Task existing = new Task("Regar plantas", null, false);
        existing.setDueDate(dueDate);
        existing.setRecurrenceFrequency(RecurrenceFrequency.DAILY);
        existing.setRecurrenceInterval(1);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.update(1L, new UpdateTaskRequest("Regar plantas", null, true, dueDate,
                RecurrenceFrequency.DAILY, 1, null, null));

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(repository, times(2)).save(captor.capture());
        Task nextOccurrence = captor.getAllValues().get(1);
        assertThat(nextOccurrence.isDone()).isFalse();
        assertThat(nextOccurrence.getDueDate()).isEqualTo(dueDate.plus(1, ChronoUnit.DAYS));
    }

    @Test
    void update_tarefaSemanalComVariosDias_avancaParaProximoDiaMarcado() {
        // 2026-07-01 é uma quarta-feira
        Instant wednesday = Instant.parse("2026-07-01T10:00:00Z");
        Task existing = new Task("Academia", null, false);
        existing.setDueDate(wednesday);
        existing.setRecurrenceFrequency(RecurrenceFrequency.WEEKLY);
        existing.setRecurrenceInterval(1);
        existing.setRecurrenceDaysOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(repository.findById(2L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.update(2L, new UpdateTaskRequest("Academia", null, true, wednesday,
                RecurrenceFrequency.WEEKLY, 1, Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), null));

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(repository, times(2)).save(captor.capture());
        Task nextOccurrence = captor.getAllValues().get(1);
        assertThat(nextOccurrence.getDueDate()).isEqualTo(wednesday.plus(2, ChronoUnit.DAYS));
        assertThat(nextOccurrence.getDueDate().atZone(java.time.ZoneOffset.UTC).getDayOfWeek())
                .isEqualTo(DayOfWeek.FRIDAY);
    }

    @Test
    void update_tarefaRecorrenteAposDataFinal_naoGeraProximaOcorrencia() {
        Instant dueDate = Instant.parse("2026-07-01T10:00:00Z");
        Instant endDate = Instant.parse("2026-07-01T23:59:59Z");
        Task existing = new Task("Tarefa com fim", null, false);
        existing.setDueDate(dueDate);
        existing.setRecurrenceFrequency(RecurrenceFrequency.DAILY);
        existing.setRecurrenceInterval(1);
        existing.setRecurrenceEndDate(endDate);
        when(repository.findById(3L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.update(3L, new UpdateTaskRequest("Tarefa com fim", null, true, dueDate,
                RecurrenceFrequency.DAILY, 1, null, endDate));

        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void update_tarefaNaoRecorrenteConcluida_naoGeraOcorrencia() {
        Task existing = new Task("Tarefa única", null, false);
        when(repository.findById(4L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.update(4L, new UpdateTaskRequest("Tarefa única", null, true, null, null, null, null, null));

        verify(repository, times(1)).save(any(Task.class));
    }
}
