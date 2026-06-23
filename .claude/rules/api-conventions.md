# Regra: Convenções da API REST

Contrato entre o backend Spring Boot e o frontend Angular. Mudou aqui, atualize o `service.ts` correspondente.

## URLs e recursos

- Prefixo: `/api`.
- Recursos no **plural** e em `kebab-case`: `/api/tasks`, `/api/tasks/{id}`.
- Nada de verbos na URL — a ação vem do método HTTP.

| Operação            | Método | Rota               | Status sucesso |
|---------------------|--------|--------------------|----------------|
| Listar              | GET    | `/api/tasks`       | 200            |
| Buscar por id       | GET    | `/api/tasks/{id}`  | 200            |
| Criar               | POST   | `/api/tasks`       | 201 + `Location` |
| Atualizar           | PUT    | `/api/tasks/{id}`  | 200            |
| Atualizar parcial   | PATCH  | `/api/tasks/{id}`  | 200            |
| Remover             | DELETE | `/api/tasks/{id}`  | 204            |

## Payloads

- JSON em `camelCase`.
- **Request e response usam DTOs**, nunca a entidade JPA diretamente.
- Datas em ISO-8601 (`2026-06-23T10:00:00Z`).

## Validação e erros

- Valide a entrada com Bean Validation (`@Valid`, `@NotBlank`, `@Size`...).
- Erros em formato consistente (padrão `ProblemDetail` do Spring 6 ou objeto próprio):

```json
{ "status": 400, "error": "Bad Request", "message": "title não pode ser vazio", "path": "/api/tasks" }
```

- Códigos: `400` validação, `404` não encontrado, `409` conflito, `500` erro interno. Nunca vaze stack trace.

## CORS e segredos

- Configure CORS explicitamente para a origem do frontend (`http://localhost:4200` em dev). **Não** use `*` em produção.
- URLs de banco, usuários e senhas vêm de variáveis de ambiente / `application-{profile}.properties` — nunca commitados.

## Lado Angular

- Centralize a base URL em `environment.ts`.
- Cada recurso tem seu `service.ts` tipado, espelhando exatamente este contrato.
