#!/usr/bin/env python3
"""
lê results/benchmark.csv (gerado com: mvnw -q exec:java -Dexec.args="--csv")
e gera results/tempo_por_tamanho.png — requer matplotlib.

uso: python scripts/plot_benchmark.py
"""

from __future__ import annotations

import csv
import sys
from pathlib import Path

try:
    import matplotlib.pyplot as plt
except ImportError:
    print("instale matplotlib: pip install matplotlib", file=sys.stderr)
    sys.exit(1)


def main() -> None:
    root = Path(__file__).resolve().parent.parent
    csv_path = root / "results" / "benchmark.csv"
    if not csv_path.is_file():
        print(f"arquivo nao encontrado: {csv_path}", file=sys.stderr)
        print("gere com: mvnw -q exec:java -Dexec.args=\"--csv\"", file=sys.stderr)
        sys.exit(1)

    casos: list[str] = []
    t_dp: list[float] = []
    t_memo: list[float] = []
    t_rec: list[float] = []

    with csv_path.open(encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            casos.append(row["caso"])
            t_dp.append(float(row["t_dp_ms"]))
            t_memo.append(float(row["t_memo_ms"]))
            tr = row["t_rec_ms"]
            t_rec.append(float(tr) if tr != "n/a" else float("nan"))

    x = range(len(casos))
    plt.figure(figsize=(10, 5))
    plt.plot(x, t_dp, marker="o", label="dp")
    plt.plot(x, t_memo, marker="s", label="memo")
    plt.plot(x, t_rec, marker="^", label="recursivo")
    plt.xticks(x, casos, rotation=45, ha="right")
    plt.ylabel("tempo (ms)")
    plt.title("lcs — tempo por caso (benchmark)")
    plt.legend()
    plt.tight_layout()
    out = root / "results" / "tempo_por_tamanho.png"
    out.parent.mkdir(parents=True, exist_ok=True)
    plt.savefig(out, dpi=150)
    print(f"grafico salvo em: {out}")


if __name__ == "__main__":
    main()
