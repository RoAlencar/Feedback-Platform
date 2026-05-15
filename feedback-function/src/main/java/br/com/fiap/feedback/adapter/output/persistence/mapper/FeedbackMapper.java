package br.com.fiap.feedback.adapter.output.persistence.mapper;

import br.com.fiap.feedback.adapter.output.persistence.entity.CourseJpaEntity;
import br.com.fiap.feedback.adapter.output.persistence.entity.FeedbackEntity;
import br.com.fiap.feedback.adapter.output.persistence.entity.StudentEntity;
import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.valueObject.Description;
import br.com.fiap.shared.domain.valueObject.Score;
import jakarta.persistence.EntityManager;

public class FeedbackMapper {

    private FeedbackMapper(){}


    public static FeedbackEntity toJpaEntity(Feedback feedback, EntityManager entityManager){

        FeedbackEntity entity = new FeedbackEntity();

        entity.setId(feedback.getId());
        entity.setStudent(entityManager.getReference(StudentEntity.class, feedback.getStudentId()));
        entity.setCourse(entityManager.getReference(CourseJpaEntity.class, feedback.getCourseId()));
        entity.setDescription(feedback.getDescription().valor());
        entity.setScore(feedback.getScore().valor());
        entity.setUrgency(feedback.getUrgency());
        entity.setProcessStatus(feedback.getStatus());
        entity.setSubmittedAt(feedback.getSubmittedAt());

        return entity;
    }


    public static Feedback toDomain(FeedbackEntity entity){

        return new Feedback(
                entity.getId(),
                entity.getStudent().getId(),
                entity.getCourse().getId(),
                new Description(entity.getDescription()),
                new Score(entity.getScore()),
                entity.getProcessStatus(),
                entity.getSubmittedAt()
        );
    }
}
