# Workflow: Revisão de código

Usado pelo comando `/review` e em revisões manuais.

## 1. Reunir contexto
- Diff do branch vs. `main` (`git diff main...HEAD`).
- Leia arquivos vizinhos quando o diff sozinho não der contexto.

## 2. Avaliar (em ordem de prioridade)
1. **Correção:** faz o que promete? Casos de borda: lista vazia, id inexistente, input nulo, concorrência.
2. **Contrato da API:** mudança em endpoint/DTO refletida no service Angular? (@.claude/rules/api-conventions.md)
3. **Testes:** o que mudou está coberto? Testes são significativos? (@.claude/rules/testing-standard.md)
4. **Qualidade:** duplicação, nomes, camadas (controller fino, lógica no service), tratamento de erro. (@.claude/rules/code-style.md)
5. **Segurança:** validação de input, segredos, exposição de dados. Para profundidade, use o agente `security-reviewer`.

## 3. Reportar
- Agrupe por severidade: 🔴 Bloqueante / 🟡 Atenção / 🟢 Sugestão.
- Cada achado: `arquivo:linha` + problema + correção sugerida.
- Reconheça o que está bom, não só o que está errado.

## 4. Veredito
- **Aprovar** / **Aprovar com ressalvas** / **Precisa de mudanças**.
- Seja específico e respeitoso; foque no código, não na pessoa.
