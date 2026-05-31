package br.com.fiap.notification.adapter.output.persistence.repository;

import java.util.List;
import java.util.UUID;

import br.com.fiap.notification.domain.enums.NotificationSendStatus;
import br.com.fiap.notification.infra.NotificationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationRepository implements PanacheRepositoryBase<NotificationEntity, UUID> {

    private static final int MAX_ATTEMPTS = 3;

    public List<NotificationEntity> findFailedEligibleForRetry() {
        return list("sendStatus = ?1 and attempts < ?2",
                NotificationSendStatus.FAILED, MAX_ATTEMPTS);
    }
}
