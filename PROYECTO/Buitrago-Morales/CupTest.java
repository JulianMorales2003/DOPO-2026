import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase Cup.
 *
 * @author Julian Morales - Sergio Buitrago
 *
 */

public class CupTest {


    /**
     * Debe rechazar un identificador <= 0 (cero).
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldRejectNonPositiveNumber_zero() {
        new Cup(0);
    }

    /**
     * Debe rechazar un identificador <= 0 (negativo).
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldRejectNonPositiveNumber_negative() {
        new Cup(-3);
    }

    /**
     * La altura debe seguir H(n) = 2*n - 1 (impar) y coincidir con getHeightCm().
     */
    @Test
    public void height_shouldFollow_2nMinus1_andBeOdd_andMatchGetHeightCm() {
        for (int n = 1; n <= 8; n++) {
            Cup c = new Cup(n);
            int expectedH = 2 * n - 1;
            assertEquals("Altura incorrecta para n=" + n, expectedH, c.getHeight());
            assertEquals("getHeightCm inconsistente para n=" + n, expectedH, c.getHeightCm());
            assertTrue("Altura debe ser impar para n=" + n, (c.getHeight() % 2) == 1);
        }
    }

    /**
     * La altura interna debe ser H(n) - 1 (quitando 1 cm de base).
     */
    @Test
    public void innerHeight_shouldBeHeightMinusOne() {
        Cup c1 = new Cup(1);
        Cup c3 = new Cup(3);
        Cup c5 = new Cup(5);

        assertEquals(0, c1.getInnerHeight());
        assertEquals(4, c3.getInnerHeight());
        assertEquals(8, c5.getInnerHeight());
    }

    /**
     * getNumber() debe devolver el número con el que se construyó la copa.
     */
    @Test
    public void getNumber_shouldReturnConstructorValue() {
        for (int n = 1; n <= 10; n++) {
            Cup c = new Cup(n);
            assertEquals(n, c.getNumber());
        }
    }

    /**
     * El color debe asignarse de forma cíclica según la paleta definida.
     */
    @Test
    public void color_shouldCycleOverPalette() {
        String[] expected = {"red", "blue", "green", "yellow", "magenta", "black"};
        // Dos ciclos completos (12 copas)
        for (int n = 1; n <= 12; n++) {
            Cup c = new Cup(n);
            String exp = expected[(n - 1) % expected.length];
            assertEquals("Color inesperado para n=" + n, exp, c.getColor());
        }
    }

    /**
     * getWidth() debe seguir la regla informativa 40 + 5*n (según implementación actual).
     */
    @Test
    public void getWidth_shouldFollowInformativeRule() {
        Cup c1 = new Cup(1);
        Cup c4 = new Cup(4);
        Cup c6 = new Cup(6);
        assertEquals(45, c1.getWidth());
        assertEquals(60, c4.getWidth());
        assertEquals(70, c6.getWidth());
    }


    /**
     * toString() debe incluir el número, la altura y por defecto "sin tapa".
     */
    @Test
    public void toString_shouldContainNumberHeight_andNoLidByDefault() {
        Cup c = new Cup(2); // H = 3
        String s = c.toString();
        assertTrue("toString debe incluir el número", s.contains("Copa #2"));
        assertTrue("toString debe incluir la altura", s.contains("altura: 3"));
        assertTrue("Estado por defecto: sin tapa", s.contains("sin tapa"));
    }

    /**
     * makeInvisible() debe ser seguro e idempotente (múltiples llamadas sin error).
     */
    @Test
    public void makeInvisible_shouldBeCallableSafely_andIdempotent() {
        Cup c = new Cup(4);
        c.makeInvisible();
        c.makeInvisible(); 
    }

    /**
     * makeVisibleAt(...) debe poder invocarse sin lanzar excepciones con parámetros válidos.
     */
    @Test
    public void makeVisibleAt_shouldBeCallableWithValidParams() {
        final int scale = 10; 

        
        Cup c3 = new Cup(3);
        int hCm3 = c3.getHeight();     
        int wPx3 = hCm3 * scale;       
        int hPx3 = hCm3 * scale;      
        c3.makeVisibleAt(100, 100, wPx3, hPx3);
        c3.makeVisibleAt(120, 80,  wPx3, hPx3); 

        Cup c5 = new Cup(5);
        int hCm5 = c5.getHeight();     
        int wPx5 = hCm5 * scale;       
        int hPx5 = hCm5 * scale;       
        c5.makeVisibleAt(20, 50, wPx5, hPx5);
    }

    /**
     * makeVisibleAt(...) con tamaños mínimos —unidad 1 px/cm— debe ser seguro.
     */
    @Test
    public void makeVisibleAt_shouldHandleMinimalUnit() {
        Cup c1 = new Cup(1); // H = 1
        
        c1.makeVisibleAt(0, 0, 1, 1);
    }
}
