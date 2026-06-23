/** Representa uma tarefa retornada pela API (espelha o TaskResponse do backend). */
export interface Task {
  id: number;
  title: string;
  description?: string;
  done: boolean;
}

/** Corpo enviado ao criar uma tarefa. */
export interface CreateTaskRequest {
  title: string;
  description?: string;
}

/** Corpo enviado ao atualizar uma tarefa por completo. */
export interface UpdateTaskRequest {
  title: string;
  description?: string;
  done: boolean;
}
