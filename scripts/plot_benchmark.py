#!/usr/bin/env python3
# script para gerar grafico a partir de results benchmark csv
# ele le o arquivo csv e extrai os tempos de dp memo e recursivo
# depois salva uma figura em results tempo por caso png
# requer matplotlib instalado

from __future__ import annotations
import csv  # modulo para leitura de csv
import sys  # modulo para mensagens de erro
from pathlib import Path  # modulo para caminhos de arquivos

try:
    import matplotlib.pyplot as plt  # modulo que permite desenhar o grafico
except ImportError:
    # avisa que o modulo nao esta instalado
    print("instale matplotlib: pip install matplotlib", file=sys.stderr)
    sys.exit(1)

def main() -> None:
    # calcula o caminho raiz do projeto
    root = Path(__file__).resolve().parent.parent
    # caminho completo do arquivo csv gerado pelo benchmark
    csv_path = root / "results" / "benchmark.csv"
    # valida se o arquivo existe antes de ler
    if not csv_path.is_file():
        # mensagem de erro para o usuario
        print(f"arquivo nao encontrado: {csv_path}", file=sys.stderr)
        # dica de como gerar o csv novamente
        print("gere com: mvnw -q exec:java -Dexec.args=\"--csv\"", file=sys.stderr)
        sys.exit(1)

    # lista com o nome de cada caso
    casos: list[str] = []
    # lista com o tempo do metodo dp
    t_dp: list[float] = []
    # lista com o tempo do metodo memo
    t_memo: list[float] = []
    # lista com o tempo do metodo recursivo
    t_rec: list[float] = []

    # abre o csv em modo texto
    with csv_path.open(encoding="utf-8") as f:
        # cria um leitor que usa a primeira linha como cabecalho
        reader = csv.DictReader(f)
        # percorre cada linha do csv
        for row in reader:
            # extrai o nome do caso
            casos.append(row["caso"])
            # extrai o tempo dp como float
            t_dp.append(float(row["t_dp_ms"]))
            # extrai o tempo memo como float
            t_memo.append(float(row["t_memo_ms"]))
            # recupera o valor de tempo rec
            tr = row["t_rec_ms"]
            # se o recursivo nao foi executado entao o csv guarda n a
            # convertemos n a em nan para nao quebrar o grafico
            t_rec.append(float(tr) if tr != "n/a" else float("nan"))

    # eixo x e apenas um indice numerico
    x = range(len(casos))
    # cria a figura com tamanho definido
    plt.figure(figsize=(10, 5))
    # desenha a curva dp
    plt.plot(x, t_dp, marker="o", label="dp")
    # desenha a curva memo
    plt.plot(x, t_memo, marker="s", label="memo")
    # desenha a curva recursiva
    plt.plot(x, t_rec, marker="^", label="recursivo")
    # coloca os nomes dos casos como rotulos do eixo x
    plt.xticks(x, casos, rotation=45, ha="right")
    plt.ylabel("tempo (ms)")
    plt.title("lcs — tempo por caso (benchmark)")
    plt.legend()
    plt.tight_layout()
    # caminho completo para o arquivo png de saida
    out = root / "results" / "tempo_por_tamanho.png"
    # garante que a pasta exista
    out.parent.mkdir(parents=True, exist_ok=True)
    # salva a imagem em disco
    plt.savefig(out, dpi=150)
    print(f"grafico salvo em: {out}")

if __name__ == "__main__":
    main()
