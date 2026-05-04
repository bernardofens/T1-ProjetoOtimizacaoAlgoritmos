package com.poa.lcs;
//contador simples usado para medir quantas operacoes foram executadas
//o algoritmo recebe uma instancia deste contador e chama increment quando quer registrar um evento
public final class OperationCounter {
    //valor atual do contador
          //cresce durante a execucao
    private long count;
    //incrementa o contador em uma unidade
          //contar um passo do algoritmo
    public void increment() {
        count++;
    }
    //retorna o valor atual do contador
          //imprimir e comparar resultados no benchmark
    public long get() {
        return count;
    }
    //zera o contador para iniciar uma nova medicao
          //compara metodo por metodo no mesmo caso
    public void reset() {
        count = 0;
    }
}
