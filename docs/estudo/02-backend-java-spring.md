# 📗 Módulo 2 — Backend (Java + Spring Boot)

> Objetivo: entender as **camadas** do backend e seguir uma requisição real, linha a linha.

## 2.1 As camadas (a arquitetura mais importante de saber)

O backend é organizado em **camadas**, cada uma com uma responsabilidade. Isso se chama
**separação de responsabilidades** e é a pergunta nº 1 de entrevista Java/Spring:

```
Requisição HTTP
      │
      ▼
┌──────────────┐   "porteiro": recebe HTTP, valida, devolve HTTP
│  Controller  │   @RestController
└──────┬───────┘
       ▼
┌──────────────┐   "cérebro": regra de negócio, orquestra
│   Service    │   @Service
└──────┬───────┘
       ▼
┌──────────────┐   "porta do banco": salva/busca dados
│  Repository  │   interface JpaRepository
└──────┬───────┘
       ▼
┌──────────────┐   "molde da tabela"
│   Entity     │   @Entity
└──────────────┘
```

**Regra de ouro:** o Controller é **fino** (só lida com HTTP), a lógica fica no **Service**, e o acesso a
dados fica no **Repository**. Misturar isso é o erro clássico de iniciante.

No TaskFlow, tudo da feature de tarefas está em `backend/src/main/java/com/taskflow/task/`.

## 2.2 Entity — o molde da tabela

`backend/src/main/java/com/taskflow/task/Task.java`:

```java
@Entity                                          // ① esta classe vira uma tabela
@Table(name = "tasks")                           // ② nome da tabela
public class Task {
    @Id                                          // ③ chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ④ id auto-incremento
    private Long id;

    @Column(nullable = false, length = 120)      // ⑤ coluna NOT NULL, varchar(120)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean done = false;

    protected Task() { }                         // ⑥ construtor exigido pelo JPA
    public Task(String title, String description, boolean done) { ... }
    // getters e setters
}
```

- **JPA** (Jakarta Persistence API) é a **especificação** de como mapear objetos Java ↔ tabelas.
  O **Hibernate** é a **implementação** que o Spring usa por baixo. Esse mapeamento chama-se **ORM**
  (Object-Relational Mapping).
- Cada **instância** de `Task` = uma **linha** da tabela. Cada **campo** = uma **coluna**.
- O Spring, com `ddl-auto=update`, **cria a tabela sozinho** a partir desta classe (você viu o
  `create table tasks (...)` no log quando rodou).

## 2.3 Repository — a porta do banco

`backend/src/main/java/com/taskflow/task/TaskRepository.java`:

```java
public interface TaskRepository extends JpaRepository<Task, Long> {
}
```

Isso é quase mágico: você **só declara uma interface vazia** e ganha de graça `findAll()`, `findById(id)`,
`save(task)`, `deleteById(id)`, `existsById(id)`, etc. O Spring Data JPA **gera a implementação em tempo de
execução**. `<Task, Long>` diz: "entidade `Task`, cuja chave é do tipo `Long`".

> Detalhe de entrevista: você pode criar **derived queries** só pelo nome do método —
> `List<Task> findByDone(boolean done)` vira o SQL `WHERE done = ?` automaticamente.

## 2.4 DTO — por que não usar a Entity na API

`backend/src/main/java/com/taskflow/task/dto/`:

```java
public record CreateTaskRequest(
    @NotBlank String title,
    @Size(max = 1000) String description) { }

public record TaskResponse(Long id, String title, String description, boolean done) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.isDone());
    }
}
```

- **DTO** = objeto que transporta dados na API, separado da Entity.
- **Por que não expor a Entity direto?** (pergunta de entrevista)
  1. **Segurança** — você controla exatamente quais campos entram/saem (não vaza coluna interna).
  2. **Estabilidade** — mudar a tabela não quebra a API e vice-versa.
  3. **Validação** — o DTO de entrada carrega as regras (`@NotBlank`, `@Size`).
- `record` é uma classe Java **imutável** e enxuta (gera construtor, getters e `equals` sozinho). Perfeito para DTO.

## 2.5 Service — a regra de negócio

`backend/src/main/java/com/taskflow/task/TaskService.java`:

```java
@Service                                  // ① "sou um bean de regra de negócio"
@Transactional                            // ② cada método roda numa transação do banco
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {   // ③ injeção por construtor
        this.repository = repository;
    }

    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task(request.title(), request.description(), false);
        return TaskResponse.from(repository.save(task));
    }

    private Task getOrThrow(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));   // ④ trata "não encontrado"
    }
}
```

- **①** `@Service` marca a classe como um **bean** gerenciado pelo Spring.
- **②** `@Transactional` garante que, se algo falhar no meio, **nada é gravado** (rollback). Em leituras usamos
  `@Transactional(readOnly = true)` (mais eficiente).
- **③ Injeção de Dependência por construtor:** você **não dá `new TaskRepository()`**. Você só declara que
  *precisa* de um `TaskRepository` no construtor, e o Spring **entrega** a instância. Vantagens: fácil de testar
  (passa um mock no teste), e o campo pode ser `final`. **Essa é a pergunta mais importante de Spring.**
- **④** Quando não acha a tarefa, lança exceção — que vira um HTTP 404 (ver 2.7).

## 2.6 Controller — o porteiro HTTP

`backend/src/main/java/com/taskflow/task/TaskController.java`:

