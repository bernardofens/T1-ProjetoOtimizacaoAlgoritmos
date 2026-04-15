package com.poa.lcs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * executa benchmarks comparando recursivo, dp e memoização; imprime tabela e opcionalmente grava csv.
 */
public final class BenchmarkRunner {

    /** não executa o recursivo quando {@code s1.length() + s2.length()} excede este valor (evita explosão). */
    public static final int RECURSIVE_MAX_SUM_LENGTH = 25;

    public record Case(String name, String s1, String s2) {
    }

    public record Row(
            String name,
            int len1,
            int len2,
            int lcsLength,
            String recursiveTime,
            String recursiveOps,
            String dpTime,
            String dpOps,
            String memoTime,
            String memoOps
    ) {
    }

    private BenchmarkRunner() {
    }

    public static List<Case> defaultCases() {
        List<Case> list = new ArrayList<>();
        list.add(new Case("classico", "ABCBDAB", "BDCABA"));
        list.add(new Case("iguais", "AAAA", "AAAA"));
        list.add(new Case("disjunto", "ABCD", "EFGH"));
        list.add(new Case("vazio", "", "ABC"));
        list.add(new Case("aggtab", "AGGTAB", "GXTXAYB"));
        list.add(new Case("prefixo", "abcdefghij", "acegikmoq"));
        list.add(new Case("tema", "programacao", "dinamica"));
        return list;
    }

    public static List<Row> runCases(Iterable<Case> cases) {
        List<Row> rows = new ArrayList<>();
        OperationCounter ops = new OperationCounter();
        for (Case c : cases) {
            rows.add(runSingle(c, ops));
        }
        return rows;
    }

    static Row runSingle(Case c, OperationCounter ops) {
        String s1 = c.s1();
        String s2 = c.s2();
        int m = s1.length();
        int n = s2.length();

        ops.reset();
        long t0 = System.nanoTime();
        int lenDp = LcsAlgorithms.lcsDp(s1, s2, ops);
        long nanosDp = System.nanoTime() - t0;
        long opsDp = ops.get();

        ops.reset();
        t0 = System.nanoTime();
        int lenMemo = LcsAlgorithms.lcsMemo(s1, s2, ops);
        long nanosMemo = System.nanoTime() - t0;
        long opsMemo = ops.get();

        if (lenMemo != lenDp) {
            throw new IllegalStateException("lcs dp e memo divergem no caso " + c.name());
        }

        String recTime;
        String recOps;
        if (m + n <= RECURSIVE_MAX_SUM_LENGTH) {
            ops.reset();
            t0 = System.nanoTime();
            int lenRec = LcsAlgorithms.lcsRecursive(s1, s2, ops);
            long nanosRec = System.nanoTime() - t0;
            long opsRec = ops.get();
            if (lenRec != lenDp) {
                throw new IllegalStateException("lcs recursivo diverge no caso " + c.name());
            }
            recTime = formatMs(nanosRec);
            recOps = Long.toString(opsRec);
        } else {
            recTime = "n/a";
            recOps = "n/a";
        }

        return new Row(
                c.name(),
                m,
                n,
                lenDp,
                recTime,
                recOps,
                formatMs(nanosDp),
                Long.toString(opsDp),
                formatMs(nanosMemo),
                Long.toString(opsMemo)
        );
    }

    private static String formatMs(long nanos) {
        return String.format(Locale.ROOT, "%.6f", nanos / 1_000_000.0);
    }

    public static void printTable(List<Row> rows) {
        String headerFmt = "| %-10s | %4s | %4s | %3s | %12s | %10s | %12s | %10s | %12s | %10s |%n";
        String rowFmt = "| %-10s | %4d | %4d | %3d | %12s | %10s | %12s | %10s | %12s | %10s |%n";
        System.out.println();
        System.out.println("comparativo de desempenho (tempo em ms)");
        System.out.println("-".repeat(120));
        System.out.printf(Locale.ROOT, headerFmt,
                "caso", "|s1|", "|s2|", "lcs", "t_rec(ms)", "ops_rec", "t_dp(ms)", "ops_dp", "t_memo(ms)", "ops_memo");
        System.out.println("-".repeat(120));
        for (Row r : rows) {
            System.out.printf(Locale.ROOT, rowFmt,
                    r.name(), r.len1(), r.len2(), r.lcsLength(),
                    r.recursiveTime(), r.recursiveOps(),
                    r.dpTime(), r.dpOps(),
                    r.memoTime(), r.memoOps());
        }
        System.out.println("-".repeat(120));
    }

    public static void writeCsv(Path path, List<Row> rows) throws IOException {
        Objects.requireNonNull(path, "path");
        Files.createDirectories(path.getParent() != null ? path.getParent() : Path.of("."));
        StringBuilder sb = new StringBuilder();
        sb.append("caso,len1,len2,lcs,t_rec_ms,ops_rec,t_dp_ms,ops_dp,t_memo_ms,ops_memo\n");
        for (Row r : rows) {
            sb.append(csvEscape(r.name())).append(',')
                    .append(r.len1()).append(',')
                    .append(r.len2()).append(',')
                    .append(r.lcsLength()).append(',')
                    .append(r.recursiveTime()).append(',')
                    .append(r.recursiveOps()).append(',')
                    .append(r.dpTime()).append(',')
                    .append(r.dpOps()).append(',')
                    .append(r.memoTime()).append(',')
                    .append(r.memoOps()).append('\n');
        }
        Files.writeString(path, sb.toString(), StandardCharsets.UTF_8);
    }

    private static String csvEscape(String s) {
        if (s.indexOf(',') < 0 && s.indexOf('"') < 0) {
            return s;
        }
        return '"' + s.replace("\"", "\"\"") + '"';
    }
}
