package br.com.fiap.notification.adapter.output.persistence.mapper;

import br.com.fiap.notification.adapter.output.persistence.entity.AdminJpaEntity;
import br.com.fiap.shared.domain.entity.Admin;
import br.com.fiap.shared.domain.valueObject.Email;
import br.com.fiap.shared.domain.valueObject.Name;

public final class AdminMapper {

    private AdminMapper() {
    }

    public static AdminJpaEntity toJpaEntity(Admin admin) {
        return new AdminJpaEntity(
                admin.getId(),
                admin.getName().value(),
                admin.getEmail().value(),
                admin.isActive()
        );
    }

    public static Admin toDomain(AdminJpaEntity entity) {
        return new Admin(
                entity.getId(),
                new Name(entity.getName()),
                new Email(entity.getEmail()),
                entity.isActive()
        );
    }
}