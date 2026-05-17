package br.com.fiap.analytics.adapter.input.web;

import br.com.fiap.analytics.adapter.input.web.dto.WeeklyReportResponse;
import br.com.fiap.analytics.application.usecase.GetWeeklyReportsUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/admin/reports")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class AdminReportController {

    private final GetWeeklyReportsUseCase getWeeklyReportsUseCase;

    public AdminReportController(GetWeeklyReportsUseCase getWeeklyReportsUseCase) {
        this.getWeeklyReportsUseCase = getWeeklyReportsUseCase;
    }

    @GET
    @Path("/weekly")
    public List<WeeklyReportResponse> getWeeklyReports() {
        return getWeeklyReportsUseCase.execute();
    }
}