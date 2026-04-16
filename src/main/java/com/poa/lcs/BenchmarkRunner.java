package com.poa.lcs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/* benchmark runner
   esta classe organiza uma bateria de casos
   para cada caso ela executa lcs recursivo lcs dp e lcs memo
   depois imprime uma tabela no terminal
   se o usuario pedir ela tambem grava um csv em results */
public final class BenchmarkRunner {

    /* limiar de seguranca para nao chamar o recursivo em entradas grandes
       quando o tamanho total excede esse valor o recursivo e mostrado como n a */ 
    public static final int RECURSIVE_MAX_SUM_LENGTH = 25;

    /* estrutura para guardar um caso de teste
       name e um texto para identificar
       s1 e s2 sao as duas strings que serao comparadas */ 
    public record Case(String name, String s1, String s2) {
    }

    /* estrutura para uma linha de resultados do benchmark
       cada campo guarda um dado para imprimir e salvar no csv */ 
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

    /* construtor privado
       this e utilitaria e evita instanciacao acidental */ 
    private BenchmarkRunner() {
    }

    /* retorna uma lista fixa de casos
       serve para executar sem precisar passar argumentos na linha de comando */ 
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

    /* executa todos os casos recebidos e devolve uma lista de linhas
       ops e um contador reutilizavel para economizar objetos */ 
    public static List<Row> runCases(Iterable<Case> cases) {
        List<Row> rows = new ArrayList<>();
        OperationCounter ops = new OperationCounter();
        for (Case c : cases) {
            /* runsingle calcula uma linha completa para o caso atual */ 
            rows.add(runSingle(c, ops));
        }
        return rows;
    }

    /* calcula a linha de resultados de um caso
       a funcao mede tempo com nanotime e usa ops para contar eventos */ 
    static Row runSingle(Case c, OperationCounter ops) {
        String s1 = c.s1();
        String s2 = c.s2();

        /* m e n sao os tamanhos das strings
           isso ajuda a decidir se o recursivo pode ser executado */ 
        int m = s1.length();
        int n = s2.length();

        /* calcula dp primeiro
           dp e sempre executado porque tem complexidade previsivel */ 
        ops.reset();
        long t0 = System.nanoTime();
        int lenDp = LcsAlgorithms.lcsDp(s1, s2, ops);
        long nanosDp = System.nanoTime() - t0;
        long opsDp = ops.get();

        /* calcula memo segundo
           memo deve devolver a mesma lcs comprimento que dp */ 
        ops.reset();
        t0 = System.nanoTime();
        int lenMemo = LcsAlgorithms.lcsMemo(s1, s2, ops);
        long nanosMemo = System.nanoTime() - t0;
        long opsMemo = ops.get();

        /* validacao de consistencia
           se dp e memo divergem entao ha um erro logico */ 
        if (lenMemo != lenDp) {
            throw new IllegalStateException("lcs dp e memo divergem no caso " + c.name());
        }

        /* estes campos serao preenchidos depois
           recursivo pode ficar como n a dependendo do tamanho */ 
        String recTime;
        String recOps;
        if (m + n <= RECURSIVE_MAX_SUM_LENGTH) {
            /* recursivo executado quando e pequeno o suficiente
               isso evita tempo explosivo no pior caso */ 
            ops.reset();
            t0 = System.nanoTime();
            int lenRec = LcsAlgorithms.lcsRecursive(s1, s2, ops);
            long nanosRec = System.nanoTime() - t0;
            long opsRec = ops.get();
            if (lenRec != lenDp) {
                /* consistencia adicional no caso recursivo */ 
                throw new IllegalStateException("lcs recursivo diverge no caso " + c.name());
            }
            recTime = formatMs(nanosRec);
            recOps = Long.toString(opsRec);
        } else {
            /* recursivo nao executado
               isso evita travar o processo de benchmark */ 
            recTime = "n/a";
            recOps = "n/a";
        }

        /* cria a linha de resultados final
           ela contem tempos e contagens de cada metodo */ 
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

    /* converte nanos em milissegundos com 6 casas
       isso deixa a saida mais legivel */ 
    private static String formatMs(long nanos) {
        return String.format(Locale.ROOT, "%.6f", nanos / 1_000_000.0);
    }

    /* imprime uma tabela bonita no terminal
       a tabela lista um resumo por caso */ 
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

    /* escreve um csv no caminho indicado
       serve para depois gerar graficos */ 
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

    /* trata escape basico de csv para campos que contm virgula ou aspas
       se nao houver esses caracteres entao devolve como esta */ 
    private static String csvEscape(String s) {
        if (s.indexOf(',') < 0 && s.indexOf('"') < 0) {
            return s;
        }
        return '"' + s.replace("\"", "\"\"") + '"';
    }
}
