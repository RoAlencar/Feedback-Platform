package br.com.fiap.notification.application.port.output;

import br.com.fiap.shared.application.dto.FeedbackEventDTO;

public interface NotifierPort {

    void notificar(FeedbackEventDTO event);
}
