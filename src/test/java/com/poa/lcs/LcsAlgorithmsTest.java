package com.poa.lcs; //pacote com os testes
import org.junit.jupiter.api.Test; //importacao do teste
import static org.junit.jupiter.api.Assertions.assertEquals; //importacao do assertEquals
//testes de corretude dos metodos de lcs
   //o objetivo e garantir que recursivo dp e memo retornam o mesmo comprimento
   //e que a reconstrucao devolve uma string com o tamanho correto
class LcsAlgorithmsTest {
    //helper que roda os tres metodos e compara com o valor esperado
    //s1 e s2 sao as strings de entrada
    //expected e o comprimento esperado da lcs
    private static void assertAllEqual(String s1, String s2, int expected) {
        //contador de operacoes
        //neste teste usamos apenas como suporte para o codigo
        //o numero de operacoes nao e comparado
        OperationCounter ops = new OperationCounter();
        //roda recursivo e compara com esperado
        assertEquals(expected, LcsAlgorithms.lcsRecursive(s1, s2, ops), "recursivo");
        ops.reset();//reseta o contador antes do proximo metodo
        assertEquals(expected, LcsAlgorithms.lcsDp(s1, s2, ops), "dp"); //executa dp e compara com esperado
        ops.reset(); //reseta o contador antes do proximo metodo
        assertEquals(expected, LcsAlgorithms.lcsMemo(s1, s2, ops), "memo"); //executa memo e compara com esperado
    }
    //caso com strings vazias
    //a lcs nao tem elementos comuns e portanto tem tamanho zero
    @Test
    void vazias() {
        assertAllEqual("", "", 0); //caso com strings vazias
        assertAllEqual("", "abc", 0); //caso com string vazia e string nao vazia
        assertAllEqual("x", "", 0); //caso com string nao vazia e string vazia
    }
    //caso em que as duas strings sao iguais
    //a lcs maxima e a propria string
    @Test
    void iguais() {
        assertAllEqual("aaaa", "aaaa", 4); //caso com strings iguais
    }
    //caso em que as strings nao compartilham caracteres
    //o resultado deve ser lcs de tamanho zero
    @Test
    void semIntersecao() {
        assertAllEqual("abcd", "efgh", 0); //caso com strings sem intersecao
    }
    //caso classico usado em muitos materiais sobre lcs
    //o comprimento esperado e 4
    @Test
    void classico() {
        assertAllEqual("ABCBDAB", "BDCABA", 4); //caso classico
    }
    //caso simples em que existe apenas um caractere comum
    @Test
    void umCaractereComum() {
        assertAllEqual("a", "bbbbba", 1); //caso com um caractere comum
    }
    //caso medio com padroes
    //aqui o teste valida que dp memo e recursivo concordam
    @Test
    void medioPrefixoEAlternancia() {
        String s1 = "prefixo_comum_xyz"; //cria s1 com um prefixo comum e alternancia
        String s2 = "prefixo_outro_abc"; //cria s2 com um prefixo comum e alternancia
         //contador de operacoes para controlar eventos nas rotinas
        OperationCounter ops = new OperationCounter(); //contador de operacoes
        //roda dp
        //d e o comprimento da lcs obtida
        int d = LcsAlgorithms.lcsDp(s1, s2, ops); //executa dp e guarda o comprimento
        //reseta antes de memo
        ops.reset(); //reseta o contador antes do proximo metodo
        //roda memo e guarda em m
        int m = LcsAlgorithms.lcsMemo(s1, s2, ops); //executa memo e guarda o comprimento
        //reseta antes de recursivo
        ops.reset(); //reseta o contador antes do proximo metodo

        int r = LcsAlgorithms.lcsRecursive(s1, s2, ops); //executa recursivo e guarda o comprimento (em r porque e o retorno)

        assertEquals(d, m); //verifica se dp e memo concordam
        assertEquals(d, r); //verifica se dp e recursivo concordam
        assertEquals(17, s1.length()); //verifica se o tamanho de s1 e 17
        assertEquals(17, s2.length()); //verifica se o tamanho de s2 e 17
    }
    //teste da reconstrucao
    //a funcao reconstructlcs devolve uma string
    //este teste verifica se o tamanho dessa string bate com o tamanho da lcs via dp
    @Test
    void reconstrucaoTemComprimentoCorreto() {
        String s1 = "AGGTAB"; //cria s1 com um padrao comum e alternancia
        String s2 = "GXTXAYB"; //cria s2 com um padrao comum e alternancia
        //recebe uma subsequencia maxima como string
        String sub = LcsAlgorithms.reconstructLcs(s1, s2); //executa reconstrucao e guarda a string
        assertEquals(LcsAlgorithms.lcsDp(s1, s2, new OperationCounter()), sub.length()); //verifica se o comprimento da string e igual ao comprimento da lcs via dp
    }
}
