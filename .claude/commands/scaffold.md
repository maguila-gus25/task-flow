---
description: Cria o esqueleto de uma nova feature CRUD ponta a ponta (entidade → repository → service → controller → service Angular → componente)
argument-hint: <NomeDaEntidade> [campos: ex. "title:String description:String done:boolean"]
allowed-tools: Read, Write, Edit, Grep, Glob, Bash
---

Gere o scaffold completo de uma feature para a entidade **$ARGUMENTS** no TaskFlow, seguindo @.claude/rules/api-conventions.md e @.claude/rules/code-style.md.

Antes de gerar, **inspecione o código existente** (ex.: a entidade `Task`, se já houver) para imitar o padrão atual de pacotes, nomes e estilo. Não invente estrutura nova se já existe um padrão.

## Backend (`backend/src/main/java/...`)
1. **Entity** JPA com `@Entity`, `@Id`/`@GeneratedValue` e os campos informados.
2. **Repository** estendendo `JpaRepository<Entidade, Long>`.
3. **DTOs** de request e response (não exponha a entidade direto).
4. **Service** com a lógica de CRUD e tratamento de "não encontrado".
5. **Controller** REST em `/api/<recurso-no-plural>` com GET (lista + por id), POST, PUT, DELETE; use `@Valid` nos inputs.

## Frontend (`frontend/src/app/...`)
6. **Model/interface** TypeScript espelhando o DTO de response.
7. **Service** Angular com `HttpClient` cobrindo os endpoints.
8. **Componente** de listagem com create/toggle/delete básico.

## Finalização
- Atualize as rotas do Angular se um novo componente for criado.
- Liste os arquivos gerados e os próximos passos manuais (migrations, validações específicas).
- **Não** rode deploy. Sugira rodar os testes com o agente `test-writer`.
