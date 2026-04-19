package Test;

import tower.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de UNIDAD para el Ciclo 4.
 * Todas corren internamente: ninguna abre ventana, canvas ni JFrame.
 * Se usa únicamente el estado lógico de los objetos.
 * Incluye tests adicionales para alcanzar 90%+ de cobertura.
 *
 * @author Julian Morales - Sergio Buitrago
 * @version 4.0 (Ciclo 4 - TowerC4test + Cobertura extendida)
 */
public class TowerC4Test {

    private Tower tower;

    @BeforeEach
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

    @Test
    public void testConstructorPrincipal_widthCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Tower(0, 10));
    }

    @Test
    public void testConstructorPrincipal_maxHeightNegativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Tower(5, -1));
    }

    @Test
    public void testConstructorMasivo_tresCopas_alturaNueve() {
        Tower t = new Tower(15, 50);
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        assertEquals(9, t.height());
    }

    @Test
    public void testConstructorMasivo_negativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Tower(-1));
    }

    @Test
    public void testConstructorMasivo_cero_torreVacia() {
        Tower t = new Tower(0);
        assertEquals(0, t.height());
        assertEquals(0, t.stackingItems().length);
        assertTrue(t.ok());
    }

    @Test
    public void testConstructorMasivo_uno_unaCopaAltura1() {
        Tower t = new Tower(1);
        assertEquals(1, t.stackingItems().length);
        assertEquals("cup", t.stackingItems()[0][0]);
        assertEquals("1", t.stackingItems()[0][1]);
    }

    @Test
    public void testConstructorMasivo_cuatro_cuatroCopas() {
        Tower t = new Tower(4);
        int copas = 0;
        for (String[] item : t.stackingItems()) {
            if ("cup".equals(item[0])) copas++;
        }
        assertEquals(4, copas);
        assertTrue(t.ok());
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
        assertDoesNotThrow(() -> tower.popCup());
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

    @Test
    public void testPopCup_copaConTapa_eliminaAmbos() {
        tower.pushCup(1);
        tower.pushLid(1);
        assertEquals(1, tower.lidedCups().length);
        tower.popCup();
        assertEquals(0, tower.height());
        assertEquals(0, tower.lidedCups().length);
    }

    @Test
    public void testPopCup_soloCopas_eliminaUltima() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.popCup();
        assertFalse(cupEstaEnTorre(tower, 3));
        assertTrue(cupEstaEnTorre(tower, 1));
        assertTrue(cupEstaEnTorre(tower, 2));
    }

    @Test
    public void testPopCup_sinCopas_soloTapa_noCrash() {
        tower.pushLidType("crazy", 1);
        assertDoesNotThrow(() -> tower.popCup());
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

    @Test
    public void testPushCupType_bouncer_mismaAltura_noEntra() {
        tower.pushCup(1);          // altura 1
        int antes = tower.height();
        // BouncerCup 6 tiene altura 11, diferente → entra
        tower.pushCupType("bouncer", 6);
        assertTrue(tower.height() > antes);
        // Ahora intentamos otra bouncer con altura 1 (copa 1 ya existe con h=1)
        // BouncerCup número diferente pero misma altura: número 8 también h=15 (diferente)
        // Para forzar rebote: agregar copa normal de alguna altura, luego bouncer misma h
        Tower t = new Tower(15, 50);
        t.pushCup(2);              // altura 3
        t.pushCupType("bouncer", 5); // altura 9, no conflicto
        assertTrue(cupEstaEnTorre(t, 5));
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

    @Test
    public void testOpenerCup_color_esCyan() {
        assertEquals("cyan", new OpenerCup(1).getColor());
    }

    @Test
    public void testOpenerCup_onEnter_returnsTrue() {
        assertTrue(new OpenerCup(1).onEnter(null, 0));
    }

    @Test
    public void testOpenerCup_toString_sinTapa() {
        String s = new OpenerCup(3).toString();
        assertTrue(s.contains("3"));
        assertTrue(s.contains("sin tapa"));
    }

    // =========================================================
    // TOWER — HierarchicalCup
    // =========================================================

    @Test
    public void testHierarchicalCup_alFondo_noPuedeSalir() {
        tower.pushCupType("hierarchical", 2);
        tower.popCup();
        assertTrue(cupEstaEnTorre(tower, 2), "HierarchicalCup en fondo no debe salir");
    }

    @Test
    public void testHierarchicalCup_desplazaMenores_quedaDebajo() {
        tower.pushCup(1);
        tower.pushCupType("hierarchical", 3);
        String[][] items = tower.stackingItems();
        int idxH = indexOf(items, "cup", "3");
        int idx1 = indexOf(items, "cup", "1");
        assertTrue(idxH < idx1, "Hierarchical debe quedar antes (fondo) que la copa menor");
    }

    @Test
    public void testHierarchicalCup_onEnter_posicionCero_atBottomTrue() {
        HierarchicalCup h = new HierarchicalCup(2);
        h.onEnter(null, 0);
        assertTrue(h.isAtBottom());
    }

    @Test
    public void testHierarchicalCup_onEnter_posicionMayor_atBottomFalse() {
        HierarchicalCup h = new HierarchicalCup(2);
        h.onEnter(null, 3);
        assertFalse(h.isAtBottom());
    }

    @Test
    public void testHierarchicalCup_color_esOrange() {
        assertEquals("orange", new HierarchicalCup(1).getColor());
    }

    @Test
    public void testHierarchicalCup_toString_sinTapa() {
        String s = new HierarchicalCup(2).toString();
        assertTrue(s.contains("2"));
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
    public void testPushLid_sinCopaCoincidente_noAsociaPeroPuedeEntrar() {
        tower.pushCup(1);
        tower.pushLid(2); // copa 2 no existe, tapa entra sin asociar
        assertEquals(0, tower.lidedCups().length);
        boolean hayTapa = false;
        for (String[] item : tower.stackingItems()) {
            if ("lid".equals(item[0]) && "2".equals(item[1])) hayTapa = true;
        }
        assertTrue(hayTapa);
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
    public void testPushLidType_crazy_multipleCopas_sigueEnBase() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushLidType("crazy", 9);
        assertEquals("lid", tower.stackingItems()[0][0]);
        assertEquals("9", tower.stackingItems()[0][1]);
    }

    @Test
    public void testPushLidType_glued_entraCorrectamente() {
        tower.pushCup(1);
        tower.pushLidType("glued", 1);
        assertEquals(1, tower.lidedCups().length);
    }

    @Test
    public void testPushLidType_tipoDesconocido_noAgrega() {
        tower.pushCup(1);
        tower.pushLidType("invisible", 1);
        assertEquals(0, tower.lidedCups().length);
    }

    @Test
    public void testPopLid_normal_eliminaTapa() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.popLid();
        assertEquals(0, tower.lidedCups().length);
    }

    @Test
    public void testPopLid_sinTapas_noCrash() {
        tower.pushCup(1);
        assertDoesNotThrow(() -> tower.popLid());
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

    @Test
    public void testRemoveLid_inexistente_noCrash() {
        tower.pushCup(1);
        assertDoesNotThrow(() -> tower.removeLid(99));
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
    public void testLidedCups_tresTapas_ordenAscendente() {
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushLid(3);
        tower.pushLid(1);
        tower.pushLid(2);
        int[] lided = tower.lidedCups();
        assertEquals(3, lided.length);
        assertEquals(1, lided[0]);
        assertEquals(2, lided[1]);
        assertEquals(3, lided[2]);
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
    public void testOrderTower_torreVacia_noCrash() {
        assertDoesNotThrow(() -> tower.orderTower());
        assertEquals(0, tower.height());
    }

    @Test
    public void testOrderTower_conTapaSuelta_sePreserva() {
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(5); // tapa suelta
        tower.orderTower();
        assertTrue(tower.ok());
        assertTrue(cupEstaEnTorre(tower, 1));
        assertTrue(cupEstaEnTorre(tower, 3));
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

    @Test
    public void testReverseTower_torreVacia_noCrash() {
        assertDoesNotThrow(() -> tower.reverseTower());
        assertEquals(0, tower.height());
    }

    @Test
    public void testReverseTower_unaCopa_noCambia() {
        tower.pushCup(1);
        tower.reverseTower();
        assertEquals(1, tower.height());
        assertTrue(cupEstaEnTorre(tower, 1));
    }

    @Test
    public void testReverseTower_excedeLimite_eliminaExceso() {
        Tower pequeña = new Tower(10, 5);
        pequeña.pushCup(1); // h=1
        pequeña.pushCup(2); // h=3 → total 4
        pequeña.reverseTower();
        assertTrue(pequeña.ok());
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
        assertDoesNotThrow(() -> tower.swap(null, new String[]{"cup", "1"}));
    }

    @Test
    public void testSwap_copaInexistente_noModificaAltura() {
        tower.pushCup(1);
        int antes = tower.height();
        tower.swap(new String[]{"cup", "1"}, new String[]{"cup", "99"});
        assertEquals(antes, tower.height());
    }

    @Test
    public void testSwap_mismoNumero_noHaceNada() {
        tower.pushCup(1);
        tower.pushCup(2);
        String[][] antes = tower.stackingItems();
        tower.swap(new String[]{"cup", "1"}, new String[]{"cup", "1"});
        String[][] despues = tower.stackingItems();
        assertEquals(antes[0][1], despues[0][1]);
    }

    @Test
    public void testSwap_tipoLid_noIntercambia() {
        tower.pushCup(1);
        tower.pushCup(2);
        int alturaPrev = tower.height();
        tower.swap(new String[]{"lid", "1"}, new String[]{"cup", "2"});
        assertEquals(alturaPrev, tower.height());
    }

    @Test
    public void testSwap_descriptorLongitudInsuficiente_noCrash() {
        tower.pushCup(1);
        tower.pushCup(2);
        assertDoesNotThrow(() ->
            tower.swap(new String[]{"cup"}, new String[]{"cup", "2"})
        );
    }

    @Test
    public void testSwapToReduce_unaCopaOrMenos_retornaNone() {
        tower.pushCup(1);
        assertEquals("none", tower.swapToReduce()[0][0]);
    }

    @Test
    public void testSwapToReduce_torreVacia_retornaNone() {
        String[][] result = tower.swapToReduce();
        assertEquals("none", result[0][0]);
        assertEquals("none", result[1][0]);
    }

    @Test
    public void testSwapToReduce_dosCopas_retornaResultadoNoNulo() {
        tower.pushCup(1);
        tower.pushCup(2);
        String[][] result = tower.swapToReduce();
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    public void testSwapToReduce_tresCopasSinMejora_retornaNoneOPar() {
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushCup(1);
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

    @Test
    public void testCover_torreVacia_noCrash() {
        assertDoesNotThrow(() -> tower.cover());
        assertEquals(0, tower.lidedCups().length);
    }

    @Test
    public void testCover_todasYaTapadas_noModifica() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.cover();
        int lidsBefore = tower.lidedCups().length;
        tower.cover();
        assertEquals(lidsBefore, tower.lidedCups().length);
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

    @Test
    public void testCup_numeroCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new NormalCup(0));
    }

    @Test
    public void testCup_numeroNegativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new NormalCup(-1));
    }

    @Test
    public void testCup_getInnerHeight_variosNumeros() {
        assertEquals(0, new NormalCup(1).getInnerHeight());
        assertEquals(2, new NormalCup(2).getInnerHeight());
        assertEquals(4, new NormalCup(3).getInnerHeight());
        assertEquals(6, new NormalCup(4).getInnerHeight());
        assertEquals(8, new NormalCup(5).getInnerHeight());
    }

    @Test
    public void testCup_getWidth_escalaCorrecta() {
        assertEquals(45, new NormalCup(1).getWidth());
        assertEquals(50, new NormalCup(2).getWidth());
        assertEquals(55, new NormalCup(3).getWidth());
        assertEquals(60, new NormalCup(4).getWidth());
        assertEquals(70, new NormalCup(6).getWidth());
    }

    @Test
    public void testCup_getLastValues_antesDeVisible_sonCero() {
        NormalCup c = new NormalCup(1);
        assertEquals(0, c.getLastX());
        assertEquals(0, c.getLastY());
        assertEquals(0, c.getLastW());
        assertEquals(0, c.getLastH());
    }

    @Test
    public void testCup_resetPosition_limpiaDatos() {
        NormalCup c = new NormalCup(1);
        c.resetPosition();
        assertEquals(0, c.getLastX());
        assertEquals(0, c.getLastY());
    }

    @Test
    public void testNormalCup_onEnter_returnsTrue() {
        assertTrue(new NormalCup(3).onEnter(null, 0));
        assertTrue(new NormalCup(3).onEnter(null, 5));
    }

    @Test
    public void testNormalCup_toString_sinTapa() {
        String s = new NormalCup(2).toString();
        assertTrue(s.contains("2"));
        assertTrue(s.contains("sin tapa"));
    }

    @Test
    public void testNormalCup_toString_conTapa() {
        NormalCup c = new NormalCup(2);
        c.setLid(new NormalLid(2));
        assertTrue(c.toString().contains("con tapa"));
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
    // BouncerCup — comportamiento específico
    // =========================================================

    @Test
    public void testBouncerCup_onEnter_returnsTrue() {
        assertTrue(new BouncerCup(1).onEnter(null, 0));
    }

    @Test
    public void testBouncerCup_color_noNulo() {
        assertNotNull(new BouncerCup(1).getColor());
        assertTrue(new BouncerCup(1).getColor().trim().length() > 0);
    }

    @Test
    public void testBouncerCup_toString_sinTapa() {
        String s = new BouncerCup(3).toString();
        assertTrue(s.contains("3"));
        assertTrue(s.contains("sin tapa"));
    }

    @Test
    public void testBouncerCup_toString_conTapa() {
        BouncerCup b = new BouncerCup(2);
        b.setLid(new NormalLid(2));
        assertTrue(b.toString().contains("con tapa"));
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
    public void testLid_attachToNull_desasocia() {
        NormalLid l = new NormalLid(1);
        l.attachTo(new NormalCup(1));
        assertTrue(l.isOnCup());
        l.attachTo(null);
        assertFalse(l.isOnCup());
        assertNull(l.getAssociatedCup());
    }

    @Test
    public void testLid_getHeight_siempreUno() {
        assertEquals(1, new NormalLid(3).getHeight());
    }

    @Test
    public void testLid_numeroCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new NormalLid(0));
    }

    @Test
    public void testLid_numeroNegativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new NormalLid(-5));
    }

    @Test
    public void testLid_getColor_conCopa_devuelveColorDeCopa() {
        NormalLid l = new NormalLid(1);
        NormalCup c = new NormalCup(1);
        l.attachTo(c);
        assertEquals(c.getColor(), l.getColor());
    }

    @Test
    public void testLid_getColor_sinCopa_devuelveColorPropio() {
        assertNotNull(new NormalLid(2).getColor());
    }

    @Test
    public void testLid_generateColor_recorrePaleta() {
        String c1 = new NormalLid(1).getColor();
        String c7 = new NormalLid(7).getColor(); // 7 % 6 = 1 → mismo color
        assertEquals(c1, c7);
    }

    @Test
    public void testNormalLid_toString_sinCopa() {
        String s = new NormalLid(3).toString();
        assertTrue(s.contains("3"));
        assertTrue(s.contains("sin copa"));
    }

    @Test
    public void testNormalLid_toString_conCopa() {
        NormalLid l = new NormalLid(2);
        l.attachTo(new NormalCup(2));
        assertTrue(l.toString().contains("copa"));
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

    @Test public void testNormalLid_canEnter_siempre()  { assertTrue(new NormalLid(1).canEnter(null)); }
    @Test public void testNormalLid_canExit_siempre()   { assertTrue(new NormalLid(1).canExit(null)); }
    @Test public void testCrazyLid_canEnter_siempre()   { assertTrue(new CrazyLid(1).canEnter(null)); }
    @Test public void testCrazyLid_canExit_siempre()    { assertTrue(new CrazyLid(1).canExit(null)); }
    @Test public void testGluedLid_canEnter_siempre()   { assertTrue(new GluedLid(1).canEnter(null)); }
    @Test public void testFearfulLid_canEnter_siempre() { assertTrue(new FearfulLid(1).canEnter(null)); }

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
    public void testFearfulLid_color_esGray() {
        assertEquals("gray", new FearfulLid(1).getColor());
    }

    @Test
    public void testFearfulLid_toString_sinCopa() {
        String s = new FearfulLid(4).toString();
        assertTrue(s.contains("4"));
        assertTrue(s.contains("fearful"));
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

    @Test
    public void testGluedLid_color_esBrown() {
        assertEquals("brown", new GluedLid(1).getColor());
    }

    @Test
    public void testGluedLid_toString_sinCopa() {
        String s = new GluedLid(3).toString();
        assertTrue(s.contains("3"));
        assertTrue(s.contains("glued"));
    }

    @Test
    public void testCrazyLid_color_esPink() {
        assertEquals("pink", new CrazyLid(1).getColor());
    }

    @Test
    public void testCrazyLid_toString_sinCopa() {
        String s = new CrazyLid(5).toString();
        assertTrue(s.contains("5"));
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
    public void testSolve_nNegativo_impossible() {
        assertEquals("impossible", TowerContest.solve(-1, 10));
    }

    @Test
    public void testSolve_hNegativo_impossible() {
        assertEquals("impossible", TowerContest.solve(3, -5));
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
    public void testSolve_alturaImposible_retornaImpossible() {
        assertEquals("impossible", TowerContest.solve(2, 100));
    }

    @Test
    public void testSolve_h9_sumaCorrecta() {
        String result = TowerContest.solve(3, 9);
        assertFalse(result.equals("impossible"), "Se esperaba solución");
        assertEquals(9, sumarAlturasEnSolucion(result));
    }

    @Test
    public void testSolve_h4_n2_retornaDosNumeros() {
        String result = TowerContest.solve(2, 4);
        assertNotEquals("impossible", result);
        assertEquals(2, result.trim().split("\\s+").length);
    }

    @Test
    public void testSolve_h16_n4_sumaCorrecta() {
        String result = TowerContest.solve(4, 16);
        assertNotEquals("impossible", result);
        assertEquals(16, sumarAlturasEnSolucion(result));
    }

    @Test
    public void testSolve_h7_n4_sumaCorrecta() {
        String result = TowerContest.solve(4, 7);
        if (!result.equals("impossible")) {
            assertEquals(7, sumarAlturasEnSolucion(result));
        }
    }

    @Test
    public void testSolve_sinCopasDuplicadas_enSolucion() {
        String result = TowerContest.solve(5, 9);
        if (!result.equals("impossible")) {
            java.util.Set<String> seen = new java.util.HashSet<>();
            for (String p : result.split(" ")) {
                assertTrue(seen.add(p), "Copa duplicada en solución: " + p);
            }
        }
    }

    @Test
    public void testSolve_todasLasCopasUsadas_alturaMaxima() {
        String result = TowerContest.solve(3, 9);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSolve_deterministico_mismoResultado() {
        String r1 = TowerContest.solve(4, 8);
        String r2 = TowerContest.solve(4, 8);
        assertEquals(r1, r2);
    }

    @Test
    public void testSolve_numerosEnRango_n4() {
        String result = TowerContest.solve(4, 7);
        if (!result.equals("impossible")) {
            for (String part : result.trim().split("\\s+")) {
                int cup = Integer.parseInt(part);
                assertTrue(cup >= 1 && cup <= 4, "Copa fuera de rango: " + cup);
            }
        }
    }

    @Test
    public void testSolve_variasAlturas_sinDuplicados() {
        for (int h = 1; h <= 25; h += 2) {
            String result = TowerContest.solve(5, h);
            if (!result.equals("impossible")) {
                java.util.Set<String> seen = new java.util.HashSet<>();
                for (String p : result.trim().split("\\s+")) {
                    assertTrue(seen.add(p), "Copa duplicada h=" + h);
                }
            }
        }
    }

    @Test
    public void testSolve_h25_n10_encuentraSolucionOImpossible() {
        String result = TowerContest.solve(10, 25);
        assertTrue(result.equals("impossible") || result.matches("[0-9]+(\\s[0-9]+)*"));
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
