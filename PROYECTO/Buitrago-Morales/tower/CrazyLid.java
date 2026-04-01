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
     * Override de makeVisible para CrazyLid.
     * Se dibuja de forma especial en la base.
     */
    @Override
    public void makeVisible() {
        
        drawStandalone();
    }
}