package Test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tower.Lid;
import tower.NormalLid;

/**
 * Clase de pruebas unitarias para la clase Lid.
 *
 * @author Julian Morales - Sergio Buitrago
 */
public class LidTest {

    private Lid lid(int n) {
        return new NormalLid(n);
    }

    @Test
    public void constructor_shouldStoreNumber_andGetterShouldReturnIt() {
        assertEquals(1, lid(1).getNumber());
        assertEquals(7, lid(7).getNumber());
    }

    @Test
    public void defaultState_shouldNotBeOnCup_andAssociatedCupIsNull() {
        Lid l = lid(3);
        assertFalse(l.isOnCup());
        assertNull(l.getAssociatedCup());
    }

    @Test
    public void makeInvisible_shouldBeCallableSafely_andIdempotent() {
        Lid l = lid(2);
        l.makeInvisible();
        l.makeInvisible();
    }

    @Test
    public void makeVisible_withoutAssociation_shouldNotThrow() {
        Lid l = lid(4);
        l.makeVisible();
        l.makeInvisible();
    }

    @Test
    public void height_getters_shouldReturnOne() {
        Lid l = lid(6);
        assertEquals(1, l.getHeight());
        assertEquals(1, l.getHeightCm());
    }

    @Test
    public void getColor_beforeAssociation_shouldReturnFallback() {
        Lid l = lid(1);
        String color = l.getColor();
        assertNotNull(color);
        assertTrue(color.trim().length() > 0, "Color debe ser no vacío");
    }

    @Test
    public void toString_shouldContainNumber() {
        Lid l = lid(9);
        assertTrue(l.toString().contains("9"), "toString debe contener el número");
    }
}
