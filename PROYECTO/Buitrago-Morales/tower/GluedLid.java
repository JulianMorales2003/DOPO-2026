package tower;

/**
 * Clase con un tipo de tapa que no se puede remover es decir queda pegada permanentemente.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4 - Tipo Propuesto)
 */
public class GluedLid extends Lid {

    public GluedLid(int number) {
        super(number);
        this.color = "brown";
    }

    /**
     * Puede entrar siempre.
     * 
     * @param tower Torre en la que entra
     * @return siempre true
     */
    @Override
    public boolean canEnter(Object tower) {
        return true;
    }

    /**
     * No puede salir si está sobre su copa.
     * Una vez pegada, permanece para siempre.
     * 
     * @param tower Torre de la que intenta salir
     * @return false si está sobre su copa, true si está suelta
     */
    @Override
    public boolean canExit(Object tower) {
        return !isOnCup();
    }

    @Override
    public String getType() {
        return "glued";
    }
}