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
    private static final int BASE_X = 150;
    private static final int BASE_Y = 400;
    
    private Rectangle base;
    private ArrayList<Rectangle> visualElements;
    private ArrayList<Rectangle> heightMarkers;
    
    /**
     * Crea una torre con una altura y un ancho especifico
     * 
     * @param width Width of the tower in pixels
     * @param maxHeight Maximum height of the tower in cm
     */
    public Tower(int width, int maxHeight) {
        if (width <= 0 || maxHeight <= 0) {
            throw new IllegalArgumentException("Width y height deben ser positivos");
        }
        this.width = width;
        this.maxHeight = maxHeight;
        this.items = new ArrayList<>();
        this.isVisible = false;
        this.visualElements = new ArrayList<>();
        this.heightMarkers = new ArrayList<>();
        initializeBase();
    }
    
    
    private void initializeBase() {
        base = new Rectangle();
        base.changeSize(5, width);
        base.changeColor("black");
    }
    
    /**
     * Agrega una taza a la torre y solo una taza por número puede existir.
     * 
     * @param i The number of the cup to add
     */
    public void pushCup(int i) {
        if (cupExists(i)) {
            showError("Cup #" + i + " already exists in the tower");
            return;
        }
        
        Cup cup = new Cup(i);
        
        if (calculateHeight() + cup.getHeight() <= maxHeight) {
            items.add(cup);
            if (isVisible) {
                redraw();
            }
        } else {
            showError("Cannot add cup #" + i + 
                    ": would exceed maximum height of " + maxHeight + " cm");
        }
    }
    
    /**
     * Remueve la taza más alta de la torre.
     */
    public void popCup() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Cup) {
                Cup cup = (Cup) items.get(i);
                items.remove(cup);
                if (isVisible) {
                    redraw();
                }
                return;
            }
        }
        showError("Ninguna taza para remover");
    }
    
    /**
     * Remueve una taza específica por su número.
     * 
     * @param i The number of the cup to remove
     */
    public void removeCup(int i) {
        for (int j = 0; j < items.size(); j++) {
            if (items.get(j) instanceof Cup) {
                Cup cup = (Cup) items.get(j);
                if (cup.getNumber() == i) {
                    items.remove(cup);
                    if (isVisible) {
                        redraw();
                    }
                    return;
                }
            }
        }
        showError("Cup #" + i + " not found in tower");
    }
    
    /**
     * Orders tower elements from largest to smallest.
     * Cups by height, lids by number (descending).
     * Only includes elements that fit within maximum height.
     * Paired cup-lid move together.
     */
    public void orderTower() {
        ArrayList<Cup> cups = new ArrayList<>();
        
        
        // Altura de las tazas (descendente)
        Collections.sort(cups, new Comparator<Cup>() {
            public int compare(Cup c1, Cup c2) {
                return c2.getHeight() - c1.getHeight();
            }
        });
        
        
        // Construir de nuevo la lista de elementos ordenados
        items.clear();
        int currentHeight = 0;
        
        // Add cups (with their lids if paired)
        for (Cup cup : cups) {
            int totalHeight = cup.getHeight();
            
            if (currentHeight + totalHeight <= maxHeight) {
                items.add(cup);
                }
                currentHeight += totalHeight;
            }
        }
        
        
    }
    
    /**
     * Revertir el orden de los elementos en la torre.
     */
    public void reverseTower() {
        Collections.reverse(items);
        
        while (calculateHeight() > maxHeight && !items.isEmpty()) {
            Object removed = items.remove(items.size() - 1);
            if (removed instanceof Cup) {
                Cup cup = (Cup) removed;
            }
        }
        
        if (isVisible) {
            redraw();
        }
    }
    
    /**
     * Gets the current height of stacked elements in cm.
     * 
     * @return The total height in cm
     */
    public int height() {
        return calculateHeight();
    }
    
    /**
     * Makes the tower visible.
     */
    public void makeVisible() {
        isVisible = true;
        base.moveHorizontal(BASE_X - 70);
        base.moveVertical(BASE_Y - 15);
        base.makeVisible();
        createHeightMarkers();
        redraw();
    }
    
    /**
     * Makes the tower invisible.
     */
    public void makeInvisible() {
        isVisible = false;
        base.makeInvisible();
        removeHeightMarkers();
        clearVisualElements();
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
            if (item instanceof Cup) {
                total += ((Cup) item).getHeight();
            }
        }
        return total;
    }
    
    private boolean cupExists(int number) {
        for (Object item : items) {
            if (item instanceof Cup && ((Cup) item).getNumber() == number) {
                return true;
            }
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
        for (int i = 0; i <= maxHeight; i += 5) {
            Rectangle marker = new Rectangle();
            marker.changeSize(2, 25);
            marker.changeColor("black");
            int yPos = BASE_Y - (i * PIXELS_PER_CM);
            marker.moveHorizontal(BASE_X - width/2 - 95);
            marker.moveVertical(yPos - 15);
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
    
    private void clearVisualElements() {
        for (Rectangle element : visualElements) {
            element.makeInvisible();
        }
        visualElements.clear();
    }
    
    private void redraw() {
        if (!isVisible) return;
        
        clearVisualElements();
        
        int currentHeight = 0;
        for (Object item : items) {
            Rectangle visual = new Rectangle();
            int itemHeight, itemWidth;
            String color;
            
            if (item instanceof Cup) {
                Cup cup = (Cup) item;
                itemHeight = cup.getHeight();
                itemWidth = cup.getWidth();
                color = cup.getColor();
            }
            
            int pixelHeight = itemHeight * PIXELS_PER_CM;
            int yPos = BASE_Y - ((currentHeight + itemHeight) * PIXELS_PER_CM);
            visual.changeSize(pixelHeight, itemWidth);
            visual.changeColor(color);
            visual.moveHorizontal(BASE_X - 70);
            visual.moveVertical(yPos - 15);
            visual.makeVisible();
            
            visualElements.add(visual);
            currentHeight += itemHeight;
        }
    }
    
    private void showError(String message) {
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message, 
                "Tower Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
