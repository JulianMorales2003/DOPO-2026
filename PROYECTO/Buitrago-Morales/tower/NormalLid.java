package tower;

/**
 * Tapa Normal - comportamiento estándar.
 * Es la tapa que ya teníamos en los ciclos anteriores.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4)
 */
public class NormalLid extends Lid {

    public NormalLid(int number) {
        super(number);
    }

    @Override
    public boolean canEnter(Object tower) {
        return true;
    }

    @Override
    public boolean canExit(Object tower) {
        return true;
    }

    @Override
    public String getType() {
        return "normal";
    }
}