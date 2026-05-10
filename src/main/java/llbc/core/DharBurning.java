package llbc.core;

/**
 * Implements Dhar's Burning Algorithm for recurrence verification.
 *
 * <p>A stable configuration is <em>recurrent</em> if and only if it belongs to
 * the sandpile group — i.e., the network state is self-healing and will be
 * revisited infinitely often under random external load.</p>
 *
 * <h3>Algorithm (O(n²) per configuration)</h3>
 * <ol>
 *   <li>Start a fire from the sink (boundary).</li>
 *   <li>A cell burns if its value ≥ number of its unburned neighbours.</li>
 *   <li>Repeat until no new cell can burn.</li>
 *   <li>The configuration is recurrent iff all cells burn.</li>
 * </ol>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public final class DharBurning {

    private DharBurning() {}

    /**
     * Tests whether the given matrix is a recurrent configuration.
     *
     * @param matrix a stable SandpileMatrix
     * @return {@code true} if recurrent
     */
    public static boolean isRecurrent(SandpileMatrix matrix) {
        int n = matrix.dimension();
        boolean[][] burned = new boolean[n][n];

        boolean progress;
        do {
            progress = false;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (!burned[i][j] && matrix.get(i, j) >= unburnedNeighbours(burned, i, j, n)) {
                        burned[i][j] = true;
                        progress = true;
                    }
                }
            }
        } while (progress);

        return allBurned(burned);
    }

    /**
     * Counts all recurrent n×n configurations by exhaustive enumeration.
     *
     * <p><b>Warning:</b> exponential time O(4^(n²)). Feasible only for n ≤ 3.</p>
     *
     * @param n grid dimension
     * @return number of recurrent configurations
     */
    public static int countRecurrent(int n) {
        int size = n * n;
        long total = (long) Math.pow(SandpileConfig.CRITICAL_THRESHOLD, size);
        int count = 0;
        for (long k = 0; k < total; k++) {
            SandpileMatrix m = fromIndex(k, n);
            if (isRecurrent(m)) count++;
        }
        return count;
    }

    /**
     * Converts a long index to its corresponding n×n stable matrix.
     * Enumerates all 4^(n²) stable configurations in a canonical order.
     *
     * @param index configuration index (0 to 4^(n²)−1)
     * @param n     grid dimension
     * @return the corresponding SandpileMatrix
     */
    public static SandpileMatrix fromIndex(long index, int n) {
        int[][] grid = new int[n][n];
        long temp = index;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = (int) (temp % SandpileConfig.CRITICAL_THRESHOLD);
                temp /= SandpileConfig.CRITICAL_THRESHOLD;
            }
        }
        return new SandpileMatrix(grid);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static int unburnedNeighbours(boolean[][] burned, int row, int col, int n) {
        int count = 0;
        if (row - 1 >= 0 && !burned[row - 1][col]) count++;
        if (row + 1 <  n && !burned[row + 1][col]) count++;
        if (col - 1 >= 0 && !burned[row][col - 1]) count++;
        if (col + 1 <  n && !burned[row][col + 1]) count++;
        return count;
    }

    private static boolean allBurned(boolean[][] burned) {
        for (boolean[] row : burned) {
            for (boolean cell : row) {
                if (!cell) return false;
            }
        }
        return true;
    }
}
