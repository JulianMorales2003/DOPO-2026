package tower;

/**
 * Clase con la copa de tipo Hierarchical que desplaza los objetos en este copas que este abajo si son mas chiquitas que la que se va a agregar y esta luego no se deja quitar.
 * 
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4)
 */
public class HierarchicalCup extends Cup {

    private boolean atBottom; 

    /**
     * Crea una copa hierarchical.
     * 
     * @param number Número de la copa
     */
    public HierarchicalCup(int number) {
        super(number);
        this.atBottom = false;
        this.tone = "orange";
    }

    /**
     * Comportamiento al entrar: desplaza objetos menores.
     * 
     * @param tower Torre en la que entra
     * @param position Posición donde finalmente se ubica
     * @return siempre true
     */
    @Override
    public boolean onEnter(Object tower, int position) {
        this.atBottom = (position == 0);
        return true;
    }

    /**
     * Comportamiento al salir: NO puede salir si está en el fondo.
     * 
     * @param tower Torre de la que intenta salir
     * @return false si está en el fondo, true en caso contrario
     */
    @Override
    public boolean canExit(Object tower) {
        return !atBottom;
    }

    /**
     * Obtiene el tipo de copa.
     * 
     * @return "hierarchical"
     */
    @Override
    public String getType() {
        return "hierarchical";
    }

    /**
     * Marca si la copa está en el fondo.
     * Este método es llamado por Tower cuando la copa cambia de posición.
     * 
     * @param isAtBottom true si está en el fondo, false si no
     */
    public void setAtBottom(boolean isAtBottom) {
        this.atBottom = isAtBottom;
    }

    /**
     * Verifica si la copa está en el fondo.
     * 
     * @return true si está en el fondo, false si no
     */
    public boolean isAtBottom() {
        return atBottom;
    }
}