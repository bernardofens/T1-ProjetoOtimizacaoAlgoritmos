# evidencias de execucao

este arquivo contem um exemplo real de execucao do programa.

## comando usado

```text
scripts\generate-evidence.bat
```

isso gera:

- `docs/evidencias/execucao_terminal.txt`
- `results/benchmark.csv`

comando maven equivalente:

```text
.\mvnw.cmd -q -DskipTests compile exec:java "-Dexec.args=--csv"
```

## saida de exemplo do terminal

```text
==============================================
  lcs — recursivo | dp (bottom-up) | memoização
==============================================
limiar recursivo: |s1|+|s2| <= 25 (acima: n/a)

caso "classico" — strings: "ABCBDAB" e "BDCABA"
  comprimento lcs: 4 | uma subsequência: "BCBA"
  ---
caso "iguais" — strings: "AAAA" e "AAAA"
  comprimento lcs: 4 | uma subsequência: "AAAA"
  ---
caso "disjunto" — strings: "ABCD" e "EFGH"
  comprimento lcs: 0 | uma subsequência: ""
  ---
caso "vazio" — strings: "" e "ABC"
  comprimento lcs: 0 | uma subsequência: ""
  ---
caso "aggtab" — strings: "AGGTAB" e "GXTXAYB"
  comprimento lcs: 4 | uma subsequência: "GTAB"
  ---
caso "prefixo" — strings: "abcdefghij" e "acegikmoq"
  comprimento lcs: 5 | uma subsequência: "acegi"
  ---
caso "tema" — strings: "programacao" e "dinamica"
  comprimento lcs: 4 | uma subsequência: "amca"
  ---

comparativo de desempenho (tempo em ms)
------------------------------------------------------------------------------------------------------------------------
| caso       | |s1| | |s2| | lcs |    t_rec(ms) |    ops_rec |     t_dp(ms) |     ops_dp |   t_memo(ms) |   ops_memo |
------------------------------------------------------------------------------------------------------------------------
| classico   |    7 |    6 |   4 |     0.034000 |        152 |     1.464700 |         42 |     0.041700 |       42 |
| iguais     |    4 |    4 |   4 |     0.001900 |          5 |     0.005200 |         16 |     0.012300 |        5 |
| disjunto   |    4 |    4 |   0 |     0.024400 |        139 |     0.006700 |         16 |     0.009200 |       33 |
| vazio      |    0 |    3 |   0 |     0.000600 |          1 |     0.002300 |          0 |     0.001200 |        1 |
| aggtab     |    6 |    7 |   4 |     0.074900 |        346 |     0.011700 |         42 |     0.013500 |       45 |
| prefixo    |   10 |    9 |   5 |     3.123500 |      83726 |     0.018400 |         90 |     0.038000 |      176 |
| tema       |   11 |    8 |   4 |     0.951900 |      15714 |     0.021500 |         88 |     0.035300 |      135 |
------------------------------------------------------------------------------------------------------------------------
csv gravado em: F:\Game Projects\Godot 4\T1-ProjetoOtimizacaoAlgoritmos\results\benchmark.csv
```

## execucao manual mostrada no segundo print

```text
==============================================
  lcs ? recursivo | dp (bottom-up) | memoização
==============================================
limiar recursivo: |s1|+|s2| <= 25 (acima: n/a)

caso "cli" ? strings: "ABCBDAB" e "BDCABA"
  comprimento lcs: 4 | uma subsequência: "BCBA"
  ---

comparativo de desempenho (tempo em ms)
------------------------------------------------------------------------------------------------------------------------
| caso       | |s1| | |s2| | lcs |    t_rec(ms) |    ops_rec |     t_dp(ms) |     ops_dp |   t_memo(ms) |   ops_memo |
------------------------------------------------------------------------------------------------------------------------
| cli        |    7 |    6 |   4 |     0.030300 |        152 |     0.860200 |         42 |     0.038000 |         42 |
------------------------------------------------------------------------------------------------------------------------
```

observacao: os tempos podem variar entre execucoes e maquinas, mas este arquivo foi alinhado aos valores exibidos nos prints `images/print1.jpeg` e `images/print2.jpeg`.
