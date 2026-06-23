---
name: code-review
description: Use ao revisar mudanças de código no TaskFlow (backend Spring Boot e/ou frontend Angular). Fornece um checklist estruturado por severidade cobrindo correção, contrato da API, testes, qualidade e segurança.
---

# Code Review — TaskFlow

Revisão consistente de mudanças. Combine com o comando `/review` e o workflow @.claude/workflows/code-review.md.

## Como conduzir

1. Pegue o diff (`git diff main...HEAD`) e leia o contexto vizinho necessário.
2. Percorra o checklist abaixo.
3. Reporte por severidade com `arquivo:linha` e correção sugerida.

## Checklist

### Correção
- [ ] Faz o que promete; sem regressões óbvias.
- [ ] Casos de borda: lista vazia, id inexistente (404), input nulo/vazio, valores limite.
- [ ] Tratamento de erro adequado (sem engolir exceções).

### Contrato da API (full-stack)
- [ ] Mudança em endpoint/DTO refletida no `service.ts` do Angular.
- [ ] Status HTTP e formato de payload conforme @.claude/rules/api-conventions.md.

### Testes
- [ ] O que mudou tem teste; testes são significativos, não só de cobertura.
- [ ] Suíte passando (`mvn test`, `ng test`).

### Qualidade
- [ ] Camadas respeitadas (controller fino, regra no service, dados no repository).
- [ ] Sem duplicação evitável; nomes claros; sem código morto.
- [ ] Injeção por construtor; DTOs imutáveis quando possível.

### Segurança
- [ ] Validação de input (`@Valid`); sem segredos hardcoded; sem vazamento de stack trace; CORS restrito.

## Severidades
- 🔴 **Bloqueante:** bug, falha de segurança, contrato quebrado.
- 🟡 **Atenção:** risco ou dívida que deveria ser resolvida em breve.
- 🟢 **Sugestão:** melhoria opcional.

Termine com veredito: **aprovar** / **aprovar com ressalvas** / **precisa de mudanças**.
