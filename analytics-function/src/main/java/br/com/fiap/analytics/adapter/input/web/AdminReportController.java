package br.com.fiap.analytics.adapter.input.web;

import br.com.fiap.analytics.adapter.input.web.dto.WeeklyReportResponse;
import br.com.fiap.analytics.adapter.input.web.exporter.WeeklyReportCsvExporter;
import br.com.fiap.analytics.application.usecase.GetWeeklyReportsUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/admin/reports")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class AdminReportController {

    private final GetWeeklyReportsUseCase getWeeklyReportsUseCase;
    private final WeeklyReportCsvExporter weeklyReportCsvExporter;

    public AdminReportController(GetWeeklyReportsUseCase getWeeklyReportsUseCase, WeeklyReportCsvExporter weeklyReportCsvExporter) {
        this.getWeeklyReportsUseCase = getWeeklyReportsUseCase;
        this.weeklyReportCsvExporter = weeklyReportCsvExporter;
    }

    @GET
    @Path("/weekly")
    public List<WeeklyReportResponse> getWeeklyReports() {
        return getWeeklyReportsUseCase.execute();
    }


    @GET
    @Path("/weekly/export")
    @Produces("text/csv")
    public Response exportWeeklyReports() {
        List<WeeklyReportResponse> reports = getWeeklyReportsUseCase.execute();
        String csv = weeklyReportCsvExporter.export(reports);

        return Response.ok(csv)
                .header("Content-Disposition", "attachment; filename=\"weekly-reports.csv\"")
                .build();
    }
}