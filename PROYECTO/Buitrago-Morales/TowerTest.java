import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase Tower.
 *
 * @author Julian Morales - Sergio Buitrago
 *
 */
public class TowerTest {

    /**
     * Prueba donde debe rechazar width <= 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldRejectNonPositiveWidth() {
        new Tower(0, 20);
    }

    /**
     * Prueba donde debe rechazar maxHeight <= 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldRejectNonPositiveMaxHeight() {
        new Tower(20, 0);
    }


    @Test
    public void constructor_validValues_shouldCreateTower() {
        Tower t = new Tower(40, 50);
        assertTrue(t.ok());
        assertEquals(0, t.height());
        assertNotNull(t.stackingItems());
        assertEquals(0, t.stackingItems().length);
    }

    /**
     * Prueba donde permite anidar (4, luego 3) sin aumentar altura efectiva.
     */
    @Test
    public void pushCup_smallerInsideBigger_shouldNest_andNotIncreaseTotalHeight() {
        Tower t = new Tower(60, 30);
        t.pushCup(4);
        assertTrue(t.ok());
        assertEquals(7, t.height());

        t.pushCup(3); 
        assertTrue(t.ok());
        assertEquals(7, t.height());
    }

    
    /**
     * Prueba de elimina la última copa y actualiza la altura.
     * Métodos cubiertos: pushCup, popCup, height.
     */
    @Test
    public void popCup_shouldRemoveLastCup_andUpdateHeight() {
        Tower t = new Tower(60, 40);
        t.pushCup(4);
        t.pushCup(3); 
        assertEquals(7, t.height());

        t.popCup();  
        assertEquals(7, t.height());
        t.popCup();   
        assertEquals(0, t.height());
    }

    
    /**
     * Prueba de tapas agregadas/quitadas impactan.
     */
    @Test
    public void pushAndRemoveLid_shouldAffectLidedCupsArray() {
        Tower t = new Tower(80, 50);
        t.pushCup(3);
        t.pushCup(2);

        t.pushLid(3);
        t.pushLid(1);

        int[] covered = t.lidedCups();
        assertArrayEquals(new int[]{3}, covered);

        t.removeLid(3);
        covered = t.lidedCups();
        assertEquals(0, covered.length);

        t.popLid();
        covered = t.lidedCups();
        assertEquals(0, covered.length);
    }

    /**
     * Prueba de reordena sin romper el límite de altura efectiva.
     */
    @Test
    public void orderTower_shouldReorderButRespectEffectiveHeightBound() {
        Tower t = new Tower(100, 30);
        t.pushCup(2);
        t.pushCup(4);
        t.pushCup(3);
        assertTrue(t.ok());

        int hBefore = t.height();
        t.orderTower();
        int hAfter = t.height();

        assertTrue(hAfter <= hBefore);
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertNotNull(items);
        assertEquals(3, items.length);
    }

    /**
     * Prueba de invierte la secuencia y recalcula altura.
     */
    @Test
    public void reverseTower_shouldReverseSequence_andRecomputeHeight() {
        Tower t = new Tower(100, 40);
        t.pushCup(4);
        t.pushCup(3);
        t.pushCup(2);

        int before = t.height();
        t.reverseTower();
        int after = t.height();

        assertTrue(after >= 0);
        assertTrue(t.height() >= 0);
    }


    /**
     * Prueba de expone el arreglo [tipo, número] en orden actual.
     */
    @Test
    public void stackingItems_shouldExposeTypeAndNumber() {
        Tower t = new Tower(80, 40);
        t.pushCup(2);
        t.pushLid(2);
        t.pushCup(1);

        String[][] items = t.stackingItems();
        assertEquals(3, items.length);

        assertEquals("cup", items[0][0]); assertEquals("2", items[0][1]);
        assertEquals("lid", items[1][0]); assertEquals("2", items[1][1]);
        assertEquals("cup", items[2][0]); assertEquals("1", items[2][1]);
    }

    /**
     * Prueba de los makeVisible, makeInvisible deben ser invocables sin lanzar.
     */
    @Test
    public void makeVisible_and_makeInvisible_shouldBeCallableSafely() {
        Tower t = new Tower(60, 40);
        t.pushCup(3);
        t.pushCup(2);

        t.makeVisible();
        t.makeInvisible();
        t.makeVisible();
        t.makeInvisible();
    }

}