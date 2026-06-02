package br.com.fiap.analytics.adapter.input.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WeeklyReportResponse(
        UUID id,
        LocalDate periodStart,
        LocalDate periodEnd,
        BigDecimal averageScore,
        Integer totalFeedbacks,
        LocalDateTime generatedAt,
        List<DailyReportItemResponse> dailyItems,
        List<UrgencyReportItemResponse> urgencyItems
) {}