import java.util.*;

/**
 * Clase de uso con el objetivo de resolver el problema de la maraton.
 * 
 * @author Julian Morales - Sergio Buitrago
 */
public class TowerContest {
    
    /**
     * Resuelve el problema de la maratón.
     * 
     * @param n Número de copas disponibles (1 a n)
     * @param h Altura objetivo en cm
     * @return String con la solución o "impossible"
     * 
     * Formato de salida:
     * - Si es posible: secuencia de números de copas separados por espacios
     * - Si es imposible: "impossible"
     */
    public static String solve(int n, int h) {
        if (n <= 0 || h <= 0) {
            return "impossible";
        }
        
        int[] cupHeights = new int[n];
        for (int i = 0; i < n; i++) {
            cupHeights[i] = 2 * (i + 1) - 1;
        }
        
        List<Integer> sequence = new ArrayList<>();
        if (findSequence(cupHeights, h, sequence, new boolean[n])) {
            return formatSolution(sequence);
        }
        
        return "impossible";
    }

    
    /**
     * Backtracking que construye la torre de abajo hacia arriba.
     * El orden de colocación SÍ importa.
     */
    private static boolean findSequence(int[] heights, int targetHeight, List<Integer> sequence, boolean[] used) {
        
        int currentHeight = 0;
        for (int cupNum : sequence) {
            currentHeight += heights[cupNum - 1];
        }
        
        if (currentHeight == targetHeight) {
            return true;
        }
        
        if (currentHeight > targetHeight) {
            return false;
        }
        
        for (int i = 0; i < heights.length; i++) {
            if (!used[i]) {
                
                used[i] = true;
                sequence.add(i + 1);  
                
                if (findSequence(heights, targetHeight, sequence, used)) {
                    return true;  
                }
                
                sequence.remove(sequence.size() - 1);
                used[i] = false;
            }
        }
        
        return false;
    }
    
    /**
     * Formatea la solución como string.
     * 
     * @param solution Lista de números de copas
     * @return String con copas separadas por espacios
     */
    private static String formatSolution(List<Integer> solution) {
        if (solution.isEmpty()) {
            return "impossible";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < solution.size(); i++) {
            sb.append(solution.get(i));
            if (i < solution.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    
    /**
     * Simula la solución visualmente usando Tower.
     * 
     * @param n Número de copas disponibles
     * @param h Altura objetivo en cm
     * 
     * Si existe solución, crea una Tower y muestra visualmente las copas apiladas.
     * Si no existe solución, muestra un mensaje indicándolo.
     */
    public static void simulate(int n, int h) {
        String solution = solve(n, h);
        
        if (solution.equals("impossible")) {
            System.out.println("No es posible alcanzar la altura " + h + " cm con " + n + " copas.");
            System.out.println("Solución: impossible");
            return;
        }
        
        System.out.println("Solución encontrada: " + solution);
        System.out.println("Simulando...");
        
        String[] cupNumbers = solution.split(" ");
        
        Tower tower = new Tower(15, h + 10); 
        tower.makeVisible();
        
        for (String cupNumStr : cupNumbers) {
            int cupNum = Integer.parseInt(cupNumStr);
            tower.pushCup(cupNum);
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                
            }
        }
        
        System.out.println("Altura alcanzada: " + tower.height() + " cm");
        System.out.println("Altura objetivo: " + h + " cm");
        
        if (tower.height() == h) {
            System.out.println("✓ ¡Solución correcta!");
        } else {
            System.out.println("✗ Error: La altura no coincide");
        }
    }
    
}