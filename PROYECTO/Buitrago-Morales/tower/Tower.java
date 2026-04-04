package tower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;
import shapes.Rectangle;

/**
 * Torre que maneja copas y tapas con polimorfismo.
 * Refactorizada en Ciclo 4 para soportar diferentes tipos de copas y tapas.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4 - Polimorfismo)
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
    
    /**
     * Constructor principal.
     */
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
     * Constructor con número de copas.
     * Crea copas NORMALES por defecto (requisito: creador masivo usa normales).
     */
    public Tower(int numberOfCups) {
        if (numberOfCups < 0) {
            throw new IllegalArgumentException("numberOfCups must be >= 0");
        }

        this.width = (numberOfCups == 0) ? 0 : (2 * numberOfCups - 1);
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

        for (int i = 1; i <= numberOfCups; i++) {
            items.add(new NormalCup(i));
        }

        makeVisible();
    }

    private void initializeBase() {
        base = new Rectangle();
        base.changeSize(5, width * scale);
        base.changeColor("black");
    }
    
    
    /**
     * Agrega una copa NORMAL .
     * Mantiene compatibilidad con código anterior.
     */
    public void pushCup(int i) {
        pushCupType("normal", i);
    }
    
    /**
     * Agrega una copa de un tipo específico.
     * 
     * @param type Tipo de copa: "normal", "opener", "hierarchical", "bouncer"
     * @param i Número de la copa
     */
    public void pushCupType(String type, int i) {
        if (cupExists(i)) {
            showError("Cup #" + i + " already exists");
            return;
        }
        
        Cup cup = createCup(type, i);
        if (cup == null) {
            showError("Unknown cup type: " + type);
            return;
        }
        
        if (cup instanceof BouncerCup) {
            if (hasCupWithHeight(cup.getHeight())) {
                showError("BouncerCup #" + i + " bounced! Another cup with same height exists");
                return;
            }
        }
        
        ArrayList<Object> sim = new ArrayList<>(items);
        sim.add(cup);
        int eff = calculateEffectiveHeightCm(sim);
        
        if (eff <= maxHeight) {
            if (cup instanceof OpenerCup) {
                removeLidsInPath();
            }
            
            if (cup instanceof HierarchicalCup) {
                int finalPosition = displaceSmaller(cup);
                items.add(finalPosition, cup);
                cup.onEnter(this, finalPosition);
                updateHierarchicalPositions();
            } else {
                items.add(cup);
                cup.onEnter(this, findCupPosition(cup));
            }
             
            if (isVisible) redraw();
        } else {
            showError("Cannot add cup #" + i + ": exceeds max height");
        }
    }
    
    /**
     * Crea una copa del tipo especificado.
     */
    private Cup createCup(String type, int number) {
        switch (type.toLowerCase()) {
            case "normal":
                return new NormalCup(number);
            case "opener":
                return new OpenerCup(number);
            case "hierarchical":
                return new HierarchicalCup(number);
            case "bouncer":
                return new BouncerCup(number);
            default:
                return null;
        }
    }
    
    /**
     * Elimina todas las tapas para OpenerCup.
     */
    private void removeLidsInPath() {
        ArrayList<Object> toRemove = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof Lid) {
                toRemove.add(item);
            }
        }
        for (Object item : toRemove) {
            Lid lid = (Lid) item;
            lid.makeInvisible();
            if (lid.isOnCup()) {
                lid.getAssociatedCup().setLid(null);
                lid.attachTo(null);
            }
            items.remove(lid);
        }
    }
    
    /**
     * Calcula la posición donde debe insertarse la HierarchicalCup.
     * 
     */
    private int displaceSmaller(Cup newCup) {
        int insertAfter = -1; 
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof Cup) {
                Cup existing = (Cup) items.get(i);
                if (existing.getHeight() >= newCup.getHeight()) {
                    insertAfter = i;
                }
            }
        }
        
        return insertAfter + 1;
    }
    
    /**
     * Actualiza el estado atBottom de todas las HierarchicalCups.
     * Debe llamarse después de cualquier operación que reordene items.
     */
    private void updateHierarchicalPositions() {
        
        int firstCupIndex = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof Cup) {
                firstCupIndex = i;
                break;
            }
        }
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof HierarchicalCup) {
                HierarchicalCup hCup = (HierarchicalCup) items.get(i);
                hCup.setAtBottom(i == firstCupIndex);
            }
        }
    }
    
    /**
     * Verifica si hay una copa con cierta altura.
     */
    private boolean hasCupWithHeight(int height) {
        for (Object item : items) {
            if (item instanceof Cup) {
                Cup cup = (Cup) item;
                if (cup.getHeight() == height) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Encuentra la posición de una copa en items.
     */
    private int findCupPosition(Cup cup) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) == cup) {
                return i;
            }
        }
        return -1;
    }
    
    public void popCup() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Cup) {
                Cup cup = (Cup) items.get(i);
                
                if (!cup.canExit(this)) {
                    showError("Cup #" + cup.getNumber() + " (" + cup.getType() + ") cannot exit");
                    return;
                }
                
                cup.makeInvisible();
                if (cup.hasLid()) {
                    items.remove(cup.getLid());
                }
                items.remove(cup);
                updateHierarchicalPositions();
                if (isVisible) redraw();
                return;
            }
        }
    }
    
    
    
    /**
     * Agrega una tapa NORMAL (comportamiento original).
     */
    public void pushLid(int i) {
        pushLidType("normal", i);
    }
    
    /**
     * Agrega una tapa de un tipo específico.
     * 
     * @param type Tipo de tapa: "normal", "fearful", "crazy", "glued"
     * @param i Número de la tapa
     */
    public void pushLidType(String type, int i) {
        if (lidExists(i)) {
            showError("Lid #" + i + " already exists");
            return;
        }
        Lid lid = createLid(type, i);
        if (lid == null) {
            showError("Unknown lid type: " + type);
            return;
        }
        if (lid instanceof FearfulLid) {
            if (!cupExists(i)) {
                showError("FearfulLid #" + i + " won't enter: its cup is not in the tower");
                return;
            }
        }
        if (!lid.canEnter(this)) {
            showError("Lid #" + i + " (" + lid.getType() + ") cannot enter");
            return;
        }
        // CrazyLid goes to the base and does NOT cover any cup
        if (!(lid instanceof CrazyLid)) {
            Cup matchingCup = findCup(i);
            if (matchingCup != null && !matchingCup.hasLid()) {
                matchingCup.setLid(lid);
                lid.attachTo(matchingCup);
            }
        }
        
        if (lid instanceof CrazyLid) {
            items.add(0, lid); 
        } else {
            ArrayList<Object> sim = new ArrayList<>(items);
            sim.add(lid);
            int eff = calculateEffectiveHeightCm(sim);
            
            if (eff <= maxHeight) {
                items.add(lid);
            } else {
                showError("Cannot add lid #" + i + ": exceeds max height");
                return;
            }
        }
        
        if (isVisible) redraw();
    }
    
    /**
     * Crea una tapa del tipo especificado.
     */
    private Lid createLid(String type, int number) {
        switch (type.toLowerCase()) {
            case "normal":
                return new NormalLid(number);
            case "fearful":
                return new FearfulLid(number);
            case "crazy":
                return new CrazyLid(number);
            case "glued":
                return new GluedLid(number);
            default:
                return null;
        }
    }
    
    public void popLid() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Lid) {
                Lid lid = (Lid) items.get(i);
                
                if (!lid.canExit(this)) {
                    showError("Lid #" + lid.getNumber() + " (" + lid.getType() + ") cannot exit");
                    return;
                }
                
                lid.makeInvisible();
                if (lid.isOnCup()) {
                    lid.getAssociatedCup().setLid(null);
                    lid.attachTo(null);
                }
                items.remove(lid);
                if (isVisible) redraw();
                return;
            }
        }
    }
    
    public void removeLid(int i) {
        for (Object item : items) {
            if (item instanceof Lid) {
                Lid lid = (Lid) item;
                if (lid.getNumber() == i) {
                    
                    if (!lid.canExit(this)) {
                        showError("Lid #" + i + " (" + lid.getType() + ") cannot be removed");
                        return;
                    }
                    
                    lid.makeInvisible();
                    if (lid.isOnCup()) {
                        lid.getAssociatedCup().setLid(null);
                        lid.attachTo(null);
                    }
                    items.remove(lid);
                    if (isVisible) redraw();
                    return;
                }
            }
        }
    }
    
    
    public void orderTower() {
        ArrayList<Cup> cups = new ArrayList<>();
        ArrayList<Lid> lids = new ArrayList<>();
        
        for (Object item : items) {
            if (item instanceof Cup) cups.add((Cup) item);
            else if (item instanceof Lid) lids.add((Lid) item);
        }
        
        Collections.sort(cups, new Comparator<Cup>() {
            public int compare(Cup c1, Cup c2) {
                return c2.getNumber() - c1.getNumber();
            }
        });
        
        Collections.sort(lids, new Comparator<Lid>() {
            public int compare(Lid l1, Lid l2) {
                return l2.getNumber() - l1.getNumber();
            }
        });
        
        items.clear();
        
        for (Cup cup : cups) {
            ArrayList<Object> sim = new ArrayList<>(items);
            sim.add(cup);
            if (calculateEffectiveHeightCm(sim) <= maxHeight) {
                items.add(cup);
                
                if (cup.hasLid()) {
                    items.add(cup.getLid());
                }
            }
        }
        
        for (Lid lid : lids) {
            if (lid.isOnCup()) continue;
            ArrayList<Object> sim = new ArrayList<>(items);
            sim.add(lid);
            if (calculateEffectiveHeightCm(sim) <= maxHeight) {
                items.add(lid);
            }
        }
        
        updateHierarchicalPositions();
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
        
        updateHierarchicalPositions(); 
        if (isVisible) redraw();
    }
    
    public void swap(String[] descriptor1, String[] descriptor2) {
        if (descriptor1 == null || descriptor2 == null) {
            showError("Cannot swap: null descriptor");
            return;
        }
        if (descriptor1.length < 2 || descriptor2.length < 2) {
            showError("Invalid descriptor format");
            return;
        }
        if (!descriptor1[0].equals("cup") || !descriptor2[0].equals("cup")) {
            showError("Can only swap cups");
            return;
        }
        
        int num1 = Integer.parseInt(descriptor1[1]);
        int num2 = Integer.parseInt(descriptor2[1]);
        
        if (num1 == num2) return;
        
        int idx1 = -1, idx2 = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof Cup) {
                Cup cup = (Cup) items.get(i);
                if (cup.getNumber() == num1) idx1 = i;
                if (cup.getNumber() == num2) idx2 = i;
            }
        }
        
        if (idx1 == -1 || idx2 == -1) {
            showError("One or both cups not found");
            return;
        }
        
        ArrayList<Object> backup = new ArrayList<>(items);
        Collections.swap(items, idx1, idx2);
         
        if (calculateEffectiveHeightCm(items) > maxHeight) {
            items = backup;
            showError("Swap would exceed max height");
        } else {
            updateHierarchicalPositions(); 
            if (isVisible) redraw();
        }
    }
    
    public String[][] swapToReduce() {
        ArrayList<Cup> cups = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof Cup) cups.add((Cup) item);
        }
        
        if (cups.size() < 2) {
            return new String[][] {{"none", "0"}, {"none", "0"}};
        }
        
        int currentHeight = calculateEffectiveHeightCm(items);
        int bestHeight = currentHeight;
        int bestI = -1, bestJ = -1;
        
        for (int i = 0; i < cups.size(); i++) {
            for (int j = i + 1; j < cups.size(); j++) {
                ArrayList<Object> sim = new ArrayList<>(items);
                int idxI = sim.indexOf(cups.get(i));
                int idxJ = sim.indexOf(cups.get(j));
                Collections.swap(sim, idxI, idxJ);
                
                int newHeight = calculateEffectiveHeightCm(sim);
                if (newHeight < bestHeight) {
                    bestHeight = newHeight;
                    bestI = i;
                    bestJ = j;
                }
            }
        }
        
        if (bestI == -1) {
            return new String[][] {{"none", "0"}, {"none", "0"}};
        }
        
        return new String[][] {
            {"cup", String.valueOf(cups.get(bestI).getNumber())},
            {"cup", String.valueOf(cups.get(bestJ).getNumber())}
        };
    }
    
    public void cover() {
        ArrayList<Integer> cupNumbers = new ArrayList<>();
        
        for (Object item : items) {
            if (item instanceof Cup) {
                Cup cup = (Cup) item;
                cupNumbers.add(cup.getNumber());
            }
        }
        
        for (Integer cupNumber : cupNumbers) {
            if (!lidExists(cupNumber)) {
                pushLid(cupNumber);
            }
        }
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
    
    public boolean ok() {
        return calculateEffectiveHeightCm(items) <= maxHeight;
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
    
    private void showError(String message) {
        System.err.println("Tower Error: " + message);
    }
    
    private int calculateEffectiveHeightCm(ArrayList<Object> itemsList) {
        int accumulatedHeightCm = 0;
        int groupMaxTopCm = 0;
        java.util.Deque<Integer> innerWidths = new java.util.ArrayDeque<>();

        for (Object obj : itemsList) {
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
            } else if (obj instanceof Lid && !((Lid) obj).isOnCup()) {
                if (!innerWidths.isEmpty()) innerWidths.pop();
            }
        }
        accumulatedHeightCm += groupMaxTopCm;
        return accumulatedHeightCm;
    }

    private void createHeightMarkers() {
        removeHeightMarkers();
        int totalWidthPx = width * scale;
        int baseX = (CANVAS_WIDTH - totalWidthPx) / 2;
        int baseY = CANVAS_HEIGHT - MARGIN;
        
        for (int cm = 1; cm <= maxHeight; cm++) {
            Rectangle marker = new Rectangle();
            marker.changeSize(1, 10);
            marker.changeColor("gray");
            int markerY = baseY - (cm * scale);
            marker.moveHorizontal(-70 + baseX - 15);
            marker.moveVertical(-15 + markerY);
            marker.makeVisible();
            heightMarkers.add(marker);
        }
    }
    
    private void removeHeightMarkers() {
        for (Rectangle marker : heightMarkers) {
            marker.makeInvisible();
        }
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
        java.util.List<Lid> associatedLidsToDrawLater = new java.util.ArrayList<>();

        for (Object obj : items) {
            if (!(obj instanceof Cup) && !(obj instanceof Lid)) continue;
            
            if (obj instanceof CrazyLid) continue;

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
            int yTopPx    = yBottomPx - hPx;
            int xPos = baseX + (totalWidthPx - wPx) / 2;

            if (obj instanceof Cup) {
                Cup cup = (Cup) obj;
                cup.makeVisibleAt(xPos, yTopPx, wPx, hPx);
                if (cup.hasLid()) {
                    associatedLidsToDrawLater.add(cup.getLid());
                }
                innerWidthsStack.push(innerCm);
            } else if (obj instanceof Lid) {
                Lid lid = (Lid) obj;
                if (!lid.isOnCup()) {
                    lid.makeVisible();
                }
            }
        }

        for (Lid lid : associatedLidsToDrawLater) {
            lid.makeVisible();
        }
        
        for (Object item : items) {
            if (item instanceof CrazyLid) {
                CrazyLid cl = (CrazyLid) item;
                Cup matchingCup = null;
                for (Object o : items) {
                    if (o instanceof Cup && ((Cup) o).getNumber() == cl.getNumber()) {
                        matchingCup = (Cup) o;
                        break;
                    }
                }
                if (matchingCup != null && matchingCup.getLastW() > 0) {
                    int wallPx = WALL_CM * scale;
                    int wPx = matchingCup.getLastW() - 2 * wallPx;
                    int hPx = Math.max(3, scale / 2);
                    int xPos = matchingCup.getLastX() + wallPx;
                    int yPos = matchingCup.getLastY() + matchingCup.getLastH() - hPx;
                    cl.drawAtBase(xPos, yPos, wPx, hPx);
                }
            }
        }
    }
}