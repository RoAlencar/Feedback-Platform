package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.exception.FeedbackCountException;

import java.time.LocalDate;
import java.util.Objects;

public class DailyReportItem {

    private final LocalDate date;
    private final int feedbackCount;


    public DailyReportItem(LocalDate date, int feedbackCount) {
        this.date = Objects.requireNonNull(date, "date is required");
        this.feedbackCount = feedbackCount;

        validate();
    }


    //GETTERS
    public LocalDate getDate() {
        return date;
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
