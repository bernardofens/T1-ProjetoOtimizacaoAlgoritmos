# instrucoes de entrega

## objetivo

este arquivo descreve como montar a entrega final em zip com
- codigo fonte
- relatorio em pdf
- comandos para compilar executar e testar

## gerando o relatorio em pdf

o relatorio principal esta em `docs/RELATORIO.md`

opcao 1 usando pandoc

```text
pandoc docs/RELATORIO.md -o docs/RELATORIO.pdf
```

opcao 2 usando editor

- abra `docs/RELATORIO.md` no vscode ou no word
- exporte para pdf
- salve como `docs/RELATORIO.pdf`

## gerando evidencias de execucao

no windows

```text
scripts\generate-evidence.bat
```

isso gera
- `results/benchmark.csv`
- `docs/evidencias/execucao_terminal.txt`

## montando o zip

crie um arquivo `entrega.zip` contendo no minimo

```
pom.xml
mvnw
mvnw.cmd
.mvn/
src/
scripts/
docs/RELATORIO.pdf
docs/RELATORIO.md
docs/ENTREGA.md
docs/evidencias/
```

## validacao rapida antes de zipar

```text
.\mvnw.cmd -q clean test
.\mvnw.cmd -q -DskipTests compile exec:java "-Dexec.args=--csv"
```

