package br.com.fiap.feedback.adapter.input.web.handler;

import br.com.fiap.feedback.adapter.input.web.dto.ErrorResponse;
import br.com.fiap.shared.domain.exception.ValidationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof NotFoundException) {
            return buildResponse(
                    Response.Status.NOT_FOUND,
                    exception.getMessage()
            );
        }

        if (exception instanceof ValidationException
                || exception instanceof IllegalArgumentException
                || exception instanceof BadRequestException) {
            return buildResponse(
                    Response.Status.BAD_REQUEST,
                    exception.getMessage()
            );
        }

        if (exception instanceof WebApplicationException webApplicationException) {
            int status = webApplicationException.getResponse().getStatus();

            return Response
                    .status(status)
                    .entity(ErrorResponse.of(status, exception.getMessage()))
                    .build();
        }

        return buildResponse(
                Response.Status.INTERNAL_SERVER_ERROR,
                "Internal error while processing the request"
        );
    }

    private Response buildResponse(Response.Status status, String message) {
        return Response
                .status(status)
                .entity(ErrorResponse.of(status.getStatusCode(), message))
                .build();
    }
}
