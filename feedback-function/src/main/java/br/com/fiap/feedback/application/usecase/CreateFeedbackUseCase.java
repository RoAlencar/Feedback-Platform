package br.com.fiap.feedback.application.usecase;

import br.com.fiap.shared.application.dto.FeedbackRequest;
import br.com.fiap.shared.application.dto.FeedbackResponse;
import br.com.fiap.shared.application.port.output.EventPublisherPort;
import br.com.fiap.shared.application.port.output.FeedbackRepositoryPort;
import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class CreateFeedbackUseCase {

    private final FeedbackRepositoryPort feedbackRepository;
    private final EventPublisherPort eventPublisher;

    public CreateFeedbackUseCase(FeedbackRepositoryPort feedbackRepository, EventPublisherPort eventPublisher) {
        this.feedbackRepository = feedbackRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public FeedbackResponse execute(UUID studentId, UUID courseId, FeedbackRequest request) {
        Feedback feedback = Feedback.create(studentId, courseId, request.description(), request.grade());
        feedbackRepository.save(feedback);

        FeedbackCreatedEvent event = new FeedbackCreatedEvent(
                feedback.getId(),
                feedback.getDescription().valor(),
                feedback.getScore().valor(),
                feedback.getUrgency().name(),
                feedback.getSubmittedAt());
        eventPublisher.publish(event);

        return FeedbackResponse.fromDomain(feedback);
    }
}
