---
name: github-flow
description: Use ao iniciar, conduzir ou finalizar qualquer mudança de código no TaskFlow. Define o fluxo de branching (GitHub Flow) — quando criar branch, como nomeá-la, quando abrir PR e como fechar o ciclo.
---

# GitHub Flow — TaskFlow

Fluxo de branching do projeto. Complementa (não substitui) @.claude/rules/pr.md — aqui é sobre
*quando/como criar a branch e fechar o ciclo*; commits e formato de PR ficam em `pr.md` e na skill `pr-description`.

## Regra central

`main` é sempre estável e deployável. Nunca commitar direto nela — toda mudança nasce em uma branch e volta via Pull Request.

## Como conduzir

1. **Atualize `main` local** antes de ramificar: `git pull --ff-only origin main`.
2. **Crie uma branch por mudança lógica**, a partir de `main`:
   `tipo/descricao-curta-em-kebab-case`
   - Mesmo vocabulário de tipos do Conventional Commits: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `style`, `perf`.
   - Exemplos observados neste repo: `feat/claude-setup-and-crud`, `docs/guia-de-estudo`, `chore/tsconfig-rootdir`.
   - Uma branch = um escopo de PR. Se a mudança crescer para algo não relacionado, pare e abra outra branch.
3. **Trabalhe e commite na branch** seguindo @.claude/rules/pr.md (Conventional Commits, um commit por mudança lógica).
4. **Antes de abrir o PR**, rode a suíte relevante: `mvn test` (backend) e/ou `ng test` (frontend). Não abra PR com suíte vermelha.
5. **Abra o PR cedo e pequeno**, usando a skill `pr-description` para gerar título/corpo no padrão do projeto.
6. **Merge** só com a suíte passando e o PR revisado. Depois do merge, apague a branch (local e remota) para manter o repositório limpo.

## Quando NÃO ramificar

- Já estar em uma branch de feature/fix ativa para o mesmo escopo — continue nela em vez de criar uma nova.
- Mudanças puramente exploratórias que o usuário pediu para não commitar.

## Guardrails

- Nunca execute `git commit`, `git push`, `git branch -D` ou crie/feche PRs sem o usuário pedir explicitamente — a criação da branch em si é reversível e pode ser feita ao iniciar o trabalho, mas commit/push/PR exigem confirmação.
- Não force-push em `main`; não dê merge com testes quebrados.
- PRs pequenos e focados — prefira várias branches sequenciais a uma branch gigante com múltiplos escopos.
