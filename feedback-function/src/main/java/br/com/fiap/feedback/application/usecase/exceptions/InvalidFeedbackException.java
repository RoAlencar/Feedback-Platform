package br.com.fiap.feedback.application.usecase.exceptions;

public class InvalidFeedbackException extends RuntimeException {
    public InvalidFeedbackException(String message) {
        super(message);
    }
}
