package br.com.fiap.shared.domain.valueobject;

import br.com.fiap.shared.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotaTest {

    @Test
    void deveCriarNotaComValorValidoMinimo() {
        Nota nota = new Nota(0);
        assertEquals(0, nota.valor());
    }

    @Test
    void deveCriarNotaComValorValidoIntermediario() {
        Nota nota = new Nota(5);
        assertEquals(5, nota.valor());
    }

    @Test
    void deveCriarNotaComValorValidoMaximo() {
        Nota nota = new Nota(10);
        assertEquals(10, nota.valor());
    }

    @Test
    void deveLancarExcecaoParaValorAbaixoDoMinimo() {
        assertThrows(ValidationException.class, () -> new Nota(-1));
    }

    @Test
    void deveLancarExcecaoParaValorAcimaDoMaximo() {
        assertThrows(ValidationException.class, () -> new Nota(11));
    }

    @Test
    void deveSerCriticaQuandoNotaMenorOuIgualAQuatro() {
        assertTrue(new Nota(0).isCritica());
        assertTrue(new Nota(4).isCritica());
    }

    @Test
    void naoDeveSerCriticaQuandoNotaMaiorQueQuatro() {
        assertFalse(new Nota(5).isCritica());
        assertFalse(new Nota(10).isCritica());
    }
}
