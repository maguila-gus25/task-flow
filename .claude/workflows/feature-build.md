# Workflow: Construir uma feature

Siga esta sequência ao implementar uma nova funcionalidade no TaskFlow.

## 1. Entender
- Leia o pedido e identifique o impacto: backend, frontend ou ambos.
- Use o agente `research` para mapear o código existente relacionado.
- Confirme o contrato da API necessário (ver @.claude/rules/api-conventions.md).

## 2. Planejar
- Descreva o plano em passos curtos: entidade/DTO → repository → service → controller → service Angular → componente.
- Liste o que será testado.

## 3. Implementar (de dentro para fora)
1. **Backend:** entity/DTO → repository → service (regra de negócio) → controller.
2. **Testes de backend** junto, não depois (`test-writer` ajuda).
3. **Frontend:** model → service HTTP → componente/UI.
4. Mantenha o contrato sincronizado entre as duas pontas.

> Atalho: `/scaffold <Entidade>` gera o esqueleto inicial dessa cadeia.

## 4. Validar
- Rode `mvn test` e `ng test`.
- Suba backend + frontend e teste o fluxo manualmente.
- Opcional: `.claude/hooks/validate-code.sh` para validar tudo de uma vez.

## 5. Finalizar
- Atualize @.claude/memory/progress.md (feito/pendente) e, se houve escolha relevante, @.claude/memory/decisions.md.
- Commit no padrão de @.claude/rules/pr.md. Só faça commit/push se o usuário pedir.
