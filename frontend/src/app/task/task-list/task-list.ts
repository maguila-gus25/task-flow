import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DayOfWeek, RecurrenceFrequency, Task } from '../task.model';
import { TaskService } from '../task.service';

/** Opção de frequência de repetição exibida no formulário. */
interface FrequencyOption {
  value: RecurrenceFrequency;
  label: string;
}

/** Opção de dia da semana exibida como chip selecionável. */
interface WeekDayOption {
  value: DayOfWeek;
  label: string;
}

/**
 * Tela principal: lista, cria, alterna status e remove tarefas (com suporte a data de
 * vencimento e recorrência ao estilo Apple Lembretes / Google Tarefas).
 */
@Component({
  selector: 'app-task-list',
  imports: [FormsModule, DatePipe],
  templateUrl: './task-list.html',
  styleUrl: './task-list.scss',
})
export class TaskList implements OnInit {
  private readonly service = inject(TaskService);

  readonly tasks = signal<Task[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  readonly frequencyOptions: FrequencyOption[] = [
    { value: 'NONE', label: 'Não repete' },
    { value: 'DAILY', label: 'Diariamente' },
    { value: 'WEEKLY', label: 'Semanalmente' },
    { value: 'MONTHLY', label: 'Mensalmente' },
    { value: 'YEARLY', label: 'Anualmente' },
  ];

  readonly weekDayOptions: WeekDayOption[] = [
    { value: 'MONDAY', label: 'Seg' },
    { value: 'TUESDAY', label: 'Ter' },
    { value: 'WEDNESDAY', label: 'Qua' },
    { value: 'THURSDAY', label: 'Qui' },
    { value: 'FRIDAY', label: 'Sex' },
    { value: 'SATURDAY', label: 'Sáb' },
    { value: 'SUNDAY', label: 'Dom' },
  ];

  newTitle = '';
  newDescription = '';
  newDueDate = '';
  newRecurrenceFrequency: RecurrenceFrequency = 'NONE';
  newRecurrenceInterval = 1;
  newRecurrenceDaysOfWeek: DayOfWeek[] = [];
  newRecurrenceEndDate = '';

  get canSubmit(): boolean {
    if (!this.newTitle.trim()) {
      return false;
    }
    if (this.newRecurrenceFrequency === 'WEEKLY' && this.newRecurrenceDaysOfWeek.length === 0) {
      return false;
    }
    return true;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.service.list().subscribe({
      next: (tasks) => {
        this.tasks.set(tasks);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Não foi possível carregar as tarefas. O backend está rodando?');
        this.loading.set(false);
      },
    });
  }

  toggleRecurrenceDay(day: DayOfWeek): void {
    this.newRecurrenceDaysOfWeek = this.newRecurrenceDaysOfWeek.includes(day)
      ? this.newRecurrenceDaysOfWeek.filter((d) => d !== day)
      : [...this.newRecurrenceDaysOfWeek, day];
  }

  add(): void {
    const title = this.newTitle.trim();
    if (!title || !this.canSubmit) {
      return;
    }
    const description = this.newDescription.trim() || undefined;
    const recurring = this.newRecurrenceFrequency !== 'NONE';

    this.error.set(null);
    this.service
      .create({
        title,
        description,
        dueDate: this.newDueDate ? new Date(this.newDueDate).toISOString() : undefined,
        recurrenceFrequency: recurring ? this.newRecurrenceFrequency : undefined,
        recurrenceInterval: recurring ? this.newRecurrenceInterval : undefined,
        recurrenceDaysOfWeek:
          recurring && this.newRecurrenceFrequency === 'WEEKLY' ? this.newRecurrenceDaysOfWeek : undefined,
        recurrenceEndDate:
          recurring && this.newRecurrenceEndDate ? new Date(this.newRecurrenceEndDate).toISOString() : undefined,
      })
      .subscribe({
        next: (task) => {
          this.tasks.update((list) => [...list, task]);
          this.resetForm();
        },
        error: () => this.error.set('Não foi possível criar a tarefa.'),
      });
  }

  toggle(task: Task): void {
    this.error.set(null);
    const { id, ...fields } = task;
    this.service.update(id, { ...fields, done: !task.done }).subscribe({
      next: () => this.load(),
      error: () => this.error.set('Não foi possível atualizar a tarefa.'),
    });
  }

  remove(task: Task): void {
    this.error.set(null);
    this.service.delete(task.id).subscribe({
      next: () => this.tasks.update((list) => list.filter((t) => t.id !== task.id)),
      error: () => this.error.set('Não foi possível remover a tarefa.'),
    });
  }

  recurrenceLabel(task: Task): string | null {
    if (!task.recurrenceFrequency || task.recurrenceFrequency === 'NONE') {
      return null;
    }
    const option = this.frequencyOptions.find((o) => o.value === task.recurrenceFrequency);
    return option ? `🔁 ${option.label}` : null;
  }

  private resetForm(): void {
    this.newTitle = '';
    this.newDescription = '';
    this.newDueDate = '';
    this.newRecurrenceFrequency = 'NONE';
    this.newRecurrenceInterval = 1;
    this.newRecurrenceDaysOfWeek = [];
    this.newRecurrenceEndDate = '';
  }
}
