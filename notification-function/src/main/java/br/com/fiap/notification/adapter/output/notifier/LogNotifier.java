package br.com.fiap.notification.adapter.output.notifier;

import br.com.fiap.notification.application.port.output.NotifierPort;
import br.com.fiap.shared.application.dto.FeedbackEventDTO;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class LogNotifier implements NotifierPort {

    private static final Logger LOG = Logger.getLogger(LogNotifier.class);

    @Override
    public void notificar(FeedbackEventDTO event) {
        LOG.warnf("[NOTIFICACAO CRITICA] feedbackId=%s nota=%d descricao=%s",
                event.feedbackId(), event.nota(), event.descricao());
    }
}
