# t1 — maior subsequência comum (lcs)

**integrantes:** Bernardo Fensterseifer, Bernardo Klein Heitz, João Pedro Aiolfi, Rafael Toneto.

implementação em java: lcs recursiva, programação dinâmica (bottom-up), memoização, benchmark de tempo e operações, testes junit. relatório em `docs/RELATORIO.md` (exportar para pdf conforme `docs/ENTREGA.md`).

## requisitos

- java 17+
- maven: use `mvnw` / `mvnw.cmd` na raiz 

## compilar e testar

windows:

```text
.\mvnw.cmd clean test
```

ou `scripts\run-all-tests.bat`.

linux/mac:

```text
chmod +x mvnw scripts/run-all-tests.sh
./scripts/run-all-tests.sh
```

## executar

casos embutidos:

```text
.\mvnw.cmd -q compile exec:java
```

duas strings:

```text
.\mvnw.cmd -q compile exec:java "-Dexec.args=ABCBDAB BDCABA"
```

sem maven (windows): `scripts\run.bat`

## benchmark (csv)

```text
.\mvnw.cmd -q exec:java "-Dexec.args=--csv"
```

grava `results/benchmark.csv`. se `|s1|+|s2| > 25`, o recursivo não corre no benchmark (`n/a` na tabela).

## evidências para o relatório

windows: `scripts\generate-evidence.bat` (saída em `docs\evidencias\` e csv em `results\`).

## gráfico (opcional)

com csv gerado: `pip install matplotlib` e `python scripts/plot_benchmark.py` → `results/tempo_por_tamanho.png`

## entrega

lista de ficheiros e zip: `docs/ENTREGA.md`.
