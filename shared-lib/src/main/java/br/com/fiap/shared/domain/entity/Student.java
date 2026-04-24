package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.valueObject.Email;
import br.com.fiap.shared.domain.valueObject.Name;

import java.util.UUID;

public final class Student extends User {

    public Student(UUID id, Name name, Email email, boolean is_active) {
        super(id, name, email, is_active);
    }

    public static Student create(String name, String email) {
        return new Student(
                UUID.randomUUID(),
                new Name(name),
                new Email(email),
                true
        );
    }

    @Override
    public String getType() {
        return "STUDENT";
    }

}
