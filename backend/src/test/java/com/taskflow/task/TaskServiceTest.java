package com.taskflow.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.taskflow.task.dto.CreateTaskRequest;
import com.taskflow.task.dto.TaskResponse;
import com.taskflow.task.dto.UpdateTaskRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

        TaskResponse result = service.create(new CreateTaskRequest("Estudar", "Spring Boot"));

        assertThat(result.title()).isEqualTo("Estudar");
        assertThat(result.done()).isFalse();
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

        TaskResponse result = service.update(1L, new UpdateTaskRequest("Novo", "nova desc", true));

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
}
