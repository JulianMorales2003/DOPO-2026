/**
 * Clase con el objetivo de crear la copa para x base
 * 
 * @author Julian Morales - Sergio Buitrago
 */
public class Cup {
    private int number;           
    private int height;           
    private String color;
    private Lid lid;
    
    // Componentes visuales (3 rectángulos para formar una copa)
    private Rectangle base;        // Base de la copa (1 cm)
    private Rectangle leftWall;    // Pared izquierda
    private Rectangle rightWall;   // Pared derecha
    
    // Constantes para posición por defecto de Rectangle
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;
    
    /**
     * Crea una copa con un tamaño especifico
     * @param number El numero de la copa debe ser >0
     */
    public Cup(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Cup number must be positive");
        }
        this.number = number;
        this.height = (int) Math.pow(2, number - 1);
        this.color = generateColor(number);
        this.lid = null;
        
        // Inicializar componentes visuales
        this.base = new Rectangle();
        this.leftWall = new Rectangle();
        this.rightWall = new Rectangle();
    }
    
    /**
     * Genera un color unico para cada copa.
     * 
     * @param number El tamaño de la copa
     * @return Un color como string
     */
    private String generateColor(int number) {
        String[] colors = {"red", "blue", "green", "yellow", "magenta", "black"};
        return colors[(number - 1) % colors.length];
    }
    
    /**
     * Hace visible la copa en una posición y tamaño específicos.
     * Dibuja la copa con forma de "U" usando 3 rectángulos.
     * 
     * @param x Posición X en píxeles
     * @param y Posición Y en píxeles (parte superior)
     * @param w Ancho en píxeles
     * @param h Alto en píxeles
     */
    public void makeVisibleAt(int x, int y, int w, int h) {
        // Calcular dimensiones
        int pixelesPorCm = (height > 0) ? h / height : 1;
        if (pixelesPorCm <= 0) pixelesPorCm = 1;
        
        int alturaBasePx = Math.max(1, pixelesPorCm);  // Base de 1 cm
        int alturaParedesPx = Math.max(0, h - alturaBasePx);
        int yBase = y + h - alturaBasePx;
        
        // Dibujar base (horizontal)
        base.changeSize(alturaBasePx, w);
        base.changeColor(color);
        base.moveHorizontal(-RECT_DEFAULT_X + x);
        base.moveVertical(-RECT_DEFAULT_Y + yBase);
        base.makeVisible();
        
        // Dibujar paredes verticales (solo si hay altura suficiente)
        int anchoParedPx = Math.max(1, alturaBasePx);
        if (alturaParedesPx > 0) {
            // Pared izquierda
            leftWall.changeSize(alturaParedesPx, anchoParedPx);
            leftWall.changeColor(color);
            leftWall.moveHorizontal(-RECT_DEFAULT_X + x);
            leftWall.moveVertical(-RECT_DEFAULT_Y + y);
            leftWall.makeVisible();
            
            // Pared derecha
            rightWall.changeSize(alturaParedesPx, anchoParedPx);
            rightWall.changeColor(color);
            rightWall.moveHorizontal(-RECT_DEFAULT_X + (x + w - anchoParedPx));
            rightWall.moveVertical(-RECT_DEFAULT_Y + y);
            rightWall.makeVisible();
        } else {
            leftWall.makeInvisible();
            rightWall.makeInvisible();
        }
    }
    
    /**
     * Hace invisible la copa ocultando todos sus componentes.
     */
    public void makeInvisible() {
        base.makeInvisible();
        leftWall.makeInvisible();
        rightWall.makeInvisible();
    }
    
    // ===== Métodos de gestión de tapa =====
    
    public void setLid(Lid lid) {
        this.lid = lid;
    }
    
    public Lid getLid() {
        return lid;
    }
    
    public boolean hasLid() {
        return lid != null;
    }
    
    // ===== Métodos getters =====
    
    public int getNumber() {
        return number;
    }
    
    public int getHeight() {
        return height;
    }
    
    /**
     * Retorna la altura en centímetros (para compatibilidad con Tower).
     */
    public int getHeightCm() {
        return height;
    }
    
    public int getInnerHeight() {
        return height - 1;
    }
    
    public String getColor() {
        return color;
    }
    
    public int getWidth() {
        return 40 + (number * 5);
    }
    
    @Override
    public String toString() {
        String lidInfo = hasLid() ? " con tapa" : " sin tapa";
        return "Copa #" + number + " (altura: " + height + " cm)" + lidInfo;
    }
}