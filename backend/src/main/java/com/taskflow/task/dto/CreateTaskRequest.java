package com.taskflow.task.dto;

import com.taskflow.task.RecurrenceFrequency;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.Set;

/**
 * Dados de entrada para criar uma tarefa. Uma tarefa nasce sempre como pendente.
 */
public record CreateTaskRequest(
        @NotBlank(message = "title não pode ser vazio")
        @Size(max = 120, message = "title deve ter no máximo 120 caracteres")
        String title,

        @Size(max = 1000, message = "description deve ter no máximo 1000 caracteres")
        String description,

        Instant dueDate,

        RecurrenceFrequency recurrenceFrequency,

        Integer recurrenceInterval,

        Set<DayOfWeek> recurrenceDaysOfWeek,

        Instant recurrenceEndDate) {

    @AssertTrue(message = "recurrenceInterval deve ser maior ou igual a 1 quando a tarefa é recorrente")
    public boolean isRecurrenceIntervalValid() {
        return recurrenceFrequency == null
                || recurrenceFrequency == RecurrenceFrequency.NONE
                || recurrenceInterval == null
                || recurrenceInterval >= 1;
    }

    @AssertTrue(message = "recurrenceDaysOfWeek não pode ser vazio quando a frequência é semanal")
    public boolean isRecurrenceDaysOfWeekValid() {
        return recurrenceFrequency != RecurrenceFrequency.WEEKLY
                || (recurrenceDaysOfWeek != null && !recurrenceDaysOfWeek.isEmpty());
    }
}
