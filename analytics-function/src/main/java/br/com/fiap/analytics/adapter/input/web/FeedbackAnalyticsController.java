package br.com.fiap.analytics.adapter.input.web;

import br.com.fiap.analytics.adapter.input.web.dto.FeedbackRequestDTO;
import br.com.fiap.analytics.application.service.FeedbackProcessingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/analytics")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackAnalyticsController {

    @Inject
    FeedbackProcessingService service;

    @POST
    @Path("/process-feedback")
    public Response processFeedback(FeedbackRequestDTO dto) {

        service.process(dto);

        return Response
                .accepted()
                .entity("Feedback processed successfully")
                .build();
    }
}