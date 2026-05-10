package llbc.core;

/**
 * Central configuration constants for the sandpile model.
 *
 * <p>Extracting constants into a dedicated class prevents magic numbers from
 * scattering across the codebase and makes the model's parameters explicit
 * and easy to adjust.</p>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public final class SandpileConfig {

    /** Number of tasks at which a node must topple. */
    public static final int CRITICAL_THRESHOLD = 4;

    /** Tasks subtracted from a toppling node per fire. */
    public static final int TOPPLE_DECREMENT = 4;

    /** Tasks added to each neighbour of a toppling node. */
    public static final int TOPPLE_INCREMENT = 1;

    /** Maximum matrix dimension accepted by the application. */
    public static final int MAX_DIMENSION = 1000;

    /** Minimum valid matrix dimension (1×1 is trivial and rejected). */
    public static final int MIN_DIMENSION = 2;

    /** Maximum heatmap snapshots saved during stabilised addition. */
    public static final int MAX_HEATMAP_STEPS = 20;

    /** Default input directory relative to the working directory. */
    public static final String INPUT_DIR = "input/";

    /** Default output directory relative to the working directory. */
    public static final String OUTPUT_DIR = "output/";

    private SandpileConfig() {
        // Utility class — not instantiated
    }
}
