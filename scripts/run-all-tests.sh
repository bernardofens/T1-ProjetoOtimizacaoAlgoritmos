#!/usr/bin/env sh
# script para rodar todos os testes com maven
set -e 
# cd para o diretorio raiz do projeto
cd "$(dirname "$0")/.."
# roda os testes com maven
./mvnw -q test
echo
echo "testes concluidos com sucesso."
