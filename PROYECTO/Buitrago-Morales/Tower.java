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
        
        if (calculateHeight() + cup.getHeight() <= maxHeight) {
            items.add(cup);
            if (isVisible) redraw();
        } else {
            showError("Cannot add cup #" + i + ": exceeds max height");
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
        
        if (calculateHeight() + lid.getHeight() <= maxHeight) {
            Cup matchingCup = findCup(i);
            if (matchingCup != null && !matchingCup.hasLid()) {
                matchingCup.setLid(lid);
                lid.setAssociatedCup(matchingCup);
            }
            items.add(lid);
            if (isVisible) redraw();
        } else {
            showError("Cannot add lid #" + i + ": exceeds max height");
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
        return calculateHeight();
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
        return calculateHeight() <= maxHeight;
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
        
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            
            int heightCm = (item instanceof Cup) 
                ? ((Cup) item).getHeight() 
                : ((Lid) item).getHeight();
            
            int heightPx = heightCm * scale;
            int widthCm = Math.max(2, width - (i * 1));
            int widthPx = widthCm * scale;
            int xPos = baseX + (totalWidthPx - widthPx) / 2;
            int yPos = baseY - (accumulatedHeightCm + heightCm) * scale;
            
            if (item instanceof Cup) {
                ((Cup) item).makeVisibleAt(xPos, yPos, widthPx, heightPx);
            } else if (item instanceof Lid) {
                ((Lid) item).makeVisibleAt(xPos, yPos, widthPx, heightPx);
            }
            
            accumulatedHeightCm += heightCm;
        }
    }
    
    private void showError(String message) {
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message, 
                "Tower Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}