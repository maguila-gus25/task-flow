package com.taskflow.task;

import com.taskflow.task.dto.CreateTaskRequest;
import com.taskflow.task.dto.TaskResponse;
import com.taskflow.task.dto.UpdateTaskRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regra de negócio do CRUD de tarefas. Converte entre entidade e DTOs.
 */
@Service
@Transactional
public class TaskService {

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
        return TaskResponse.from(repository.save(task));
    }

    public TaskResponse update(Long id, UpdateTaskRequest request) {
        Task task = getOrThrow(id);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDone(request.done());
        return TaskResponse.from(repository.save(task));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private Task getOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }
}
