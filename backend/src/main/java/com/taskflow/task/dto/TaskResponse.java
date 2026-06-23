package com.taskflow.task.dto;

import com.taskflow.task.Task;

/**
 * Representação de uma tarefa retornada pela API (não expõe a entidade JPA diretamente).
 */
public record TaskResponse(Long id, String title, String description, boolean done) {

    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.isDone());
    }
}
