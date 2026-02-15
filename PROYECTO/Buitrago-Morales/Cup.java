/**
 * Clase con el objetivo de crear la copa para x base
 * 
 * @author Julian Morales - Sergio Buitrago
 */
public class Cup {
    private int number;           
    private int height;           
    private String color;
    
    
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
    }
    
    /**
     * Genera un color unico para cada copa.
     * 
     * @param number El tamaño de la copa
     * @return A color string
     */
    private String generateColor(int number) {
        String[] colors = {"red", "blue", "green", "yellow", "magenta", "black"};
        return colors[(number - 1) % colors.length];
    }

    
    public void setLid(Lid lid) {
        this.lid = lid;
    }
    
    
    public Lid getLid() {
        return lid;
    }
    
    
    public boolean hasLid() {
        return lid != null;
    }
    
    
    public int getNumber() {
        return number;
    }
    
    /**
     * Pide la altura de la taza
     * 
     * @return Devuelce la altura en cm
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Este es el espacio que quedara dentro de la copa
     * 
     * @return La altura del espacio dentro
     */
    public int getInnerHeight() {
        return height - 1;
    }
    
    
    public String getColor() {
        return color;
    }
    
    
    public int getWidth() {
        return 40 + (number * 5);
    }
    
    
    /**
     * Returns a string representation of the cup.
     * 
     * @return String with cup information
     */
    @Override
    public String toString() {
        String lidInfo = hasLid() ? " with lid" : " without lid";
        return "Cup #" + number + " (height: " + height + " cm)" + lidInfo;
    }
}
