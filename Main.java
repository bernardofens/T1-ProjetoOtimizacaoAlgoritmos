public class Main {
    public static void main(String[] args) {
        // Casos de teste: pares de strings
        String[][] testCases = {
            {"ABCBDAB", "BDCABA"},        // Exemplo clássico
            {"AAAA", "AAAA"},             // Strings iguais
            {"ABCD", "EFGH"},             // Sem caracteres comuns
            {"", "ABC"},                  // Uma string vazia
            {"AGGTAB", "GXTXAYB"},        // Outro exemplo comum
            {"abcdefghij", "acegikmoq"},  // Longas, pouca interseção
            {"programacao", "dinamica"}   // Palavras relacionadas ao tema
        };

        System.out.println("===============================================");
        System.out.println("     ANÁLISE DO ALGORITMO LCS (FORÇA BRUTA x PD)");
        System.out.println("===============================================\n");

        for (String[] pair : testCases) {
            String s1 = pair[0];
            String s2 = pair[1];

            System.out.println("Strings: \"" + s1 + "\" e \"" + s2 + "\"");

            // --- Recursivo ---
            LCS.resetCounters();
            long startTime = System.nanoTime();
            int lenRec = LCS.lcsRecursive(s1, s2);
            long endTime = System.nanoTime();
            long timeRec = endTime - startTime;
            long iterRec = LCS.iterationsRecursive;

            // --- Programação Dinâmica ---
            LCS.resetCounters();
            startTime = System.nanoTime();
            int lenDP = LCS.lcsDP(s1, s2);
            endTime = System.nanoTime();
            long timeDP = endTime - startTime;
            long iterDP = LCS.iterationsDP;

            // --- Resultados ---
            System.out.println("  Tamanho da LCS: " + lenDP);
            System.out.println("  Subsequência:   \"" + LCS.getLCS(s1, s2) + "\"");
            System.out.println("  Algoritmo Recursivo:");
            System.out.println("    - Iterações: " + iterRec);
            System.out.printf ("    - Tempo:     %.3f ms%n", timeRec / 1_000_000.0);
            System.out.println("  Algoritmo PD:");
            System.out.println("    - Iterações: " + iterDP);
            System.out.printf ("    - Tempo:     %.3f ms%n", timeDP / 1_000_000.0);
            System.out.println("-----------------------------------------------\n");
        }
    }
}