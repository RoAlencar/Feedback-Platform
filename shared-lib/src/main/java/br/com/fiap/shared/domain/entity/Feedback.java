package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.common.DateUtils;
import br.com.fiap.shared.domain.valueobject.Descricao;
import br.com.fiap.shared.domain.valueobject.Nota;
import br.com.fiap.shared.domain.valueobject.Urgencia;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Feedback {

    private final UUID id;
    private final Descricao descricao;
    private final Nota nota;
    private final Urgencia urgencia;
    private final LocalDateTime criadoEm;

    public Feedback(UUID id, Descricao descricao, Nota nota, LocalDateTime criadoEm) {
        this.id = Objects.requireNonNull(id, "id");
        this.descricao = Objects.requireNonNull(descricao, "descricao");
        this.nota = Objects.requireNonNull(nota, "nota");
        this.criadoEm = Objects.requireNonNull(criadoEm, "criadoEm");
        this.urgencia = Urgencia.fromNota(nota);
    }

    public static Feedback criar(String descricao, int nota) {
        return new Feedback(
                UUID.randomUUID(),
                new Descricao(descricao),
                new Nota(nota),
                DateUtils.now());
    }

    public UUID getId() {
        return id;
    }

    public Descricao getDescricao() {
        return descricao;
    }

    public Nota getNota() {
        return nota;
    }

    public Urgencia getUrgencia() {
        return urgencia;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
