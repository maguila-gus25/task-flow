# Decisões arquiteturais (ADR leve)

> Registre escolhas relevantes e o porquê. Uma entrada por decisão. Não reescreva o histórico — adicione.

Formato:
```
## NNN — Título  (AAAA-MM-DD)
**Contexto:** por que a decisão foi necessária.
**Decisão:** o que foi escolhido.
**Consequências:** trade-offs e impactos.
```

---

## 001 — Stack: Spring Boot + Angular  (2026-06-23)
**Contexto:** o projeto serve para praticar arquitetura cliente-servidor com tecnologias de mercado.
**Decisão:** backend em Java/Spring Boot com JPA; frontend em Angular consumindo API REST.
**Consequências:** duas bases de código que evoluem juntas; contrato da API é o ponto de acoplamento crítico.

## 002 — H2 em dev, MySQL em prod  (2026-06-23)
**Contexto:** desenvolvimento precisa ser rápido e sem dependências externas.
**Decisão:** H2 em memória no perfil de desenvolvimento; MySQL no perfil de produção.
**Consequências:** banco volátil em dev (reinício zera dados); cuidado com SQL específico de dialeto.

## 003 — DTOs em vez de expor entidades JPA  (2026-06-23)
**Contexto:** evitar acoplar a API ao modelo de persistência e vazar campos internos.
**Decisão:** request/response sempre via DTOs; conversão na camada de service.
**Consequências:** um pouco mais de boilerplate, em troca de API estável e segura.

## 004 — Tarefas recorrentes: conclusão gera nova ocorrência  (2026-07-01)
**Contexto:** modelar recorrência inspirada em Apple Lembretes (frequência + intervalo + dias da semana) e Google Tarefas (conclusão avança para a próxima instância). O modelo de `Task` não tinha `dueDate`.
**Decisão:** adicionar `dueDate`, `recurrenceFrequency` (NONE/DAILY/WEEKLY/MONTHLY/YEARLY), `recurrenceInterval`, `recurrenceDaysOfWeek` e `recurrenceEndDate` à entidade `Task`. Ao concluir uma tarefa recorrente (transição pendente → concluída), o `TaskService` cria uma nova `Task` com a próxima `dueDate` calculada; a tarefa concluída permanece como histórico. Sem `seriesId`/vínculo entre ocorrências — cada uma é uma linha independente com a mesma config de recorrência copiada.
**Consequências:** schema cresce (nova tabela `task_recurrence_days` para os dias da semana). Em dev, `ddl-auto=update` cobre a migração automaticamente; **em produção (`ddl-auto=validate`, sem Flyway/Liquibase) as colunas/tabela novas exigem DDL manual antes do deploy** — não há migration tool no projeto ainda.
