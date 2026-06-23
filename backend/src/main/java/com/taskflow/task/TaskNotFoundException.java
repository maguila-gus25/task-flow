package com.taskflow.task;

/**
 * Lançada quando uma tarefa não é encontrada. Mapeada para HTTP 404 pelo handler global.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Tarefa não encontrada: " + id);
    }
}
