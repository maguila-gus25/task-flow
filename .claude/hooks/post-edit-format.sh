#!/usr/bin/env bash
# PostToolUse hook (matcher: Write|Edit)
# Formata automaticamente o arquivo recém-editado, conforme a extensão.
# Falhas de formatação nunca quebram o fluxo (best-effort).
set -euo pipefail

input="$(cat)"

if command -v jq >/dev/null 2>&1; then
  file_path="$(printf '%s' "$input" | jq -r '.tool_input.file_path // empty')"
else
  file_path="$(printf '%s' "$input" | grep -o '"file_path"[[:space:]]*:[[:space:]]*"[^"]*"' | head -1 | sed 's/.*"file_path"[[:space:]]*:[[:space:]]*"//;s/"$//')"
fi

[ -z "$file_path" ] && exit 0
[ -f "$file_path" ] || exit 0

case "$file_path" in
  *.java)
    # Spotless (se configurado no pom.xml). Silencioso e não-fatal.
    if [ -f backend/pom.xml ] && command -v mvn >/dev/null 2>&1; then
      (cd backend && mvn -q spotless:apply >/dev/null 2>&1) || true
    fi
    ;;
  *.ts|*.tsx|*.html|*.scss|*.css|*.json)
    if command -v npx >/dev/null 2>&1; then
      npx --no-install prettier --write "$file_path" >/dev/null 2>&1 || true
    fi
    ;;
esac

exit 0
