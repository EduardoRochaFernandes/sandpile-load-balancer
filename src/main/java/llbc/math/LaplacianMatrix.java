package llbc.math;

import org.apache.commons.math4.legacy.linear.Array2DRowRealMatrix;
import org.apache.commons.math4.legacy.linear.EigenDecomposition;
import org.apache.commons.math4.legacy.linear.RealMatrix;

/**
 * Constructs and analyses the reduced Laplacian matrix of an n×n sandpile grid.
 *
 * <p>The reduced Laplacian Δ̃ is an n²×n² matrix. Its determinant equals the
 * order of the sandpile group — the total number of recurrent configurations.</p>
 *
 * <p>Entry (i, j) of the Laplacian:</p>
 * <ul>
 *   <li>4  if i == j (every interior vertex has degree 4 counting the sink)</li>
 *   <li>−1 if vertex j is an orthogonal neighbour of vertex i</li>
 *   <li>0  otherwise</li>
 * </ul>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public final class LaplacianMatrix {

    private final double[][] matrix;
    private final int n;

    /**
     * Constructs the reduced Laplacian for an n×n sandpile grid.
     *
     * @param n grid dimension
     */
    public LaplacianMatrix(int n) {
        this.n = n;
        this.matrix = build(n);
    }

    /** Returns the raw n²×n² Laplacian array. */
    public double[][] toArray() { return matrix; }

    /** Returns the grid dimension n. */
    public int dimension() { return n; }

    /**
     * Computes the determinant of the Laplacian via eigendecomposition.
     * This equals the order of the sandpile group (number of recurrent configurations).
     *
     * @return det(Δ̃)
     */
    public double determinant() {
        RealMatrix rm = new Array2DRowRealMatrix(matrix);
        return new EigenDecomposition(rm).getDeterminant();
    }

    // ── Private builders ──────────────────────────────────────────────────────

    private static double[][] build(int n) {
        int total = n * n;
        int[][] vertices = buildVertices(n);
        double[][] lap = new double[total][total];

        for (int col = 0; col < total; col++) {
            int[][] neighbours = orthogonalNeighbours(vertices[col]);
            for (int row = 0; row < total; row++) {
                if (col == row) {
                    lap[row][col] = 4.0;
                } else if (contains(vertices[row], neighbours)) {
                    lap[row][col] = -1.0;
                }
            }
        }
        return lap;
    }

    private static int[][] buildVertices(int n) {
        int[][] v = new int[n * n][2];
        int idx = 0;
        for (int r = 1; r <= n; r++) {
            for (int c = 1; c <= n; c++) {
                v[idx][0] = r;
                v[idx][1] = c;
                idx++;
            }
        }
        return v;
    }

    private static int[][] orthogonalNeighbours(int[] v) {
        return new int[][]{{v[0]-1, v[1]}, {v[0]+1, v[1]}, {v[0], v[1]-1}, {v[0], v[1]+1}};
    }

    private static boolean contains(int[] target, int[][] list) {
        for (int[] item : list) {
            if (item[0] == target[0] && item[1] == target[1]) return true;
        }
        return false;
    }
}
