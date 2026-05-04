#!/usr/bin/env python3
# script para gerar grafico a partir de results benchmark csv
# le o arquivo csv e extrai os tempos de dp memo e recursivo
# depois salva uma figura em results tempo por caso png
# requer matplotlib instalado

from __future__ import annotations
import csv             # modulo para leitura de csv
import sys              # modulo para mensagens de erro
from pathlib import Path  # modulo para caminhos de arquivos

try:
    import matplotlib.pyplot as plt  # modulo que permite desenhar o grafico
except ImportError:
    print("instale matplotlib: pip install matplotlib", file=sys.stderr)
    sys.exit(1)

def main() -> None:
    # calcula o caminho raiz do projeto
    root = Path(__file__).resolve().parent.parent
    # caminho completo do arquivo csv gerado pelo benchmark
    csv_path = root / "results" / "benchmark.csv"
    # valida se o arquivo existe antes de ler
    if not csv_path.is_file():
        print(f"arquivo nao encontrado: {csv_path}", file=sys.stderr)
        print("gere com: mvnw -q exec:java -Dexec.args=\"--csv\"", file=sys.stderr)
        sys.exit(1)

    casos: list[str] = []     # lista com o nome de cada caso
    t_dp: list[float] = []    # lista com o tempo do metodo dp
    t_memo: list[float] = []    # lista com o tempo do metodo memo
    t_rec: list[float] = []    # lista com o tempo do metodo recursivo
    # abre o csv em modo texto
    with csv_path.open(encoding="utf-8") as f:
        reader = csv.DictReader(f)         # cria um leitor que usa a primeira linha como cabecalho
        # percorre cada linha do csv
        for row in reader:
            casos.append(row["caso"])                                    # extrai o nome do caso
            t_dp.append(float(row["t_dp_ms"]))                             # extrai o tempo dp como float
            t_memo.append(float(row["t_memo_ms"]))                         # extrai o tempo memo como float
            tr = row["t_rec_ms"]                                          # recupera o valor de tempo rec
            t_rec.append(float(tr) if tr != "n/a" else float("nan"))         # extrai o tempo recursivo como float

    x = range(len(casos))                      
    plt.figure(figsize=(10, 5))                     
    plt.plot(x, t_dp, marker="o", label="dp")                # desenha a curva dp
    plt.plot(x, t_memo, marker="s", label="memo")                # desenha a curva memo
    plt.plot(x, t_rec, marker="^", label="recursivo")          # desenha a curva recursiva
    plt.xticks(x, casos, rotation=45, ha="right")          
    plt.ylabel("tempo (ms)")                               
    plt.title("lcs — tempo por caso (benchmark)")    
    plt.legend()                                       
    plt.tight_layout()                                
    out = root / "results" / "tempo_por_tamanho.png"    # caminho completo para o arquivo png de saida
    out.parent.mkdir(parents=True, exist_ok=True)    
    plt.savefig(out, dpi=150)   
    print(f"grafico salvo em: {out}")    

if __name__ == "__main__":    # se o script for executado diretamente
    main()
