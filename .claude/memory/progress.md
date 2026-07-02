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
- [x] Skill `github-flow` criada (`.claude/skills/github-flow/SKILL.md`), branch `docs/github-flow-skill`.
- [x] Tarefas recorrentes (branch `feat/recurring-tasks`): `dueDate` + `recurrenceFrequency`/`recurrenceInterval`/`recurrenceDaysOfWeek`/`recurrenceEndDate` na entidade `Task`, DTOs e `TaskService` (concluir tarefa recorrente gera a próxima ocorrência). Ver decisão 004 em `decisions.md`.
- [x] Frontend: formulário com vencimento + repetição (presets diário/semanal/mensal/anual + intervalo + chips de dia da semana), badge de recorrência e due date na lista.
- [x] Testes: backend 16 passando (5 novos de recorrência), frontend 8 passando (4 novos no `task-list.spec.ts`); build de produção OK.
- [x] Validado manualmente via curl: tarefa semanal (seg/qua/sex) concluída na quarta gera próxima ocorrência na sexta; tarefa não recorrente não gera efeito colateral ao concluir.
- [x] Refresh visual Apple-style (branch `feat/apple-design-refresh`): tokens de design (`styles.scss`) com cores de sistema iOS/macOS, tipografia `-apple-system`, espaçamento 8px, dark mode via `prefers-color-scheme`; cards com sombra, checkbox circular estilo iOS, ícones SVG no lugar de emoji. Validado com Playwright (screenshots light/dark) — corrigido bug de layout onde `flex: 1 1 180px` aplicado via seletor descendente do SCSS virava altura em containers `flex-direction: column` aninhados.

## Em andamento
- [ ] —

## A fazer (próximos passos)
- [ ] Filtro por status (pendentes/concluídas) na UI.
- [ ] Edição de título/descrição na UI (hoje só toggle de status e delete).
- [ ] Perfil `prod` com MySQL e variáveis de ambiente + DDL manual para as novas colunas/tabela de recorrência (ver decisão 004).
- [ ] Testes de integração de repository (@DataJpaTest).

## Notas
- Backend usa Java 21 como target (compila com o JDK 23 instalado).
- Spring Boot 4 renomeou starters/pacotes: `spring-boot-starter-webmvc`, `@WebMvcTest` em `org.springframework.boot.webmvc.test.autoconfigure`, e `@MockitoBean` no lugar de `@MockBean`.
- Angular 22 usa o builder de testes `@angular/build:unit-test` (vitest + jsdom, sem browser).
