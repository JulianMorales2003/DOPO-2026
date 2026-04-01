package tower;

/**
 * Clase con la copa de tipo opener esta elimina todas las tapas que le impiden el paso al entrar.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4)
 */
public class OpenerCup extends Cup {

    /**
     * Crea una copa opener.
     * 
     * @param number Número de la copa
     */
    public OpenerCup(int number) {
        super(number);
        this.tone = "cyan"; 
    }

    /**
     * Comportamiento al entrar: elimina todas las tapas en su camino.
     * 
     * @param tower Torre en la que entra
     * @param position Posición donde se coloca
     * @return siempre true
     */
    @Override
    public boolean onEnter(Object tower, int position) {
        return true;
    }

    /**
     * Comportamiento al salir: puede salir sin restricciones.
     * 
     * @param tower Torre de la que sale
     * @return siempre true
     */
    @Override
    public boolean canExit(Object tower) {
        return true;
    }

    /**
     * Obtiene el tipo de copa.
     * 
     * @return "opener"
     */
    @Override
    public String getType() {
        return "opener";
    }
}