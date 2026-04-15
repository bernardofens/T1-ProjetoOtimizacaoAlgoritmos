package com.poa.lcs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LcsAlgorithmsTest {

    private static void assertAllEqual(String s1, String s2, int expected) {
        OperationCounter ops = new OperationCounter();
        assertEquals(expected, LcsAlgorithms.lcsRecursive(s1, s2, ops), "recursivo");
        ops.reset();
        assertEquals(expected, LcsAlgorithms.lcsDp(s1, s2, ops), "dp");
        ops.reset();
        assertEquals(expected, LcsAlgorithms.lcsMemo(s1, s2, ops), "memo");
    }

    @Test
    void vazias() {
        assertAllEqual("", "", 0);
        assertAllEqual("", "abc", 0);
        assertAllEqual("x", "", 0);
    }

    @Test
    void iguais() {
        assertAllEqual("aaaa", "aaaa", 4);
    }

    @Test
    void semIntersecao() {
        assertAllEqual("abcd", "efgh", 0);
    }

    @Test
    void classico() {
        assertAllEqual("ABCBDAB", "BDCABA", 4);
    }

    @Test
    void umCaractereComum() {
        assertAllEqual("a", "bbbbba", 1);
    }

    @Test
    void medioPrefixoEAlternancia() {
        String s1 = "prefixo_comum_xyz";
        String s2 = "prefixo_outro_abc";
        OperationCounter ops = new OperationCounter();
        int d = LcsAlgorithms.lcsDp(s1, s2, ops);
        ops.reset();
        int m = LcsAlgorithms.lcsMemo(s1, s2, ops);
        ops.reset();
        int r = LcsAlgorithms.lcsRecursive(s1, s2, ops);
        assertEquals(d, m);
        assertEquals(d, r);
        assertEquals(17, s1.length());
        assertEquals(17, s2.length());
    }

    @Test
    void reconstrucaoTemComprimentoCorreto() {
        String s1 = "AGGTAB";
        String s2 = "GXTXAYB";
        String sub = LcsAlgorithms.reconstructLcs(s1, s2);
        assertEquals(LcsAlgorithms.lcsDp(s1, s2, new OperationCounter()), sub.length());
    }
}
