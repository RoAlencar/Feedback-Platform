package br.com.fiap.shared.domain.valueObject;

import br.com.fiap.shared.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GradeTest {

    @Test
    void deveCriarNotaComValorValidoMinimo() {
        Grade grade = new Grade(0);
        assertEquals(0, grade.valor());
    }

    @Test
    void deveCriarNotaComValorValidoIntermediario() {
        Grade grade = new Grade(5);
        assertEquals(5, grade.valor());
    }

    @Test
    void deveCriarNotaComValorValidoMaximo() {
        Grade grade = new Grade(10);
        assertEquals(10, grade.valor());
    }

    @Test
    void deveLancarExcecaoParaValorAbaixoDoMinimo() {
        assertThrows(ValidationException.class, () -> new Grade(-1));
    }

    @Test
    void deveLancarExcecaoParaValorAcimaDoMaximo() {
        assertThrows(ValidationException.class, () -> new Grade(11));
    }

    @Test
    void deveSerCriticaQuandoNotaMenorOuIgualAQuatro() {
        assertTrue(new Grade(0).isCritical());
        assertTrue(new Grade(4).isCritical());
    }

    @Test
    void naoDeveSerCriticaQuandoNotaMaiorQueQuatro() {
        assertFalse(new Grade(5).isCritical());
        assertFalse(new Grade(10).isCritical());
    }
}
