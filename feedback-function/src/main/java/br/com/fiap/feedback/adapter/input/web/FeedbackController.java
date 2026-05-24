package br.com.fiap.feedback.adapter.input.web;

import br.com.fiap.feedback.adapter.input.web.dto.CreateFeedbackRequest;
import br.com.fiap.feedback.adapter.input.web.dto.CreateFeedbackResponse;
import br.com.fiap.feedback.adapter.input.web.mapper.FeedbackRequestMapper;
import br.com.fiap.feedback.application.usecase.CreateFeedbackUseCase;
import br.com.fiap.shared.domain.entity.Feedback;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/avaliacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackController {

    private final CreateFeedbackUseCase createFeedbackUseCase;

    public FeedbackController(CreateFeedbackUseCase createFeedbackUseCase) {
        this.createFeedbackUseCase = createFeedbackUseCase;
    }

    @POST
    public Response create(CreateFeedbackRequest request) {
        Feedback feedback = createFeedbackUseCase.execute(
                FeedbackRequestMapper.toCommand(request)
        );

        return Response
                .status(Response.Status.CREATED)
                .entity(CreateFeedbackResponse.fromDomain(feedback))
                .build();
    }
}
