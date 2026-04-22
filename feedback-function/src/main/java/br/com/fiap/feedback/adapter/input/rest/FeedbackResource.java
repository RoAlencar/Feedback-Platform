package br.com.fiap.feedback.adapter.input.rest;

import br.com.fiap.shared.application.dto.FeedbackRequest;
import br.com.fiap.shared.application.dto.FeedbackResponse;
import br.com.fiap.shared.application.port.input.CriarFeedbackUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/feedbacks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackResource {

    @Inject
    CriarFeedbackUseCase criarFeedback;

    @POST
    public Response criar(@Valid FeedbackRequest request) {
        FeedbackResponse response = criarFeedback.executar(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
