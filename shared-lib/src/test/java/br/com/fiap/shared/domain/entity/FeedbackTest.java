package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.valueObject.ProcessStatus;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FeedbackTest {

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID COURSE_ID = UUID.randomUUID();

    @Test
    void deveCriarFeedbackComUrgenciaNormalParaNotaAlta() {
        Feedback feedback = Feedback.create(STUDENT_ID, COURSE_ID, "Ótimo curso", 8);

        assertEquals("Ótimo curso", feedback.getDescription().valor());
        assertEquals(8, feedback.getScore().valor());
        assertEquals(UrgencyLevel.LOW, feedback.getUrgency());
        assertEquals(ProcessStatus.PENDING, feedback.getStatus());
    }

    @Test
    void deveCriarFeedbackComUrgenciaCriticaParaNotaBaixa() {
        Feedback feedback = Feedback.create(STUDENT_ID, COURSE_ID, "Péssimo", 2);

        assertEquals(UrgencyLevel.CRITICAL, feedback.getUrgency());
    }

    @Test
    void devePreencherIdECriadoEmAutomaticamente() {
        Feedback feedback = Feedback.create(STUDENT_ID, COURSE_ID, "Bom", 7);

        assertNotNull(feedback.getId());
        assertNotNull(feedback.getSubmittedAt());
    }

    @Test
    void deveGerarIdsDistintosParaFeedbacksDiferentes() {
        Feedback f1 = Feedback.create(STUDENT_ID, COURSE_ID, "Bom", 7);
        Feedback f2 = Feedback.create(STUDENT_ID, COURSE_ID, "Bom", 7);

        assertNotNull(f1.getId());
        assertNotNull(f2.getId());
        org.junit.jupiter.api.Assertions.assertNotEquals(f1.getId(), f2.getId());
    }
}
