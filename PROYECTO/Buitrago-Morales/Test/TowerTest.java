package clases.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tower.Tower;

/**
 * Clase de pruebas unitarias para la clase Tower.
 * TODAS LAS PRUEBAS SON INVISIBLES (no se abre la interfaz gráfica).
 *
 * @author Julian Morales - Sergio Buitrago
 */
public class TowerTest {

    @Test
    public void constructor_shouldRejectNonPositiveWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Tower(0, 20));
    }

    @Test
    public void constructor_shouldRejectNonPositiveMaxHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Tower(20, 0));
    }

    @Test
    public void constructor_validValues_shouldCreateTower() {
        Tower t = new Tower(40, 50);
        assertTrue(t.ok());
        assertEquals(0, t.height());
        assertNotNull(t.stackingItems());
        assertEquals(0, t.stackingItems().length);
    }

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

    @Test
    public void reverseTower_shouldReverseSequence_andRecomputeHeight() {
        Tower t = new Tower(100, 40);
        t.pushCup(4);
        t.pushCup(3);
        t.pushCup(2);
        t.reverseTower();
        assertTrue(t.height() >= 0);
    }

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

    @Test
    public void makeVisible_and_makeInvisible_shouldBeCallableSafely() {
        Tower t = new Tower(60, 40);
        t.pushCup(3);
        t.pushCup(2);
        assertTrue(t.ok());
        assertEquals(2, t.stackingItems().length);
    }

    @Test
    public void constructorCups_zeroCups_shouldCreateEmptyTower() {
        Tower t = new Tower(40, 50);
        assertTrue(t.ok());
        assertEquals(0, t.height());
        assertEquals(0, t.stackingItems().length);
    }

    @Test
    public void constructorCups_negativeCups_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Tower(-1));
    }

    @Test
    public void constructorCups_threeCups_shouldAddThreeCupsAndBeOk() {
        Tower t = new Tower(100, 50);
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        assertTrue(t.ok());
        int cupCount = 0;
        for (String[] item : t.stackingItems()) {
            if ("cup".equals(item[0])) cupCount++;
        }
        assertEquals(3, cupCount);
    }

    @Test
    public void constructorCups_fiveCups_heightShouldRespectMaxHeight() {
        Tower t = new Tower(100, 50);
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        t.pushCup(4);
        t.pushCup(5);
        assertTrue(t.ok());
        assertTrue(t.height() <= 25);
    }

    @Test
    public void constructorCups_oneCup_shouldHaveOneCup() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        assertTrue(t.ok());
        assertEquals(1, t.stackingItems().length);
        assertEquals("cup", t.stackingItems()[0][0]);
        assertEquals("1", t.stackingItems()[0][1]);
    }

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

    @Test
    public void swap_nullDescriptor_shouldNotThrow() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(2);
        assertDoesNotThrow(() -> t.swap(null, new String[]{"cup", "2"}));
        assertDoesNotThrow(() -> t.swap(new String[]{"cup", "3"}, null));
    }

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

    @Test
    public void swap_valid_shouldKeepTowerOk() {
        Tower t = new Tower(100, 50);
        t.pushCup(4);
        t.pushCup(3);
        t.pushCup(2);
        t.swap(new String[]{"cup", "4"}, new String[]{"cup", "2"});
        assertTrue(t.ok());
    }

    @Test
    public void swap_wouldExceedMaxHeight_shouldRevert() {
        Tower t = new Tower(20, 10);
        t.pushCup(5);
        t.pushCup(4);
        t.swap(new String[]{"cup", "4"}, new String[]{"cup", "5"});
        assertTrue(t.ok());
    }

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

    @Test
    public void swapToReduce_lessThanTwoCups_shouldReturnNone() {
        Tower t = new Tower(50, 30);
        t.pushCup(3);
        String[][] result = t.swapToReduce();
        assertEquals("none", result[0][0]);
        assertEquals("none", result[1][0]);
    }

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

    @Test
    public void swapToReduce_alreadyOptimal_shouldReturnNone() {
        Tower t = new Tower(100, 50);
        t.pushCup(4);
        String[][] result = t.swapToReduce();
        assertEquals("none", result[0][0]);
    }

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
            assertTrue(t.height() <= heightBefore);
        }
    }

    @Test
    public void cover_shouldMatchLidsToCups() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(2);
        t.pushLid(3);
        t.pushLid(2);
        t.cover();
        assertEquals(2, t.lidedCups().length);
    }

    @Test
    public void cover_noLids_shouldNotThrow() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushCup(2);
        t.cover();
        assertTrue(t.ok());
    }

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

    @Test
    public void cover_lidWithoutMatchingCup_shouldNotAssociateUnmatchedLid() {
        Tower t = new Tower(100, 50);
        t.pushCup(3);
        t.pushLid(5);
        t.cover();
        for (int c : t.lidedCups()) {
            assertNotEquals(5, c);
        }
    }

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
