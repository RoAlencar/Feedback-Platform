package br.com.fiap.shared.domain.valueObject;

import br.com.fiap.shared.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoreTest {

    @Test
    void deveCriarNotaComValorValidoMinimo() {
        Score score = new Score(0);
        assertEquals(0, score.valor());
    }

    @Test
    void deveCriarNotaComValorValidoIntermediario() {
        Score score = new Score(5);
        assertEquals(5, score.valor());
    }

    @Test
    void deveCriarNotaComValorValidoMaximo() {
        Score score = new Score(10);
        assertEquals(10, score.valor());
    }

    @Test
    void deveLancarExcecaoParaValorAbaixoDoMinimo() {
        assertThrows(ValidationException.class, () -> new Score(-1));
    }

    @Test
    void deveLancarExcecaoParaValorAcimaDoMaximo() {
        assertThrows(ValidationException.class, () -> new Score(11));
    }

    @Test
    void deveSerCriticaQuandoNotaMenorOuIgualAQuatro() {
        assertTrue(new Score(0).isCritical());
        assertTrue(new Score(2).isCritical());
    }

    @Test
    void naoDeveSerCriticaQuandoNotaMaiorQueQuatro() {
        assertFalse(new Score(5).isCritical());
        assertFalse(new Score(10).isCritical());
    }
}
