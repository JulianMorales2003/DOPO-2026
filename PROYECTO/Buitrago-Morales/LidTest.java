import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase Lid.
 *
 * @author Julian Morales - Sergio Buitrago
 *
 */
public class LidTest {

    /**
     * Verifica que el constructor almacena el número y que getNumber() lo devuelve.
     */
    @Test
    public void constructor_shouldStoreNumber_andGetterShouldReturnIt() {
        Lid l1 = new Lid(1);
        Lid l7 = new Lid(7);
        assertEquals(1, l1.getNumber());
        assertEquals(7, l7.getNumber());
    }

    /**
     * Por defecto, un Lid no está asociado a ninguna copa.
     */
    @Test
    public void defaultState_shouldNotBeOnCup_andAssociatedCupIsNull() {
        Lid lid = new Lid(3);
        assertFalse("Por defecto no debe estar sobre una copa", lid.isOnCup());
        assertNull("Por defecto no debe haber copa asociada", lid.getAssociatedCup());
    }

    /**
     * setAssociatedCup(null) debe dejar el Lid sin asociación (independiente de Cup).
     */
    @Test
    public void setAssociatedCup_null_shouldClearAssociation() {
        Lid lid = new Lid(5);

        lid.setAssociatedCup(null);
        assertFalse(lid.isOnCup());
        assertNull(lid.getAssociatedCup());

        lid.setAssociatedCup(null); 
        assertFalse(lid.isOnCup());
        assertNull(lid.getAssociatedCup());
    }

    /**
     * makeInvisible() debe ser invocable en cualquier momento sin lanzar excepción.
     */
    @Test
    public void makeInvisible_shouldBeCallableSafely_andIdempotent() {
        Lid lid = new Lid(2);
        lid.makeInvisible();
        lid.makeInvisible(); 
    }

    /**
     * makeVisibleAt(...) debe aceptar parámetros válidos sin lanzar excepción.
     */
    @Test
    public void makeVisibleAt_shouldBeCallableWithValidParams() {
        Lid lid = new Lid(4);
        final int scale = 10; 
        final int sizeCm = 3;  

        int wPx = sizeCm * scale;
        int hPx = sizeCm * scale;

        lid.makeVisibleAt(50, 60, wPx, hPx);
        lid.makeVisibleAt(10, 15, wPx + 5, hPx + 2);
    }

    /**
     * toString() debe incluir, al menos, el número del Lid (sin depender de Cup).
     */
    @Test
    public void toString_shouldContainNumber() {
        Lid lid = new Lid(9);
        String s = lid.toString();
        assertTrue(s.contains("9"));
    }
}