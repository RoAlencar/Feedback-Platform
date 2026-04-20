package br.com.fiap.shared.contract;

public interface EventConsumer<T> {
    void consume(T event);
}
