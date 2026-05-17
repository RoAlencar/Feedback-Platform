package br.com.fiap.analytics.adapter.input.web.dto;

import java.time.LocalDate;

public record DailyReportItemResponse(
    LocalDate date,
    Integer feedbackCount
) {}
