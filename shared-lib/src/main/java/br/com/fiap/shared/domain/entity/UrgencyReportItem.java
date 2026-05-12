package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.exception.FeedbackCountException;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;

import java.util.Objects;

public class UrgencyReportItem {

    private final UrgencyLevel urgency;
    private final int feedbackCount;

    public UrgencyReportItem(UrgencyLevel urgency, int feedbackCount) {
        this.urgency = Objects.requireNonNull(urgency, "urgencyLevel is required");
        this.feedbackCount = feedbackCount;

        validate();
    }


    //GETTERS
    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }


    //VALIDATE
    private void validate(){

        if(feedbackCount < 0){
            throw new FeedbackCountException("feedbackCount cannot be negative");
        }
    }
}
