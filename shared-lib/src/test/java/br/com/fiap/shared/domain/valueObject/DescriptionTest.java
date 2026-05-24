package br.com.fiap.shared.domain.valueObject;

import br.com.fiap.shared.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DescriptionTest {

    @Test
    void deveCriarDescricaoComValorValido() {
        Description descricao = new Description("Excelente aula");
        assertEquals("Excelente aula", descricao.valor());
    }

    @Test
    void deveLancarExcecaoParaValorNulo() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new Description(null));
        assertEquals("Description must not be null or blank", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoParaValorVazio() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new Description(""));
        assertEquals("Description must not be null or blank", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoParaValorEmBranco() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new Description("   "));
        assertEquals("Description must not be null or blank", ex.getMessage());
    }

    @Test
    void deveCriarDescricaoComTextoLongo() {
        String textoLongo = "A".repeat(500);
        Description descricao = new Description(textoLongo);
        assertEquals(textoLongo, descricao.valor());
    }

    @Test
    void deveLancarExcecaoParaValorComApenasTab() {
        assertThrows(ValidationException.class, () -> new Description("\t"));
    }

    @Test
    void deveLancarExcecaoParaValorComApenasNewline() {
        assertThrows(ValidationException.class, () -> new Description("\n"));
    }
}
