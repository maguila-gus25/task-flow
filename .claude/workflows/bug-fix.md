# Workflow: Corrigir um bug

## 1. Reproduzir
- Entenda o comportamento esperado vs. o observado.
- Reproduza de forma confiável (passos, request específica, estado dos dados).
- Determine a camada: frontend (Angular), API (contrato) ou backend (Spring).

## 2. Localizar a causa raiz
- Use o agente `research` para rastrear o fluxo do dado do componente → service Angular → controller → service → repository.
- Confirme a **causa**, não só o sintoma. Não conserte no lugar errado.

## 3. Escrever um teste que falha
- Antes de corrigir, escreva um teste que reproduz o bug e **falha** (regressão).
- Backend: JUnit/Mockito. Frontend: Jasmine. (ver @.claude/rules/testing-standard.md)

## 4. Corrigir
- Faça a menor mudança que resolve a causa raiz.
- Rode o teste novo (agora passa) e a suíte inteira (nada quebrou).

## 5. Finalizar
- Verifique casos de borda relacionados ao mesmo bug.
- Registre em @.claude/memory/progress.md se for relevante.
- Commit `fix(...)` descrevendo o problema e a causa (ver @.claude/rules/pr.md).
