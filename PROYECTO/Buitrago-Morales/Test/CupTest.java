package Test;

import static org.junit.Assert.*;
import org.junit.Test;
import tower.Cup;
import tower.NormalCup;

/**
 * Clase de pruebas unitarias para la clase Cup.
 *
 * @author Julian Morales - Sergio Buitrago
 */
public class CupTest {

    // Helper: crea una NormalCup (Cup es abstracta)
    private Cup cup(int n) {
        return new NormalCup(n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldRejectNonPositiveNumber_zero() {
        new NormalCup(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldRejectNonPositiveNumber_negative() {
        new NormalCup(-3);
    }

    @Test
    public void height_shouldFollow_2nMinus1_andBeOdd_andMatchGetHeightCm() {
        for (int n = 1; n <= 8; n++) {
            Cup c = cup(n);
            int expectedH = 2 * n - 1;
            assertEquals("Altura incorrecta para n=" + n, expectedH, c.getHeight());
            assertEquals("getHeightCm inconsistente para n=" + n, expectedH, c.getHeightCm());
            assertTrue("Altura debe ser impar para n=" + n, (c.getHeight() % 2) == 1);
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
            assertEquals("Color inesperado para n=" + n, exp, cup(n).getColor());
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
        Cup c = cup(2); // H = 3
        String s = c.toString();
        assertTrue("toString debe incluir el número", s.contains("2"));
        assertTrue("toString debe incluir la altura", s.contains("3"));
        assertTrue("Estado por defecto: sin tapa", s.contains("sin tapa"));
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