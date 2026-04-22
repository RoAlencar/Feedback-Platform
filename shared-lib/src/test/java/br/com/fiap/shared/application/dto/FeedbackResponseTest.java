package br.com.fiap.shared.application.dto;

import br.com.fiap.shared.domain.entity.Feedback;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeedbackResponseTest {

    @Test
    void deveConverterEntidadeParaResponse() {
        Feedback feedback = Feedback.criar("Muito bom", 9);

        FeedbackResponse response = FeedbackResponse.fromDomain(feedback);

        assertEquals(feedback.getId(), response.id());
        assertEquals("Muito bom", response.descricao());
        assertEquals(9, response.nota());
        assertEquals("NORMAL", response.urgencia());
        assertEquals(feedback.getCriadoEm(), response.criadoEm());
    }

    @Test
    void deveConverterFeedbackCriticoParaResponse() {
        Feedback feedback = Feedback.criar("Ruim", 1);

        FeedbackResponse response = FeedbackResponse.fromDomain(feedback);

        assertEquals("CRITICA", response.urgencia());
    }
}
