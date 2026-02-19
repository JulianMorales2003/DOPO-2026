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
                if (cup.hasLid()) {
                    items.remove(cup.getLid());
                }
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
                    if (cup.hasLid()) {
                        items.remove(cup.getLid());
                    }
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
     * Agrega una tapa a la torre y solo una tapa por número puede existir.
     * @param i El numero de la tapa a agregar.
     */
    public void pushLid(int i) {
        if (lidExists(i)) {
            showError("Lid #" + i + " already exists in the tower");
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
            if (isVisible) {
                redraw();
            }
        } else {
            showError("Cannot add lid #" + i + 
                    ": would exceed maximum height of " + maxHeight + " cm");
        }
    }
    
    /**
     * Remueve la tapa más alta de la torre.
     */
    public void popLid() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Lid) {
                Lid lid = (Lid) items.get(i);
                if (lid.isOnCup()) {
                    lid.getAssociatedCup().setLid(null);
                }
                items.remove(lid);
                if (isVisible) {
                    redraw();
                }
                return;
            }
        }
        showError("No lids to remove");
    }
    
    /**
     * Remueve una tapa específica por su número.
     * 
     * @param i El numero de la tapa a remover.
     */
    public void removeLid(int i) {
        for (int j = 0; j < items.size(); j++) {
            if (items.get(j) instanceof Lid) {
                Lid lid = (Lid) items.get(j);
                if (lid.getNumber() == i) {
                    if (lid.isOnCup()) {
                        lid.getAssociatedCup().setLid(null);
                    }
                    items.remove(lid);
                    if (isVisible) {
                        redraw();
                    }
                    return;
                }
            }
        }
        showError("Lid #" + i + " not found in tower");
    }
    
    /**
     * Ordena los elementos de la torre de mayor a menor.
     */
    public void orderTower() {
        ArrayList<Cup> cups = new ArrayList<>();
        ArrayList<Lid> lids = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof Cup) {
                cups.add((Cup) item);
            } else if (item instanceof Lid) {
                Lid lid = (Lid) item;
                if (!lid.isOnCup()) {
                    lids.add(lid);
                }
            }
        }
        
        // Ordenar tazas por altura descendente.
        Collections.sort(cups, new Comparator<Cup>() {
            public int compare(Cup c1, Cup c2) {
                return c2.getHeight() - c1.getHeight();
            }
        });
        
        // Ordena tapas por numero descendente.
        Collections.sort(lids, new Comparator<Lid>() {
            public int compare(Lid l1, Lid l2) {
                return l2.getNumber() - l1.getNumber();
            }
        });
        
        // Reconstruir la torre con tazas ordenadas y luego tapas sin pareja.
        items.clear();
        int currentHeight = 0;
        for (Cup cup : cups) {
            int totalHeight = cup.getHeight();
            if (cup.hasLid()) {
                totalHeight += cup.getLid().getHeight();
            }
            
            if (currentHeight + totalHeight <= maxHeight) {
                items.add(cup);
                if (cup.hasLid()) {
                    items.add(cup.getLid());
                }
                currentHeight += totalHeight;
            }
        }
        
        for (Lid lid : lids) {
            if (currentHeight + lid.getHeight() <= maxHeight) {
                items.add(lid);
                currentHeight += lid.getHeight();
            }
        }
        
        if (isVisible) {
            redraw();
        }
    }
    
    /**
     * Invierte el orden de los elementos en la torre.
     */
    public void reverseTower() {
        Collections.reverse(items);
        
        while (calculateHeight() > maxHeight && !items.isEmpty()) {
            Object removed = items.remove(items.size() - 1);
            if (removed instanceof Cup) {
                Cup cup = (Cup) removed;
                if (cup.hasLid()) {
                    items.remove(cup.getLid());
                }
            }
        }
        
        if (isVisible) {
            redraw();
        }
    }
    
    /**.
     * 
     * @return La altura total de la torre en cm.
     */
    public int height() {
        return calculateHeight();
    }
    
    /**
     * Obtiene el número de tazas en la torre. 
     * @return Lista de números de tazas en la torre.
     */ 

    public int[] lidedCups() {
        ArrayList<Integer> covered = new ArrayList<>();
        
        for (Object item : items) {
            if (item instanceof Cup) {
                Cup cup = (Cup) item;
                if (cup.hasLid()) {
                    covered.add(cup.getNumber());
                }
            }
        }
        
        // Ordenar los números de las tazas cubiertas de menor a mayor
        Collections.sort(covered);
        
        // Convierte la lista de Integer a un array de int.
        int[] result = new int[covered.size()];
        for (int i = 0; i < covered.size(); i++) {
            result[i] = covered.get(i);
        }
        
        return result;
    }
    
    /**
     * Obtiene la información de los elementos apilados.
     * @return Una matriz 2D con el tipo ya sea cup o lid y su numero
     */
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
        base.moveHorizontal(BASE_X - 70);
        base.moveVertical(BASE_Y - 15);
        base.makeVisible();
        createHeightMarkers();
        redraw();
    }
    
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
            } else if (item instanceof Lid) {
                total += ((Lid) item).getHeight();
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
    
    private boolean lidExists(int number) {
        for (Object item : items) {
            if (item instanceof Lid && ((Lid) item).getNumber() == number) {
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
            } else {
                Lid lid = (Lid) item;
                itemHeight = lid.getHeight();
                itemWidth = lid.getWidth();
                color = lid.getColor();
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
