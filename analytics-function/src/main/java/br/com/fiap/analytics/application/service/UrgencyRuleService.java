package br.com.fiap.analytics.application.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UrgencyRuleService {

    public String calculate(Integer rating, String comment) {

        if (rating <= 1) {
            return "CRITICAL";
        }

        if (rating == 2) {
            return "HIGH";
        }

        if (rating == 3) {
            return "MEDIUM";
        }

        return "LOW";
    }
}