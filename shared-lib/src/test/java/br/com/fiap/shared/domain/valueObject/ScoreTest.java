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
        ValidationException ex = assertThrows(ValidationException.class, () -> new Score(-1));
        assertEquals("Score must be between 0 and 10, received: -1", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoParaValorAcimaDoMaximo() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new Score(11));
        assertEquals("Score must be between 0 and 10, received: 11", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoParaValorMuitoAbaixoDoMinimo() {
        assertThrows(ValidationException.class, () -> new Score(-100));
    }

    @Test
    void deveLancarExcecaoParaValorMuitoAcimaDoMaximo() {
        assertThrows(ValidationException.class, () -> new Score(100));
    }

    @Test
    void deveSerCriticaQuandoNotaMenorOuIgualADois() {
        assertTrue(new Score(0).isCritical());
        assertTrue(new Score(1).isCritical());
        assertTrue(new Score(2).isCritical());
    }

    @Test
    void naoDeveSerCriticaQuandoNotaMaiorQueDois() {
        assertFalse(new Score(3).isCritical());
        assertFalse(new Score(5).isCritical());
        assertFalse(new Score(10).isCritical());
    }

    @Test
    void deveSerWarningQuandoNotaMenorOuIgualAQuatro() {
        assertTrue(new Score(0).isWarning());
        assertTrue(new Score(4).isWarning());
    }

    @Test
    void naoDeveSerWarningQuandoNotaMaiorQueQuatro() {
        assertFalse(new Score(5).isWarning());
        assertFalse(new Score(10).isWarning());
    }

    @Test
    void deveSerAttentionQuandoNotaMenorOuIgualASete() {
        assertTrue(new Score(0).isAttention());
        assertTrue(new Score(7).isAttention());
    }

    @Test
    void naoDeveSerAttentionQuandoNotaMaiorQueSete() {
        assertFalse(new Score(8).isAttention());
        assertFalse(new Score(10).isAttention());
    }

    @Test
    void deveSerElevatedQuandoNotaMenorOuIgualADez() {
        assertTrue(new Score(0).isElevated());
        assertTrue(new Score(10).isElevated());
    }
}
