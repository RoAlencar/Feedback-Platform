package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.exception.FeedbackCountException;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrgencyReportItemTest {

    @Test
    void shouldCreateUrgencyReportItemSuccessfully() {
        UrgencyLevel urgencyLevel = UrgencyLevel.HIGH;
        int feedbackCount = 3;

        UrgencyReportItem item = new UrgencyReportItem(urgencyLevel, feedbackCount);

        assertNotNull(item);
        assertEquals(urgencyLevel, item.getUrgency());
        assertEquals(feedbackCount, item.getFeedbackCount());
    }

    @Test
    void shouldThrowExceptionWhenFeedbackCountIsNegative() {
        UrgencyLevel urgencyLevel = UrgencyLevel.CRITICAL;
        int feedbackCount = -1;

        assertThrows(FeedbackCountException.class, () -> new UrgencyReportItem(
                urgencyLevel,
                feedbackCount
        ));
    }

    @Test
    void shouldThrowExceptionWhenUrgencyLevelIsNull() {
        int feedbackCount = 2;

        assertThrows(NullPointerException.class, () -> new UrgencyReportItem(
                null,
                feedbackCount
        ));
    }
}