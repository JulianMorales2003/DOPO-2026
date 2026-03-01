/**
 * Representa la tapa que está relacionada con la copa.
 * 
 * @author Julian Morales - Sergio Buitrago
 */

/**
 * Representa la tapa como línea delgada que se acopla al borde interno superior de su copa.
 */
public class Lid {
    private int number;
    private String color;
    private Cup associatedCup;

    private Rectangle rect;
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;

    public Lid(int number) {
        if (number <= 0) throw new IllegalArgumentException("Lid number must be positive");
        this.number = number;
        this.color = generateColor(number); // fallback si aún no hay copa
        this.associatedCup = null;
        this.rect = new Rectangle();
    }

    private String generateColor(int number) {
        String[] colors = {"red", "blue", "green", "yellow", "magenta", "black"};
        return colors[(number - 1) % colors.length];
    }

    /** Asocia la tapa a una copa (sin pasar posiciones). */
    public void attachTo(Cup cup) {
        this.associatedCup = cup;
        if (cup != null) this.color = cup.getColor(); // sincroniza color
        // Si ya está dibujada la copa, nos acoplamos de una
        snapToCup();
    }

    /** Acopla la tapa a la geometría actual de la copa (si existe y fue dibujada). */
    public void snapToCup() {
        if (associatedCup == null) return;
        // La copa debe proveer su última geometría en pantalla:
        int cupX = associatedCup.getLastX();      // top‑left del rectángulo de la COPA
        int cupY = associatedCup.getLastY();
        int cupW = associatedCup.getLastW();
        int cupH = associatedCup.getLastH();
        if (cupW <= 0 || cupH <= 0) return;       // aún no ha sido dibujada

        // px/cm derivados de la altura real de la copa dibujada
        int unitPx = Math.max(1, cupH / Math.max(1, associatedCup.getHeight()));

        // Ancho exterior real de la copa (regla: outer = altura en cm)
        int outerPx = associatedCup.getHeight() * unitPx;

        // La copa se centra en cupW: hay que hallar la X izquierda real de su “U”
        int xCupLeft = cupX + (cupW - outerPx) / 2;

        // Pared visual mínima de la “U”
        int wallPx = 1;

        // Longitud interior = ancho interior de la copa
        int innerPx = Math.max(1, outerPx - 2 * wallPx);

        // Espesor de la tapa: 0.3 cm (>=1 px)
        int lineThPx = Math.max(1, Math.round(0.3f * unitPx));

        // Borde interno superior de la copa: Y del top de la copa
        int finalX = xCupLeft + wallPx;
        int finalY = cupY; // justo el límite superior interno
        int finalW = innerPx;

        rect.changeColor(associatedCup.getColor());
        rect.changeSize(lineThPx, finalW);
        rect.moveHorizontal(finalX - RECT_DEFAULT_X);
        rect.moveVertical(finalY - RECT_DEFAULT_Y);
        rect.makeVisible();
    }

    /** Muestra la tapa (se acopla automáticamente). */
    public void makeVisible() {
        snapToCup();
    }

    public void makeInvisible() { rect.makeInvisible(); }

    // ---- Getters básicos / API compatibilidad ----
    public int getNumber() { return number; }
    public int getHeight() { return 1; }      // mantener 1 cm para Tower (no se usa ya para posicionar)
    public int getHeightCm() { return 1; }
    public String getColor() { return (associatedCup != null) ? associatedCup.getColor() : color; }
    public Cup getAssociatedCup() { return associatedCup; }
    public boolean isOnCup() { return associatedCup != null; }

    @Override public String toString() {
        return "Tapa #" + number + (associatedCup != null ? " (sobre copa #" + associatedCup.getNumber() + ")" : " (sin copa)");
    }
}