```java
@RestController                           // ① controller que devolve JSON
@RequestMapping("/api/tasks")             // ② prefixo de todas as rotas aqui
public class TaskController {

    private final TaskService service;
    public TaskController(TaskService service) { this.service = service; }

    @GetMapping                           // GET /api/tasks
    public List<TaskResponse> list() { return service.findAll(); }

    @GetMapping("/{id}")                  // GET /api/tasks/5
    public TaskResponse getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping                          // POST /api/tasks
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/api/tasks/" + created.id())).body(created);
    }

    @PutMapping("/{id}")                  // PUT /api/tasks/5
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) { ... }

    @DeleteMapping("/{id}")               // DELETE /api/tasks/5
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
```

Anotações-chave (decore estas):
- `@RestController` = `@Controller` + `@ResponseBody` → o retorno vira **JSON** automaticamente.
- `@RequestMapping("/api/tasks")` → prefixo comum.
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` → mapeiam o **verbo HTTP**.
- `@PathVariable Long id` → pega o `{id}` da URL.
- `@RequestBody` → converte o **JSON do corpo** num objeto Java.
- `@Valid` → dispara a **validação** do DTO (`@NotBlank` etc.); se falhar, vira HTTP 400.
- `ResponseEntity` → permite controlar status (201) e headers (`Location`).

## 2.7 Tratamento de erros — respostas consistentes

`backend/src/main/java/com/taskflow/config/GlobalExceptionHandler.java`:

```java
@RestControllerAdvice                     // captura exceções de TODOS os controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail handleNotFound(TaskNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());  // 404
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(...) { ... }   // 400
}
```

- `@RestControllerAdvice` centraliza o tratamento de erros — sem `try/catch` espalhado pelos controllers.
- `ProblemDetail` é o formato padrão de erro (RFC 7807): `{ "status": 404, "detail": "...", ... }`.
- **Por que isso importa?** Sem isso, um erro vazaria o **stack trace** (informação interna) para o cliente —
  falha de segurança. Com isso, o cliente recebe uma mensagem limpa e um status correto.

## 2.8 A requisição completa: `POST /api/tasks`

Juntando tudo, quando o front cria uma tarefa:

```
1. Chega POST /api/tasks com body {"title":"Estudar"}
2. Spring converte o JSON → CreateTaskRequest        (@RequestBody)
3. @Valid checa as regras (title não vazio)          → se falhar, 400 (GlobalExceptionHandler)
4. TaskController.create() chama service.create()
5. TaskService cria new Task(...) e repository.save()
6. Repository (Hibernate) faz INSERT no banco e devolve a Task com id
7. Service converte Task → TaskResponse (DTO)
8. Controller devolve 201 Created + JSON {"id":1,"title":"Estudar",...}
```

---

## 🏋️ Exercícios — Módulo 2

1. Quais são as 4 camadas do backend e a responsabilidade de cada uma?
2. O que é injeção de dependência por construtor e por que é melhor que dar `new`?
3. Diferencie **JPA** e **Hibernate**.
4. Cite 3 motivos para usar DTO em vez de retornar a Entity na API.
5. O que cada anotação faz: `@RestController`, `@PathVariable`, `@RequestBody`, `@Valid`?
6. O `TaskRepository` é uma interface vazia. De onde vêm os métodos `save`, `findById`, etc.?
7. (Aplicação) Você precisa de um endpoint `GET /api/tasks/done` que retorna só as concluídas.
   Em quais camadas você mexe e o que adiciona em cada uma?

---

## ✅ Gabarito — Módulo 2

**1.** **Controller** (recebe/responde HTTP, valida entrada), **Service** (regra de negócio, transações),
**Repository** (acesso ao banco), **Entity** (mapeia a tabela). O Controller é fino; a lógica vive no Service.

**2.** É declarar a dependência como parâmetro do **construtor** (`public TaskService(TaskRepository r)`) e deixar
o Spring **entregar** a instância. Melhor que `new` porque: (a) **testabilidade** — no teste você injeta um mock;
(b) **baixo acoplamento** — a classe não sabe como o repositório é criado; (c) o campo pode ser `final` (imutável).

**3.** **JPA** é a **especificação** (a "interface", o padrão) de mapeamento objeto-relacional. **Hibernate** é a
**implementação concreta** que o Spring usa por baixo para gerar o SQL.

**4.** (a) **Segurança** — controla quais campos entram/saem, sem vazar colunas internas; (b) **estabilidade** —
desacopla o modelo do banco do contrato da API; (c) **validação** — o DTO de entrada carrega as regras.

**5.** `@RestController`: controller cujo retorno vira JSON. `@PathVariable`: extrai um valor da URL (o `{id}`).
`@RequestBody`: converte o JSON do corpo da requisição num objeto Java. `@Valid`: dispara a validação das
anotações do DTO (e gera 400 se inválido).

**6.** Do **Spring Data JPA**. Ao estender `JpaRepository<Task, Long>`, o Spring **gera a implementação em tempo
de execução** com todo o CRUD pronto.

**7.** Mexe em 3 camadas:
- **Repository:** adiciona uma derived query → `List<Task> findByDone(boolean done);`
- **Service:** novo método → `findDone()` que chama `repository.findByDone(true)` e mapeia para `TaskResponse`.
- **Controller:** novo endpoint → `@GetMapping("/done") public List<TaskResponse> listDone() { ... }`.
  (A Entity não muda, pois o campo `done` já existe.)
