# CLAUDE.md — TaskFlow

Orientações para o Claude Code trabalhar neste repositório. Leia antes de qualquer tarefa.

## Visão geral do projeto

TaskFlow é um sistema de gerenciamento de tarefas (To-Do List) full-stack:

- **Backend:** Java 17+, Spring Boot, Spring Data JPA, banco H2 (dev) / MySQL (prod). Expõe uma API REST para CRUD de tarefas.
- **Frontend:** Angular + TypeScript, usando RxJS e `HttpClient` para consumir a API.

O objetivo é demonstrar uma arquitetura cliente-servidor limpa: API RESTful, injeção de dependência, componentização e persistência relacional.

## Estrutura esperada do repositório

```
backend/    → projeto Spring Boot (Maven)
frontend/   → projeto Angular (Angular CLI)
```

> Se essas pastas ainda não existem, crie o backend com Spring Boot e o frontend com `ng new` antes de implementar features. Use o comando `/scaffold` para iniciar.

## Comandos principais

### Backend (a partir de `backend/`)
- Rodar: `mvn spring-boot:run`
- Testes: `mvn test`
- Build: `mvn clean package`

### Frontend (a partir de `frontend/`)
- Rodar: `ng serve` (http://localhost:4200)
- Testes: `ng test`
- Build: `ng build`

## Regras e convenções

Estas regras são obrigatórias. Consulte-as conforme a tarefa:

- @.claude/rules/code-style.md — estilo de código Java e TypeScript
- @.claude/rules/api-conventions.md — convenções da API REST
- @.claude/rules/testing-standard.md — padrões de teste
- @.claude/rules/pr.md — formato de commits e pull requests

## Fluxos de trabalho

Para tarefas estruturadas, siga o workflow correspondente:

- @.claude/workflows/feature-build.md — implementar uma nova funcionalidade
- @.claude/workflows/bug-fix.md — corrigir um bug
- @.claude/workflows/code-review.md — revisar código

## Contexto e decisões

- @.claude/memory/project-context.md — contexto vivo do projeto
- @.claude/memory/decisions.md — decisões arquiteturais (ADR leve)
- @.claude/memory/progress.md — o que já foi feito e o que falta

## Princípios

1. **Não invente endpoints, entidades ou dependências** — confirme no código existente antes.
2. **Sempre escreva/atualize testes** ao mudar comportamento (ver `rules/testing-standard.md`).
3. **Nunca commite segredos** — credenciais ficam em variáveis de ambiente, nunca no código.
4. **Backend e frontend evoluem juntos:** ao mudar um contrato da API, atualize o cliente Angular.
5. Responda e escreva documentação/commits em **português** (código e identificadores em inglês).
