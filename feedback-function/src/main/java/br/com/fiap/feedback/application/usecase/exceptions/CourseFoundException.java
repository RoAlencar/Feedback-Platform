package br.com.fiap.feedback.application.usecase.exceptions;

public class CourseFoundException extends RuntimeException {
    public CourseFoundException(String message) {
        super(message);
    }
}
