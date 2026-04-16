package com.poa.lcs;
//registro simples que agrupa resultado de benchmark
   //length e o tamanho da lcs
   //operations e a contagem de eventos feita pelo contador
   //nanos e o tempo medido em nanossegundos
public record LcsStats(int length, long operations, long nanos) {
}
