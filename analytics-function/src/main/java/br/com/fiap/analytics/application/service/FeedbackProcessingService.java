package br.com.fiap.analytics.application.service;

import br.com.fiap.analytics.adapter.input.web.dto.FeedbackRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class FeedbackProcessingService {

    private static final Logger log =
            LoggerFactory.getLogger(FeedbackProcessingService.class);

    @Inject
    UrgencyRuleService urgencyRuleService;

    @Inject
    NotificationDispatcherService notificationDispatcherService;

    public void process(FeedbackRequestDTO dto) {

        try {

            log.info("Processing feedback: {}", dto.feedbackId());

            String urgency = urgencyRuleService.calculate(
                    dto.rating(),
                    dto.comment()
            );

            log.info("Urgency level: {}", urgency);

            if ("CRITICAL".equals(urgency)) {

                notificationDispatcherService.notifyCriticalFeedback(dto);

                log.info("Critical notification sent");
            }

        } catch (Exception ex) {

            log.error("Error processing feedback. feedbackId={}", dto.feedbackId(), ex);

            throw new RuntimeException("Feedback processing failed", ex);
        }
    }
}