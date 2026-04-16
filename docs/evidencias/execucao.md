# evidencias de execucao

este arquivo contem um exemplo real de execucao do programa

## comando usado

```text
.\mvnw.cmd -q -dskiptests compile exec:java "-dexec.args=--csv"
```

## saida do terminal

```text
==============================================
  lcs � recursivo | dp (bottom-up) | memoiza��o
==============================================
limiar recursivo: |s1|+|s2| <= 25 (acima: n/a)

caso "classico" � strings: "ABCBDAB" e "BDCABA"
  comprimento lcs: 4 | uma subsequ�ncia: "BCBA"
  ---
caso "iguais" � strings: "AAAA" e "AAAA"
  comprimento lcs: 4 | uma subsequ�ncia: "AAAA"
  ---
caso "disjunto" � strings: "ABCD" e "EFGH"
  comprimento lcs: 0 | uma subsequ�ncia: ""
  ---
caso "vazio" � strings: "" e "ABC"
  comprimento lcs: 0 | uma subsequ�ncia: ""
  ---
caso "aggtab" � strings: "AGGTAB" e "GXTXAYB"
  comprimento lcs: 4 | uma subsequ�ncia: "GTAB"
  ---
caso "prefixo" � strings: "abcdefghij" e "acegikmoq"
  comprimento lcs: 5 | uma subsequ�ncia: "acegi"
  ---
caso "tema" � strings: "programacao" e "dinamica"
  comprimento lcs: 4 | uma subsequ�ncia: "amca"
  ---

comparativo de desempenho (tempo em ms)
------------------------------------------------------------------------------------------------------------------------
| caso       | |s1| | |s2| | lcs |    t_rec(ms) |    ops_rec |     t_dp(ms) |     ops_dp |   t_memo(ms) |   ops_memo |
------------------------------------------------------------------------------------------------------------------------
| classico   |    7 |    6 |   4 |     0.023100 |        152 |     0.948400 |         42 |     0.032300 |         42 |
| iguais     |    4 |    4 |   4 |     0.001300 |          5 |     0.005100 |         16 |     0.016700 |          5 |
| disjunto   |    4 |    4 |   0 |     0.021400 |        139 |     0.004500 |         16 |     0.006400 |         33 |
| vazio      |    0 |    3 |   0 |     0.000500 |          1 |     0.001100 |          0 |     0.000900 |          1 |
| aggtab     |    6 |    7 |   4 |     0.038400 |        346 |     0.007900 |         42 |     0.008700 |         45 |
| prefixo    |   10 |    9 |   5 |     1.726700 |      83726 |     0.025000 |         90 |     0.028400 |        176 |
| tema       |   11 |    8 |   4 |     0.312200 |      15714 |     0.015000 |         88 |     0.024200 |        135 |
------------------------------------------------------------------------------------------------------------------------
csv gravado em: c:\users\joxto\downloads\t1-projetootimizacaoalgoritmos\results\benchmark.csv
```

observacao

a saida acima mostra caracteres quebrados no windows por causa de codificacao do console
para corrigir execute `chcp 65001` antes de rodar o programa

