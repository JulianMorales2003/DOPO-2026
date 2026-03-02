/**
 * Representa la tapa que está relacionada con la copa.
 * Dibuja una línea delgada que se acopla al borde superior interno de su copa.
 * Si no tiene copa asociada, se dibuja de forma independiente.
 * 
 * @author Julian Morales - Sergio Buitrago
 */
public class Lid {
    private int number;
    private String color;
    private Cup associatedCup;
    private Rectangle rect;

    /** Posición inicial del Rectangle según su constructor. */
    private static final int RECT_INIT_X = 70;
    private static final int RECT_INIT_Y = 15;

    /** Posición real actual del rect en el canvas (trackeada manualmente). */
    private int rectX = RECT_INIT_X;
    private int rectY = RECT_INIT_Y;

    /**
     * Crea una tapa con el número dado.
     * @param number número identificador de la tapa, debe ser mayor a 0.
     */
    public Lid(int number) {
        if (number <= 0) throw new IllegalArgumentException("Lid number must be positive");
        this.number = number;
        this.color  = generateColor(number);
        this.associatedCup = null;
        this.rect = new Rectangle();
        this.rectX = RECT_INIT_X;
        this.rectY = RECT_INIT_Y;
    }

    /**
     * Genera un color basado en el número de la tapa.
     * @param number número de la tapa.
     * @return color asignado.
     */
    private String generateColor(int number) {
        String[] colors = {"red", "blue", "green", "yellow", "magenta", "black"};
        return colors[(number - 1) % colors.length];
    }

    /**
     * Asocia esta tapa a una copa y sincroniza su color y número con ella.
     * @param cup la copa a la que se asocia la tapa.
     */
    public void attachTo(Cup cup) {
        this.associatedCup = cup;
        if (cup != null) {
            this.color  = cup.getColor();
            this.number = cup.getNumber();
        }
        snapToCup();
    }

    /**
     * Acopla visualmente la tapa al borde superior de su copa.
     * Solo dibuja si la copa ya fue renderizada (cupW y cupH > 0).
     * Si la copa aún no fue dibujada, no hace nada (se dibujará cuando Tower llame redraw).
     */
    public void snapToCup() {
        if (associatedCup == null) return;

        int cupX = associatedCup.getLastX();
        int cupY = associatedCup.getLastY();
        int cupW = associatedCup.getLastW();
        int cupH = associatedCup.getLastH();

        if (cupW <= 0 || cupH <= 0) return;

        int targetW  = cupW;
        int lidThick = Math.max(3, cupH / 5);
        int targetX  = cupX;
        int targetY  = cupY;

        rect.changeColor(associatedCup.getColor());
        rect.changeSize(lidThick, targetW);
        rect.moveHorizontal(targetX - rectX);
        rect.moveVertical(targetY - rectY);

        rectX = targetX;
        rectY = targetY;

        rect.makeVisible();
    }

    /**
     * Hace visible la tapa. Si tiene copa, se acopla a ella.
     * Si no tiene copa, se dibuja de forma independiente.
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
     * Usa el número de la tapa para determinar su ancho.
     */
    private void drawStandalone() {
        int widthPx  = number * 10;
        int heightPx = 3;
        rect.changeColor(color);
        rect.changeSize(heightPx, widthPx);
        rect.makeVisible();
    }

    /**
     * Hace invisible la tapa en el canvas.
     * No modifica la posición trackeada del rect.
     */
    public void makeInvisible() {
        rect.makeInvisible();
    }

    // ---- Getters ----

    /** @return número identificador de la tapa. */
    public int getNumber()        { return number; }

    /** @return altura de la tapa en cm (siempre 1, para compatibilidad con Tower). */
    public int getHeight()        { return 1; }

    /** @return altura de la tapa en cm. */
    public int getHeightCm()      { return 1; }

    /** @return color de la tapa; si tiene copa, devuelve el color de la copa. */
    public String getColor()      { return (associatedCup != null) ? associatedCup.getColor() : color; }

    /** @return la copa a la que está asociada, o null si no tiene. */
    public Cup getAssociatedCup() { return associatedCup; }

    /** @return true si la tapa está asociada a una copa. */
    public boolean isOnCup()      { return associatedCup != null; }

    public String toString() {
        return "Tapa #" + number + (associatedCup != null
            ? " (sobre copa #" + associatedCup.getNumber() + ")"
            : " (sin copa)");
    }
}