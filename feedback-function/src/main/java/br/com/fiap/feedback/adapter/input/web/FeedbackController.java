package br.com.fiap.feedback.adapter.input.web;

import br.com.fiap.feedback.application.usecase.CreateFeedbackUseCase;
import br.com.fiap.shared.application.dto.FeedbackRequest;
import br.com.fiap.shared.application.dto.FeedbackResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/students/{studentId}/courses/{courseId}/feedbacks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackController {

    private final CreateFeedbackUseCase createFeedbackUseCase;

    public FeedbackController(CreateFeedbackUseCase createFeedbackUseCase) {
        this.createFeedbackUseCase = createFeedbackUseCase;
    }

    @POST
    public Response create(
            @PathParam("studentId") UUID studentId,
            @PathParam("courseId") UUID courseId,
            @Valid FeedbackRequest request) {
        FeedbackResponse response = createFeedbackUseCase.execute(studentId, courseId, request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
