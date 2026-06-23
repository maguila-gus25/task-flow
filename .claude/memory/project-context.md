# Contexto do projeto

> Documento vivo. Atualize quando o entendimento do projeto mudar.

## O que é
TaskFlow — sistema de gerenciamento de tarefas (To-Do List) full-stack, criado para praticar arquitetura cliente-servidor.

## Arquitetura
- **Backend:** Java 17+, Spring Boot, Spring Data JPA. Expõe API REST sob `/api`.
- **Banco:** H2 em memória no desenvolvimento; MySQL previsto para produção.
- **Frontend:** Angular + TypeScript + RxJS, consumindo a API via `HttpClient`.

## Modelo de domínio (inicial)
**Task**
- `id` (Long, gerado)
- `title` (String, obrigatório)
- `description` (String, opcional)
- `done` (boolean, default false)

> Ajuste/expanda conforme o domínio evoluir.

## Funcionalidades-alvo (do README)
- Criar tarefa (título + descrição)
- Listar tarefas (pendentes e concluídas)
- Atualizar status (marcar/desmarcar como concluída)
- Deletar tarefa

## Convenções rápidas
- Código em inglês; docs/commits em português.
- Contrato da API em @api-conventions.md; backend e frontend evoluem juntos.

## Estado atual
O repositório contém o setup do Claude Code e o README. O código de aplicação
(`backend/`, `frontend/`) ainda **não foi criado** — começar com `/scaffold` ou Spring Initializr + `ng new`.
