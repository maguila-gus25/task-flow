#!/usr/bin/env bash
# Hook de validação rápida — pode ser ligado como Stop hook em settings.json
# ou chamado manualmente nos workflows antes de abrir um PR.
# Compila e roda os testes de backend e frontend; sai != 0 se algo falhar.
set -uo pipefail

status=0

if [ -f backend/pom.xml ]; then
  echo "▶ Validando backend (mvn test)..."
  if command -v mvn >/dev/null 2>&1; then
    (cd backend && mvn -q test) || status=1
  else
    echo "⚠ mvn não encontrado — pulando backend." >&2
  fi
fi

if [ -f frontend/package.json ]; then
  echo "▶ Validando frontend (ng test + build)..."
  if command -v npx >/dev/null 2>&1; then
    (cd frontend && npx ng test --watch=false --browsers=ChromeHeadless) || status=1
    (cd frontend && npx ng build) || status=1
  else
    echo "⚠ npx não encontrado — pulando frontend." >&2
  fi
fi

if [ "$status" -ne 0 ]; then
  echo "❌ Validação falhou." >&2
else
  echo "✅ Validação OK."
fi

exit "$status"
