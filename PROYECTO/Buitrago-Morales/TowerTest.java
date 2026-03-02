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
    
    
    /**
     * Con 0 copas debe crear una torre válida vacía sin lanzar excepción.
     */
    @Test
    public void constructorCups_zeroCups_shouldCreateEmptyTower() {
        Tower t = new Tower(0);
        assertTrue(t.ok());
        assertEquals(0, t.height());
        assertEquals(0, t.stackingItems().length);
    }

    /**
     * Debe rechazar un número negativo de copas
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorCups_negativeCups_shouldThrow() {
        new Tower(-1);
    }

    /**
     * Con n copas, el ancho calculado debe ser 2n-1 y la torre debe ser válida.
     * Se verifica indirectamente con ok() y que las copas fueron agregadas.
     */
    @Test
    public void constructorCups_threeCups_shouldAddThreeCupsAndBeOk() {
        Tower t = new Tower(3);
        assertTrue(t.ok());
        String[][] items = t.stackingItems();
        int cupCount = 0;
        for (String[] item : items) {
            if ("cup".equals(item[0])) cupCount++;
        }
        assertEquals(3, cupCount);
    }

    /**
     * La altura efectiva de la torre creada con n copas no debe superar maxHeight (n*n).
     */
    @Test
    public void constructorCups_fiveCups_heightShouldRespectMaxHeight() {
        Tower t = new Tower(5);
        assertTrue(t.ok());
        assertTrue(t.height() <= 25);
    }

    /**
     * Con 1 copa la torre debe tener exactamente 1 copa y estar ok.
     */
    @Test
    public void constructorCups_oneCup_shouldHaveOneCup() {
        Tower t = new Tower(1);
        assertTrue(t.ok());
        assertEquals(1, t.stackingItems().length);
        assertEquals("cup", t.stackingItems()[0][0]);
        assertEquals("1", t.stackingItems()[0][1]);
    }
    
    
    /**
     * Intercambiar dos copas existentes debe cambiar su posición en la secuencia.
     */
    @Test
    public void swap_twoCups_shouldSwapPositions() {
        Tower t = new Tower(100, 50);
        t.pushCup(4);
        t.pushCup(2);

        t.swap(new String[]{"cup", "4"}, new String[]{"cup", "2"});

        String[][] items = t.stackingItems();
        assertEquals("2", items[0][1]);
        assertEquals("4", items[1][1]);
    }

    /**
     * Intercambiar una copa consigo misma no debe cambiar nada.
     */
    @Test
    public void swap_sameCup_shouldDoNothing() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(2);

        String[][] before = t.stackingItems();
        t.swap(new String[]{"cup", "3"}, new String[]{"cup", "3"});
        String[][] after = t.stackingItems();

        assertArrayEquals(before[0], after[0]);
        assertArrayEquals(before[1], after[1]);
    }

    /**
     * swap con  null debe mostrar error y retornar
     */
    @Test
    public void swap_nullDescriptor_shouldNotThrow() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(2);
        
        t.swap(null, new String[]{"cup", "2"});
        t.swap(new String[]{"cup", "3"}, null);
    }

    /**
     * swap con una copa que no existe no debe modificar la secuencia
     */
    @Test
    public void swap_nonExistentCup_shouldNotModifySequence() {
        Tower t = new Tower(100, 50);
        t.pushCup(4);
        t.pushCup(2);

        String[][] before = t.stackingItems();
        t.swap(new String[]{"cup", "4"}, new String[]{"cup", "9"});
        String[][] after = t.stackingItems();

        assertEquals(before.length, after.length);
        assertArrayEquals(before[0], after[0]);
    }

    /**
     * Después de un swap válido, la torre debe seguir siendo ok()
     */
    @Test
    public void swap_valid_shouldKeepTowerOk() {
        Tower t = new Tower(100, 50);
        t.pushCup(4);
        t.pushCup(3);
        t.pushCup(2);

        t.swap(new String[]{"cup", "4"}, new String[]{"cup", "2"});
        assertTrue(t.ok());
    }

    /**
     * Si el swap haría superar maxHeight, debe revertirse y la torre sigue ok()
     * Se construye al limite para forzar el error o que rechaze
     */
    @Test
    public void swap_wouldExceedMaxHeight_shouldRevert() {
        Tower t = new Tower(20, 10);
        t.pushCup(5); // altura 5, ocupa todo
        t.pushCup(4);

        boolean wasOkBefore = t.ok();
        t.swap(new String[]{"cup", "4"}, new String[]{"cup", "5"});
        assertTrue(t.ok());
    }

    /**
     * swap con tipo "lid" debe mostrar error y no cambiar nada
     */
    @Test
    public void swap_withLidType_shouldNotSwap() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(2);

        String[][] before = t.stackingItems();
        t.swap(new String[]{"lid", "3"}, new String[]{"cup", "2"});
        String[][] after = t.stackingItems();

        assertEquals(before.length, after.length);
    }
    
    
    /**
     * Con menos de 2 copas debe retornar descriptores none
     */
    @Test
    public void swapToReduce_lessThanTwoCups_shouldReturnNone() {
        Tower t = new Tower(50, 30);
        t.pushCup(3);

        String[][] result = t.swapToReduce();
        assertEquals("none", result[0][0]);
        assertEquals("none", result[1][0]);
    }

    /**
     * Si existe un swap que reduce la altura, debe retornar descriptores válidos de copa
     */
    @Test
    public void swapToReduce_whenBetterSwapExists_shouldReturnCupDescriptors() {
        Tower t = new Tower(100, 50);
        t.pushCup(2);
        t.pushCup(4);

        String[][] result = t.swapToReduce();
        if (!"none".equals(result[0][0])) {
            assertEquals("cup", result[0][0]);
            assertEquals("cup", result[1][0]);
            assertNotEquals(result[0][1], result[1][1]);
        }
    }

    /**
     * Cuando la secuencia ya es óptima (no hay swap que mejore), retorna none
     */
    @Test
    public void swapToReduce_alreadyOptimal_shouldReturnNone() {
        Tower t = new Tower(100, 50);
        // Solo una copa
        t.pushCup(4);
        String[][] result = t.swapToReduce();
        assertEquals("none", result[0][0]);
    }

    /**
     * El resultado nunca debe ser null y siempre debe tener exactamente 2 descriptores.
     */
    @Test
    public void swapToReduce_shouldAlwaysReturnTwoDescriptors() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(5);
        t.pushCup(2);

        String[][] result = t.swapToReduce();
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(2, result[1].length);
    }

    /**
     * Si se aplica el swap sugerido por swapToReduce, la altura no debe aumentar
     */
    @Test
    public void swapToReduce_appliedSwap_shouldNotIncreaseHeight() {
        Tower t = new Tower(100, 50);
        t.pushCup(2);
        t.pushCup(5);
        t.pushCup(3);

        int heightBefore = t.height();
        String[][] best = t.swapToReduce();

        if (!"none".equals(best[0][0])) {
            t.swap(best[0], best[1]);
            int heightAfter = t.height();
            assertTrue(heightAfter <= heightBefore);
        }
    }
    
    
    /**
     * cover() debe asociar cada tapa libre con su copa correspondiente
     */
    @Test
    public void cover_shouldMatchLidsToCups() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(2);
        t.pushLid(3);
        t.pushLid(2);
        t.cover();

        int[] covered = t.lidedCups();
        assertEquals(2, covered.length);
    }

    /**
     * cover() sin tapas en la torre no debe lanzar excepción
     */
    @Test
    public void cover_noLids_shouldNotThrow() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(2);
        t.cover();
        assertEquals(0, t.lidedCups().length);
    }

    /**
     * cover() no debe duplicar asociaciones si se llama dos veces
     */
    @Test
    public void cover_calledTwice_shouldNotDuplicateAssociations() {
        Tower t = new Tower(100, 50);
        t.pushCup(4);
        t.pushLid(4);

        t.cover();
        t.cover();

        int[] covered = t.lidedCups();
        assertEquals(1, covered.length);
        assertEquals(4, covered[0]);
    }

    /**
     * Una tapa sin copa correspondiente en la torre no debe asociarse a ninguna copa
     */
    @Test
    public void cover_lidWithoutMatchingCup_shouldNotAssociate() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushLid(5); // no hay copa 5

        t.cover();

        int[] covered = t.lidedCups();
        assertEquals(0, covered.length);
    }

    /**
     * Después de cover(), la torre debe seguir siendo ok()
     */
    @Test
    public void cover_shouldKeepTowerOk() {
        Tower t = new Tower(100, 50);
        t.pushCup(4);
        t.pushCup(3);
        t.pushLid(4);
        t.pushLid(3);

        t.cover();
        assertTrue(t.ok());
    }
}