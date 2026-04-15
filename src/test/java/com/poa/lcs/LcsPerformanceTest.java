package com.poa.lcs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * casos maiores: recursivo só onde barato; dp/memo para strings longas.
 */
class LcsPerformanceTest {

    @Test
    @Timeout(30)
    void dpEMemoConcordamEmGrandeEscala() {
        String s1 = "a".repeat(500);
        String s2 = "a".repeat(500);
        OperationCounter ops = new OperationCounter();
        int d = LcsAlgorithms.lcsDp(s1, s2, ops);
        ops.reset();
        int m = LcsAlgorithms.lcsMemo(s1, s2, ops);
        assertEquals(d, m);
        assertEquals(500, d);
    }

    @Test
    @Timeout(10)
    void aleatorioPequenoTresMetodosIguais() {
        Random rnd = new Random(42L);
        for (int t = 0; t < 50; t++) {
            String s1 = randomString(rnd, 8, "abc");
            String s2 = randomString(rnd, 9, "abc");
            if (s1.length() + s2.length() > BenchmarkRunner.RECURSIVE_MAX_SUM_LENGTH) {
                continue;
            }
            OperationCounter ops = new OperationCounter();
            int r = LcsAlgorithms.lcsRecursive(s1, s2, ops);
            ops.reset();
            int d = LcsAlgorithms.lcsDp(s1, s2, ops);
            ops.reset();
            int m = LcsAlgorithms.lcsMemo(s1, s2, ops);
            assertEquals(r, d, "t=" + t + " s1=" + s1 + " s2=" + s2);
            assertEquals(d, m);
        }
    }

    private static String randomString(Random rnd, int len, String alphabet) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
}
