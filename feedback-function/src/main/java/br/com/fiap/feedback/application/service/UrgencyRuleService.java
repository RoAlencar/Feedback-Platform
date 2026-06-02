package br.com.fiap.feedback.application.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UrgencyRuleService {

    public boolean isCritical(Integer score) {

        return score <= 2;
    }
}

