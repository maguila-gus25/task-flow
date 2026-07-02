package com.taskflow.task.dto;

import com.taskflow.task.RecurrenceFrequency;
import com.taskflow.task.Task;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.Set;

/**
 * Representação de uma tarefa retornada pela API (não expõe a entidade JPA diretamente).
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        boolean done,
        Instant dueDate,
        RecurrenceFrequency recurrenceFrequency,
        int recurrenceInterval,
        Set<DayOfWeek> recurrenceDaysOfWeek,
        Instant recurrenceEndDate) {

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isDone(),
                task.getDueDate(),
                task.getRecurrenceFrequency(),
                task.getRecurrenceInterval(),
                task.getRecurrenceDaysOfWeek(),
                task.getRecurrenceEndDate());
    }
}
