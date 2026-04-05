package Test;

import tower.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de UNIDAD para el Ciclo 4.
 * Todas corren internamente: ninguna abre ventana, canvas ni JFrame.
 * Se usa únicamente el estado lógico de los objetos.
 *
 * @author Julian Morales - Sergio Buitrago
 * @version 4.0 (Ciclo 4 - TowerC4test)
 */
public class TowerC4test {

    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(15, 50);
    }

    // =========================================================
    // TOWER — constructores
    // =========================================================

    @Test
    public void testConstructorPrincipal_parametrosValidos_okTrue() {
        Tower t = new Tower(10, 30);
        assertTrue(t.ok());
    }

    @Test
    public void testConstructorPrincipal_torreNueva_alturaCero() {
        assertEquals(0, tower.height());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPrincipal_widthCero_lanzaExcepcion() {
        new Tower(0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPrincipal_maxHeightNegativo_lanzaExcepcion() {
        new Tower(5, -1);
    }

    @Test
    public void testConstructorMasivo_tresCopas_alturaNueve() {
        Tower t = new Tower(15, 50);
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        assertEquals(9, t.height());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMasivo_negativo_lanzaExcepcion() {
        new Tower(-1);
    }

    // =========================================================
    // TOWER — pushCup / popCup
    // =========================================================

    @Test
    public void testPushCup_copaUno_alturaUno() {
        tower.pushCup(1);
        assertEquals(1, tower.height());
    }

    @Test
    public void testPushCup_dosCopas_alturaSuma() {
        tower.pushCup(1);
        tower.pushCup(2);
        assertEquals(4, tower.height());
    }

    @Test
    public void testPushCup_copaDuplicada_noAgrega() {
        tower.pushCup(1);
        tower.pushCup(1);
        assertEquals(1, tower.height());
    }

    @Test
    public void testPushCup_excedeAlturaMaxima_noAgrega() {
        Tower small = new Tower(5, 1);
        small.pushCup(1);
        small.pushCup(2);
        assertEquals(1, small.height());
    }

    @Test
    public void testPopCup_unaCopaAgregada_alturaVuelveACero() {
        tower.pushCup(1);
        tower.popCup();
        assertEquals(0, tower.height());
    }

    @Test
    public void testPopCup_torreVacia_noLanzaExcepcion() {
        tower.popCup();
        assertEquals(0, tower.height());
    }

    @Test
    public void testPopCup_dosCopas_eliminaLaUltima() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.popCup();
        assertEquals(1, tower.height());
        assertFalse(cupEstaEnTorre(tower, 2));
    }

    // =========================================================
    // TOWER — pushCupType (todos los tipos)
    // =========================================================

    @Test
    public void testPushCupType_normal_seAgrega() {
        tower.pushCupType("normal", 1);
        assertEquals(1, tower.height());
    }

    @Test
    public void testPushCupType_opener_seAgrega() {
        tower.pushCupType("opener", 1);
        assertEquals(1, tower.height());
    }

    @Test
    public void testPushCupType_hierarchical_seAgrega() {
        tower.pushCupType("hierarchical", 2);
        assertEquals(3, tower.height());
    }

    @Test
    public void testPushCupType_bouncer_sinDuplicadoAltura_seAgrega() {
        tower.pushCupType("bouncer", 1);
        assertEquals(1, tower.height());
    }

    @Test
    public void testPushCupType_tipoDesconocido_noAgrega() {
        tower.pushCupType("fantasma", 1);
        assertEquals(0, tower.height());
    }

    // =========================================================
    // TOWER — OpenerCup elimina tapas
    // =========================================================

    @Test
    public void testOpenerCup_alEntrar_eliminaTodasLasTapas() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushCup(3);
        tower.pushLid(3);
        assertEquals(2, tower.lidedCups().length);
        tower.pushCupType("opener", 2);
        assertEquals(0, tower.lidedCups().length);
    }

    @Test
    public void testOpenerCup_sinTapasPrevias_entraCorrectamente() {
        tower.pushCup(1);
        tower.pushCupType("opener", 2);
        assertEquals(4, tower.height());
    }

    // =========================================================
    // TOWER — HierarchicalCup
    // =========================================================

    @Test
    public void testHierarchicalCup_alFondo_noPuedeSalir() {
        tower.pushCupType("hierarchical", 2);
        tower.popCup();
        assertTrue("HierarchicalCup en fondo no debe salir", cupEstaEnTorre(tower, 2));
    }

    @Test
    public void testHierarchicalCup_desplazaMenores_quedaDebajo() {
        tower.pushCup(1);
        tower.pushCupType("hierarchical", 3);
        String[][] items = tower.stackingItems();
        int idxH = indexOf(items, "cup", "3");
        int idx1 = indexOf(items, "cup", "1");
        assertTrue("Hierarchical debe quedar antes (fondo) que la copa menor", idxH < idx1);
    }

    // =========================================================
    // TOWER — pushLidType / popLid / removeLid
    // =========================================================

    @Test
    public void testPushLid_normal_copaTieneTapa() {
        tower.pushCup(1);
        tower.pushLid(1);
        assertEquals(1, tower.lidedCups().length);
    }

    @Test
    public void testPushLid_duplicada_noAgrega() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushLid(1);
        assertEquals(1, tower.lidedCups().length);
    }

    @Test
    public void testPushLidType_fearful_sinCopa_noEntra() {
        tower.pushLidType("fearful", 1);
        assertEquals(0, tower.lidedCups().length);
    }

    @Test
    public void testPushLidType_fearful_conCopa_entra() {
        tower.pushCup(1);
        tower.pushLidType("fearful", 1);
        assertEquals(1, tower.lidedCups().length);
    }

    @Test
    public void testPushLidType_crazy_vaALaBase() {
        tower.pushCup(1);
        tower.pushLidType("crazy", 2);
        String[][] items = tower.stackingItems();
        assertEquals("lid", items[0][0]);
    }

    @Test
    public void testPushLidType_glued_entraCorrectamente() {
        tower.pushCup(1);
        tower.pushLidType("glued", 1);
        assertEquals(1, tower.lidedCups().length);
    }

    @Test
    public void testPopLid_normal_eliminaTapa() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.popLid();
        assertEquals(0, tower.lidedCups().length);
    }

    @Test
    public void testPopLid_fearfulSobreCopa_noSale() {
        tower.pushCup(1);
        tower.pushLidType("fearful", 1);
        tower.popLid();
        assertEquals(1, tower.lidedCups().length);
    }

    @Test
    public void testPopLid_gluedSobreCopa_noSale() {
        tower.pushCup(1);
        tower.pushLidType("glued", 1);
        tower.popLid();
        assertEquals(1, tower.lidedCups().length);
    }

    @Test
    public void testRemoveLid_porNumero_eliminaCorrectamente() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushLid(2);
        tower.removeLid(1);
        int[] lided = tower.lidedCups();
        assertEquals(1, lided.length);
        assertEquals(2, lided[0]);
    }

    @Test
    public void testRemoveLid_gluedSobreCopa_noSale() {
        tower.pushCup(1);
        tower.pushLidType("glued", 1);
        tower.removeLid(1);
        assertEquals(1, tower.lidedCups().length);
    }

    // =========================================================
    // TOWER — consultas: height, ok, lidedCups, stackingItems
    // =========================================================

    @Test
    public void testHeight_torreVacia_cero() {
        assertEquals(0, tower.height());
    }

    @Test
    public void testOk_torreVacia_true() {
        assertTrue(tower.ok());
    }

    @Test
    public void testOk_dentroDelLimite_true() {
        tower.pushCup(1);
        assertTrue(tower.ok());
    }

    @Test
    public void testLidedCups_sinTapas_arregloVacio() {
        tower.pushCup(1);
        assertEquals(0, tower.lidedCups().length);
    }

    @Test
    public void testLidedCups_dosTapas_ordenadosAscendente() {
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushLid(2);
        int[] lided = tower.lidedCups();
        assertEquals(2, lided.length);
        assertEquals(1, lided[0]);
        assertEquals(2, lided[1]);
    }

    @Test
    public void testStackingItems_conteo_correcto() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushCup(2);
        assertEquals(3, tower.stackingItems().length);
    }

    @Test
    public void testStackingItems_contieneCopaYTapa() {
        tower.pushCup(1);
        tower.pushLid(1);
        String[][] items = tower.stackingItems();
        boolean hasCup = false, hasLid = false;
        for (String[] item : items) {
            if ("cup".equals(item[0]) && "1".equals(item[1])) hasCup = true;
            if ("lid".equals(item[0]) && "1".equals(item[1])) hasLid = true;
        }
        assertTrue(hasCup);
        assertTrue(hasLid);
    }

    // =========================================================
    // TOWER — orderTower / reverseTower
    // =========================================================

    @Test
    public void testOrderTower_tresCopas_mayorAlFondo() {
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.orderTower();
        assertEquals("3", tower.stackingItems()[0][1]);
    }

    @Test
    public void testOrderTower_conservaTodasLasCopas() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.orderTower();
        assertEquals(3, contarCopas(tower));
    }

    @Test
    public void testReverseTower_dosCopas_invierteOrden() {
        tower.pushCup(1);
        tower.pushCup(2);
        String[][] before = tower.stackingItems();
        tower.reverseTower();
        String[][] after = tower.stackingItems();
        assertEquals(before[0][1], after[after.length - 1][1]);
    }

    // =========================================================
    // TOWER — swap / swapToReduce
    // =========================================================

    @Test
    public void testSwap_dosCopasValidas_intercambia() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.swap(new String[]{"cup", "1"}, new String[]{"cup", "2"});
        String[][] items = tower.stackingItems();
        assertEquals("2", items[0][1]);
        assertEquals("1", items[1][1]);
    }

    @Test
    public void testSwap_descriptorNulo_noLanzaExcepcion() {
        tower.pushCup(1);
        tower.swap(null, new String[]{"cup", "1"});
    }

    @Test
    public void testSwap_copaInexistente_noModificaAltura() {
        tower.pushCup(1);
        int antes = tower.height();
        tower.swap(new String[]{"cup", "1"}, new String[]{"cup", "99"});
        assertEquals(antes, tower.height());
    }

    @Test
    public void testSwapToReduce_unaCopaOrMenos_retornaNone() {
        tower.pushCup(1);
        assertEquals("none", tower.swapToReduce()[0][0]);
    }

    @Test
    public void testSwapToReduce_dosCopas_retornaResultadoNoNulo() {
        tower.pushCup(1);
        tower.pushCup(2);
        String[][] result = tower.swapToReduce();
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    // =========================================================
    // TOWER — cover
    // =========================================================

    @Test
    public void testCover_dosCopasSinTapa_ambasCubiertas() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.cover();
        assertEquals(2, tower.lidedCups().length);
    }

    @Test
    public void testCover_copaYaTieneTapa_noDuplica() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.cover();
        assertEquals(2, tower.lidedCups().length);
    }

    // =========================================================
    // CUP — getters y validaciones
    // =========================================================

    @Test
    public void testCup_getHeight_formula2nMenos1() {
        assertEquals(1, new NormalCup(1).getHeight());
        assertEquals(3, new NormalCup(2).getHeight());
        assertEquals(5, new NormalCup(3).getHeight());
    }

    @Test
    public void testCup_getNumber_devuelveNumero() {
        assertEquals(4, new NormalCup(4).getNumber());
    }

    @Test
    public void testCup_sinTapa_hasLidFalse() {
        assertFalse(new NormalCup(1).hasLid());
    }

    @Test
    public void testCup_conTapaAsignada_hasLidTrue() {
        NormalCup c = new NormalCup(1);
        c.setLid(new NormalLid(1));
        assertTrue(c.hasLid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCup_numeroCero_lanzaExcepcion() {
        new NormalCup(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCup_numeroNegativo_lanzaExcepcion() {
        new NormalCup(-1);
    }

    // =========================================================
    // CUP — getType por subtipo
    // =========================================================

    @Test public void testNormalCup_getType()      { assertEquals("normal",       new NormalCup(1).getType()); }
    @Test public void testOpenerCup_getType()       { assertEquals("opener",       new OpenerCup(1).getType()); }
    @Test public void testHierarchicalCup_getType() { assertEquals("hierarchical", new HierarchicalCup(1).getType()); }
    @Test public void testBouncerCup_getType()      { assertEquals("bouncer",      new BouncerCup(1).getType()); }

    // =========================================================
    // CUP — canExit por subtipo
    // =========================================================

    @Test public void testNormalCup_canExit_siempre()  { assertTrue(new NormalCup(1).canExit(null)); }
    @Test public void testOpenerCup_canExit_siempre()  { assertTrue(new OpenerCup(1).canExit(null)); }
    @Test public void testBouncerCup_canExit_siempre() { assertTrue(new BouncerCup(1).canExit(null)); }

    @Test
    public void testHierarchicalCup_canExit_falseAlFondo() {
        HierarchicalCup h = new HierarchicalCup(2);
        h.setAtBottom(true);
        assertFalse(h.canExit(null));
    }

    @Test
    public void testHierarchicalCup_canExit_trueNoAlFondo() {
        HierarchicalCup h = new HierarchicalCup(2);
        h.setAtBottom(false);
        assertTrue(h.canExit(null));
    }

    @Test
    public void testHierarchicalCup_setAtBottom_actualizaEstado() {
        HierarchicalCup h = new HierarchicalCup(1);
        assertFalse(h.isAtBottom());
        h.setAtBottom(true);
        assertTrue(h.isAtBottom());
    }

    // =========================================================
    // LID — getters
    // =========================================================

    @Test
    public void testLid_getNumber_devuelveNumero() {
        assertEquals(2, new NormalLid(2).getNumber());
    }

    @Test
    public void testLid_sinCopa_isOnCupFalse() {
        assertFalse(new NormalLid(1).isOnCup());
    }

    @Test
    public void testLid_attachTo_isOnCupTrue() {
        NormalLid l = new NormalLid(1);
        l.attachTo(new NormalCup(1));
        assertTrue(l.isOnCup());
    }

    @Test
    public void testLid_getHeight_siempreUno() {
        assertEquals(1, new NormalLid(3).getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLid_numeroCero_lanzaExcepcion() {
        new NormalLid(0);
    }

    // =========================================================
    // LID — getType por subtipo
    // =========================================================

    @Test public void testNormalLid_getType()  { assertEquals("normal",  new NormalLid(1).getType()); }
    @Test public void testFearfulLid_getType() { assertEquals("fearful", new FearfulLid(1).getType()); }
    @Test public void testCrazyLid_getType()   { assertEquals("crazy",   new CrazyLid(1).getType()); }
    @Test public void testGluedLid_getType()   { assertEquals("glued",   new GluedLid(1).getType()); }

    // =========================================================
    // LID — canEnter / canExit por subtipo
    // =========================================================

    @Test public void testNormalLid_canEnter_siempre() { assertTrue(new NormalLid(1).canEnter(null)); }
    @Test public void testNormalLid_canExit_siempre()  { assertTrue(new NormalLid(1).canExit(null)); }
    @Test public void testCrazyLid_canEnter_siempre()  { assertTrue(new CrazyLid(1).canEnter(null)); }
    @Test public void testCrazyLid_canExit_siempre()   { assertTrue(new CrazyLid(1).canExit(null)); }
    @Test public void testGluedLid_canEnter_siempre()  { assertTrue(new GluedLid(1).canEnter(null)); }

    @Test
    public void testFearfulLid_canExit_falseEnCopa() {
        FearfulLid fl = new FearfulLid(1);
        fl.attachTo(new NormalCup(1));
        assertFalse(fl.canExit(null));
    }

    @Test
    public void testFearfulLid_canExit_trueSinCopa() {
        assertTrue(new FearfulLid(1).canExit(null));
    }

    @Test
    public void testGluedLid_canExit_falseEnCopa() {
        GluedLid gl = new GluedLid(1);
        gl.attachTo(new NormalCup(1));
        assertFalse(gl.canExit(null));
    }

    @Test
    public void testGluedLid_canExit_trueSuelta() {
        assertTrue(new GluedLid(1).canExit(null));
    }

    // =========================================================
    // TOWERCONTEST — solve (sin simulate: no abre ventana)
    // =========================================================

    @Test
    public void testSolve_nCero_impossible() {
        assertEquals("impossible", TowerContest.solve(0, 5));
    }

    @Test
    public void testSolve_hCero_impossible() {
        assertEquals("impossible", TowerContest.solve(5, 0));
    }

    @Test
    public void testSolve_h1_retornaCopa1() {
        assertEquals("1", TowerContest.solve(1, 1));
    }

    @Test
    public void testSolve_h3_retornaCopa2() {
        assertEquals("2", TowerContest.solve(2, 3));
    }

    @Test
    public void testSolve_imposible_retornaImpossible() {
        assertEquals("impossible", TowerContest.solve(1, 2));
    }

    @Test
    public void testSolve_h9_sumaCorrecta() {
        String result = TowerContest.solve(3, 9);
        assertFalse("Se esperaba solución", result.equals("impossible"));
        assertEquals(9, sumarAlturasEnSolucion(result));
    }

    @Test
    public void testSolve_sinCopasDuplicadas_enSolucion() {
        String result = TowerContest.solve(5, 9);
        if (!result.equals("impossible")) {
            java.util.Set<String> seen = new java.util.HashSet<>();
            for (String p : result.split(" ")) {
                assertTrue("Copa duplicada en solución: " + p, seen.add(p));
            }
        }
    }

    @Test
    public void testSolve_todasLasCopasUsadas_alturaMaxima() {
        String result = TowerContest.solve(3, 9);
        assertNotNull(result);
        assertFalse(result.isEmpty());
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

    private int contarCopas(Tower t) {
        int c = 0;
        for (String[] item : t.stackingItems())
            if ("cup".equals(item[0])) c++;
        return c;
    }

    private int sumarAlturasEnSolucion(String result) {
        int total = 0;
        for (String p : result.split(" "))
            total += 2 * Integer.parseInt(p.trim()) - 1;
        return total;
    }
}
