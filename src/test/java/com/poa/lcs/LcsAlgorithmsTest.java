package com.poa.lcs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/* testes de corretude dos metodos de lcs
   o objetivo e garantir que recursivo dp e memo retornam o mesmo comprimento
   e que a reconstrucao devolve uma string com o tamanho correto */
class LcsAlgorithmsTest {

    /* helper que roda os tres metodos e compara com o valor esperado
       s1 e s2 sao as strings de entrada
       expected e o comprimento esperado da lcs */
    private static void assertAllEqual(String s1, String s2, int expected) {
        /* contador de operacoes
           neste teste usamos apenas como suporte para o codigo
           o numero de operacoes nao e comparado */
        OperationCounter ops = new OperationCounter();

        /* roda recursivo e compara com esperado */
        assertEquals(expected, LcsAlgorithms.lcsRecursive(s1, s2, ops), "recursivo");

        /* reseta para separar a contagem entre metodos */
        ops.reset();

        /* roda dp e compara com esperado */
        assertEquals(expected, LcsAlgorithms.lcsDp(s1, s2, ops), "dp");

        /* reseta antes do proximo metodo */
        ops.reset();

        /* roda memo e compara com esperado */
        assertEquals(expected, LcsAlgorithms.lcsMemo(s1, s2, ops), "memo");
    }

    /* caso com strings vazias
       a lcs nao tem elementos comuns e portanto tem tamanho zero */
    @Test
    void vazias() {
        assertAllEqual("", "", 0);
        assertAllEqual("", "abc", 0);
        assertAllEqual("x", "", 0);
    }

    /* caso em que as duas strings sao iguais
       a lcs maxima e a propria string */
    @Test
    void iguais() {
        assertAllEqual("aaaa", "aaaa", 4);
    }

    /* caso em que as strings nao compartilham caracteres
       o resultado deve ser lcs de tamanho zero */
    @Test
    void semIntersecao() {
        assertAllEqual("abcd", "efgh", 0);
    }

    /* caso classico usado em muitos materiais sobre lcs
       o comprimento esperado e 4 */ 
    @Test
    void classico() {
        assertAllEqual("ABCBDAB", "BDCABA", 4);
    }

    /* caso simples em que existe apenas um caractere comum */ 
    @Test
    void umCaractereComum() {
        assertAllEqual("a", "bbbbba", 1);
    }

    /* caso medio com padroes
       aqui o teste valida que dp memo e recursivo concordam */ 
    @Test
    void medioPrefixoEAlternancia() {
        String s1 = "prefixo_comum_xyz";
        String s2 = "prefixo_outro_abc";

        /* contador de operacoes para controlar eventos nas rotinas */ 
        OperationCounter ops = new OperationCounter();

        /* roda dp
           d e o comprimento da lcs obtida */ 
        int d = LcsAlgorithms.lcsDp(s1, s2, ops);

        /* reseta antes de memo */
        ops.reset();

        /* roda memo e guarda em m */ 
        int m = LcsAlgorithms.lcsMemo(s1, s2, ops);

        /* reseta antes de recursivo */
        ops.reset();

        /* roda recursivo e guarda em r */ 
        int r = LcsAlgorithms.lcsRecursive(s1, s2, ops);

        /* compara dp com memo */ 
        assertEquals(d, m);

        /* compara dp com recursivo */ 
        assertEquals(d, r);

        /* valida o tamanho das strings escolhidas
           isso ajuda a tornar o teste mais claro */ 
        assertEquals(17, s1.length());
        assertEquals(17, s2.length());
    }

    /* teste da reconstrucao
       a funcao reconstructlcs devolve uma string
       este teste verifica se o tamanho dessa string bate com o tamanho da lcs via dp */ 
    @Test
    void reconstrucaoTemComprimentoCorreto() {
        String s1 = "AGGTAB";
        String s2 = "GXTXAYB";

        /* recebe uma subsequencia maxima como string */
        String sub = LcsAlgorithms.reconstructLcs(s1, s2);

        /* calcula o comprimento esperado com dp usando um contador descartavel */
        assertEquals(LcsAlgorithms.lcsDp(s1, s2, new OperationCounter()), sub.length());
    }
}
