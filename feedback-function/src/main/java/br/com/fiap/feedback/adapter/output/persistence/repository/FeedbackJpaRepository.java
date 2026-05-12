package br.com.fiap.feedback.adapter.output.persistence.repository;

import br.com.fiap.feedback.adapter.output.persistence.entity.FeedbackEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class FeedbackJpaRepository implements PanacheRepositoryBase<FeedbackEntity, UUID> {
}
