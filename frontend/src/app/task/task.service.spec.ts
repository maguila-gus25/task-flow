import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { TaskService } from './task.service';

describe('TaskService', () => {
  const baseUrl = 'http://localhost:8080/api/tasks';
  let service: TaskService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(TaskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('list() deve fazer GET em /api/tasks', () => {
    service.list().subscribe((tasks) => expect(tasks.length).toBe(1));

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 1, title: 'Estudar', done: false }]);
  });

  it('create() deve fazer POST com o corpo informado', () => {
    service.create({ title: 'Nova tarefa' }).subscribe();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body.title).toBe('Nova tarefa');
    req.flush({ id: 2, title: 'Nova tarefa', done: false });
  });

  it('delete() deve fazer DELETE em /api/tasks/{id}', () => {
    service.delete(5).subscribe();

    const req = httpMock.expectOne(`${baseUrl}/5`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
