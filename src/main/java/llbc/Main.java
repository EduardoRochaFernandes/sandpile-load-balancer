package llbc;

/**
 * Entry point for the Sandpile Load Balancer application.
 *
 * <p>Routes to interactive or non-interactive mode based on whether
 * command-line arguments are present.</p>
 *
 * <p>For full usage, run the pre-compiled JAR in {@code releases/final-release_1.0.0/}
 * which contains the original complete CLI implementation.</p>
 *
 * <p>The source code in {@code src/} represents the refactored, OO-clean, and
 * fully-tested domain model. A complete CLI re-implementation is planned for v3.0.</p>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Sandpile Load Balancer v2.0.0");
        System.out.println("For the full interactive application, use:");
        System.out.println("  cd releases/final-release_1.0.0 && java -jar main.jar");
        System.out.println();
        System.out.println("To run the test suite:");
        System.out.println("  mvn test");
    }
}
