package clases.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tower.Cup;
import tower.NormalCup;

/**
 * Clase de pruebas unitarias para la clase Cup.
 *
 * @author Julian Morales - Sergio Buitrago
 */
public class CupTest {

    private Cup cup(int n) {
        return new NormalCup(n);
    }

    @Test
    public void constructor_shouldRejectNonPositiveNumber_zero() {
        assertThrows(IllegalArgumentException.class, () -> new NormalCup(0));
    }

    @Test
    public void constructor_shouldRejectNonPositiveNumber_negative() {
        assertThrows(IllegalArgumentException.class, () -> new NormalCup(-3));
    }

    @Test
    public void height_shouldFollow_2nMinus1_andBeOdd_andMatchGetHeightCm() {
        for (int n = 1; n <= 8; n++) {
            Cup c = cup(n);
            int expectedH = 2 * n - 1;
            assertEquals(expectedH, c.getHeight(),        "Altura incorrecta para n=" + n);
            assertEquals(expectedH, c.getHeightCm(),      "getHeightCm inconsistente para n=" + n);
            assertTrue((c.getHeight() % 2) == 1,          "Altura debe ser impar para n=" + n);
        }
    }

    @Test
    public void innerHeight_shouldBeHeightMinusOne() {
        assertEquals(0, cup(1).getInnerHeight());
        assertEquals(4, cup(3).getInnerHeight());
        assertEquals(8, cup(5).getInnerHeight());
    }

    @Test
    public void getNumber_shouldReturnConstructorValue() {
        for (int n = 1; n <= 10; n++) {
            assertEquals(n, cup(n).getNumber());
        }
    }

    @Test
    public void color_shouldCycleOverPalette() {
        String[] expected = {"red", "blue", "green", "yellow", "magenta", "black"};
        for (int n = 1; n <= 12; n++) {
            String exp = expected[(n - 1) % expected.length];
            assertEquals(exp, cup(n).getColor(), "Color inesperado para n=" + n);
        }
    }

    @Test
    public void getWidth_shouldFollowInformativeRule() {
        assertEquals(45, cup(1).getWidth());
        assertEquals(60, cup(4).getWidth());
        assertEquals(70, cup(6).getWidth());
    }

    @Test
    public void toString_shouldContainNumberHeight_andNoLidByDefault() {
        Cup c = cup(2);
        String s = c.toString();
        assertTrue(s.contains("2"),        "toString debe incluir el número");
        assertTrue(s.contains("3"),        "toString debe incluir la altura");
        assertTrue(s.contains("sin tapa"), "Estado por defecto: sin tapa");
    }

    @Test
    public void makeInvisible_shouldBeCallableSafely_andIdempotent() {
        Cup c = cup(4);
        c.makeInvisible();
        c.makeInvisible();
    }

    @Test
    public void makeVisibleAt_shouldBeCallableWithValidParams() {
        final int scale = 10;
        Cup c3 = cup(3);
        int hCm3 = c3.getHeight();
        c3.makeVisibleAt(100, 100, hCm3 * scale, hCm3 * scale);
        c3.makeVisibleAt(120, 80,  hCm3 * scale, hCm3 * scale);
        c3.makeInvisible();

        Cup c5 = cup(5);
        int hCm5 = c5.getHeight();
        c5.makeVisibleAt(20, 50, hCm5 * scale, hCm5 * scale);
        c5.makeInvisible();
    }

    @Test
    public void makeVisibleAt_shouldHandleMinimalUnit() {
        Cup c1 = cup(1);
        c1.makeVisibleAt(0, 0, 1, 1);
        c1.makeInvisible();
    }
}
