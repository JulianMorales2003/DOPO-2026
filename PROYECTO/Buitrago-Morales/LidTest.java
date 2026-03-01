import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase Lid.
 *
 * @author
 *   Julian Morales - Sergio Buitrago
 */
public class LidTest {

    /** El constructor debe almacenar el número. */
    @Test
    public void constructor_shouldStoreNumber_andGetterShouldReturnIt() {
        Lid l1 = new Lid(1);
        Lid l7 = new Lid(7);
        assertEquals(1, l1.getNumber());
        assertEquals(7, l7.getNumber());
    }

    /** Estado por defecto: sin asociación. */
    @Test
    public void defaultState_shouldNotBeOnCup_andAssociatedCupIsNull() {
        Lid lid = new Lid(3);
        assertFalse(lid.isOnCup());
        assertNull(lid.getAssociatedCup());
    }

    /** makeInvisible() debe ser invocable en cualquier momento y ser idempotente. */
    @Test
    public void makeInvisible_shouldBeCallableSafely_andIdempotent() {
        Lid lid = new Lid(2);
        lid.makeInvisible();
        lid.makeInvisible();
    }

    /**
     * makeVisible() sin asociación no debe lanzar excepción.
     * (No valida la GUI; solo robustez de la llamada en estado por defecto.)
     */
    @Test
    public void makeVisible_withoutAssociation_shouldNotThrow() {
        Lid lid = new Lid(4);
        lid.makeVisible();   
        lid.makeInvisible(); 
    }

    /** getHeight()/getHeightCm() deben ser 1 (compatibilidad con Tower). */
    @Test
    public void height_getters_shouldReturnOne() {
        Lid lid = new Lid(6);
        assertEquals(1, lid.getHeight());
        assertEquals(1, lid.getHeightCm());
    }

    /** getColor() antes de asociar debe devolver el color por defecto (fallback). */
    @Test
    public void getColor_beforeAssociation_shouldReturnFallback() {
        Lid lid = new Lid(1);
        String color = lid.getColor();
        assertNotNull(color);
        assertTrue("Color debe ser no vacío", color.trim().length() > 0);
    }


    /** toString() debe incluir, al menos, el número de la tapa. */
    @Test
    public void toString_shouldContainNumber() {
        Lid lid = new Lid(9);
        String s = lid.toString();
        assertTrue("toString debe contener el número", s.contains("9"));
    }
}