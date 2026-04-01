package tower;

/**
 *Clase con el tipo de tapa que necesita si o si una copa o no entrara en visualizacion.
 *
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4)
 */
public class FearfulLid extends Lid {

    public FearfulLid(int number) {
        super(number);
        this.color = "gray";
    }

    /**
     * Puede entrar solo si su copa compañera está en la torre.
     * La verificación se hace en Tower.
     * 
     * @param tower Torre en la que intenta entrar
     * @return true si su copa está en la torre, false si no
     */
    @Override
    public boolean canEnter(Object tower) {
        return true; 
    }

    /**
     * NO puede salir si está tapando a su copa.
     * 
     * @param tower Torre de la que intenta salir
     * @return false si está sobre su copa, true si no
     */
    @Override
    public boolean canExit(Object tower) {
        return !isOnCup();
    }

    @Override
    public String getType() {
        return "fearful";
    }
}