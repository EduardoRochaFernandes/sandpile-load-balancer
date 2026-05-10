package llbc.math;

import java.util.Arrays;

/**
 * Immutable value object holding a paired set of eigenvalues and eigenvectors.
 *
 * <p>Eigenvalues and eigenvectors are kept in sorted ascending order by eigenvalue.
 * The i-th row of {@link #eigenvectors()} corresponds to the i-th element of
 * {@link #eigenvalues()}.</p>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public final class EigenResult {

    private final double[] eigenvalues;
    private final double[][] eigenvectors;

    /**
     * Constructs an EigenResult. Arrays are deep-copied for immutability.
     *
     * @param eigenvalues  array of eigenvalues (ascending order)
     * @param eigenvectors matrix where row i is the eigenvector for eigenvalue i
     */
    public EigenResult(double[] eigenvalues, double[][] eigenvectors) {
        this.eigenvalues  = Arrays.copyOf(eigenvalues, eigenvalues.length);
        this.eigenvectors = deepCopy(eigenvectors);
    }

    /** Returns a copy of the eigenvalue array. */
    public double[] eigenvalues() {
        return Arrays.copyOf(eigenvalues, eigenvalues.length);
    }

    /** Returns a deep copy of the eigenvector matrix. */
    public double[][] eigenvectors() {
        return deepCopy(eigenvectors);
    }

    /** Returns the number of eigenvalue/eigenvector pairs. */
    public int size() { return eigenvalues.length; }

    /** Returns the i-th eigenvalue. */
    public double eigenvalue(int i) { return eigenvalues[i]; }

    /** Returns the i-th eigenvector as a copy. */
    public double[] eigenvector(int i) {
        return Arrays.copyOf(eigenvectors[i], eigenvectors[i].length);
    }

    private static double[][] deepCopy(double[][] src) {
        double[][] copy = new double[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = Arrays.copyOf(src[i], src[i].length);
        }
        return copy;
    }
}
