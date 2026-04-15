package com.poa.lcs;

/**
 * resultado de uma medição: comprimento da lcs, operações contadas e tempo em nanossegundos.
 */
public record LcsStats(int length, long operations, long nanos) {
}
