package br.com.fiap.feedback.adapter.input.web;

import br.com.fiap.shared.application.dto.UserRequest;
import br.com.fiap.shared.application.dto.UserResponse;
import br.com.fiap.shared.application.port.input.CreateStudentUseCase;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/students")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {

    private final CreateStudentUseCase createStudentUseCase;

    public StudentResource(CreateStudentUseCase createStudentUseCase) {
        this.createStudentUseCase = createStudentUseCase;
    }

    @POST
    public Response create(@Valid UserRequest request) {
        UserResponse response = createStudentUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}