---
name: testing-patterns
description: Use ao escrever testes no TaskFlow. Fornece padrões e exemplos prontos de JUnit 5 + Mockito (backend Spring Boot) e Jasmine + Karma (frontend Angular).
---

# Testing Patterns — TaskFlow

Exemplos de referência. Siga os padrões de @.claude/rules/testing-standard.md.

## Backend — Service (JUnit 5 + Mockito)

```java
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock TaskRepository repository;
    @InjectMocks TaskService service;

    @Test
    void findById_idInexistente_lancaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void create_dadosValidos_salvaERetorna() {
        var input = new CreateTaskRequest("Estudar", "Spring", false);
        when(repository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.create(input);

        assertEquals("Estudar", result.title());
        verify(repository).save(any(Task.class));
    }
}
```

## Backend — Controller (@WebMvcTest + MockMvc)

```java
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean TaskService service;

    @Test
    void createTask_semTitulo_retorna400() throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\"}"))
            .andExpect(status().isBadRequest());
    }
}
```

## Frontend — Service (HttpClientTestingModule)

```ts
describe('TaskService', () => {
  let service: TaskService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(TaskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('deve listar tarefas via GET /api/tasks', () => {
    service.list().subscribe(tasks => expect(tasks.length).toBe(1));
    const req = httpMock.expectOne('/api/tasks');
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 1, title: 'Estudar', done: false }]);
  });
});
```

## Sempre cubra
Caminho feliz · borda (vazio/limites) · erro (input inválido, recurso ausente, falha HTTP).
