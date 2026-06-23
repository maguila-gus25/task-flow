# Regra: Commits e Pull Requests

## Commits — Conventional Commits

Formato: `tipo(escopo): descrição` no imperativo, em português.

- **Tipos:** `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `style`, `perf`.
- **Escopo:** `backend`, `frontend`, `api`, ou o recurso (`task`).
- Exemplos:
  - `feat(task): adiciona endpoint de conclusão de tarefa`
  - `fix(frontend): corrige lista que não atualiza após delete`
  - `test(backend): cobre TaskService com casos de borda`

Um commit = uma mudança lógica. Não misture refactor com feature.

## Pull Requests

Título no mesmo formato do commit. Corpo:

```markdown
## O que muda
<resumo em 1-3 linhas>

## Por quê
<motivação / issue relacionada>

## Como testar
<passos para validar manualmente>

## Checklist
- [ ] Testes adicionados/atualizados e passando (mvn test / ng test)
- [ ] Contrato da API e service Angular sincronizados (se aplicável)
- [ ] Sem segredos commitados
- [ ] Build de produção ok (se relevante)
```

## Regras

- PRs pequenos e focados; descreva trade-offs quando houver.
- Não faça merge com a suíte vermelha.
- **Nunca** commite/pushe sem o usuário pedir explicitamente. Trabalhe em branch, não direto na `main`.
