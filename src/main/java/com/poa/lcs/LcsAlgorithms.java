package com.poa.lcs;

/* esta classe contem as implementacoes do problema lcs
   lcs e a maior sequencia que aparece nas duas strings mantendo a ordem
   existem tres abordagens
   lcs recursivo sem memo
   lcs dp bottom up
   lcs memoizacao top down
   a funcao reconstructlcs monta uma subsequencia maxima depois de calcular a tabela dp
   as funcoes recursivas e dp recebem um contador para registrar eventos de operacoes */ 
public final class LcsAlgorithms {

    /* construtor privado para impedir instanciacao
       isso reforca que a classe e utilitaria e so tem metodos estaticos */
    private LcsAlgorithms() {
    }

    /* interface publica para lcs recursivo sem memo
       ela prepara os tamanhos totais e delega para o helper que recebe i e j */
    public static int lcsRecursive(String s1, String s2, OperationCounter ops) {
        return lcsRecursive(s1, s2, s1.length(), s2.length(), ops);
    }

    /* helper recursivo
       i representa o tamanho do prefixo de s1 que estamos considerando
       j representa o tamanho do prefixo de s2 que estamos considerando */
    private static int lcsRecursive(String s1, String s2, int i, int j, OperationCounter ops) {
        /* conta uma operacao a cada entrada na funcao
           isso permite comparar a carga de trabalho entre execucoes pequenas */
        ops.increment();
        if (i == 0 || j == 0) {
            /* caso base
               se um prefixo tem tamanho zero entao a lcs entre os prefixos e vazia */
            return 0;
        }
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            /* se os ultimos caracteres dos dois prefixos forem iguais
               entao eles pertencem a alguma lcs maxima
               assim a lcs tem tamanho um mais a lcs do problema menor */
            return 1 + lcsRecursive(s1, s2, i - 1, j - 1, ops);
        }
        /* caso os ultimos caracteres sejam diferentes
           duas possibilidades sao relevantes
           ignorar o ultimo caractere de s1 ou ignorar o ultimo caractere de s2 */
        return Math.max(
                lcsRecursive(s1, s2, i - 1, j, ops),
                lcsRecursive(s1, s2, i, j - 1, ops)
        );
    }

    /* lcs dp bottom up
       dp significa programacao dinamica e bottom up significa preencher a tabela do zero */
    public static int lcsDp(String s1, String s2, OperationCounter ops) {
        /* tamanhos das strings
           m e o tamanho de s1 e n e o tamanho de s2 */
        int m = s1.length();
        int n = s2.length();

        /* matriz dp
           dp i j armazena o tamanho da lcs entre os prefixos s1 com i caracteres e s2 com j caracteres
           a linha zero e coluna zero representam lcs vazia */ 
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                /* conta uma operacao a cada celula interna avaliada */
                ops.increment();
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    /* caso os caracteres atuais sejam iguais
                       entao crescemos a lcs a partir do canto diagonal dp i menos 1 j menos 1 */
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    /* caso os caracteres atuais sejam diferentes
                       a lcs maxima virara do melhor entre
                       ignorar o ultimo caractere de s1 ou ignorar o ultimo caractere de s2 */
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        /* a resposta final esta na ultima celula dp m n */
        return dp[m][n];
    }

    /* lcs memoizacao top down
       memoizacao significa guardar resultados para nao recomputar estados repetidos */
    public static int lcsMemo(String s1, String s2, OperationCounter ops) {
        /* m e n novamente para facilitar leitura */
        int m = s1.length();
        int n = s2.length();

        /* tabela memo
           memo i j guarda o resultado de lcs entre prefixos de tamanho i e j
           usamos tipo inteiro para conseguir detectar se a celula ja foi preenchida com null */ 
        Integer[][] memo = new Integer[m + 1][n + 1];
        return lcsMemoHelper(s1, s2, m, n, memo, ops);
    }

    /* helper da memoizacao
       i e j definem o estado atual
       memo e a tabela de cache */
    private static int lcsMemoHelper(String s1, String s2, int i, int j, Integer[][] memo, OperationCounter ops) {
        /* conta operacao ao entrar neste estado */
        ops.increment();
        if (i == 0 || j == 0) {
            /* caso base
               se i ou j for zero entao a lcs do estado e zero */
            return 0;
        }
        if (memo[i][j] != null) {
            /* se ja existe resposta guardada entao retornamos sem recomputar
               isso e o que da ganho de desempenho na memoizacao */
            return memo[i][j];
        }
        int v;
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            /* caracteres finais iguais
               soma um com a lcs do estado menor diagonal */
            v = 1 + lcsMemoHelper(s1, s2, i - 1, j - 1, memo, ops);
        } else {
            /* caracteres finais diferentes
               calculamos os dois estados possiveis
               depois escolhemos o maior */
            v = Math.max(
                    lcsMemoHelper(s1, s2, i - 1, j, memo, ops),
                    lcsMemoHelper(s1, s2, i, j - 1, memo, ops)
            );
        }
        /* guarda resultado no cache para estados futuros */
        memo[i][j] = v;
        /* devolve valor calculado para quem chamou */
        return v;
    }

    /* reconstructlcs
       retorna uma subsequencia maxima como string
       esta funcao reusa a ideia de dp bottom up para construir a tabela
       depois retrocede de m n ate encontrar os caracteres que pertencem a uma lcs */
    public static String reconstructLcs(String s1, String s2) {
        /* tamanhos */
        int m = s1.length();
        int n = s2.length();

        /* matriz dp para reconstruir a subsequencia
           aqui nao contamos operacoes porque a funcao e focada apenas em montar a string */ 
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    /* se caracteres forem iguais a lcs cresce em um */
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    /* se diferentes entao pegamos o melhor entre acima e esquerda */
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        /* string builder para montar a resposta em ordem reversa */
        StringBuilder lcs = new StringBuilder();

        /* i e j comecam na celula final dp m n */
        int i = m;
        int j = n;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                /* se os caracteres atuais forem iguais entao este caractere pertence a uma lcs maxima */
                lcs.append(s1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                /* se a celula acima tem valor maior ou igual entao o caminho segue para cima */
                i--;
            } else {
                /* caso contrario o caminho segue para a esquerda */
                j--;
            }
        }
        /* a subsequencia foi montada de tras para frente
           entao precisamos inverter para obter a ordem correta */
        return lcs.reverse().toString();
    }
}
