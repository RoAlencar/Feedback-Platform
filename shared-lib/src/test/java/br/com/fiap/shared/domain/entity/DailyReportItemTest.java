package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.exception.FeedbackCountException;
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

    @Test
    void shouldThrowExceptionWhenFeedbackCountIsNegative(){

        LocalDate date = LocalDate.of(2026, 4, 23);
        int feedbackCount = -5;

        assertThrows(FeedbackCountException.class, () -> new DailyReportItem(
                date,
                feedbackCount
        ));
    }

    @Test
    void shouldThrowExceptionWhenDateIsNull() {
        int feedbackCount = 5;

        assertThrows(NullPointerException.class, () -> new DailyReportItem(
                null,
                feedbackCount
        ));
    }
}