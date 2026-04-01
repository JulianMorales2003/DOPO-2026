package tower;

/**
 * Copa normal - comportamiento estándar.
 * Es la copa que ya teníamos en los ciclos anteriores.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4)
 */
public class NormalCup extends Cup {

    /**
     * Crea una copa normal.
     * 
     * @param number Número de la copa
     */
    public NormalCup(int number) {
        super(number);
    }

    /**
     * Comportamiento al entrar: simplemente entra sin restricciones.
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
     * @return "normal"
     */
    @Override
    public String getType() {
        return "normal";
    }
}