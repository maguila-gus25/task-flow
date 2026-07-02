import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { TaskList } from './task-list';

describe('TaskList', () => {
  const baseUrl = 'http://localhost:8080/api/tasks';
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskList],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  function createComponent() {
    const fixture = TestBed.createComponent(TaskList);
    fixture.detectChanges();
    httpMock.expectOne(baseUrl).flush([]);
    return fixture;
  }

  it('deve carregar as tarefas ao iniciar', () => {
    const fixture = TestBed.createComponent(TaskList);
    fixture.detectChanges();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 1, title: 'Estudar', done: false }]);

    expect(fixture.componentInstance.tasks()).toHaveLength(1);
  });

  it('não permite enviar Semanalmente sem nenhum dia da semana selecionado', () => {
    const fixture = createComponent();
    const component = fixture.componentInstance;

    component.newTitle = 'Academia';
    component.newRecurrenceFrequency = 'WEEKLY';

    expect(component.canSubmit).toBe(false);

    component.toggleRecurrenceDay('MONDAY');
    expect(component.canSubmit).toBe(true);
  });

  it('add() envia os campos de recorrência no corpo do POST', () => {
    const fixture = createComponent();
    const component = fixture.componentInstance;

    component.newTitle = 'Academia';
    component.newRecurrenceFrequency = 'WEEKLY';
    component.newRecurrenceInterval = 1;
    component.toggleRecurrenceDay('MONDAY');
    component.toggleRecurrenceDay('WEDNESDAY');

    component.add();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body.recurrenceFrequency).toBe('WEEKLY');
    expect(req.request.body.recurrenceDaysOfWeek).toEqual(['MONDAY', 'WEDNESDAY']);
    req.flush({
      id: 2,
      title: 'Academia',
      done: false,
      recurrenceFrequency: 'WEEKLY',
      recurrenceDaysOfWeek: ['MONDAY', 'WEDNESDAY'],
    });
  });

  it('toggle() envia o objeto completo com done invertido e recarrega a lista', () => {
    const fixture = createComponent();
    const component = fixture.componentInstance;
    const task = {
      id: 1,
      title: 'Regar plantas',
      done: false,
      dueDate: '2026-07-01T10:00:00Z',
      recurrenceFrequency: 'DAILY' as const,
      recurrenceInterval: 1,
    };

    component.toggle(task);

    const putReq = httpMock.expectOne(`${baseUrl}/1`);
    expect(putReq.request.method).toBe('PUT');
    expect(putReq.request.body.done).toBe(true);
    expect(putReq.request.body.recurrenceFrequency).toBe('DAILY');
    putReq.flush({ ...task, done: true });

    httpMock.expectOne(baseUrl).flush([]);
  });
});
