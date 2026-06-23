package com.taskflow.task;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acesso à persistência de {@link Task}. O Spring Data JPA fornece o CRUD básico.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
}
