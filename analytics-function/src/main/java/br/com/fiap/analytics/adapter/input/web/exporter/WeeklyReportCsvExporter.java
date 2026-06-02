package br.com.fiap.analytics.adapter.input.web.exporter;

import br.com.fiap.analytics.adapter.input.web.dto.DailyReportItemResponse;
import br.com.fiap.analytics.adapter.input.web.dto.UrgencyReportItemResponse;
import br.com.fiap.analytics.adapter.input.web.dto.WeeklyReportResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WeeklyReportCsvExporter {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public String export(List<WeeklyReportResponse> reports) {
        StringBuilder csv = new StringBuilder();

        csv.append("record_type,report_id,period_start,period_end,average_score,total_feedbacks,generated_at,item_key,feedback_count")
                .append(LINE_SEPARATOR);

        for (WeeklyReportResponse report : reports) {
            appendLine(
                    csv,
                    "SUMMARY",
                    report.id(),
                    report.periodStart(),
                    report.periodEnd(),
                    report.averageScore(),
                    report.totalFeedbacks(),
                    report.generatedAt(),
                    "",
                    ""
            );

            for (DailyReportItemResponse dailyItem : safeList(report.dailyItems())) {
                appendLine(
                        csv,
                        "DAILY",
                        report.id(),
                        report.periodStart(),
                        report.periodEnd(),
                        report.averageScore(),
                        report.totalFeedbacks(),
                        report.generatedAt(),
                        dailyItem.date(),
                        dailyItem.feedbackCount()
                );
            }

            for (UrgencyReportItemResponse urgencyItem : safeList(report.urgencyItems())) {
                appendLine(
                        csv,
                        "URGENCY",
                        report.id(),
                        report.periodStart(),
                        report.periodEnd(),
                        report.averageScore(),
                        report.totalFeedbacks(),
                        report.generatedAt(),
                        urgencyItem.urgencyLevel(),
                        urgencyItem.feedbackCount()
                );
            }
        }

        return csv.toString();
    }

    private void appendLine(StringBuilder csv, Object... values) {
        for (int index = 0; index < values.length; index++) {
            if (index > 0) {
                csv.append(",");
            }

            csv.append(escape(values[index]));
        }

        csv.append(LINE_SEPARATOR);
    }

    private String escape(Object value) {
        if (value == null) {
            return "";
        }

        String text = value.toString();

        boolean mustEscape = text.contains(",")
                || text.contains("\"")
                || text.contains("\n")
                || text.contains("\r");

        if (!mustEscape) {
            return text;
        }

        return "\"" + text.replace("\"", "\"\"") + "\"";
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? List.of() : values;
    }
}