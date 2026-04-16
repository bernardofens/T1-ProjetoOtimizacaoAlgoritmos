package com.poa.lcs;
//classe de testes para comportamento em escala
//ela tenta garantir dois pontos
//primeiro que dp e memo retornam o mesmo tamanho de lcs
//segundo que o recursivo da a mesma resposta que dp e memo quando o tamanho e pequeno
class LcsPerformanceTest {
    //teste para strings grandes
    //como as duas strings sao todas iguais a lcs tem tamanho previsivel
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Timeout(30)
    void dpEMemoConcordamEmGrandeEscala() {
        String s1 = "a".repeat(500);//cria s1 com muitos caracteres iguais
        String s2 = "a".repeat(500);//cria s2 com muitos caracteres iguais
        //contador de operacoes
        //e passado para dp e memo para que cada metodo conte eventos separadamente
        OperationCounter ops = new OperationCounter();
        //executa dp e guarda o comprimento da lcs
        int d = LcsAlgorithms.lcsDp(s1, s2, ops);//guarda o comprimento da lcs
        ops.reset();//reseta o contador antes do proximo metodo
        //executa memo e guarda o comprimento da lcs
        int m = LcsAlgorithms.lcsMemo(s1, s2, ops);//guarda o comprimento da lcs
        org.junit.jupiter.api.Assertions.assertEquals(d, m);//verifica se dp e memo concordam
        org.junit.jupiter.api.Assertions.assertEquals(500, d);//verifica se o comprimento da lcs e 500
    }
    //teste com casos aleatorios pequenos
    //a ideia e comparar os tres metodos em instancias pequenas
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Timeout(10)
    void aleatorioPequenoTresMetodosIguais() {
         //cria um gerador aleatorio com seed fixa
        //seed fixa torna o teste reprodutivel
        java.util.Random rnd = new java.util.Random(42L); 
        //cria varios casos
        //cada iteracao cria duas strings aleatorias
        for (int t = 0; t < 50; t++) {
            String s1 = randomString(rnd, 8, "abc"); //gera s1 usando um alfabeto pequeno
            String s2 = randomString(rnd, 9, "abc"); //gera s2 usando o mesmo alfabeto e tamanho ligeiramente diferente
            //evita recursivo quando os tamanhos seriam grandes demais
            //isso evita estouro de tempo no pior caso
            if (s1.length() + s2.length() > BenchmarkRunner.RECURSIVE_MAX_SUM_LENGTH) {
                continue;
            }

            OperationCounter ops = new OperationCounter();//contador de operacoes para esta rodada
            //executa recursivo
            //r e o comprimento da lcs retornada
            int r = LcsAlgorithms.lcsRecursive(s1, s2, ops);
            ops.reset();//reseta o contador para dp
            int d = LcsAlgorithms.lcsDp(s1, s2, ops); //executa dp e guarda o comprimento
            ops.reset();//reseta o contador para memo
            int m = LcsAlgorithms.lcsMemo(s1, s2, ops); //executa memo e guarda o comprimento
            //compara recursivo com dp
            //se falhar a mensagem inclui o caso para ajudar a depurar
            org.junit.jupiter.api.Assertions.assertEquals(r, d, "t=" + t + " s1=" + s1 + " s2=" + s2);
            org.junit.jupiter.api.Assertions.assertEquals(d, m);//verifica se dp e memo concordam
        }
    }
    //gera uma string aleatoria
    //len define o numero de caracteres na string final
    //alphabet define quais caracteres podem aparecer
    private static String randomString(java.util.Random rnd, int len, String alphabet) {
        StringBuilder sb = new StringBuilder(len);//construtor para acumular caracteres
        for (int i = 0; i < len; i++) { //percorre o numero de posicoes da string final
            int idx = rnd.nextInt(alphabet.length()); //escolhe um indice aleatorio no alfabeto
            sb.append(alphabet.charAt(idx)); //pega o caractere no indice e adiciona ao builder
        }
        return sb.toString(); //transforma o builder em string e devolve
    }
}
