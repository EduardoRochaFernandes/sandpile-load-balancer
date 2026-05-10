package llbc.core;

import java.util.Optional;

/**
 * Operations related to the neutral element (identity) of the sandpile group.
 *
 * <p>The neutral element {@code e} is the unique recurrent configuration such that
 * {@code e ⊕ σ = σ} for every recurrent configuration σ. It acts as the
 * additive identity under stabilised addition.</p>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public final class NeutralElement {

    private NeutralElement() {}

    /**
     * Tests whether the given matrix is the neutral element of the sandpile group.
     *
     * <p>Verified exhaustively: {@code e ⊕ σ = σ} for every recurrent σ.</p>
     *
     * <p><b>Warning:</b> exponential time — only feasible for n ≤ 3.</p>
     *
     * @param candidate the matrix to test
     * @return {@code true} if it is the neutral element
     */
    public static boolean isNeutralElement(SandpileMatrix candidate) {
        int n = candidate.dimension();
        int size = n * n;
        long total = (long) Math.pow(SandpileConfig.CRITICAL_THRESHOLD, size);

        for (long k = 0; k < total; k++) {
            SandpileMatrix sigma = DharBurning.fromIndex(k, n);
            if (DharBurning.isRecurrent(sigma)) {
                SandpileMatrix result = candidate.copy().add(sigma);
                if (!result.equals(sigma)) return false;
            }
        }
        return true;
    }

    /**
     * Searches for the inverse of {@code matrixA} with respect to the neutral element {@code e}.
     *
     * <p>Finds X such that {@code stabilise(A + X) = e}, by exhaustive search over
     * all recurrent configurations.</p>
     *
     * <p><b>Warning:</b> exponential time — only feasible for n ≤ 4 in practice.</p>
     *
     * @param matrixA  the matrix whose inverse is sought (must be recurrent and stable)
     * @param neutral  the neutral element e for the same dimension
     * @param listener optional progress callback (receives progress 0.0–1.0); may be null
     * @return an Optional containing the inverse matrix, or empty if none found
     */
    public static Optional<SandpileMatrix> findInverse(
            SandpileMatrix matrixA,
            SandpileMatrix neutral,
            ProgressListener listener) {

        int n = matrixA.dimension();
        long total = (long) Math.pow(SandpileConfig.CRITICAL_THRESHOLD, (long) n * n);

        for (long index = 0; index < total; index++) {
            if (listener != null && index % 10_000_000L == 0 && index > 0) {
                listener.onProgress(index, total);
            }
            SandpileMatrix candidate = DharBurning.fromIndex(index, n);
            if (DharBurning.isRecurrent(candidate)) {
                SandpileMatrix result = matrixA.copy().add(candidate);
                if (result.equals(neutral)) {
                    return Optional.of(candidate);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Functional interface for reporting search progress.
     */
    @FunctionalInterface
    public interface ProgressListener {
        /**
         * Called periodically during long searches.
         *
         * @param current number of candidates tested so far
         * @param total   total candidates to test
         */
        void onProgress(long current, long total);
    }
}
