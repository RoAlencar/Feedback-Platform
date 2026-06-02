package br.com.fiap.notification.adapter.input.web;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/notifications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationController {

    @POST
    public Response sendNotification(String payload) {

        System.out.println(
                "Notification received: " + payload
        );

        return Response.ok().build();
    }
}