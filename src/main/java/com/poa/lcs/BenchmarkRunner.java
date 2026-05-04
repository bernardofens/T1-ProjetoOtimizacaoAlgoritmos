package com.poa.lcs; //pacote com a classe BenchmarkRunner
import java.io.IOException; 
import java.nio.charset.StandardCharsets; 
import java.nio.file.Files; //classe para manipular arquivos
import java.nio.file.Path;
import java.util.ArrayList; //lista de casos
import java.util.List; //lista de resultados
import java.util.Locale; //localizacao
import java.util.Objects; //objetos
//benchmark runner classe que organiza uma bateria de casos para testar os algoritmos
   //esta classe organiza uma bateria de casos
   //para cada caso ela executa lcs recursivo lcs dp e lcs memo
   //depois imprime uma tabela no terminal
   //se o usuario pedir ela tambem grava um csv em results
public final class BenchmarkRunner {
    //limiar de seguranca para nao chamar o recursivo em entradas grandes
       //quando o tamanho total excede esse valor o recursivo e mostrado como n/a
    public static final int RECURSIVE_MAX_SUM_LENGTH = 25;
    //estrutura para guardar um caso de teste
    //name e um texto para identificar
    //s1 e s2 sao as duas strings que serao comparadas
    public record Case(String name, String s1, String s2) {
    }
    //estrutura para uma linha de resultados do benchmark
   //cada cam  po guarda um dado para imprimir e salvar no csv
    public record Row( 
            String name,                //nome do caso
            int len1,             //tamanho da primeira string
            int len2,             //tamanho da segunda string
            int lcsLength,             //comprimento da lcs
            String recursiveTime,             //tempo do recursivo
            String recursiveOps,          //contagem de operacoes do recursivo
            String dpTime,                   //tempo do dp
            String dpOps,             //contagem de operacoes do dp
            String memoTime,             //tempo do memo
            String memoOps             //contagem de operacoes do memo
    ) {
    }
    private BenchmarkRunner() {
    }
    public static List<Case> defaultCases() {
        List<Case> list = new ArrayList<>();
        list.add(new Case("classico", "ABCBDAB", "BDCABA"));           //caso classico
        list.add(new Case("iguais", "AAAA", "AAAA"));                   //caso iguais
        list.add(new Case("disjunto", "ABCD", "EFGH"));                //caso disjunto
        list.add(new Case("vazio", "", "ABC"));                           //caso vazio
        list.add(new Case("aggtab", "AGGTAB", "GXTXAYB"));             //caso aggtab
        list.add(new Case("prefixo", "abcdefghij", "acegikmoq"));         //caso prefixo
        list.add(new Case("tema", "programacao", "dinamica"));           //caso tema
        return list;
    }
    //executa todos os casos recebidos e devolve uma lista de linhas
       //ops e um contador reutilizavel para economizar objetos 
    public static List<Row> runCases(Iterable<Case> cases) {
        List<Row> rows = new ArrayList<>();
        OperationCounter ops = new OperationCounter();
        for (Case c : cases) {
            //runsingle calcula uma linha completa para o caso atual 
            rows.add(runSingle(c, ops));
        }
        return rows;
    }
    //calcula a linha de resultados de um caso
    //a funcao mede tempo com nanotime e usa ops para contar eventos 
    static Row runSingle(Case c, OperationCounter ops) {
        String s1 = c.s1();
        String s2 = c.s2();
        //m e n sao os tamanhos das strings
        //decidir se o recursivo pode ser executado 
        int m = s1.length(); 
        int n = s2.length();
        //calcula dp primeiro
        //dp e sempre executado pq tem complexidade previsivel o(m*n)
        ops.reset();
        long t0 = System.nanoTime();
        int lenDp = LcsAlgorithms.lcsDp(s1, s2, ops);
        long nanosDp = System.nanoTime() - t0;
        long opsDp = ops.get();
        //calcula memo segundo
        //memo deve devolver o mesmo comprimento de lcs que dp 
        ops.reset();
        t0 = System.nanoTime();
        int lenMemo = LcsAlgorithms.lcsMemo(s1, s2, ops);
        long nanosMemo = System.nanoTime() - t0;
        long opsMemo = ops.get();
        //validacao de consistencia
        //se dp e memo divergem 
        // entao ha um erro logico 
        if (lenMemo != lenDp) {
            throw new IllegalStateException("lcs dp e memo divergem no caso " + c.name());
        }
        //estes campos serao preenchidos depois
        //recursivo pode ficar como n a dependendo do tamanho 
        String recTime;
        String recOps;
        if (m + n <= RECURSIVE_MAX_SUM_LENGTH) {
               //recursivo executado quando e pequeno o suficiente
               //isso evita tempo explosivo no pior caso 
            ops.reset();
            t0 = System.nanoTime();
            int lenRec = LcsAlgorithms.lcsRecursive(s1, s2, ops);
            long nanosRec = System.nanoTime() - t0;
            long opsRec = ops.get();
            if (lenRec != lenDp) {
                //consistencia adicional no caso recursivo 
                throw new IllegalStateException("lcs recursivo diverge no caso " + c.name());
            }
            recTime = formatMs(nanosRec);
            recOps = Long.toString(opsRec);
        } else {
            //recursivo nao executado
            // evita travar o processo de benchmark 
            recTime = "n/a";      //se o recursivo nao foi executado 
            // entao o tempo e n/a
            recOps = "n/a"; //se o recursivo nao foi executado 
            // entao a contagem de operacoes e n/a
        }
        return new Row(//cria a linha de resultados final
            // ela contem tempos e contagens de cada metodo 

                c.name(), 
                m,             //tamanho da primeira string
                n,            //tamanho da segunda string
                lenDp,         //comprimento da lcs
                recTime,          //tempo do recursivo
                recOps,                //contagem de operacoes do recursivo
                formatMs(nanosDp),         //tempo do dp
                Long.toString(opsDp),     //contagem de operacoes do dp
                formatMs(nanosMemo),    //tempo do memo
                Long.toString(opsMemo)   //contagem de operacoes do memo
        );
    }
    private static String formatMs(long nanos) { //converte nanos em milissegundos com 6 casas
        //deixa a saida mais legivel
        return String.format(Locale.ROOT, "%.6f", nanos / 1_000_000.0);
    }
         //imprime uma tabela bonita no terminal
    //a tabela lista um resumo por caso 
    public static void printTable(List<Row> rows) {    //imprime a tabela no terminal
        String headerFmt = "| %-10s | %4s | %4s | %3s | %12s | %10s | %12s | %10s | %12s | %10s |%n"; 
        String rowFmt = "| %-10s | %4d | %4d | %3d | %12s | %10s | %12s | %10s | %12s | %10s |%n";
        System.out.println();
        System.out.println("comparativo de desempenho (tempo em ms)");
        System.out.println("-".repeat(120));
        System.out.printf(Locale.ROOT, headerFmt,
                "caso", "|s1|", "|s2|", "lcs", "t_rec(ms)", "ops_rec", "t_dp(ms)", "ops_dp", "t_memo(ms)", "ops_memo");
        System.out.println("-".repeat(120));
        for (Row r : rows) {
            System.out.printf(Locale.ROOT, rowFmt,         //imprime a linha do csv
                    r.name(), r.len1(), r.len2(), r.lcsLength(),       //nome do caso, tamanho da primeira string, tamanho da segunda string, comprimento da lcs
                    r.recursiveTime(), r.recursiveOps(),     //tempo do recursivo, contagem de operacoes do recursivo
                    r.dpTime(), r.dpOps(),             //tempo do dp, contagem de operacoes do dp
                    r.memoTime(), r.memoOps());            //tempo do memo, contagem de operacoes do memo
        }
        System.out.println("-".repeat(120)); 
    }
         //escreve um csv no caminho indicado
    //serve para depois gerar graficos
    //caso nao haja parentes o caminho e criado automaticamente
    public static void writeCsv(Path path, List<Row> rows) throws IOException { 
        Objects.requireNonNull(path, "path"); //verifica se o caminho nao e nulo
        Files.createDirectories(path.getParent() != null ? path.getParent() : Path.of(".")); //cria o diretorio do csv
        StringBuilder sb = new StringBuilder(); //constroi a string do csv
        sb.append("caso,len1,len2,lcs,t_rec_ms,ops_rec,t_dp_ms,ops_dp,t_memo_ms,ops_memo\n");  //cabecalho do csv
        for (Row r : rows) {
            sb.append(csvEscape(r.name())).append(',')      //escapa o nome do caso
                    .append(r.len1()).append(',')   //tamanho da primeira string
                    .append(r.len2()).append(',')      //tamanho da segunda string
                    .append(r.lcsLength()).append(',')         //comprimento da lcs
                    .append(r.recursiveTime()).append(',')   //tempo do recursivo
                    .append(r.recursiveOps()).append(',')    //contagem de operacoes do recursivo
                    .append(r.dpTime()).append(',')               //tempo do dp
                    .append(r.dpOps()).append(',')            //contagem de operacoes do dp
                    .append(r.memoTime()).append(',')          //tempo do memo
                    .append(r.memoOps()).append('\n');      //contagem de operacoes do memo
        }
        Files.writeString(path, sb.toString(), StandardCharsets.UTF_8); //escreve o csv no arquivo
    }
          //trata escape basico de csv para campos que contm virgula ou aspas
    //se nao houver esses caracteres 
    // entao devolve como esta 
    private static String csvEscape(String s) {
        if (s.indexOf(',') < 0 && s.indexOf('"') < 0) { 
            return s;
        }
        return '"' + s.replace("\"", "\"\"") + '"'; 
    }
}
