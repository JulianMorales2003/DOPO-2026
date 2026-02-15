
import java.util.ArrayList;

public class Tower {

    private int width;       
    private int maxHeight;   
    private boolean visible;  
    private boolean lastOk;   
    //Lista para guarda tazas y tapas en orden.
    private ArrayList<Item> stack;

    //Clase para representar tazas y tapas, con su tipo y número.
    private class Item {
        String type;  
        int number;    

        Item(String t, int n) {
            type = t;
            number = n;
        }
    }

    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.visible = false;
        this.lastOk = true;
        this.stack = new ArrayList<>();
    }


    public void pushCup(int i) {

        lastOk = true;
    }

    public void popCup() {

        lastOk = true;
    }

    public void removeCup(int i) {
        
        lastOk = true;
    }


    public void pushLid(int i) {
        lastOk = true;
    }

    public void popLid() {
        lastOk = true;
    }

    public void removeLid(int i) {
        lastOk = true;
    }

    public void orderTower() {
        lastOk = true;
    }

    public void reverseTower() {
        lastOk = true;
    }

    public int height() {
        return 0;  // luego lo calculamos
    }

    public int[] lidedCups() {
        return new int[0]; // luego lo hacemos
    }

    public String[][] stackingItems() {
        return new String[0][]; // luego lo hacemos
    }

    
    public void makeVisible() {
        visible = true;
        lastOk = true;
    }

    public void makeInvisible() {
        visible = false;
        lastOk = true;
    }

    public void exit() {
        lastOk = true;
    }

    public boolean ok() {
        return lastOk;
    }
}

