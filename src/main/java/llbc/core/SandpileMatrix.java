package llbc.core;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an Abelian Sandpile matrix — an n×n grid where each cell
 * holds a non-negative integer task count.
 *
 * <p>This class is the core domain object of the sandpile model. It encapsulates
 * the matrix data and provides all operations defined by the sandpile formalism:
 * validation, toppling, stabilisation, and stabilised addition (⊕).</p>
 *
 * <p>Instances are <em>mutable</em>: {@link #stabilise()} and {@link #toppleStep()}
 * modify the matrix in-place for performance. Use {@link #copy()} to preserve
 * the original before mutation.</p>
 *
 * <h3>Invariants</h3>
 * <ul>
 *   <li>Dimension n ≥ {@link SandpileConfig#MIN_DIMENSION}</li>
 *   <li>All cell values ≥ 0 (enforced at construction and after reads)</li>
 * </ul>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public class SandpileMatrix {

    private final int[][] grid;
    private final int n;

    // ── Constructors ──────────────────────────────────────────────────────────

    /**
     * Constructs a SandpileMatrix from a raw 2D array.
     *
     * <p>The array is deep-copied so the caller retains ownership of the original.</p>
     *
     * @param grid a square n×n array of non-negative integers
     * @throws IllegalArgumentException if the array is null, not square, or contains
     *                                  negative values
     */
    public SandpileMatrix(int[][] grid) {
        Objects.requireNonNull(grid, "Grid must not be null");
        this.n = grid.length;
        if (n < SandpileConfig.MIN_DIMENSION) {
            throw new IllegalArgumentException(
                "Matrix dimension must be at least " + SandpileConfig.MIN_DIMENSION);
        }
        for (int[] row : grid) {
            if (row.length != n) {
                throw new IllegalArgumentException("Matrix must be square (n×n)");
            }
        }
        this.grid = deepCopy(grid);
    }

    /**
     * Constructs an all-zero n×n SandpileMatrix.
     *
     * @param n the grid dimension (must be ≥ {@link SandpileConfig#MIN_DIMENSION})
     */
    public SandpileMatrix(int n) {
        if (n < SandpileConfig.MIN_DIMENSION) {
            throw new IllegalArgumentException(
                "Matrix dimension must be at least " + SandpileConfig.MIN_DIMENSION);
        }
        this.n = n;
        this.grid = new int[n][n];
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /** Returns the dimension n of this n×n matrix. */
    public int dimension() { return n; }

    /** Returns the value at position (row, col). */
    public int get(int row, int col) { return grid[row][col]; }

    /** Sets the value at position (row, col). */
    public void set(int row, int col, int value) { grid[row][col] = value; }

    /**
     * Returns {@code true} if all cell values are non-negative.
     * A matrix with any negative value is considered corrupt or invalid input.
     */
    public boolean isValid() {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell < 0) return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if no cell holds ≥ {@link SandpileConfig#CRITICAL_THRESHOLD} tasks.
     */
    public boolean isStable() {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell >= SandpileConfig.CRITICAL_THRESHOLD) return false;
            }
        }
        return true;
    }

    // ── Mutations ─────────────────────────────────────────────────────────────

    /**
     * Applies one full pass of the toppling rule (in-place).
     *
     * <p>Every cell with value ≥ {@link SandpileConfig#CRITICAL_THRESHOLD} fires:
     * it loses {@link SandpileConfig#TOPPLE_DECREMENT} tasks and each orthogonal
     * neighbour gains {@link SandpileConfig#TOPPLE_INCREMENT} task.
     * Tasks sent to the boundary are absorbed by the sink.</p>
     *
     * @return {@code this} for method chaining
     */
    public SandpileMatrix toppleStep() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] >= SandpileConfig.CRITICAL_THRESHOLD) {
                    grid[row][col] -= SandpileConfig.TOPPLE_DECREMENT;
                    if (row - 1 >= 0) grid[row - 1][col] += SandpileConfig.TOPPLE_INCREMENT;
                    if (row + 1 <  n) grid[row + 1][col] += SandpileConfig.TOPPLE_INCREMENT;
                    if (col - 1 >= 0) grid[row][col - 1] += SandpileConfig.TOPPLE_INCREMENT;
                    if (col + 1 <  n) grid[row][col + 1] += SandpileConfig.TOPPLE_INCREMENT;
                }
            }
        }
        return this;
    }

    /**
     * Repeatedly applies {@link #toppleStep()} until stable (in-place).
     *
     * <p>The final result is unique regardless of toppling order — the abelian property.</p>
     *
     * @return {@code this} for method chaining
     */
    public SandpileMatrix stabilise() {
        while (!isStable()) {
            toppleStep();
        }
        return this;
    }

    /**
     * Counts the number of toppling steps required to stabilise this matrix.
     *
     * <p>A temporary copy is used — this matrix is not modified.</p>
     *
     * @return number of steps to reach stability
     */
    public int countStabilisationSteps() {
        SandpileMatrix temp = this.copy();
        int steps = 0;
        while (!temp.isStable()) {
            temp.toppleStep();
            steps++;
        }
        return steps;
    }

    // ── Sandpile group operations ─────────────────────────────────────────────

    /**
     * Returns a new matrix equal to stabilise(this + other).
     *
     * <p>This is the ⊕ operation of the sandpile group. Neither matrix is modified.</p>
     *
     * @param other a SandpileMatrix of the same dimension
     * @return stabilise(this + other)
     * @throws IllegalArgumentException if dimensions differ
     */
    public SandpileMatrix add(SandpileMatrix other) {
        if (other.n != this.n) {
            throw new IllegalArgumentException(
                "Cannot add matrices of different dimensions: " + n + " vs " + other.n);
        }
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = this.grid[i][j] + other.grid[i][j];
            }
        }
        return new SandpileMatrix(result).stabilise();
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /**
     * Returns an independent deep copy of this matrix.
     */
    public SandpileMatrix copy() {
        return new SandpileMatrix(this.grid);
    }

    /**
     * Returns the raw 2D array as a deep copy.
     * Use this when interfacing with code that expects primitive arrays.
     */
    public int[][] toArray() {
        return deepCopy(grid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SandpileMatrix other)) return false;
        return Arrays.deepEquals(this.grid, other.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : grid) {
            sb.append("[");
            for (int i = 0; i < n; i++) {
                sb.append(String.format("%3d", row[i]));
            }
            sb.append("  ]\n");
        }
        return sb.toString();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static int[][] deepCopy(int[][] src) {
        int[][] copy = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = Arrays.copyOf(src[i], src[i].length);
        }
        return copy;
    }
}
