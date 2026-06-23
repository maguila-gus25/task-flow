#!/usr/bin/env bash
# PreToolUse hook (matcher: Write|Edit)
# Bloqueia escrita/edição em arquivos sensíveis (segredos, chaves, .env).
# Lê o evento JSON via stdin; sair com código 2 BLOQUEIA a ação no Claude Code.
set -euo pipefail

input="$(cat)"

# Extrai o caminho do arquivo do payload do tool. Usa jq se disponível; senão, grep.
if command -v jq >/dev/null 2>&1; then
  file_path="$(printf '%s' "$input" | jq -r '.tool_input.file_path // empty')"
else
  file_path="$(printf '%s' "$input" | grep -o '"file_path"[[:space:]]*:[[:space:]]*"[^"]*"' | head -1 | sed 's/.*"file_path"[[:space:]]*:[[:space:]]*"//;s/"$//')"
fi

[ -z "$file_path" ] && exit 0

case "$file_path" in
  *.env|*.env.*|*id_rsa*|*.pem|*.key|*/secrets/*|*application-secret*|*application-local.properties)
    echo "🚫 Bloqueado: escrita em arquivo sensível não é permitida -> $file_path" >&2
    echo "   Use variáveis de ambiente para segredos (ver CLAUDE.md)." >&2
    exit 2
    ;;
esac

exit 0
