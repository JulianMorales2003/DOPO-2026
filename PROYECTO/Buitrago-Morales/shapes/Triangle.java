package shapes;

import java.awt.*;

/**
 * Un triángulo que puede ser manipulado y que se dibuja a sí mismo en un canvas.
 * 
 * @author Michael Kolling and David J. Barnes
 * @author Julian Morales - Sergio Buitrago
 * @version 2.0 (Refactorizado con herencia de Shape)
 */
public class Triangle extends Shape {
    
    public static int VERTICES = 3;
    
    private int height;
    private int width;

    public Triangle() {
        super(140, 15, "green");
        this.height = 30;
        this.width = 40;
    }

    public void changeSize(int newHeight, int newWidth) {
        erase();
        this.height = newHeight;
        this.width = newWidth;
        draw();
    }
    
    @Override
    public void changeSize(int... params) {
        if (params.length >= 2) {
            changeSize(params[0], params[1]);
        }
    }

    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width/2), xPosition - (width/2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
}
