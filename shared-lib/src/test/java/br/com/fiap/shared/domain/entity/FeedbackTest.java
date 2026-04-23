package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.valueObject.UrgencyLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FeedbackTest {

    @Test
    void deveCriarFeedbackComUrgenciaNormalParaNotaAlta() {
        Feedback feedback = Feedback.create("Ótimo curso", 8);

        assertEquals("Ótimo curso", feedback.getDescription().valor());
        assertEquals(8, feedback.getGrade().valor());
        assertEquals(UrgencyLevel.LOW, feedback.getUrgency());
    }

    @Test
    void deveCriarFeedbackComUrgenciaCriticaParaNotaBaixa() {
        Feedback feedback = Feedback.create("Péssimo", 2);

        assertEquals(UrgencyLevel.CRITICAL, feedback.getUrgency());
    }

    @Test
    void devePreencherIdECriadoEmAutomaticamente() {
        Feedback feedback = Feedback.create("Bom", 7);

        assertNotNull(feedback.getId());
        assertNotNull(feedback.getCreatedAt());
    }

    @Test
    void deveGerarIdsDistintosParaFeedbacksDiferentes() {
        Feedback f1 = Feedback.create("Bom", 7);
        Feedback f2 = Feedback.create("Bom", 7);

        assertNotNull(f1.getId());
        assertNotNull(f2.getId());
        org.junit.jupiter.api.Assertions.assertNotEquals(f1.getId(), f2.getId());
    }
}
