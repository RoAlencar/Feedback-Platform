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
        assertEquals("NORMAL", response.urgency());
        assertEquals(feedback.getCreatedAt(), response.createdAt());
        assertEquals(feedback.getStatus(), response.processStatus());
    }

    @Test
    void deveConverterFeedbackCriticoParaResponse() {
        Feedback feedback = Feedback.create("Ruim", 1);

        FeedbackResponse response = FeedbackResponse.fromDomain(feedback);

        assertEquals("CRITICAL", response.urgency());
    }
}
