package br.com.fiap.shared.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrgenciaTest {

    @Test
    void deveRetornarCriticaParaNotaMenorOuIgualAQuatro() {
        assertEquals(Urgencia.CRITICA, Urgencia.fromNota(new Nota(0)));
        assertEquals(Urgencia.CRITICA, Urgencia.fromNota(new Nota(4)));
    }

    @Test
    void deveRetornarNormalParaNotaMaiorQueQuatro() {
        assertEquals(Urgencia.NORMAL, Urgencia.fromNota(new Nota(5)));
        assertEquals(Urgencia.NORMAL, Urgencia.fromNota(new Nota(10)));
    }
}
