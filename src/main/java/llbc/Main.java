package llbc;

import llbc.core.DharBurning;
import llbc.core.NeutralElement;
import llbc.core.SandpileMatrix;
import llbc.io.MatrixReader;
import llbc.math.LaplacianMatrix;
import llbc.security.ResilienceAnalyser;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

/**
 * Command-line entry point for the Sandpile Load Balancer.
 *
 * <pre>
 *   (no arguments) | demo      guided demo of the model on a 3x3 server grid
 *   stabilise FILE.csv        stabilise a load matrix read from a CSV file
 *   resilience N              resilience report for an N x N grid (2 &lt;= N &lt;= 20)
 *   help                      usage
 * </pre>
 *
 * <p>The original full-featured 10-function CLI (heatmaps, inverse search, eigen-analysis
 * by flag) only exists as a pre-compiled JAR in {@code releases/final-release_1.0.0/};
 * its source is not part of this repository.</p>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public class Main {

    private static final int MAX_RESILIENCE_DIMENSION = 20;

    public static void main(String[] args) {
        System.exit(run(args, System.out, System.err));
    }

    /** Runs the CLI and returns the process exit code (0 = success). */
    static int run(String[] args, PrintStream out, PrintStream err) {
        String command = args.length == 0 ? "demo" : args[0];
        try {
            switch (command) {
                case "demo":
                    demo(out);
                    return 0;
                case "stabilise":
                case "stabilize":
                    if (args.length != 2) {
                        err.println("Usage: stabilise FILE.csv");
                        return 2;
                    }
                    stabilise(Path.of(args[1]), out);
                    return 0;
                case "resilience":
                    if (args.length != 2) {
                        err.println("Usage: resilience N");
                        return 2;
                    }
                    resilience(Integer.parseInt(args[1]));
                    return 0;
                case "help":
                case "-h":
                case "--help":
                    usage(out);
                    return 0;
                default:
                    err.println("Unknown command: " + command);
                    usage(err);
                    return 2;
            }
        } catch (IOException e) {
            err.println("Cannot read file: " + e.getMessage());
            return 1;
        } catch (IllegalArgumentException e) {
            err.println("Error: " + e.getMessage());
            return 1;
        }
    }

    private static void usage(PrintStream out) {
        out.println("Sandpile Load Balancer v2.0.0");
        out.println();
        out.println("Usage: java -jar sandpile-load-balancer-2.0.0.jar [command]");
        out.println("  demo              guided demo on a 3x3 server grid (default)");
        out.println("  stabilise FILE    stabilise the load matrix in a CSV file (e.g. input/matrix5.csv)");
        out.println("  resilience N      resilience report for an N x N grid (2 <= N <= "
            + MAX_RESILIENCE_DIMENSION + ")");
        out.println("  help              show this message");
        out.println();
        out.println("Full original CLI (heatmaps, inverse search, eigenvectors):");
        out.println("  cd releases/final-release_1.0.0 && java -jar main.jar");
    }

    private static void stabilise(Path file, PrintStream out) throws IOException {
        SandpileMatrix load = MatrixReader.read(file);
        int before = total(load);
        out.println("Initial load (" + load.dimension() + "x" + load.dimension()
            + ", " + before + " tasks):");
        out.print(load);
        int sweeps = load.countStabilisationSteps();
        SandpileMatrix stable = load.copy().stabilise();
        int after = total(stable);
        out.println("Stabilised after " + sweeps + " sweep(s); "
            + (before - after) + " tasks left through the boundary (sink):");
        out.print(stable);
        out.println("Recurrent (Dhar's burning algorithm): " + DharBurning.isRecurrent(stable));
    }

    private static void resilience(int n) {
        if (n < 2 || n > MAX_RESILIENCE_DIMENSION) {
            throw new IllegalArgumentException(
                "N must be between 2 and " + MAX_RESILIENCE_DIMENSION
                + " (the Laplacian is dense, n^2 x n^2)");
        }
        new ResilienceAnalyser(n).printReport();
    }

    private static void demo(PrintStream out) {
        out.println("=== Sandpile Load Balancer v2.0.0 - demo ===");
        out.println();
        out.println("1) A 3x3 grid of servers. A server holding >= 4 tasks is overloaded and");
        out.println("   sheds 1 task to each neighbour; tasks pushed off the edge leave (sink).");
        SandpileMatrix load = new SandpileMatrix(new int[][]{{1, 5, 2}, {4, 4, 2}, {6, 1, 0}});
        out.println("Initial load (" + total(load) + " tasks):");
        out.print(load);
        SandpileMatrix stable = load.copy().stabilise();
        out.println("Balanced after " + load.countStabilisationSteps() + " toppling sweep(s) ("
            + (total(load) - total(stable)) + " tasks left through the boundary):");
        out.print(stable);
        out.println();

        out.println("2) Is that balanced state self-healing (recurrent)?");
        out.println("Dhar's burning algorithm says: " + DharBurning.isRecurrent(stable));
        out.println();

        out.println("3) Neutral element of the 3x3 sandpile group (exhaustive check):");
        SandpileMatrix neutral = new SandpileMatrix(new int[][]{{2, 1, 2}, {1, 0, 1}, {2, 1, 2}});
        out.print(neutral);
        out.println("isNeutralElement = " + NeutralElement.isNeutralElement(neutral));
        out.println();

        out.println("4) Number of recurrent 3x3 configurations, two independent ways:");
        out.println("   brute force over all 4^9 = 262144 stable grids : " + DharBurning.countRecurrent(3));
        out.println("   determinant of the reduced Laplacian            : "
            + Math.round(new LaplacianMatrix(3).determinant()));
        new ResilienceAnalyser(3).printReport();
        out.println("Try: stabilise input/matrix5.csv | resilience 5 | help");
    }

    private static int total(SandpileMatrix m) {
        int sum = 0;
        for (int[] row : m.toArray()) {
            for (int cell : row) sum += cell;
        }
        return sum;
    }
}
