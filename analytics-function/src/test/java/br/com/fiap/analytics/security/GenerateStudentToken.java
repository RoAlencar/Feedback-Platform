package br.com.fiap.analytics.security;

import io.smallrye.jwt.build.Jwt;

import java.util.Set;

public class GenerateStudentToken {

    public static void main(String[] args) {
        String token = Jwt.issuer("feedback-platform")
                .subject("student-local")
                .upn("student@fiap.com.br")
                .groups(Set.of("STUDENT"))
                .sign();

        System.out.println(token);
    }
}