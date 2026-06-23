---
name: pr-description
description: Use para gerar a mensagem de commit e a descrição de pull request do TaskFlow no padrão Conventional Commits, a partir do diff do branch.
---

# PR Description — TaskFlow

Gera commits e descrições de PR padronizados. Segue @.claude/rules/pr.md.

## Como usar

1. Colete o diff: `git diff main...HEAD --stat` e os arquivos alterados.
2. Identifique o tipo de mudança e o escopo predominante.
3. Produza (a) sugestão de mensagem de commit e (b) corpo de PR.

## Mensagem de commit (Conventional Commits)

`tipo(escopo): descrição no imperativo, em português`

- Tipos: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `style`, `perf`.
- Escopos comuns: `backend`, `frontend`, `api`, `task`.
- Exemplo: `feat(task): adiciona filtro de tarefas concluídas`

## Template de PR

```markdown
## O que muda
<resumo em 1-3 linhas>

## Por quê
<motivação / issue>

## Como testar
<passos para validar>

## Checklist
- [ ] Testes adicionados/atualizados e passando (mvn test / ng test)
- [ ] Contrato da API e service Angular sincronizados (se aplicável)
- [ ] Sem segredos commitados
- [ ] Build de produção ok (se relevante)
```

## Regras
- Descreva o **porquê**, não só o **o quê**.
- Não invente itens do checklist — marque só o que realmente foi feito.
- Não execute `git commit`/`push` sem o usuário pedir.
