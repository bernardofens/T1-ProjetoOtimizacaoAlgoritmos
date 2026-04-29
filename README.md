# t1 — maior subsequência comum (lcs)

**integrantes:** Bernardo Fensterseifer, Bernardo Klein Heitz, João Pedro Aiolfi, Rafael Toneto.

implementação em java: lcs recursiva, programação dinâmica (bottom-up), memoização, benchmark de tempo e operações, testes junit e relatório em pdf

## compilar e testar
windows:
```text
.\mvnw.cmd clean test
```

ou `scripts\run-all-tests.bat`.

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


