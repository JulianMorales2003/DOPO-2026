package shapes;
 

import java.awt.*;
import java.awt.geom.*;

/**
 * Un círculo que puede ser manipulado y que se dibuja a sí mismo en un canvas.
 * 
 * @author Michael Kolling and David J. Barnes
 * @author Julian Morales - Sergio Buitrago
 * @version 2.0 (Refactorizado con herencia de Shape)
 */
public class Circle extends Shape {

    public static final double PI = 3.1416;
    
    private int diameter;

    public Circle() {
        super(20, 15, "blue");
        this.diameter = 30;
    }
    
    public void changeSize(int newDiameter) {
        erase();
        this.diameter = newDiameter;
        draw();
    }
    
    @Override
    public void changeSize(int... params) {
        if (params.length >= 1) {
            changeSize(params[0]);
        }
    }
    
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, 
                new Ellipse2D.Double(xPosition, yPosition, diameter, diameter));
            canvas.wait(10);
        }
    }
    
    public int getDiameter() {
        return diameter;
    }
}
