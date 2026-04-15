package com.poa.lcs;

/**
 * contador mutável de operações usado pelos algoritmos de lcs.
 */
public final class OperationCounter {
    private long count;

    public void increment() {
        count++;
    }

    public long get() {
        return count;
    }

    public void reset() {
        count = 0;
    }
}
