---
description: Prepara e valida um build de produção do backend e do frontend (checklist pré-deploy)
argument-hint: "[ambiente: staging | prod — padrão: staging]"
allowed-tools: Read, Grep, Glob, Bash
---

Execute o checklist de preparação de deploy para o ambiente **${ARGUMENTS:-staging}** do TaskFlow.

> Este comando **não publica** em servidores. Ele valida que o projeto está pronto para deploy e
> monta os artefatos. A publicação efetiva é feita manualmente pelo desenvolvedor.

## Passos

1. **Estado do git:** confirme que não há mudanças não commitadas (`git status`). Se houver, avise e pare.
2. **Backend:**
   - `cd backend && mvn clean package` (gera o `.jar` em `target/`).
   - Confirme que os testes passaram no build.
3. **Frontend:**
   - `cd frontend && ng build --configuration production` (gera `dist/`).
4. **Configuração:** verifique que segredos e URLs do ambiente alvo vêm de variáveis de ambiente,
   nunca hardcoded (ver `.claude/rules/api-conventions.md`). Liste as variáveis necessárias.
5. **Relatório final:** caminho dos artefatos, variáveis de ambiente requeridas e o comando manual
   sugerido para subir cada parte. Liste qualquer pendência que impeça o deploy.

Se qualquer etapa de build/teste falhar, **interrompa** e reporte o erro — não prossiga.
