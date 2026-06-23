# Regra: Estilo de código

Aplicável a todo código do TaskFlow. Código e identificadores em **inglês**; comentários e docs em **português**.

## Java / Spring Boot

- **Pacotes:** organize por camada (`entity`, `repository`, `service`, `controller`, `dto`).
- **Camadas finas:** controller só orquestra HTTP; regra de negócio fica no `@Service`; acesso a dados no `@Repository`.
- **Injeção de dependência:** sempre por **construtor** (nunca `@Autowired` em campo). Use `final`.
- **Imutabilidade:** prefira DTOs como `record`. Não exponha entidades JPA na API — converta para DTO.
- **Nomes:** classes em `PascalCase`, métodos/variáveis em `camelCase`, constantes em `UPPER_SNAKE_CASE`.
- **Optional:** retorne `Optional<T>` em vez de `null` em buscas; trate ausência com exceção de domínio.
- **Sem números/strings mágicos:** extraia para constantes.

```java
@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }
}
```

## TypeScript / Angular

- **Tipagem forte:** evite `any`; modele DTOs com `interface`.
- **Services:** lógica de acesso HTTP em `@Injectable` services, nunca no componente.
- **RxJS:** trate erros com `catchError`; evite subscribes aninhados (use `switchMap`/`mergeMap`). Cancele inscrições (`takeUntilDestroyed`/`async` pipe).
- **Componentes:** template e lógica separados; sem chamadas HTTP diretas no template.
- **Nomes de arquivo:** `kebab-case` (`task-list.component.ts`, `task.service.ts`).

## Geral

- Formatação automática via hook `post-edit-format.sh` (Spotless/Prettier).
- Funções pequenas e com responsabilidade única. Sem código comentado/morto.
- Comente o **porquê**, não o **o quê**.
