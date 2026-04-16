package com.poa.lcs;
//arquivo principal
   //este arquivo nao implementa o algoritmo em si
   //ele apenas organiza a execucao e a saida no terminal */
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public final class Main {//classe que coordena os exemplos e o benchmark
    private Main() {
    }
    public static void main(String[] args) {
        //se true entao o programa gera results benchmark csv
        boolean csv = false;
        //lista com os argumentos que nao sao a flag csv    
        List<String> rest = new ArrayList<>();
        for (String a : args) {
            if ("--csv".equals(a)) {
                //habilita exportacao para csv
                csv = true;
            } else {
                rest.add(a); //guarda os argumentos para usar como s1 e s2
            }
        }
        List<BenchmarkRunner.Case> cases;  //lista de casos que o benchmark vai executar
        if (rest.size() >= 2) {
            //se o usuario passou dois argumentos entao trata como s1 e s2
            String s1 = rest.get(0);
            String s2 = rest.get(1);
            cases = List.of(new BenchmarkRunner.Case("cli", s1, s2));
        } else {
            //se nao passou entao usa casos embutidos para facilitar
            cases = BenchmarkRunner.defaultCases();
        }
        //cabecalho para tornar a saida legivel
        System.out.println("==============================================");
        System.out.println("  lcs — recursivo | dp (bottom-up) | memoização");
        System.out.println("==============================================");
        System.out.println("limiar recursivo: |s1|+|s2| <= " + BenchmarkRunner.RECURSIVE_MAX_SUM_LENGTH + " (acima: n/a)");
        System.out.println();
        //executa todos os casos e recebe as linhas com tempos e operacoes
        List<BenchmarkRunner.Row> rows = BenchmarkRunner.runCases(cases);
        for (int i = 0; i < cases.size(); i++) {
            //caso atual e linha de resultados
            BenchmarkRunner.Case c = cases.get(i);
            BenchmarkRunner.Row r = rows.get(i);
            //reconstrucao de uma lcs maxima com base na tabela dp
            String sub = LcsAlgorithms.reconstructLcs(c.s1(), c.s2());
            System.out.printf(Locale.ROOT, "caso \"%s\" — strings: \"%s\" e \"%s\"%n", c.name(), c.s1(), c.s2());
            System.out.printf(Locale.ROOT, "  comprimento lcs: %d | uma subsequência: \"%s\"%n", r.lcsLength(), sub);
            System.out.println("  ---");
        }
        BenchmarkRunner.printTable(rows); //imprime a tabela comparativa completa
        if (csv) {
            Path out = Path.of("results", "benchmark.csv");   //caminho de saida do csv 
            try {
                BenchmarkRunner.writeCsv(out, rows);//escreve o csv com os dados do benchmark
                System.out.println("csv gravado em: " + out.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("falha ao gravar csv: " + e.getMessage());//mensagem de erro sem interromper todo o programa 
            }
        }
    }
}
