package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.valueobject.Urgencia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FeedbackTest {

    @Test
    void deveCriarFeedbackComUrgenciaNormalParaNotaAlta() {
        Feedback feedback = Feedback.criar("Ótimo curso", 8);

        assertEquals("Ótimo curso", feedback.getDescricao().valor());
        assertEquals(8, feedback.getNota().valor());
        assertEquals(Urgencia.NORMAL, feedback.getUrgencia());
    }

    @Test
    void deveCriarFeedbackComUrgenciaCriticaParaNotaBaixa() {
        Feedback feedback = Feedback.criar("Péssimo", 2);

        assertEquals(Urgencia.CRITICA, feedback.getUrgencia());
    }

    @Test
    void devePreencherIdECriadoEmAutomaticamente() {
        Feedback feedback = Feedback.criar("Bom", 7);

        assertNotNull(feedback.getId());
        assertNotNull(feedback.getCriadoEm());
    }

    @Test
    void deveGerarIdsDistintosParaFeedbacksDiferentes() {
        Feedback f1 = Feedback.criar("Bom", 7);
        Feedback f2 = Feedback.criar("Bom", 7);

        assertNotNull(f1.getId());
        assertNotNull(f2.getId());
        org.junit.jupiter.api.Assertions.assertNotEquals(f1.getId(), f2.getId());
    }
}
