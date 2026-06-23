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
