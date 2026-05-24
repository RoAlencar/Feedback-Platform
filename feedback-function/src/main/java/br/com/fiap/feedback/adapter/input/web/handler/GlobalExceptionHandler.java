package br.com.fiap.feedback.adapter.input.web.handler;

import br.com.fiap.shared.domain.exception.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                        "error", "Validation Error",
                        "message", exception.getMessage(),
                        "timestamp", LocalDateTime.now().toString()
                ))
                .build();
    }
}
