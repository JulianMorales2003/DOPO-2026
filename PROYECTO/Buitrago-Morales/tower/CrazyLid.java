package tower;

/**
 * Clase con el tipo de tapa que en vez de tapar la copa se coloca de base.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4)
 */
public class CrazyLid extends Lid {

    public CrazyLid(int number) {
        super(number);
        this.color = "pink";
    }

    /**
     * Siempre retorna "pink" sin importar el número.
     * Garantiza que el color sea rosado desde la construcción.
     */
    @Override
    protected String generateColor(int number) {
        return "pink";
    }

    /**
     * Puede entrar siempre.
     * Tower debe colocarla en la posición 0 (base).
     * 
     * @param tower Torre en la que entra
     * @return siempre true
     */
    @Override
    public boolean canEnter(Object tower) {
        return true;
    }

    /**
     * Puede salir siempre.
     * 
     * @param tower Torre de la que sale
     * @return siempre true
     */
    @Override
    public boolean canExit(Object tower) {
        return true;
    }

    @Override
    public String getType() {
        return "crazy";
    }

    /**
     * Dibuja la CrazyLid en la base de la torre con coordenadas exactas.
     *
     * @param x    Posición X en píxeles
     * @param y    Posición Y en píxeles (borde superior del rectángulo)
     * @param wPx  Ancho en píxeles
     * @param hPx  Alto en píxeles
     */
    public void drawAtBase(int x, int y, int wPx, int hPx) {
        rect.makeInvisible();
        rect.changeColor(color);
        rect.changeSize(hPx, wPx);
        rect.moveHorizontal(x - rectX);
        rect.moveVertical(y - rectY);
        rectX = x;
        rectY = y;
        rect.makeVisible();
    }

    /**
     * Para que no se asocie a la copa.
     */
    @Override
    public void makeVisible() {
        drawStandalone();
    }
}