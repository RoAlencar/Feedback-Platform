package br.com.fiap.analytics.security;

import io.smallrye.jwt.build.Jwt;

import java.util.Set;

public class GenerateAdminToken {

    public static void main(String[] args) {
        String token = Jwt.issuer("feedback-platform")
                .subject("admin-local")
                .upn("admin@fiap.com.br")
                .groups(Set.of("ADMIN"))
                .sign();

        System.out.println(token);
    }
}