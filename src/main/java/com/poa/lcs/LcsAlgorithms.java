package com.poa.lcs;

/**
 * implementações da maior subsequência comum (lcs) entre duas strings.
 * <p>
 * definição de contagem de operações por método:
 * <ul>
 *   <li>{@link #lcsRecursive}: uma operação por entrada na função recursiva (inclui folhas).</li>
 *   <li>{@link #lcsDp}: uma operação por célula interna (i,j) com i≥1 e j≥1.</li>
 *   <li>{@link #lcsMemo}: uma operação por entrada no helper recursivo (inclui retornos de cache).</li>
 * </ul>
 */
public final class LcsAlgorithms {

    private LcsAlgorithms() {
    }

    /**
     * lcs recursivo sem memoização.
     * tempo: exponencial no pior caso (árvore de recursão sobreposta).
     * espaço: o(m+n) na pilha de chamadas.
     */
    public static int lcsRecursive(String s1, String s2, OperationCounter ops) {
        return lcsRecursive(s1, s2, s1.length(), s2.length(), ops);
    }

    private static int lcsRecursive(String s1, String s2, int i, int j, OperationCounter ops) {
        ops.increment();
        if (i == 0 || j == 0) {
            return 0;
        }
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            return 1 + lcsRecursive(s1, s2, i - 1, j - 1, ops);
        }
        return Math.max(
                lcsRecursive(s1, s2, i - 1, j, ops),
                lcsRecursive(s1, s2, i, j - 1, ops)
        );
    }

    /**
     * programação dinâmica bottom-up (matriz completa).
     * tempo: o(m·n). espaço: o(m·n).
     */
    public static int lcsDp(String s1, String s2, OperationCounter ops) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                ops.increment();
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    /**
     * memoização (top-down) com tabela {@code Integer[m+1][n+1]}.
     * tempo: o(m·n) estados distintos. espaço: o(m·n) + pilha.
     */
    public static int lcsMemo(String s1, String s2, OperationCounter ops) {
        int m = s1.length();
        int n = s2.length();
        Integer[][] memo = new Integer[m + 1][n + 1];
        return lcsMemoHelper(s1, s2, m, n, memo, ops);
    }

    private static int lcsMemoHelper(String s1, String s2, int i, int j, Integer[][] memo, OperationCounter ops) {
        ops.increment();
        if (i == 0 || j == 0) {
            return 0;
        }
        if (memo[i][j] != null) {
            return memo[i][j];
        }
        int v;
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            v = 1 + lcsMemoHelper(s1, s2, i - 1, j - 1, memo, ops);
        } else {
            v = Math.max(
                    lcsMemoHelper(s1, s2, i - 1, j, memo, ops),
                    lcsMemoHelper(s1, s2, i, j - 1, memo, ops)
            );
        }
        memo[i][j] = v;
        return v;
    }

    /**
     * reconstrói uma subsequência comum de comprimento máximo (usa dp como no método clássico).
     */
    public static String reconstructLcs(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        StringBuilder lcs = new StringBuilder();
        int i = m;
        int j = n;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                lcs.append(s1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return lcs.reverse().toString();
    }
}
