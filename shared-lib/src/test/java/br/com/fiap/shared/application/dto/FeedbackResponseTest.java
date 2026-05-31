package br.com.fiap.shared.application.dto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import br.com.fiap.shared.domain.entity.Feedback;

public class FeedbackResponseTest {

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID COURSE_ID = UUID.randomUUID();

    @Test
    void deveConverterEntidadeParaResponse() {
        Feedback feedback = Feedback.create(STUDENT_ID, COURSE_ID, "Muito bom", 9);

        FeedbackResponse response = FeedbackResponse.fromDomain(feedback);

        assertEquals(feedback.getId(), response.id());
        assertEquals("Muito bom", response.description());
        assertEquals(9, response.score());
        assertEquals("LOW", response.urgency());
        assertEquals(feedback.getSubmittedAt(), response.createdAt());
        assertEquals(feedback.getStatus(), response.processStatus());
    }

    @Test
    void deveConverterFeedbackCriticoParaResponse() {
        Feedback feedback = Feedback.create(STUDENT_ID, COURSE_ID, "Ruim", 1);

        FeedbackResponse response = FeedbackResponse.fromDomain(feedback);

        assertEquals("CRITICAL", response.urgency());
    }
}
