/**
 * Clase con el objetivo de crear la copa para x base
 *
 * @author
 *   Julian Morales - Sergio Buitrago 
 */
public class Cup {

    private int cupId;                 
    private int cmHeight;              
    private String tone;               
    private Lid pairedCover;           

    private Rectangle slabRect;        
    private Rectangle wallLeftRect;    
    private Rectangle wallRightRect;  

    private static final int ANCHOR_X = 70;  
    private static final int ANCHOR_Y = 15;  

    private Integer lastX = null;      
    private Integer lastY = null;
    private Integer lastW = null;      
    private Integer lastH = null;

    /**
     * Crea una copa con un tamaño específico.
     * @param number El número de la copa debe ser > 0
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
     */
    private String generateColor(int n) {
        String[] palette = {"red", "blue", "green", "yellow", "magenta", "black"};
        return palette[(n - 1) % palette.length];
    }

    
    /**
     * Hace visible la copa en una posición y tamaño específicos (px).
     * Dibuja la copa con forma de "U" usando 3 rectángulos.
     *
     * @param x Posición X en píxeles (esquina superior izquierda)
     * @param y Posición Y en píxeles (borde superior)
     * @param w Ancho en píxeles
     * @param h Alto total en píxeles
     */
    public void makeVisibleAt(int x, int y, int w, int h) {
        
        int pxPerCm = (cmHeight > 0) ? Math.max(1, Math.round((float) h / cmHeight)) : 1;
        int baseThicknessPx = Math.max(1, pxPerCm);
        int wallsHeightPx   = Math.max(0, h - baseThicknessPx);
        int wallWidthPx     = Math.max(1, baseThicknessPx);

        int baseX = x;
        int baseY = y + h - baseThicknessPx;

        int leftX = x;
        int leftY = y;

        int rightX = x + w - wallWidthPx;
        int rightY = y;

        slabRect.changeSize(baseThicknessPx, w);
        slabRect.changeColor(tone);

        wallLeftRect.changeSize(wallsHeightPx, wallWidthPx);
        wallLeftRect.changeColor(tone);

        wallRightRect.changeSize(wallsHeightPx, wallWidthPx);
        wallRightRect.changeColor(tone);

        if (lastX == null) {
            
            slabRect.moveHorizontal(baseX - ANCHOR_X);
            slabRect.moveVertical(baseY - ANCHOR_Y);

            if (wallsHeightPx > 0) {
                wallLeftRect.moveHorizontal(leftX - ANCHOR_X);
                wallLeftRect.moveVertical(leftY - ANCHOR_Y);

                wallRightRect.moveHorizontal(rightX - ANCHOR_X);
                wallRightRect.moveVertical(rightY - ANCHOR_Y);
            } else {
                wallLeftRect.makeInvisible();
                wallRightRect.makeInvisible();
            }
        } else {
            
            int prevPxPerCm = (cmHeight > 0) ? Math.max(1, Math.round((float) lastH / cmHeight)) : 1;
            int prevBaseTh  = Math.max(1, prevPxPerCm);
            int prevWallsH  = Math.max(0, lastH - prevBaseTh);
            int prevWallW   = Math.max(1, prevBaseTh);

            int prevBaseX = lastX;
            int prevBaseY = lastY + lastH - prevBaseTh;

            int prevLeftX = lastX;
            int prevLeftY = lastY;

            int prevRightX = lastX + lastW - prevWallW;
            int prevRightY = lastY;

            slabRect.moveHorizontal(baseX - prevBaseX);
            slabRect.moveVertical(baseY - prevBaseY);

            if (wallsHeightPx > 0) {
                wallLeftRect.moveHorizontal(leftX - prevLeftX);
                wallLeftRect.moveVertical(leftY - prevLeftY);

                wallRightRect.moveHorizontal(rightX - prevRightX);
                wallRightRect.moveVertical(rightY - prevRightY);
            } else {
                wallLeftRect.makeInvisible();
                wallRightRect.makeInvisible();
            }
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
    }

    /**
     * Hace invisible la copa ocultando todos sus componentes.
     */
    public void makeInvisible() {
        slabRect.makeInvisible();
        wallLeftRect.makeInvisible();
        wallRightRect.makeInvisible();
    }

    public void setLid(Lid lid) { this.pairedCover = lid; }
    public Lid getLid() { return pairedCover; }
    public boolean hasLid() { return pairedCover != null; }
    
    public int getNumber() { return cupId; }
    public int getHeight() { return cmHeight; }
    public int getHeightCm() { return cmHeight; }
    public int getInnerHeight() { return cmHeight - 1; }
    public String getColor() { return tone; }

    public int getWidth() { return 40 + (cupId * 5); }

    @Override
    public String toString() {
        String lidInfo = hasLid() ? " con tapa" : " sin tapa";
        return "Copa #" + cupId + " (altura: " + cmHeight + " cm)" + lidInfo;
    }
}