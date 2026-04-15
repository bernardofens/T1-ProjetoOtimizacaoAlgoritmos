package com.poa.lcs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ponto de entrada: demonstração com casos embutidos ou par s1 s2; opção {@code --csv}.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        boolean csv = false;
        List<String> rest = new ArrayList<>();
        for (String a : args) {
            if ("--csv".equals(a)) {
                csv = true;
            } else {
                rest.add(a);
            }
        }

        List<BenchmarkRunner.Case> cases;
        if (rest.size() >= 2) {
            String s1 = rest.get(0);
            String s2 = rest.get(1);
            cases = List.of(new BenchmarkRunner.Case("cli", s1, s2));
        } else {
            cases = BenchmarkRunner.defaultCases();
        }

        System.out.println("==============================================");
        System.out.println("  lcs — recursivo | dp (bottom-up) | memoização");
        System.out.println("==============================================");
        System.out.println("limiar recursivo: |s1|+|s2| <= " + BenchmarkRunner.RECURSIVE_MAX_SUM_LENGTH + " (acima: n/a)");
        System.out.println();

        OperationCounter ops = new OperationCounter();
        List<BenchmarkRunner.Row> rows = BenchmarkRunner.runCases(cases);

        for (int i = 0; i < cases.size(); i++) {
            BenchmarkRunner.Case c = cases.get(i);
            BenchmarkRunner.Row r = rows.get(i);
            String sub = LcsAlgorithms.reconstructLcs(c.s1(), c.s2());
            System.out.printf(Locale.ROOT, "caso \"%s\" — strings: \"%s\" e \"%s\"%n", c.name(), c.s1(), c.s2());
            System.out.printf(Locale.ROOT, "  comprimento lcs: %d | uma subsequência: \"%s\"%n", r.lcsLength(), sub);
            System.out.println("  ---");
        }

        BenchmarkRunner.printTable(rows);

        if (csv) {
            Path out = Path.of("results", "benchmark.csv");
            try {
                BenchmarkRunner.writeCsv(out, rows);
                System.out.println("csv gravado em: " + out.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("falha ao gravar csv: " + e.getMessage());
            }
        }
    }
}
