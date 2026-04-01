package tower;

import shapes.Rectangle;


/**
 * Clase abstracta que representa una copa en la torre.
 * Define el comportamiento común de todas las copas y delega 
 * comportamientos específicos a las subclases.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4 - Polimorfismo)
 */
public abstract class Cup {

    protected int cupId;
    protected int cmHeight;
    protected String tone;
    protected Lid pairedCover;

    protected Rectangle slabRect;
    protected Rectangle wallLeftRect;
    protected Rectangle wallRightRect;

    protected static final int ANCHOR_X = 70;
    protected static final int ANCHOR_Y = 15;

    protected Integer lastX = null;
    protected Integer lastY = null;
    protected Integer lastW = null;
    protected Integer lastH = null;
    
    protected int slabCurrentX = ANCHOR_X;
    protected int slabCurrentY = ANCHOR_Y;
    protected int wallLCurrentX = ANCHOR_X;
    protected int wallLCurrentY = ANCHOR_Y;
    protected int wallRCurrentX = ANCHOR_X;
    protected int wallRCurrentY = ANCHOR_Y;

    /**
     * Constructor base para todas las copas.
     * 
     * @param number Número identificador de la copa (debe ser > 0)
     * @throws IllegalArgumentException si number <= 0
     */
    public Cup(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Cup number must be positive");
        }
        this.cupId = number;
        this.cmHeight = 2 * number - 1;
        this.tone = generateColor(number);
        this.pairedCover = null;

        this.slabRect = new Rectangle();
        this.wallLeftRect = new Rectangle();
        this.wallRightRect = new Rectangle();
    }

    /**
     * Genera un color único para cada copa.
     * 
     * @param n Número de la copa
     * @return Color asignado
     */
    protected String generateColor(int n) {
        String[] palette = {"red", "blue", "green", "yellow", "magenta", "black"};
        return palette[(n - 1) % palette.length];
    }

    /**
     * Hace visible la copa en una posición y tamaño específicos.
     * Dibuja la copa con forma de "U" usando 3 rectángulos.
     * 
     * @param x Posición X en píxeles
     * @param y Posición Y en píxeles
     * @param w Ancho en píxeles
     * @param h Alto total en píxeles
     */
    public void makeVisibleAt(int x, int y, int w, int h) {
        int pxPerCm = (cmHeight > 0) ? Math.max(1, Math.round((float) h / cmHeight)) : 1;
        int baseThicknessPx = Math.max(1, pxPerCm);
        int wallsHeightPx = Math.max(0, h - baseThicknessPx);
        int wallWidthPx = Math.max(1, baseThicknessPx);
        int targetSlabX  = x;
        int targetSlabY  = y + h - baseThicknessPx;
        int targetWallX  = x;  
        int targetWallY  = y;
        int targetWRightX = x + w - wallWidthPx;

        slabRect.makeInvisible();
        wallLeftRect.makeInvisible();
        wallRightRect.makeInvisible();

        slabRect.changeSize(baseThicknessPx, w);
        slabRect.changeColor(tone);
        wallLeftRect.changeSize(wallsHeightPx, wallWidthPx);
        wallLeftRect.changeColor(tone);
        wallRightRect.changeSize(wallsHeightPx, wallWidthPx);
        wallRightRect.changeColor(tone);

        slabRect.moveHorizontal(targetSlabX - slabCurrentX);
        slabRect.moveVertical(targetSlabY - slabCurrentY);
        slabCurrentX = targetSlabX;
        slabCurrentY = targetSlabY;

        if (wallsHeightPx > 0) {
            wallLeftRect.moveHorizontal(targetWallX - wallLCurrentX);
            wallLeftRect.moveVertical(targetWallY - wallLCurrentY);
            wallLCurrentX = targetWallX;
            wallLCurrentY = targetWallY;

            wallRightRect.moveHorizontal(targetWRightX - wallRCurrentX);
            wallRightRect.moveVertical(targetWallY - wallRCurrentY);
            wallRCurrentX = targetWRightX;
            wallRCurrentY = targetWallY;
        } else {
            wallLeftRect.makeInvisible();
            wallRightRect.makeInvisible();
        }

        slabRect.makeVisible();
        if (wallsHeightPx > 0) {
            wallLeftRect.makeVisible();
            wallRightRect.makeVisible();
        }

        lastX = x;
        lastY = y;
        lastW = w;
        lastH = h;

        if (hasLid()) {
            getLid().attachTo(this);
            getLid().makeVisible();
        }
    }

    /**
     * Hace invisible la copa ocultando todos sus componentes.
     */
    public void makeInvisible() {
        slabRect.makeInvisible();
        wallLeftRect.makeInvisible();
        wallRightRect.makeInvisible();
    }
    
    /**
     * Resetea la posición recordada de los rectángulos.
     * Debe llamarse cuando la copa es reubicada físicamente.
     */
    public void resetPosition() {
        lastX = null;
        lastY = null;
        lastW = null;
        lastH = null;
        slabRect.makeInvisible();
        wallLeftRect.makeInvisible();
        wallRightRect.makeInvisible();
        this.slabRect = new Rectangle();
        this.wallLeftRect = new Rectangle();
        this.wallRightRect = new Rectangle();
        slabCurrentX = ANCHOR_X;
        slabCurrentY = ANCHOR_Y;
        wallLCurrentX = ANCHOR_X;
        wallLCurrentY = ANCHOR_Y;
        wallRCurrentX = ANCHOR_X;
        wallRCurrentY = ANCHOR_Y;
    }

   
    /**
     * Comportamiento al entrar a la torre.
     * Cada tipo de copa tiene un comportamiento diferente.
     * 
     * @param tower Torre en la que está entrando
     * @param position Posición donde se insertará
     * @return true si la copa puede entrar, false si no
     */
    public abstract boolean onEnter(Object tower, int position);

    /**
     * Comportamiento al salir de la torre.
     * 
     * @param tower Torre de la que está saliendo
     * @return true si la copa puede salir, false si no
     */
    public abstract boolean canExit(Object tower);

    /**
     * Obtiene el tipo de copa como String.
     * 
     * @return Tipo de copa ("normal", "opener", "hierarchical", etc.)
     */
    public abstract String getType();


    public void setLid(Lid lid) {
        this.pairedCover = lid;
    }

    public Lid getLid() {
        return pairedCover;
    }

    public boolean hasLid() {
        return pairedCover != null;
    }

    public int getNumber() {
        return cupId;
    }

    public int getHeight() {
        return cmHeight;
    }

    public int getHeightCm() {
        return cmHeight;
    }

    public int getInnerHeight() {
        return cmHeight - 1;
    }

    public String getColor() {
        return tone;
    }

    public int getWidth() {
        return 40 + (cupId * 5);
    }

    public int getLastX() {
        return (lastX == null) ? 0 : lastX;
    }

    public int getLastY() {
        return (lastY == null) ? 0 : lastY;
    }

    public int getLastW() {
        return (lastW == null) ? 0 : lastW;
    }

    public int getLastH() {
        return (lastH == null) ? 0 : lastH;
    }

    @Override
    public String toString() {
        String lidInfo = hasLid() ? " con tapa" : " sin tapa";
        return "Copa " + getType() + " #" + cupId + " (altura: " + cmHeight + " cm)" + lidInfo;
    }
}