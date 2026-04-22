package br.com.fiap.analytics.domain;

public record RelatorioSemanal(
        long total,
        long criticos,
        double mediaNotas) {
}
