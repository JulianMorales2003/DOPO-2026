package tower;

import shapes.Rectangle;


/**
 * Clase abstracta que representa una tapa en la torre.
 * Define el comportamiento común de todas las tapas y delega
 * comportamientos específicos a las subclases.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4 - Polimorfismo)
 */
public abstract class Lid {
    
    protected int number;
    protected String color;
    protected Cup associatedCup;
    protected Rectangle rect;

    protected static final int RECT_INIT_X = 70;
    protected static final int RECT_INIT_Y = 15;

    protected int rectX = RECT_INIT_X;
    protected int rectY = RECT_INIT_Y;

    /**
     * Constructor base para todas las tapas.
     * 
     * @param number Número identificador de la tapa (debe ser > 0)
     * @throws IllegalArgumentException si number <= 0
     */
    public Lid(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Lid number must be positive");
        }
        this.number = number;
        this.color = generateColor(number);
        this.associatedCup = null;
        this.rect = new Rectangle();
        this.rectX = RECT_INIT_X;
        this.rectY = RECT_INIT_Y;
    }

    /**
     * Genera un color basado en el número de la tapa.
     * 
     * @param number Número de la tapa
     * @return Color asignado
     */
    protected String generateColor(int number) {
        String[] colors = {"red", "blue", "green", "yellow", "magenta", "black"};
        return colors[(number - 1) % colors.length];
    }

    /**
     * Asocia esta tapa a una copa y sincroniza su color y número con ella.
     * 
     * @param cup Copa a la que se asocia la tapa
     */
    public void attachTo(Cup cup) {
        this.associatedCup = cup;
        if (cup != null) {
            this.color = cup.getColor();
            this.number = cup.getNumber();
        }
        snapToCup();
    }

    /**
     * Acopla visualmente la tapa al borde superior de su copa.
     */
    public void snapToCup() {
        if (associatedCup == null) return;

        int cupX = associatedCup.getLastX();
        int cupY = associatedCup.getLastY();
        int cupW = associatedCup.getLastW();
        int cupH = associatedCup.getLastH();

        if (cupW <= 0 || cupH <= 0) return;

        int targetW = cupW;
        int lidThick = Math.max(3, cupH / 5);
        int targetX = cupX;
        int targetY = cupY;

        rect.changeColor(associatedCup.getColor());
        rect.changeSize(lidThick, targetW);
        rect.moveHorizontal(targetX - rectX);
        rect.moveVertical(targetY - rectY);

        rectX = targetX;
        rectY = targetY;

        rect.makeVisible();
    }

    /**
     * Hace visible la tapa.
     * Si tiene copa, se acopla a ella. Si no, se dibuja de forma independiente.
     */
    public void makeVisible() {
        if (associatedCup != null) {
            snapToCup();
        } else {
            drawStandalone();
        }
    }

    /**
     * Dibuja la tapa de forma independiente cuando no tiene copa asociada.
     */
    protected void drawStandalone() {
        int widthPx = number * 10;
        int heightPx = 3;
        rect.changeColor(color);
        rect.changeSize(heightPx, widthPx);
        rect.makeVisible();
    }

    /**
     * Hace invisible la tapa en el canvas.
     */
    public void makeInvisible() {
        rect.makeInvisible();
    }


    /**
     * Comportamiento al intentar entrar a la torre.
     * Cada tipo de tapa tiene condiciones diferentes para entrar.
     * 
     * @param tower Torre en la que intenta entrar
     * @return true si puede entrar, false si no
     */
    public abstract boolean canEnter(Object tower);

    /**
     * Comportamiento al intentar salir de la torre.
     * 
     * @param tower Torre de la que intenta salir
     * @return true si puede salir, false si no
     */
    public abstract boolean canExit(Object tower);

    /**
     * Obtiene el tipo de tapa como String.
     * 
     * @return Tipo de tapa ("normal", "fearful", "crazy", etc.)
     */
    public abstract String getType();


    public int getNumber() {
        return number;
    }

    public int getHeight() {
        return 1;
    }

    public int getHeightCm() {
        return 1;
    }

    public String getColor() {
        return (associatedCup != null) ? associatedCup.getColor() : color;
    }

    public Cup getAssociatedCup() {
        return associatedCup;
    }

    public boolean isOnCup() {
        return associatedCup != null;
    }

    @Override
    public String toString() {
        return "Tapa " + getType() + " #" + number +
                (associatedCup != null
                        ? " (sobre copa #" + associatedCup.getNumber() + ")"
                        : " (sin copa)");
    }
}