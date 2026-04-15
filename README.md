# t1 — maior subsequência comum (lcs)

trabalho de projeto e otimização de algoritmos: implementação recursiva, programação dinâmica (bottom-up), memoização (top-down), medição de tempo e contagem de operações, testes automatizados e relatório.

## requisitos

- java 17+
- para build e testes: o script `mvnw` (maven wrapper) baixa o maven automaticamente na primeira execução

## estrutura

```
├── pom.xml
├── mvnw / mvnw.cmd
├── src/main/java/com/poa/lcs/   — algoritmos, benchmark, main
├── src/test/java/com/poa/lcs/   — junit 5
├── docs/RELATORIO.md            — relatório técnico
├── scripts/
│   ├── run-all-tests.bat
│   ├── run-all-tests.sh
│   └── plot_benchmark.py          — gráfico opcional (matplotlib)
└── results/                     — csv/png gerados localmente
```

## compilar e testar

```text
.\mvnw.cmd clean test
```

ou apenas testes:

```text
scripts\run-all-tests.bat
```

em unix:

```text
chmod +x mvnw scripts/run-all-tests.sh
./scripts/run-all-tests.sh
```

## executar a demonstração

casos embutidos:

```text
.\mvnw.cmd -q compile exec:java
```

dois argumentos (um par de strings):

```text
.\mvnw.cmd -q compile exec:java "-Dexec.args=ABCBDAB BDCABA"
```

gravar `results/benchmark.csv` após a execução:

```text
.\mvnw.cmd -q compile exec:java "-Dexec.args=--csv"
```

## gráfico comparativo (opcional)

1. gere o csv com o comando acima
2. `pip install matplotlib` (se necessário)
3. `python scripts/plot_benchmark.py` — cria `results/tempo_por_tamanho.png`

## limiar do recursivo

se `|s1| + |s2| > 25`, o benchmark não executa o método recursivo (evita explosão de tempo); a célula aparece como `n/a` no csv e na tabela.

## documentação

ver [docs/RELATORIO.md](docs/RELATORIO.md) para definição do problema, complexidade, implementação e análise comparativa.
