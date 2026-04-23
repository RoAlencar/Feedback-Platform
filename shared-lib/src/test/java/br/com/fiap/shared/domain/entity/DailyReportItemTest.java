package br.com.fiap.shared.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DailyReportItemTest {

    @Test
    void shouldCreateDailyReportItemSuccessfully() {
        LocalDate date = LocalDate.of(2026, 4, 23);
        int feedbackCount = 5;

        DailyReportItem item = new DailyReportItem(date, feedbackCount);

        assertNotNull(item);
        assertEquals(date, item.getDate());
        assertEquals(feedbackCount, item.getFeedbackCount());
    }
}