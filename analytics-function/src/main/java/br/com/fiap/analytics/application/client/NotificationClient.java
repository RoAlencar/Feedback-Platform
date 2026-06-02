package br.com.fiap.analytics.application.client;

import br.com.fiap.analytics.adapter.input.web.dto.FeedbackRequestDTO;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/notifications")
@RegisterRestClient(configKey = "notification-api")
public interface NotificationClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void sendNotification(FeedbackRequestDTO dto);
}