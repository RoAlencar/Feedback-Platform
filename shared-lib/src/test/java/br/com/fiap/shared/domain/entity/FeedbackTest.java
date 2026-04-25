package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.valueObject.ProcessStatus;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FeedbackTest {

    @Test
    void deveCriarFeedbackComUrgenciaNormalParaNotaAlta() {
        Feedback feedback = Feedback.create("Ótimo curso", 8, ProcessStatus.PENDING);

        assertEquals("Ótimo curso", feedback.getDescription().valor());
        assertEquals(8, feedback.getScore().valor());
        assertEquals(UrgencyLevel.LOW, feedback.getUrgency());
    }

    @Test
    void deveCriarFeedbackComUrgenciaCriticaParaNotaBaixa() {
        Feedback feedback = Feedback.create("Péssimo", 2, ProcessStatus.PENDING);

        assertEquals(UrgencyLevel.CRITICAL, feedback.getUrgency());
    }

    @Test
    void devePreencherIdECriadoEmAutomaticamente() {
        Feedback feedback = Feedback.create("Bom", 7, ProcessStatus.PENDING);

        assertNotNull(feedback.getId());
        assertNotNull(feedback.getSubmittedAt());
    }

    @Test
    void deveGerarIdsDistintosParaFeedbacksDiferentes() {
        Feedback f1 = Feedback.create("Bom", 7, ProcessStatus.PENDING);
        Feedback f2 = Feedback.create("Bom", 7, ProcessStatus.PENDING);

        assertNotNull(f1.getId());
        assertNotNull(f2.getId());
        org.junit.jupiter.api.Assertions.assertNotEquals(f1.getId(), f2.getId());
    }
}
