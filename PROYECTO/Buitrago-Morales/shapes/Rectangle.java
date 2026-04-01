package shapes;

import java.awt.*;

/**
 * Un rectángulo que puede ser manipulado y que se dibuja a sí mismo en un canvas.
 * 
 * @author Michael Kolling and David J. Barnes (Modified)
 * @author Julian Morales - Sergio Buitrago
 * @version 2.0 (Refactorizado con herencia de Shape)
 */
public class Rectangle extends Shape {

    public static int EDGES = 4;
    
    private int height;
    private int width;

    /**
     * Crea un nuevo rectángulo en posición por defecto con color por defecto.
     */
    public Rectangle() {
        super(70, 15, "magenta");
        this.height = 30;
        this.width = 40;
    }
    
    /**
     * Cambia el tamaño al nuevo tamaño especificado.
     * 
     * @param newHeight La nueva altura en píxeles. Debe ser >= 0.
     * @param newWidth El nuevo ancho en píxeles. Debe ser >= 0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        this.height = newHeight;
        this.width = newWidth;
        draw();
    }
    
    /**
     * Cambia el tamaño usando varargs (para compatibilidad con Shape).
     * 
     * @param params params[0] = altura, params[1] = ancho
     */
    @Override
    public void changeSize(int... params) {
        if (params.length >= 2) {
            changeSize(params[0], params[1]);
        }
    }
    
    /**
     * Dibuja el rectángulo con las especificaciones actuales en la pantalla.
     */
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, 
                new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }
    
    /**
     * Obtiene la altura del rectángulo.
     * 
     * @return Altura en píxeles
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Obtiene el ancho del rectángulo.
     * 
     * @return Ancho en píxeles
     */
    public int getWidth() {
        return width;
    }
}
