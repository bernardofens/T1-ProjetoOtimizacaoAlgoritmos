#!/usr/bin/env sh
set -e
cd "$(dirname "$0")/.."
./mvnw -q test
echo
echo "testes concluidos com sucesso."
