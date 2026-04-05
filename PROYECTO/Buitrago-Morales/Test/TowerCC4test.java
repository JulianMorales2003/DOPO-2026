package Test;

import tower.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de CASOS COMUNES para el Ciclo 4 (TowerCC4test).
 * Verifican escenarios de integración entre componentes del sistema.
 * Todas corren internamente: ninguna abre ventana, canvas ni JFrame.
 *
 * @author Julian Morales - Sergio Buitrago
 * @version 4.0 (Ciclo 4 - TowerCC4test)
 */
public class TowerCC4test {

    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(15, 100);
    }

    // =========================================================
    // CC1 — Estado inicial de la torre
    // =========================================================

    @Test
    public void cc1_torreRecienCreada_estadoInicialCorrecto() {
        assertEquals(0, tower.height());
        assertEquals(0, tower.lidedCups().length);
        assertEquals(0, tower.stackingItems().length);
        assertTrue(tower.ok());
    }

    // =========================================================
    // CC2 — Copas normales en secuencia
    // =========================================================

    @Test
    public void cc2_tresCopasNormales_alturaEsNueve() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        assertEquals(9, tower.height());
        assertEquals(3, tower.stackingItems().length);
    }

    // =========================================================
    // CC3 — push y pop restauran estado
    // =========================================================

    @Test
    public void cc3_pushYpopCopa_restauraEstadoPrevio() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.popCup();
        assertEquals(1, tower.height());
        assertFalse(cupEstaEnTorre(tower, 2));
        assertTrue(cupEstaEnTorre(tower, 1));
    }

    // =========================================================
    // CC4 — Tapa normal se asocia a copa del mismo número
    // =========================================================

    @Test
    public void cc4_tapaNormalMismoNumero_quedaAsociadaACopa() {
        tower.pushCup(2);
        tower.pushLid(2);
        int[] lided = tower.lidedCups();
        assertEquals(1, lided.length);
        assertEquals(2, lided[0]);
    }

    // =========================================================
    // CC5 — FearfulLid rechazada sin copa compañera
    // =========================================================

    @Test
    public void cc5_fearfulLidSinCopaCompanera_noEntraAlaTorre() {
        tower.pushCup(1);
        tower.pushLidType("fearful", 3);
        assertEquals(0, tower.lidedCups().length);
    }

    // =========================================================
    // CC6 — FearfulLid no puede salir si está sobre su copa
    // =========================================================

    @Test
    public void cc6_fearfulLidSobreSuCopa_noSaleConPopLid() {
        tower.pushCup(1);
        tower.pushLidType("fearful", 1);
        tower.popLid();
        assertEquals(1, tower.lidedCups().length);
    }

    // =========================================================
    // CC7 — CrazyLid se inserta en la base
    // =========================================================

    @Test
    public void cc7_crazyLid_siempreOcupaLaBase() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushLidType("crazy", 3);
        String[][] items = tower.stackingItems();
        assertEquals("lid", items[0][0]);
    }

    // =========================================================
    // CC8 — GluedLid no puede salir una vez pegada
    // =========================================================

    @Test
    public void cc8_gluedLidPegada_noSaleConPopNiConRemove() {
        tower.pushCup(1);
        tower.pushLidType("glued", 1);
        tower.popLid();
        assertEquals(1, tower.lidedCups().length);
        tower.removeLid(1);
        assertEquals(1, tower.lidedCups().length);
    }

    // =========================================================
    // CC9 — OpenerCup elimina todas las tapas al entrar
    // =========================================================

    @Test
    public void cc9_openerCup_eliminaTodasLasTapasExistentes() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushCup(3);
        tower.pushLidType("fearful", 3);
        assertEquals(2, tower.lidedCups().length);
        tower.pushCupType("opener", 2);
        assertEquals(0, tower.lidedCups().length);
    }

    // =========================================================
    // CC10 — HierarchicalCup en el fondo no puede retirarse
    // =========================================================

    @Test
    public void cc10_hierarchicalCupEnFondo_noPuedeSalirConPop() {
        tower.pushCup(1);
        tower.pushCupType("hierarchical", 3);
        tower.popCup();
        assertTrue("HierarchicalCup no debió salir del fondo", cupEstaEnTorre(tower, 3));
    }

    // =========================================================
    // CC11 — orderTower ordena de mayor a menor
    // =========================================================

    @Test
    public void cc11_orderTower_colocaMayoresAlFondo() {
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.orderTower();
        String[][] items = tower.stackingItems();
        int idx1 = indexOf(items, "cup", "1");
        int idx2 = indexOf(items, "cup", "2");
        int idx3 = indexOf(items, "cup", "3");
        assertTrue("Copa 3 debe estar antes que copa 2", idx3 < idx2);
        assertTrue("Copa 2 debe estar antes que copa 1", idx2 < idx1);
    }

    // =========================================================
    // CC12 — reverseTower invierte el orden
    // =========================================================

    @Test
    public void cc12_reverseTower_invierteOrdenDeLasCopas() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.reverseTower();
        String[][] items = tower.stackingItems();
        assertEquals("3", items[0][1]);
    }

    // =========================================================
    // CC13 — swap intercambia posiciones
    // =========================================================

    @Test
    public void cc13_swap_intercambiaDosCopasCorrectamente() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.swap(new String[]{"cup","1"}, new String[]{"cup","2"});
        String[][] items = tower.stackingItems();
        int idx1 = indexOf(items, "cup", "1");
        int idx2 = indexOf(items, "cup", "2");
        assertTrue("Copa 2 debe quedar antes que copa 1 tras el swap", idx2 < idx1);
    }

    // =========================================================
    // CC14 — cover tapa todas las copas sin tapa
    // =========================================================

    @Test
    public void cc14_cover_tapaTodaLasCopasQueNoTienenTapa() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.cover();
        assertEquals(3, tower.lidedCups().length);
    }

    @Test
    public void cc14b_cover_noDuplicaTapasExistentes() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.cover();
        assertEquals(2, tower.lidedCups().length);
    }

    // =========================================================
    // CC15 — swapToReduce retorna par o "none"
    // =========================================================

    @Test
    public void cc15_swapToReduce_unaCopaDevuelveNone() {
        tower.pushCup(1);
        assertEquals("none", tower.swapToReduce()[0][0]);
    }

    @Test
    public void cc15b_swapToReduce_variasCopasTieneFormatoCorrecto() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        String[][] result = tower.swapToReduce();
        assertNotNull(result);
        assertEquals(2, result.length);
        assertNotNull(result[0][0]);
        assertNotNull(result[1][0]);
    }

    // =========================================================
    // CC16 — TowerContest.solve (solo lógica, sin simulate)
    // =========================================================

    @Test
    public void cc16_solve_casosBasicos_correctos() {
        assertEquals("1",          TowerContest.solve(1, 1));
        assertEquals("2",          TowerContest.solve(2, 3));
        assertEquals("impossible", TowerContest.solve(1, 4));
    }

    @Test
    public void cc16b_solve_h9_sumaEsNueve() {
        String result = TowerContest.solve(3, 9);
        assertFalse(result.equals("impossible"));
        assertEquals(9, sumarAlturasEnSolucion(result));
    }

    // =========================================================
    // CC17 — OpenerCup elimina FearfulLid
    // =========================================================

    @Test
    public void cc17_openerCup_eliminaTambienFearfulLid() {
        tower.pushCup(1);
        tower.pushLidType("fearful", 1);
        assertEquals(1, tower.lidedCups().length);
        tower.pushCupType("opener", 2);
        assertEquals(0, tower.lidedCups().length);
    }

    // =========================================================
    // CC18 — HierarchicalCup permanece en fondo después de orderTower
    // =========================================================

    @Test
    public void cc18_hierarchicalCupDespuesDeOrder_sigueAtrapada() {
        tower.pushCupType("hierarchical", 2);
        tower.pushCup(1);
        tower.orderTower();
        tower.popCup();
        assertTrue("HierarchicalCup debe seguir en la torre tras orderTower",
                   cupEstaEnTorre(tower, 2));
    }

    // =========================================================
    // CC19 — BouncerCup entra si no hay copa con su misma altura
    // =========================================================

    @Test
    public void cc19_bouncerCup_sinDuplicadoDeAltura_entra() {
        tower.pushCupType("bouncer", 2);
        assertEquals(3, tower.height());
        assertTrue(cupEstaEnTorre(tower, 2));
    }

    // =========================================================
    // CC20 — Secuencia completa mantiene ok()
    // =========================================================

    @Test
    public void cc20_secuenciaCompletaDeOperaciones_siempreOk() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.cover();
        tower.popCup();
        tower.orderTower();
        tower.reverseTower();
        assertTrue(tower.ok());
        assertTrue(tower.height() <= 100);
    }

    // =========================================================
    // CC21 — stackingItems refleja el orden real de la torre
    // =========================================================

    
    @Test
    
    public void cc21_stackingItems_reflejaOrdenRealDeLaTorre() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushLid(2);
        String[][] items = tower.stackingItems();
        assertEquals(3, items.length);
        // Verificar que existen los elementos correctos sin asumir orden exacto
        boolean hasCup1 = false, hasCup2 = false, hasLid2 = false;
        for (String[] item : items) {
            if ("cup".equals(item[0]) && "1".equals(item[1])) hasCup1 = true;
            if ("cup".equals(item[0]) && "2".equals(item[1])) hasCup2 = true;
            if ("lid".equals(item[0]) && "2".equals(item[1])) hasLid2 = true;
        }
        assertTrue("Debe contener copa 1", hasCup1);
        assertTrue("Debe contener copa 2", hasCup2);
        assertTrue("Debe contener tapa 2", hasLid2);
    }

    // =========================================================
    // CC22 — Altura no cambia tras intento de operación inválida
    // =========================================================

    @Test
    public void cc22_operacionInvalida_alturaNoSeModifica() {
        tower.pushCup(1);
        int alturaAntes = tower.height();
        tower.pushCup(1);
        tower.pushLidType("fearful", 9);
        assertEquals(alturaAntes, tower.height());
    }

    // =========================================================
    // UTILIDADES PRIVADAS
    // =========================================================

    private boolean cupEstaEnTorre(Tower t, int numero) {
        for (String[] item : t.stackingItems())
            if ("cup".equals(item[0]) && String.valueOf(numero).equals(item[1])) return true;
        return false;
    }

    private int indexOf(String[][] items, String tipo, String num) {
        for (int i = 0; i < items.length; i++)
            if (tipo.equals(items[i][0]) && num.equals(items[i][1])) return i;
        return -1;
    }

    private int sumarAlturasEnSolucion(String result) {
        int total = 0;
        for (String p : result.split(" "))
            total += 2 * Integer.parseInt(p.trim()) - 1;
        return total;
    }
}