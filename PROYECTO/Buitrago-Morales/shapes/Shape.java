package shapes;
 

import java.awt.*;

/**
 * Clase abstracta que representa una figura geométrica que puede dibujarse en un canvas.
 * Proporciona la funcionalidad común para todas las figuras: visibilidad, movimiento, color.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 2.0 (Ciclo 4 - Refactoring con Herencia)
 */
public abstract class Shape {
    
    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;
    
    public Shape() {
        this.xPosition = 0;
        this.yPosition = 0;
        this.color = "black";
        this.isVisible = false;
    }
    
    public Shape(int x, int y, String color) {
        this.xPosition = x;
        this.yPosition = y;
        this.color = color;
        this.isVisible = false;
    }
    
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    
    public void makeInvisible() {
        erase();
        isVisible = false;
    }
    
    public void moveRight() {
        moveHorizontal(20);
    }
    
    public void moveLeft() {
        moveHorizontal(-20);
    }
    
    public void moveUp() {
        moveVertical(-20);
    }
    
    public void moveDown() {
        moveVertical(20);
    }
    
    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }
    
    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }
    
    public void slowMoveHorizontal(int distance) {
        int delta = (distance < 0) ? -1 : 1;
        distance = Math.abs(distance);
        
        for (int i = 0; i < distance; i++) {
            xPosition += delta;
            draw();
        }
    }
    
    public void slowMoveVertical(int distance) {
        int delta = (distance < 0) ? -1 : 1;
        distance = Math.abs(distance);
        
        for (int i = 0; i < distance; i++) {
            yPosition += delta;
            draw();
        }
    }
    
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }
    
    protected abstract void draw();
    
    protected void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
    
    public abstract void changeSize(int... params);
    
    public int getXPosition() {
        return xPosition;
    }
    
    public int getYPosition() {
        return yPosition;
    }
    
    public String getColor() {
        return color;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
}
