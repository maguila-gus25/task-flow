# Progresso

> Atualize ao concluir ou iniciar trabalho. Mantenha curto e factual.

## Feito
- [x] Setup do Claude Code organizado no padrão `.claude/` (agents, commands, hooks, skills, rules, workflows, memory).
- [x] Documentação base preenchida: CLAUDE.md, rules, workflows e contexto.
- [x] README completo (execução, endpoints, exemplos).
- [x] Backend criado: Spring Boot 4.1 + JPA + H2, CRUD de `Task` (entity, repository, DTOs, service, controller).
- [x] Tratamento global de erros (ProblemDetail 400/404) e CORS para `http://localhost:4200`.
- [x] Testes de backend: 10 passando (`./mvnw test`).
- [x] Frontend criado: Angular 22 (standalone + signals), feature de tarefas (model, service, componente de listagem).
- [x] Testes de frontend: 4 passando (vitest); build de produção OK.
- [x] CRUD validado em runtime via curl (201/200/400/204/404 conforme esperado).

## Em andamento
- [ ] —

## A fazer (próximos passos)
- [ ] Filtro por status (pendentes/concluídas) na UI.
- [ ] Edição de título/descrição na UI (hoje só toggle de status e delete).
- [ ] Perfil `prod` com MySQL e variáveis de ambiente.
- [ ] Testes de componente (TaskList) e de integração de repository (@DataJpaTest).

## Notas
- Backend usa Java 21 como target (compila com o JDK 23 instalado).
- Spring Boot 4 renomeou starters/pacotes: `spring-boot-starter-webmvc`, `@WebMvcTest` em `org.springframework.boot.webmvc.test.autoconfigure`, e `@MockitoBean` no lugar de `@MockBean`.
- Angular 22 usa o builder de testes `@angular/build:unit-test` (vitest + jsdom, sem browser).
