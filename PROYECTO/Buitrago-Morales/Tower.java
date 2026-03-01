import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

/**
 * Clase principal con el objetivo de controlar el funcionamiento.
 * @author Julian Morales - Sergio Buitrago
 */
public class Tower {
    private int width;
    private int maxHeight;
    private ArrayList<Object> items;
    private boolean isVisible;
    private static final int PIXELS_PER_CM = 10;
    private static final int CANVAS_WIDTH = 300;
    private static final int CANVAS_HEIGHT = 300;
    private static final int MARGIN = 25;
    private static final int WALL_CM = 1;
    
    private Rectangle base;
    private ArrayList<Rectangle> heightMarkers;
    private int scale;
    
    public Tower(int width, int maxHeight) {
        if (width <= 0 || maxHeight <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.maxHeight = maxHeight;
        this.items = new ArrayList<>();
        this.isVisible = false;
        this.heightMarkers = new ArrayList<>();
        
        this.scale = Math.min(
            (CANVAS_WIDTH - 2 * MARGIN) / Math.max(1, width),
            (CANVAS_HEIGHT - 2 * MARGIN) / Math.max(1, maxHeight)
        );
        if (this.scale < 2) this.scale = 2;
        
        initializeBase();
    }
    
    /**
     * Se crea una torre a partir de n cantidad de copas y con esta cantidad de copas se calcula la altura maxima y el ancho minimo para el funcionamiento del juego.
     * @param n es la cantidad de copas que se desean.
     */
    
    public Tower(int numberOfCups) {
        if (numberOfCups < 0) throw new IllegalArgumentException("numberOfCups must be >= 0");

        this.width     = (numberOfCups == 0) ? 0 : (2 * numberOfCups - 1);
        this.maxHeight = numberOfCups * numberOfCups;

        this.items = new ArrayList<>();
        this.isVisible = false;
        this.heightMarkers = new ArrayList<>();

        this.scale = Math.min(
            (CANVAS_WIDTH - 2 * MARGIN) / Math.max(1, this.width),
            (CANVAS_HEIGHT - 2 * MARGIN) / Math.max(1, this.maxHeight)
        );
        if (this.scale < 2) this.scale = 2;
        initializeBase();

        for (int i = 1; i <= numberOfCups; i++) items.add(new Cup(i));

        makeVisible();
    }

    
    private void initializeBase() {
        base = new Rectangle();
        base.changeSize(5, width * scale);
        base.changeColor("black");
    }
    
    public void pushCup(int i) {
        if (cupExists(i)) {
            showError("Cup #" + i + " already exists");
            return;
        }
        Cup cup = new Cup(i);
        java.util.ArrayList<Object> sim = new java.util.ArrayList<>(items);
        sim.add(cup);
        int eff = calculateEffectiveHeightCm(sim);
        if (eff <= maxHeight) {
            items.add(cup);
            if (isVisible) redraw();
        } else {
            showError("Cannot add cup #" + i + ": exceeds max height when stacked");
        }
    }
    
    public void popCup() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Cup) {
                Cup cup = (Cup) items.get(i);
                cup.makeInvisible();
                if (cup.hasLid()) items.remove(cup.getLid());
                items.remove(cup);
                if (isVisible) redraw();
                return;
            }
        }
        showError("No cups to remove");
    }
    
    public void removeCup(int i) {
        for (int j = 0; j < items.size(); j++) {
            if (items.get(j) instanceof Cup) {
                Cup cup = (Cup) items.get(j);
                if (cup.getNumber() == i) {
                    cup.makeInvisible();
                    if (cup.hasLid()) items.remove(cup.getLid());
                    items.remove(cup);
                    if (isVisible) redraw();
                    return;
                }
            }
        }
        showError("Cup #" + i + " not found");
    }
    
    public void pushLid(int i) {
        if (lidExists(i)) {
            showError("Lid #" + i + " already exists");
            return;
        }
        Lid lid = new Lid(i);
        Cup matchingCup = findCup(i);
        if (matchingCup != null && !matchingCup.hasLid()) {
            matchingCup.setLid(lid);
            lid.attachTo(matchingCup);
        }
    
        java.util.ArrayList<Object> sim = new java.util.ArrayList<>(items);
        sim.add(lid);
        int eff = calculateEffectiveHeightCm(sim);
    
        if (eff <= maxHeight) {
            items.add(lid);
            if (isVisible) redraw();
        } else {
            showError("Cannot add lid #" + i + ": exceeds max height when stacked");
        }
    }
    
    public void popLid() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Lid) {
                Lid lid = (Lid) items.get(i);
                lid.makeInvisible();
                if (lid.isOnCup()) lid.getAssociatedCup().setLid(null);
                items.remove(lid);
                if (isVisible) redraw();
                return;
            }
        }
        showError("No lids to remove");
    }
    
    public void removeLid(int i) {
        for (int j = 0; j < items.size(); j++) {
            if (items.get(j) instanceof Lid) {
                Lid lid = (Lid) items.get(j);
                if (lid.getNumber() == i) {
                    lid.makeInvisible();
                    if (lid.isOnCup()) lid.getAssociatedCup().setLid(null);
                    items.remove(lid);
                    if (isVisible) redraw();
                    return;
                }
            }
        }
        showError("Lid #" + i + " not found");
    }
    
    public void orderTower() {
        for (Object item : items) {
            if (item instanceof Cup) ((Cup) item).makeInvisible();
            else if (item instanceof Lid) ((Lid) item).makeInvisible();
        }
        
        ArrayList<Cup> cups = new ArrayList<>();
        ArrayList<Lid> lids = new ArrayList<>();
        
        for (Object item : items) {
            if (item instanceof Cup) {
                cups.add((Cup) item);
            } else if (item instanceof Lid) {
                Lid lid = (Lid) item;
                if (!lid.isOnCup()) lids.add(lid);
            }
        }
        
        Collections.sort(cups, new Comparator<Cup>() {
            public int compare(Cup c1, Cup c2) {
                return c2.getHeight() - c1.getHeight();
            }
        });
        
        Collections.sort(lids, new Comparator<Lid>() {
            public int compare(Lid l1, Lid l2) {
                return l2.getNumber() - l1.getNumber();
            }
        });
        
        items.clear();
        int currentHeight = 0;
        
        for (Cup cup : cups) {
            int totalHeight = cup.getHeight();
            if (cup.hasLid()) totalHeight += cup.getLid().getHeight();
            
            if (currentHeight + totalHeight <= maxHeight) {
                items.add(cup);
                if (cup.hasLid()) items.add(cup.getLid());
                currentHeight += totalHeight;
            }
        }
        
        for (Lid lid : lids) {
            if (currentHeight + lid.getHeight() <= maxHeight) {
                items.add(lid);
                currentHeight += lid.getHeight();
            }
        }
        
        if (isVisible) redraw();
    }
    
    public void reverseTower() {
        Collections.reverse(items);
        
        while (calculateHeight() > maxHeight && !items.isEmpty()) {
            Object removed = items.remove(items.size() - 1);
            if (removed instanceof Cup) {
                Cup cup = (Cup) removed;
                cup.makeInvisible();
                if (cup.hasLid()) {
                    items.remove(cup.getLid());
                    cup.getLid().makeInvisible();
                }
            } else if (removed instanceof Lid) {
                ((Lid) removed).makeInvisible();
            }
        }
        
        if (isVisible) redraw();
    }
    
    
    public int height() {
        return calculateEffectiveHeightCm(items);
    }

    
    public int[] lidedCups() {
        ArrayList<Integer> covered = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof Cup) {
                Cup cup = (Cup) item;
                if (cup.hasLid()) covered.add(cup.getNumber());
            }
        }
        Collections.sort(covered);
        int[] result = new int[covered.size()];
        for (int i = 0; i < covered.size(); i++) {
            result[i] = covered.get(i);
        }
        return result;
    }
    
    public String[][] stackingItems() {
        String[][] result = new String[items.size()][2];
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof Cup) {
                result[i][0] = "cup";
                result[i][1] = String.valueOf(((Cup) item).getNumber());
            } else if (item instanceof Lid) {
                result[i][0] = "lid";
                result[i][1] = String.valueOf(((Lid) item).getNumber());
            }
        }
        return result;
    }
    
    public void makeVisible() {
        isVisible = true;
        int totalWidthPx = width * scale;
        int baseX = (CANVAS_WIDTH - totalWidthPx) / 2;
        int baseY = CANVAS_HEIGHT - MARGIN;
        base.moveHorizontal(-70 + baseX);
        base.moveVertical(-15 + baseY);
        base.makeVisible();
        createHeightMarkers();
        redraw();
    }
    
    public void makeInvisible() {
        isVisible = false;
        base.makeInvisible();
        removeHeightMarkers();
        for (Object item : items) {
            if (item instanceof Cup) ((Cup) item).makeInvisible();
            else if (item instanceof Lid) ((Lid) item).makeInvisible();
        }
    }
    
    
    public boolean ok() {
        return calculateEffectiveHeightCm(items) <= maxHeight;
    }

    
    public void exit() {
        makeInvisible();
        System.exit(0);
    }
    
    private int calculateHeight() {
        int total = 0;
        for (Object item : items) {
            if (item instanceof Cup) total += ((Cup) item).getHeight();
            else if (item instanceof Lid) total += ((Lid) item).getHeight();
        }
        return total;
    }
    
    private boolean cupExists(int number) {
        for (Object item : items) {
            if (item instanceof Cup && ((Cup) item).getNumber() == number) return true;
        }
        return false;
    }
    
    private boolean lidExists(int number) {
        for (Object item : items) {
            if (item instanceof Lid && ((Lid) item).getNumber() == number) return true;
        }
        return false;
    }
    
    private Cup findCup(int number) {
        for (Object item : items) {
            if (item instanceof Cup && ((Cup) item).getNumber() == number) {
                return (Cup) item;
            }
        }
        return null;
    }
    
    private void createHeightMarkers() {
        removeHeightMarkers();
        int totalWidthPx = width * scale;
        int baseX = (CANVAS_WIDTH - totalWidthPx) / 2;
        int baseY = CANVAS_HEIGHT - MARGIN;
        
        for (int cm = 0; cm <= maxHeight; cm++) {
            Rectangle marker = new Rectangle();
            marker.changeSize(2, 15);
            marker.changeColor("black");
            int yPos = baseY - (cm * scale);
            marker.moveHorizontal(-70 + (baseX - 20));
            marker.moveVertical(-15 + yPos);
            marker.makeVisible();
            heightMarkers.add(marker);
        }
    }
    
    private void removeHeightMarkers() {
        for (Rectangle marker : heightMarkers) marker.makeInvisible();
        heightMarkers.clear();
    }
    
    
    private void redraw() {
        if (!isVisible) return;
    
        
        for (Object item : items) {
            if (item instanceof Cup) ((Cup) item).makeInvisible();
            else if (item instanceof Lid) ((Lid) item).makeInvisible();
        }
    
        int totalWidthPx = width * scale;
        int baseX = (CANVAS_WIDTH - totalWidthPx) / 2;
        int baseY = CANVAS_HEIGHT - MARGIN;
    
        
        int accumulatedHeightCm = 0;   
        int groupMaxTopCm = 0;         
        java.util.Deque<Integer> innerWidthsStack = new java.util.ArrayDeque<>(); 
    
        
        for (int idx = 0; idx < items.size(); idx++) {
            Object obj = items.get(idx);
    
            int hCm = (obj instanceof Cup) ? ((Cup) obj).getHeight()
                                           : ((Lid) obj).getHeight();

            int outerCm = hCm;
    
            int innerCm = Math.max(0, outerCm - 2 * WALL_CM);
    
            boolean fitsInCurrent = innerWidthsStack.isEmpty() || (outerCm <= innerWidthsStack.peek());
    
            if (!fitsInCurrent) {
                accumulatedHeightCm += groupMaxTopCm;   
                groupMaxTopCm = 0;
                innerWidthsStack.clear();               
                
            }
               
            int depthCm = innerWidthsStack.size() * WALL_CM;  
            int topThisCupCm = depthCm + hCm;
            groupMaxTopCm = Math.max(groupMaxTopCm, topThisCupCm);
    
            int hPx = hCm * scale;
            int wPx = outerCm * scale;
    
            int yBottomPx = baseY - (accumulatedHeightCm + depthCm) * scale;
    
            int yTopPx = yBottomPx - hPx;
    
            int xPos = baseX + (totalWidthPx - wPx) / 2;
            int yPos = yTopPx;
    
            if (obj instanceof Cup) {
                ((Cup) obj).makeVisibleAt(xPos, yPos, wPx, hPx);
            } else {
                ((Lid) obj).makeVisible();
            }
    
            if (obj instanceof Cup) {
                innerWidthsStack.push(innerCm);
            }
        }
    
        accumulatedHeightCm += groupMaxTopCm;
    
    }
    
    /**
     * Calcula la altura efectiva (en cm) de la secuencia dada,
     * permitiendo anidamiento de copas es la regla: ancho exterior = altura.
     */
    private int calculateEffectiveHeightCm(java.util.List<Object> sequence) {
        int accumulatedHeightCm = 0;                
        int groupMaxTopCm = 0;                      
        java.util.Deque<Integer> innerWidths = new java.util.ArrayDeque<>(); 
    
        for (Object obj : sequence) {
            int hCm = (obj instanceof Cup) ? ((Cup) obj).getHeight()
                                           : ((Lid) obj).getHeight();
            int outerCm = hCm;
            int innerCm = Math.max(0, outerCm - 2 * WALL_CM);
            boolean fits = innerWidths.isEmpty() || (outerCm <= innerWidths.peek());
            if (!fits) {
                accumulatedHeightCm += groupMaxTopCm;
                groupMaxTopCm = 0;
                innerWidths.clear();
            }
            
            int depthCm = innerWidths.size() * WALL_CM;
            int topThis = depthCm + hCm;
            groupMaxTopCm = Math.max(groupMaxTopCm, topThis);
    
            if (obj instanceof Cup) {
                innerWidths.push(innerCm);
            } else {
                if (!innerWidths.isEmpty()) innerWidths.pop();
            }
        }

        accumulatedHeightCm += groupMaxTopCm;
        return accumulatedHeightCm;
    }
    
    //CICLO 2
    /** Intercambia dos copas por el mkomento identificadas por sus descriptores {"cup","<numero>"}.
     *  Ejemplos de uso:
     *    swap({"cup","4"},{"cup","2"});
     */
    public void swap(String[] o1, String[] o2) {
        // Validación de descriptores
        if (o1 == null || o2 == null || o1.length != 2 || o2.length != 2) {
            showError("swap: invalid descriptors");
            return;
        }
    
        String t1 = (o1[0] == null ? "" : o1[0].trim().toLowerCase());
        String t2 = (o2[0] == null ? "" : o2[0].trim().toLowerCase());
        // Solo permitimos COPAS
        if (!"cup".equals(t1) || !"cup".equals(t2)) {
            showError("swap: only cups can be swapped");
            return;
        }
    
        int n1, n2;
        try {
            n1 = Integer.parseInt(String.valueOf(o1[1]).trim());
            n2 = Integer.parseInt(String.valueOf(o2[1]).trim());
        } catch (NumberFormatException ex) {
            showError("swap: invalid number in descriptors");
            return;
        }
        if (n1 == n2) return; // nada que hacer
    
        // Buscar los índices de cada copa
        int idx1 = -1, idx2 = -1;
        for (int i = 0; i < items.size() && (idx1 == -1 || idx2 == -1); i++) {
            Object it = items.get(i);
            if (idx1 == -1 && it instanceof Cup && ((Cup) it).getNumber() == n1) idx1 = i;
            if (idx2 == -1 && it instanceof Cup && ((Cup) it).getNumber() == n2) idx2 = i;
        }
        if (idx1 == -1 || idx2 == -1) {
            showError("swap: cup not found");
            return;
        }
        if (idx1 == idx2) return;
    
        // Mantener referencias a las copas involucradas
        Cup cup1 = (Cup) items.get(idx1);
        Cup cup2 = (Cup) items.get(idx2);
    
        // Ejecutar swap
        Collections.swap(items, idx1, idx2);
    
        // Recolocar tapas a continuación de su copa (si existen)
        relocateLidNextToCup(cup1);
        relocateLidNextToCup(cup2);
    
        // Validar altura efectiva (anidamiento). Revertir si rompe el límite
        int eff = calculateEffectiveHeightCm(items);
        if (eff > maxHeight) {
            // revertimos swap
            // (hay que volver a dejar las tapas donde estaban; para simplificar, rehacemos todo)
            Collections.swap(items, items.indexOf(cup1), items.indexOf(cup2));
            relocateLidNextToCup(cup1);
            relocateLidNextToCup(cup2);
            showError("swap would exceed max height; reverted");
            return;
        }
    
        if (isVisible) redraw();
    }
    
    /**
     * @return dos descriptores {"tipo","numero"} del mejor par a intercambiar.
     *    
     */
    
    public String[][] swapToReduce() {
        
        java.util.ArrayList<Integer> cupIdx = new java.util.ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof Cup) cupIdx.add(i);
        }
        if (cupIdx.size() < 2) {
            return new String[][] { {"none","-1"}, {"none","-1"} };
        }
    
        int baseEff = calculateEffectiveHeightCm(items);
        int bestEff = baseEff;
        int bestA = -1, bestB = -1;            
        int bestCupNumA = -1, bestCupNumB = -1;

        for (int aPos = 0; aPos < cupIdx.size(); aPos++) {
            for (int bPos = aPos + 1; bPos < cupIdx.size(); bPos++) {
                int ia = cupIdx.get(aPos);
                int ib = cupIdx.get(bPos);
    
                Cup ca = (Cup) items.get(ia);
                Cup cb = (Cup) items.get(ib);
    
                java.util.ArrayList<Object> sim = new java.util.ArrayList<>(items);
                java.util.Collections.swap(sim, ia, ib);
    
                relocateLidNextToCupIn(sim, ca);
                relocateLidNextToCupIn(sim, cb);
    
                int eff = calculateEffectiveHeightCm(sim);
    
               
                if (eff < bestEff && eff <= maxHeight) {
                    bestEff = eff;
                    bestA = ia; bestB = ib;
                    bestCupNumA = ca.getNumber();
                    bestCupNumB = cb.getNumber();
                }
            }
        }
    
        if (bestA == -1) {
            return new String[][] { {"none","-1"}, {"none","-1"} };
        }
    
        // Devolvemos descriptores por número de copa 
        return new String[][] {
            { "cup", String.valueOf(bestCupNumA) },
            { "cup", String.valueOf(bestCupNumB) }
        };
    }
    
    //Helper para ciclo 2
    
    /** En la lista 'list', reubica la tapa de 'cup' inmediatamente después de esa copa. */
    private void relocateLidNextToCupIn(java.util.List<Object> list, Cup cup) {
        if (cup == null || !cup.hasLid()) return;
        Lid lid = cup.getLid();
        int lidIdx = list.indexOf(lid);
        if (lidIdx != -1) list.remove(lidIdx);
        int cupIdx = list.indexOf(cup);
        if (cupIdx != -1) {
            int insertAt = Math.min(cupIdx + 1, list.size());
            list.add(insertAt, lid);
        }
    }
    
    /** Coloca la tapa asociada (si existe) inmediatamente después de su copa en 'items'. */
    private void relocateLidNextToCup(Cup cup) {
        if (cup == null || !cup.hasLid()) return;
        Lid lid = cup.getLid();
    
        // Si la tapa está en la lista, retirarla de su posición actual
        int lidIdx = items.indexOf(lid);
        if (lidIdx != -1) items.remove(lidIdx);
    
        // Insertarla justo después de la copa
        int cupIdx = items.indexOf(cup);
        if (cupIdx != -1) {
            int insertAt = Math.min(cupIdx + 1, items.size());
            items.add(insertAt, lid);
        }
    }
    
    private void showError(String message) {
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message, 
                "Tower Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}