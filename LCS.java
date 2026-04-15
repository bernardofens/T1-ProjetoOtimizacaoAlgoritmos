public class LCS {

    // Contadores para análise de desempenho
    public static long iterationsRecursive = 0;
    public static long iterationsDP = 0;

    // ------------------------------------------------------------
    // Algoritmo Recursivo (Força Bruta)
    // ------------------------------------------------------------
    public static int lcsRecursive(String s1, String s2, int i, int j) {
        iterationsRecursive++;
        if (i == 0 || j == 0) {
            return 0;
        }
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            return 1 + lcsRecursive(s1, s2, i - 1, j - 1);
        } else {
            return Math.max(
                lcsRecursive(s1, s2, i - 1, j),
                lcsRecursive(s1, s2, i, j - 1)
            );
        }
    }

    // Wrapper para facilitar a chamada
    public static int lcsRecursive(String s1, String s2) {
        iterationsRecursive = 0;
        return lcsRecursive(s1, s2, s1.length(), s2.length());
    }

    // ------------------------------------------------------------
    // Algoritmo com Programação Dinâmica (Bottom-Up)
    // ------------------------------------------------------------
    public static int lcsDP(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        iterationsDP = 0;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                iterationsDP++;
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    // ------------------------------------------------------------
    // Algoritmo com Programação Dinâmica (com reconstrução da LCS)
    // ------------------------------------------------------------
    public static String getLCS(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        // Preenche tabela DP
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // Reconstroi a subsequência
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                lcs.append(s1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return lcs.reverse().toString();
    }

    // ------------------------------------------------------------
    // Método para resetar contadores
    // ------------------------------------------------------------
    public static void resetCounters() {
        iterationsRecursive = 0;
        iterationsDP = 0;
    }
}