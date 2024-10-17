import java.util.Iterator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        // Lê as configurações inicial e de objetivo
        String initialConfig = sc.nextLine().trim();
        String goalConfig = sc.nextLine().trim();
        // Instancia o algoritmo BestFirst e resolve o problema
        BestFirst searchAlgorithm = new BestFirst();
        Iterator<BestFirst.State> solutionPath = searchAlgorithm.solve(new ContainerBoard(initialConfig,true),
                new ContainerBoard(goalConfig,false));
        // Verifica se há solução
        if (solutionPath == null) {
            System.out.println("no solution found");
        } else {
            // Imprime cada estado do caminho até a solução
            while (solutionPath.hasNext()) {
                BestFirst.State currentState = solutionPath.next();
                System.out.println(currentState);
                if (!solutionPath.hasNext()) {
                    System.out.println((int) currentState.getCost());
                }
            }
        }
        sc.close();
    }
}
