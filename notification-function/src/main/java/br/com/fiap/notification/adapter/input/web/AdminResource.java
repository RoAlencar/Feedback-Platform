package br.com.fiap.notification.adapter.input.web;

import br.com.fiap.shared.application.dto.UserRequest;
import br.com.fiap.shared.application.dto.UserResponse;
import br.com.fiap.shared.application.port.input.CreateAdminUseCase;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admins")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {

    private final CreateAdminUseCase createAdminUseCase;

    public AdminResource(CreateAdminUseCase createAdminUseCase) {
        this.createAdminUseCase = createAdminUseCase;
    }

    @POST
    public Response create(@Valid UserRequest request) {
        UserResponse response = createAdminUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}