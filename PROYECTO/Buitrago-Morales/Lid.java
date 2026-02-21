/**
 * Representa la tapa que está relacionada con la copa.
 * 
 * @author Julian Morales - Sergio Buitrago
 */
public class Lid {
    private int number;                    
    private static final int HEIGHT = 1;   
    private String color;                  
    private Cup associatedCup;
    
    private Rectangle rect;
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;
    
    /**
     * @param number El número de la tapa (debe ser > 0)
     */
    public Lid(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Lid number must be positive");
        }
        this.number = number;
        this.color = generateColor(number);
        this.associatedCup = null;
        this.rect = new Rectangle();
    }
    
    /**
     * Genera un color que coincide con la copa del mismo número.
     * 
     * @param number El número de la tapa
     * @return Un color como string
     */
    private String generateColor(int number) {
        String[] colors = {"red", "blue", "green", "yellow", "magenta", "black"};
        return colors[(number - 1) % colors.length];
    }
    
    /**
     * Hace visible la tapa en una posición y tamaño específicos.
     * La tapa se dibuja como un rectángulo sólido plano.
     * 
     * @param x Posición X en píxeles
     * @param y Posición Y en píxeles (parte superior)
     * @param w Ancho en píxeles
     * @param h Alto en píxeles
     */
    public void makeVisibleAt(int x, int y, int w, int h) {
        rect.changeSize(h, w);
        rect.changeColor(color);
        rect.moveHorizontal(-RECT_DEFAULT_X + x);
        rect.moveVertical(-RECT_DEFAULT_Y + y);
        rect.makeVisible();
    }
    
    /**
     * Hace invisible la tapa ocultando su componente visual.
     */
    public void makeInvisible() {
        rect.makeInvisible();
    }
    
    /**
     * Asigna la copa a la que pertenece esta tapa.
     * 
     * @param cup La copa a asociar
     */
    public void setAssociatedCup(Cup cup) {
        this.associatedCup = cup;
    }
    
    /**
     * Obtiene la copa asociada con esta tapa.
     * 
     * @return La copa asociada, o null si no está asociada
     */
    public Cup getAssociatedCup() {
        return associatedCup;
    }
    
    /**
     * Verifica si esta tapa está sobre una copa.
     * 
     * @return true si está sobre una copa, false en caso contrario
     */
    public boolean isOnCup() {
        return associatedCup != null;
    }
    
    /**
     * Obtiene el número de la tapa.
     * 
     * @return El número de la tapa
     */
    public int getNumber() {
        return number;
    }
    
    /**
     * Obtiene la altura de la tapa.
     * 
     * @return La altura en cm
     */
    public int getHeight() {
        return HEIGHT;
    }
    
    /**
     * Retorna la altura en centímetros.
     * 
     * @return La altura en cm 
     */
    public int getHeightCm() {
        return HEIGHT;
    }
    
    /**
     * Obtiene el color de la tapa.
     * 
     * @return El color como string
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Obtiene el ancho para visualización.
     * Coincide con el ancho de la copa correspondiente.
     * 
     * @return El ancho en píxeles
     */
    public int getWidth() {
        return 40 + (number * 5);
    }
    
    /**
     * Representación en texto de la tapa.
     * 
     * @return Información de la tapa
     */
    @Override
    public String toString() {
        String cupInfo = isOnCup() ? 
            " (sobre copa #" + associatedCup.getNumber() + ")" : " (sin copa)";
        return "Tapa #" + number + cupInfo;
    }
}