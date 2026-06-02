package br.com.fiap.analytics.adapter.input.messaging;

import br.com.fiap.shared.application.dto.FeedbackEventDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class FeedbackEventDTODeserializer extends ObjectMapperDeserializer<FeedbackEventDTO> {

    public FeedbackEventDTODeserializer() {
        super(FeedbackEventDTO.class);
    }
}