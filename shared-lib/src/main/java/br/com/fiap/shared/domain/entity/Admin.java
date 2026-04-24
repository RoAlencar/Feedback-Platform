package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.valueObject.Email;
import br.com.fiap.shared.domain.valueObject.Name;

import java.util.UUID;

public final class Admin extends User{

    public Admin(UUID id, Name name, Email email, boolean is_active) {
        super(id, name, email, is_active);
    }

    public static Admin create(String name, String email) {
        return new Admin(
                UUID.randomUUID(),
                new Name(name),
                new Email(email),
                true
        );
    }

    @Override
    public String getType() {
        return "ADMIN";
    }

}
