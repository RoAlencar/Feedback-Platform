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
        assertThrows(ValidationException.class, () -> new Description(null));
    }

    @Test
    void deveLancarExcecaoParaValorVazio() {
        assertThrows(ValidationException.class, () -> new Description(""));
    }

    @Test
    void deveLancarExcecaoParaValorEmBranco() {
        assertThrows(ValidationException.class, () -> new Description("   "));
    }
}
