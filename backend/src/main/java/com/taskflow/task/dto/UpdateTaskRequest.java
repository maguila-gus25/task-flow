package com.taskflow.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados de entrada para atualizar uma tarefa por completo (título, descrição e status).
 */
public record UpdateTaskRequest(
        @NotBlank(message = "title não pode ser vazio")
        @Size(max = 120, message = "title deve ter no máximo 120 caracteres")
        String title,

        @Size(max = 1000, message = "description deve ter no máximo 1000 caracteres")
        String description,

        boolean done) {
}
