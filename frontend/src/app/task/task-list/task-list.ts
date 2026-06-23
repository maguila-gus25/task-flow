import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Task } from '../task.model';
import { TaskService } from '../task.service';

/**
 * Tela principal: lista, cria, alterna status e remove tarefas.
 */
@Component({
  selector: 'app-task-list',
  imports: [FormsModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.scss',
})
export class TaskList implements OnInit {
  private readonly service = inject(TaskService);

  readonly tasks = signal<Task[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  newTitle = '';
  newDescription = '';

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

  add(): void {
    const title = this.newTitle.trim();
    if (!title) {
      return;
    }
    const description = this.newDescription.trim() || undefined;
    this.error.set(null);
    this.service.create({ title, description }).subscribe({
      next: (task) => {
        this.tasks.update((list) => [...list, task]);
        this.newTitle = '';
        this.newDescription = '';
      },
      error: () => this.error.set('Não foi possível criar a tarefa.'),
    });
  }

  toggle(task: Task): void {
    this.error.set(null);
    this.service
      .update(task.id, { title: task.title, description: task.description, done: !task.done })
      .subscribe({
        next: (updated) =>
          this.tasks.update((list) => list.map((t) => (t.id === updated.id ? updated : t))),
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
}
