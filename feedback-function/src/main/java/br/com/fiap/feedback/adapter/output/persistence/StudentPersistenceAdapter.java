package br.com.fiap.feedback.adapter.output.persistence;

import br.com.fiap.feedback.adapter.output.persistence.mapper.StudentMapper;
import br.com.fiap.feedback.adapter.output.persistence.repository.StudentRepository;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Student;
import br.com.fiap.shared.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class StudentPersistenceAdapter implements UserRepositoryPort {

    private final StudentRepository studentRepository;

    public StudentPersistenceAdapter(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public void save(User user) {
        if (!(user instanceof Student student)) {
            throw new IllegalArgumentException("Only Student can be persisted in feedback-function");
        }

        studentRepository.persist(StudentMapper.toJpaEntity(student));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return studentRepository.findByIdOptional(id)
                .map(StudentMapper::toDomain)
                .map(User.class::cast);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(StudentMapper::toDomain)
                .map(User.class::cast);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
}