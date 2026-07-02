/** Frequência de repetição de uma tarefa recorrente (espelha o RecurrenceFrequency do backend). */
export type RecurrenceFrequency = 'NONE' | 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'YEARLY';

/** Dia da semana (espelha o java.time.DayOfWeek serializado pelo backend). */
export type DayOfWeek =
  | 'MONDAY'
  | 'TUESDAY'
  | 'WEDNESDAY'
  | 'THURSDAY'
  | 'FRIDAY'
  | 'SATURDAY'
  | 'SUNDAY';

/** Campos de recorrência compartilhados entre request e response. */
export interface RecurrenceFields {
  dueDate?: string;
  recurrenceFrequency?: RecurrenceFrequency;
  recurrenceInterval?: number;
  recurrenceDaysOfWeek?: DayOfWeek[];
  recurrenceEndDate?: string;
}

/** Representa uma tarefa retornada pela API (espelha o TaskResponse do backend). */
export interface Task extends RecurrenceFields {
  id: number;
  title: string;
  description?: string;
  done: boolean;
}

/** Corpo enviado ao criar uma tarefa. */
export interface CreateTaskRequest extends RecurrenceFields {
  title: string;
  description?: string;
}

/** Corpo enviado ao atualizar uma tarefa por completo. */
export interface UpdateTaskRequest extends RecurrenceFields {
  title: string;
  description?: string;
  done: boolean;
}
