package com.poa.lcs;

/* classe de testes para comportamento em escala
   ela tenta garantir dois pontos
   primeiro que dp e memo retornam o mesmo tamanho de lcs
   segundo que o recursivo da a mesma resposta que dp e memo quando o tamanho e pequeno */
class LcsPerformanceTest {

    /* teste para strings grandes
       como as duas strings sao todas iguais a lcs tem tamanho previsivel */
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Timeout(30)
    void dpEMemoConcordamEmGrandeEscala() {
        /* cria s1 com muitos caracteres iguais */
        String s1 = "a".repeat(500);
        /* cria s2 com muitos caracteres iguais */
        String s2 = "a".repeat(500);

        /* contador de operacoes
           e passado para dp e memo para que cada metodo conte eventos separadamente */
        OperationCounter ops = new OperationCounter();

        /* executa dp e guarda o comprimento da lcs */
        int d = LcsAlgorithms.lcsDp(s1, s2, ops);

        /* reseta o contador antes do proximo metodo */
        ops.reset();

        /* executa memo e guarda o comprimento da lcs */
        int m = LcsAlgorithms.lcsMemo(s1, s2, ops);

        /* verifica se dp e memo concordam */
        org.junit.jupiter.api.Assertions.assertEquals(d, m);

        /* verifica um valor esperado teorico
           quando as strings sao iguais e tudo a lcs tem tamanho 500 */
        org.junit.jupiter.api.Assertions.assertEquals(500, d);
    }

    /* teste com casos aleatorios pequenos
       a ideia e comparar os tres metodos em instancias pequenas */
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Timeout(10)
    void aleatorioPequenoTresMetodosIguais() {
        /* cria um gerador aleatorio com seed fixa
           seed fixa torna o teste reprodutivel */
        java.util.Random rnd = new java.util.Random(42L);

        /* cria varios casos
           cada iteracao cria duas strings aleatorias */
        for (int t = 0; t < 50; t++) {
            /* gera s1 usando um alfabeto pequeno */
            String s1 = randomString(rnd, 8, "abc");
            /* gera s2 usando o mesmo alfabeto e tamanho ligeiramente diferente */
            String s2 = randomString(rnd, 9, "abc");

            /* evita recursivo quando os tamanhos seriam grandes demais
               isso evita estouro de tempo no pior caso */
            if (s1.length() + s2.length() > BenchmarkRunner.RECURSIVE_MAX_SUM_LENGTH) {
                continue;
            }

            /* contador de operacoes para esta rodada */
            OperationCounter ops = new OperationCounter();

            /* executa recursivo
               r e o comprimento da lcs retornada */
            int r = LcsAlgorithms.lcsRecursive(s1, s2, ops);

            /* reseta o contador para dp */
            ops.reset();

            /* executa dp e guarda o comprimento */
            int d = LcsAlgorithms.lcsDp(s1, s2, ops);

            /* reseta o contador para memo */
            ops.reset();

            /* executa memo e guarda o comprimento */
            int m = LcsAlgorithms.lcsMemo(s1, s2, ops);

            /* compara recursivo com dp
               se falhar a mensagem inclui o caso para ajudar a depurar */
            org.junit.jupiter.api.Assertions.assertEquals(r, d, "t=" + t + " s1=" + s1 + " s2=" + s2);

            /* compara dp com memo */
            org.junit.jupiter.api.Assertions.assertEquals(d, m);
        }
    }

    /* gera uma string aleatoria
       len define o numero de caracteres na string final
       alphabet define quais caracteres podem aparecer */
    private static String randomString(java.util.Random rnd, int len, String alphabet) {
        /* construtor para acumular caracteres */
        StringBuilder sb = new StringBuilder(len);

        /* percorre o numero de posicoes da string final */
        for (int i = 0; i < len; i++) {
            /* escolhe um indice aleatorio no alfabeto */
            int idx = rnd.nextInt(alphabet.length());

            /* pega o caractere no indice e adiciona ao builder */
            sb.append(alphabet.charAt(idx));
        }

        /* transforma o builder em string e devolve */
        return sb.toString();
    }
}
