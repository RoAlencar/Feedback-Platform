package br.com.fiap.shared.application.dto;

import br.com.fiap.shared.domain.entity.Feedback;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeedbackResponseTest {

    @Test
    void deveConverterEntidadeParaResponse() {
        Feedback feedback = Feedback.create("Muito bom", 9);

        FeedbackResponse response = FeedbackResponse.fromDomain(feedback);

        assertEquals(feedback.getId(), response.id());
        assertEquals("Muito bom", response.description());
        assertEquals(9, response.grade());
        assertEquals("LOW", response.urgency());
        assertEquals(feedback.getSubmittedAt(), response.createdAt());
    }

    @Test
    void deveConverterFeedbackCriticoParaResponse() {
        Feedback feedback = Feedback.create("Ruim", 1);

        FeedbackResponse response = FeedbackResponse.fromDomain(feedback);

        assertEquals("CRITICAL", response.urgency());
    }
}
