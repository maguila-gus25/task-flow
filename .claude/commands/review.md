---
description: Revisa as mudanças do branch atual (diff) buscando bugs, qualidade e segurança
argument-hint: "[base branch — padrão: main]"
allowed-tools: Read, Grep, Glob, Bash
---

Faça uma revisão de código das mudanças pendentes neste branch contra **${ARGUMENTS:-main}**.

## Contexto a coletar
- `git diff ${ARGUMENTS:-main}...HEAD --stat` e o diff completo dos arquivos alterados.
- Leia os arquivos vizinhos quando precisar de contexto além do diff.

## O que revisar (siga @.claude/workflows/code-review.md)
1. **Correção:** o código faz o que diz? Casos de borda (lista vazia, id inexistente, input nulo)?
2. **Contrato da API:** mudanças em endpoints/DTOs estão refletidas no service Angular? (ver @.claude/rules/api-conventions.md)
3. **Qualidade:** duplicação, nomes, responsabilidades (controller fino, lógica no service).
4. **Testes:** há cobertura para o que mudou? (ver @.claude/rules/testing-standard.md)
5. **Segurança:** segredos, validação, exposição de dados. Para análise profunda, delegue ao agente `security-reviewer`.

## Saída
Agrupe os achados por severidade (🔴 Bloqueante / 🟡 Atenção / 🟢 Sugestão), cada um com `arquivo:linha` e a correção proposta. Termine com um veredito: **aprovar**, **aprovar com ressalvas** ou **precisa de mudanças**.
