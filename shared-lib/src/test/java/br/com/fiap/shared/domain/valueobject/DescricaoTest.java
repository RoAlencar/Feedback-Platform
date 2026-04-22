package br.com.fiap.shared.domain.valueobject;

import br.com.fiap.shared.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DescricaoTest {

    @Test
    void deveCriarDescricaoComValorValido() {
        Descricao descricao = new Descricao("Excelente aula");
        assertEquals("Excelente aula", descricao.valor());
    }

    @Test
    void deveLancarExcecaoParaValorNulo() {
        assertThrows(ValidationException.class, () -> new Descricao(null));
    }

    @Test
    void deveLancarExcecaoParaValorVazio() {
        assertThrows(ValidationException.class, () -> new Descricao(""));
    }

    @Test
    void deveLancarExcecaoParaValorEmBranco() {
        assertThrows(ValidationException.class, () -> new Descricao("   "));
    }
}
