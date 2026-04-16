# relatório: maior subsequência comum (lcs)

## 1. descrição do problema

dadas duas sequências (aqui, strings) `s1` e `s2`, uma **subsequência** de `s1` obtém-se removendo zero ou mais símbolos sem alterar a ordem relativa dos restantes. a **maior subsequência comum** (lcs) é uma subsequência de `s1` que também é subsequência de `s2` e tem comprimento máximo possível.

o comprimento da lcs é único; a string que a representa pode não ser única (há várias lcs distintas com o mesmo comprimento).

## 2. exemplos

| s1 | s2 | comprimento da lcs | uma lcs possível |
|----|----|--------------------|------------------|
| `ABCBDAB` | `BDCABA` | 4 | `BCBA` (outras existem) |
| `AAAA` | `AAAA` | 4 | `AAAA` |
| `ABCD` | `EFGH` | 0 | `` |
| `` | `ABC` | 0 | `` |

## 3. algoritmo recursivo

sejam `i` e `j` os comprimentos dos prefixos de `s1` e `s2` considerados.

- se `i = 0` ou `j = 0`, a lcs tem comprimento 0;
- se `s1[i-1] = s2[j-1]`, esse símbolo pode pertencer à lcs: `1 + lcs(i-1, j-1)`;
- caso contrário, `lcs(i,j) = max(lcs(i-1,j), lcs(i,j-1))`.

subproblemas repetem-se exponencialmente na árvore de recursão (sem memoização). **tempo:** no pior caso exponencial em `m` e `n`; **espaço:** `O(m+n)` na pilha de chamadas.

neste projeto, **operação** no recursivo = uma entrada na função recursiva (inclui folhas).

## 4. programação dinâmica

### 4.1 bottom-up (matriz `dp`)

preenche-se uma tabela `dp[i][j]` para `0 ≤ i ≤ m`, `0 ≤ j ≤ n` na ordem crescente de `i` e `j`, usando a mesma recorrência. **tempo:** `O(m·n)`; **espaço:** `O(m·n)`.

**operação** no dp = uma iteração da célula interna `(i,j)` com `i ≥ 1` e `j ≥ 1`.

### 4.2 memoização (top-down)

a mesma recorrência, mas só se calcula cada par `(i,j)` uma vez, guardando resultados numa tabela `memo[i][j]`. **tempo:** `O(m·n)` estados; **espaço:** `O(m·n)` mais a pilha da recursão.

**operação** no memo = uma entrada no helper recursivo (inclui retornos de cache).

## 5. implementação (neste repositório)

| classe | papel |
|--------|--------|
| `com.poa.lcs.LcsAlgorithms` | `lcsRecursive`, `lcsDp`, `lcsMemo`, `reconstructLcs` |
| `com.poa.lcs.OperationCounter` | contador de operações por execução |
| `com.poa.lcs.LcsStats` | registro genérico (comprimento, operações, nanos) — extensível para outras medições |
| `com.poa.lcs.BenchmarkRunner` | casos de teste, tabela, exportação csv, limiar `RECURSIVE_MAX_SUM_LENGTH = 25` |
| `com.poa.lcs.Main` | entrada: sem argumentos (casos padrão), dois argumentos `s1 s2`, ou `--csv` |

tempo medido com `System.nanoTime()` em torno de cada chamada, convertido para milissegundos na saída.

## 6. resultados experimentais

este repositorio inclui evidencias geradas automaticamente

- log da execucao no terminal em `docs/evidencias/execucao_terminal.txt`
- tabela em markdown em `docs/evidencias/execucao.md`
- dados em csv em `results/benchmark.csv`

para gerar novamente

```text
scripts\generate-evidence.bat
```

### 6.1 resultados coletados

abaixo esta um recorte real do `results/benchmark.csv` gerado neste projeto

| caso | len1 | len2 | lcs | t_rec_ms | ops_rec | t_dp_ms | ops_dp | t_memo_ms | ops_memo |
|------|------|------|-----|----------|---------|---------|--------|-----------|----------|
| classico | 7 | 6 | 4 | 0.023100 | 152 | 0.948400 | 42 | 0.032300 | 42 |
| iguais | 4 | 4 | 4 | 0.001300 | 5 | 0.005100 | 16 | 0.016700 | 5 |
| disjunto | 4 | 4 | 0 | 0.021400 | 139 | 0.004500 | 16 | 0.006400 | 33 |
| vazio | 0 | 3 | 0 | 0.000500 | 1 | 0.001100 | 0 | 0.000900 | 1 |
| aggtab | 6 | 7 | 4 | 0.038400 | 346 | 0.007900 | 42 | 0.008700 | 45 |
| prefixo | 10 | 9 | 5 | 1.726700 | 83726 | 0.025000 | 90 | 0.028400 | 176 |
| tema | 11 | 8 | 4 | 0.312200 | 15714 | 0.015000 | 88 | 0.024200 | 135 |

1. rode `.\mvnw.cmd -q compile exec:java "-Dexec.args=--csv"`.
2. copie a tabela do terminal ou importe `results/benchmark.csv`.

observação esperada: para pares com `|s1|+|s2|` moderado, o recursivo acumula muitas operações e tempo maior que dp/memo; dp e memo têm ordem semelhante em tempo para os mesmos tamanhos, mas as **contagens de operações** não são diretamente comparáveis entre métodos (definições diferentes).

## 7. análise comparativa e quando usar cada abordagem

- **recursivo puro:** útil para **didática** e **instâncias minúsculas**; inaceitável para entradas grandes.
- **dp bottom-up:** previsível, iterativo, bom quando se quer preencher toda a tabela e depois **reconstruir** a subsequência (como em `reconstructLcs`).
- **memoização:** mesma ordem assintótica; pode evitar trabalho em padrões específicos se muitos estados não forem alcançados (neste problema clássico de lcs em strings completas, costuma visitar `O(m·n)` estados).

## referências de execução (exemplo de entrada e saída)

**entrada** (linha de comando):

```text
.\mvnw.cmd -q compile exec:java "-Dexec.args=ABCBDAB BDCABA"
```

**saída** (trecho típico):

```text
caso "cli" — strings: "ABCBDAB" e "BDCABA"
  comprimento lcs: 4 | uma subsequência: "BCBA"
  ---
```

a tabela comparativa lista tempos em ms e contagens `ops_rec`, `ops_dp`, `ops_memo`.
