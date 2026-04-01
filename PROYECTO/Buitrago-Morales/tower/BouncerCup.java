package tower;

import java.util.Random;

/**
 * Clase con el tipo de copa propuesta por nosotros en el que si la copa ya existe en la torre, esta accion va a rebotar y rechazarla y no va a aceptar.
 *
 * @author Julian Morales - Sergio Buitrago
 * @version 3.0 (Ciclo 4 - Tipo Propuesto)
 */
public class BouncerCup extends Cup {

    private static final String[] BOUNCER_COLORS = {
        "red", "blue", "green", "yellow", "magenta",
        "cyan", "orange", "pink", "violet", "lime"
    };

    /**
     * Crea una copa bouncer con número identificador dado.
     * El color se asigna aleatoriamente de una paleta de colores vivos.
     *
     * @param number Número identificador de la copa (debe ser > 0)
     * @throws IllegalArgumentException si number <= 0 (validado en Cup)
     */
    public BouncerCup(int number) {
        super(number);
        this.tone = randomColor();
    }

    /**
     * Elige un color aleatorio de la paleta BOUNCER_COLORS.
     *
     * @return color aleatorio como String
     */
    private String randomColor() {
        Random rand = new Random();
        return BOUNCER_COLORS[rand.nextInt(BOUNCER_COLORS.length)];
    }

    /**
     * Comportamiento al entrar a la torre.
     *
     * @param tower    Torre en la que intenta entrar
     * @param position Posición donde se colocaría dentro de la torre
     * @return siempre true (la verificación de duplicado la hace Tower)
     */
    @Override
    public boolean onEnter(Object tower, int position) {
    
        return true;
    }

    /**
     * La BouncerCup puede salir de la torre sin restricciones.
     *
     * @param tower Torre de la que intenta salir
     * @return siempre true
     */
    @Override
    public boolean canExit(Object tower) {
        return true;
    }

    /**
     * Retorna el identificador de tipo de esta copa.
     *
     * @return "bouncer"
     */
    @Override
    public String getType() {
        return "bouncer";
    }

    /**
     * Representación en texto de la BouncerCup.
     *
     * @return descripción de la copa
     */
    @Override
    public String toString() {
        String lidInfo = hasLid() ? " con tapa" : " sin tapa";
        return "Copa bouncer #" + cupId + " (altura: " + cmHeight + " cm, color: " + tone + ")" + lidInfo;
    }
}