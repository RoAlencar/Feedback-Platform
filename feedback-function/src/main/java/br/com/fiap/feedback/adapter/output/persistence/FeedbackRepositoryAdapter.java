package br.com.fiap.feedback.adapter.output.persistence;

import br.com.fiap.feedback.adapter.output.persistence.entity.FeedbackEntity;
import br.com.fiap.feedback.adapter.output.persistence.mapper.FeedbackMapper;
import br.com.fiap.feedback.adapter.output.persistence.repository.FeedbackJpaRepository;
import br.com.fiap.shared.application.port.output.FeedbackRepositoryPort;
import br.com.fiap.shared.domain.entity.Feedback;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FeedbackRepositoryAdapter implements FeedbackRepositoryPort {

    @Inject
    FeedbackJpaRepository jpaRepository;

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public void save(Feedback feedback) {
        jpaRepository.persist(FeedbackMapper.toJpaEntity(feedback, entityManager));
    }

    @Override
    public Optional<Feedback> findById(UUID id) {
        return jpaRepository.findByIdOptional(id)
                .map(FeedbackMapper::toDomain);
    }

    @Override
    public List<Feedback> findAll() {
        return jpaRepository.listAll().stream()
                .map(FeedbackMapper::toDomain)
                .collect(Collectors.toList());
    }
}
