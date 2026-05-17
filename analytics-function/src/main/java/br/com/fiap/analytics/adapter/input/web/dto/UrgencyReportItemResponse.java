package br.com.fiap.analytics.adapter.input.web.dto;

public record UrgencyReportItemResponse(
        String urgencyLevel,
        Integer feedbackCount
) {}