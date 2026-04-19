package Test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tower.TowerContest;

/**
 * Clase de pruebas unitarias para TowerContest.
 * Todas las pruebas son invisibles (no se abre interfaz gráfica).
 *
 * @author Julian Morales - Sergio Buitrago
 */
public class TowerContestTest {

    @Test
    public void solve_zeroN_shouldReturnImpossible() {
        assertEquals("impossible", TowerContest.solve(0, 5));
    }

    @Test
    public void solve_zeroH_shouldReturnImpossible() {
        assertEquals("impossible", TowerContest.solve(3, 0));
    }

    @Test
    public void solve_negativeN_shouldReturnImpossible() {
        assertEquals("impossible", TowerContest.solve(-1, 10));
    }

    @Test
    public void solve_negativeH_shouldReturnImpossible() {
        assertEquals("impossible", TowerContest.solve(3, -5));
    }

    @Test
    public void solve_heightUnreachable_shouldReturnImpossible() {
        assertEquals("impossible", TowerContest.solve(2, 100));
    }

    @Test
    public void solve_heightTwoWithOneCup_shouldReturnImpossible() {
        assertEquals("impossible", TowerContest.solve(1, 2));
    }

    @Test
    public void solve_oneCupHeightOne_shouldReturnCupOne() {
        String result = TowerContest.solve(1, 1);
        assertNotEquals("impossible", result);
        assertEquals("1", result.trim());
    }

    @Test
    public void solve_validResult_shouldNotBeEmpty() {
        String result = TowerContest.solve(3, 1);
        assertNotEquals("impossible", result);
        assertFalse(result.trim().isEmpty());
    }

    @Test
    public void solve_validResult_shouldContainOnlyNumbers() {
        String result = TowerContest.solve(5, 3);
        if (!result.equals("impossible")) {
            assertTrue(result.matches("[0-9]+(\\s[0-9]+)*"));
        }
    }

    @Test
    public void solve_validResult_cupNumbersShouldBeInRange() {
        int n = 4;
        String result = TowerContest.solve(n, 7);
        if (!result.equals("impossible")) {
            for (String part : result.trim().split("\\s+")) {
                int cup = Integer.parseInt(part);
                assertTrue("Cup number out of range: " + cup, cup >= 1 && cup <= n);
            }
        }
    }

    @Test
    public void solve_validResult_noDuplicateCups() {
        int n = 5;
        String result = TowerContest.solve(n, 9);
        if (!result.equals("impossible")) {
            java.util.Set<String> seen = new java.util.HashSet<>();
            for (String part : result.trim().split("\\s+")) {
                assertTrue("Duplicate cup found: " + part, seen.add(part));
            }
        }
    }

    @Test
    public void solve_heightFour_shouldBePossibleWithTwoCups() {
        String result = TowerContest.solve(2, 4);
        assertNotEquals("impossible", result);
        assertEquals(2, result.trim().split("\\s+").length);
    }

    @Test
    public void solve_heightEqualsFirstCup_shouldReturnSingleCup() {
        String result = TowerContest.solve(5, 1);
        assertNotEquals("impossible", result);
        assertEquals("1", result.trim());
    }

    @Test
    public void solve_resultHeightMatchesTarget() {
        int n = 5;
        int h = 9;
        String result = TowerContest.solve(n, h);
        if (!result.equals("impossible")) {
            int total = 0;
            for (String p : result.trim().split("\\s+")) {
                total += 2 * Integer.parseInt(p) - 1;
            }
            assertEquals("Sum of cup heights must equal target", h, total);
        }
    }

    @Test
    public void solve_largeN_shouldFindSolutionOrImpossible() {
        String result = TowerContest.solve(10, 25);
        assertTrue(result.equals("impossible") || result.matches("[0-9]+(\\s[0-9]+)*"));
    }

    @Test
    public void solve_multipleCallsSameInput_shouldBeDeterministic() {
        String r1 = TowerContest.solve(4, 8);
        String r2 = TowerContest.solve(4, 8);
        assertEquals(r1, r2);
    }
}
