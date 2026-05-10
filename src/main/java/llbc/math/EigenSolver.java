package llbc.math;

import org.apache.commons.math4.legacy.linear.Array2DRowRealMatrix;
import org.apache.commons.math4.legacy.linear.EigenDecomposition;
import org.apache.commons.math4.legacy.linear.RealMatrix;

import java.util.Arrays;

/**
 * Computes eigenvalues and eigenvectors of the reduced Laplacian.
 *
 * <p>Two strategies are provided, each returning an {@link EigenResult}:</p>
 * <ul>
 *   <li>{@link #numerical(LaplacianMatrix)} — Apache Commons Math numerical decomposition</li>
 *   <li>{@link #closedForm(int)} — exact analytical formula for the n×n grid Laplacian</li>
 * </ul>
 *
 * <h3>Closed-form formula</h3>
 * <pre>
 *   λ_{k,l} = 4 − 2·cos(kπ/(n+1)) − 2·cos(lπ/(n+1))
 *   v_{k,l}(i,j) = sin(k·i·π/(n+1)) · sin(l·j·π/(n+1))
 * </pre>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public final class EigenSolver {

    private EigenSolver() {}

    /**
     * Computes eigenvalues and eigenvectors numerically using Apache Commons Math.
     *
     * @param laplacian the reduced Laplacian
     * @return sorted EigenResult (ascending by eigenvalue)
     */
    public static EigenResult numerical(LaplacianMatrix laplacian) {
        RealMatrix rm = new Array2DRowRealMatrix(laplacian.toArray());
        EigenDecomposition decomp = new EigenDecomposition(rm);

        int total = laplacian.dimension() * laplacian.dimension();
        double[] values = decomp.getRealEigenvalues();
        double[][] vectors = new double[total][];
        for (int i = 0; i < total; i++) {
            vectors[i] = decomp.getEigenvector(i).toArray();
        }
        sortPairs(values, vectors);
        return new EigenResult(values, vectors);
    }

    /**
     * Computes eigenvalues and eigenvectors analytically using the closed-form formula.
     *
     * @param n grid dimension
     * @return sorted EigenResult (ascending by eigenvalue)
     */
    public static EigenResult closedForm(int n) {
        int total = n * n;
        double[] values = new double[total];
        double[][] vectors = new double[total][total];

        int idx = 0;
        for (int k = 1; k <= n; k++) {
            for (int l = 1; l <= n; l++) {
                values[idx] = 4.0
                    - 2.0 * Math.cos(k * Math.PI / (n + 1))
                    - 2.0 * Math.cos(l * Math.PI / (n + 1));

                int pos = 0;
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= n; j++) {
                        vectors[idx][pos++] =
                            Math.sin(k * i * Math.PI / (n + 1)) *
                            Math.sin(l * j * Math.PI / (n + 1));
                    }
                }
                idx++;
            }
        }
        sortPairs(values, vectors);
        return new EigenResult(values, vectors);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static void sortPairs(double[] values, double[][] vectors) {
        int len = values.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (values[j] > values[j + 1]) {
                    double tmp = values[j]; values[j] = values[j+1]; values[j+1] = tmp;
                    double[] tv = vectors[j]; vectors[j] = vectors[j+1]; vectors[j+1] = tv;
                }
            }
        }
    }
}
