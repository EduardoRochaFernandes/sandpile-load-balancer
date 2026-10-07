package llbc.io;

import llbc.core.SandpileConfig;
import llbc.core.SandpileMatrix;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a sandpile matrix from a comma-separated text file.
 *
 * <p>Format: one row per line, cells separated by commas, all values non-negative
 * integers, and the grid must be square (n×n, with n between
 * {@link SandpileConfig#MIN_DIMENSION} and {@link SandpileConfig#MAX_DIMENSION}).
 * Blank lines are ignored.</p>
 */
public final class MatrixReader {

    private MatrixReader() {}

    /**
     * Reads and validates a CSV file.
     *
     * @param file path to the CSV file
     * @return the parsed matrix
     * @throws IOException              if the file cannot be read
     * @throws IllegalArgumentException if the content is not a valid square matrix
     */
    public static SandpileMatrix read(Path file) throws IOException {
        return parse(Files.readAllLines(file, StandardCharsets.UTF_8));
    }

    /**
     * Parses already-loaded CSV lines.
     *
     * @param lines the lines of the file
     * @return the parsed matrix
     * @throws IllegalArgumentException if the content is not a valid square matrix
     */
    public static SandpileMatrix parse(List<String> lines) {
        List<int[]> rows = new ArrayList<>();
        for (int lineNo = 0; lineNo < lines.size(); lineNo++) {
            String line = lines.get(lineNo).trim();
            if (line.isEmpty()) continue;
            String[] cells = line.split(",", -1);
            int[] row = new int[cells.length];
            for (int c = 0; c < cells.length; c++) {
                row[c] = parseCell(cells[c], lineNo + 1, c + 1);
            }
            rows.add(row);
        }
        int n = rows.size();
        if (n < SandpileConfig.MIN_DIMENSION || n > SandpileConfig.MAX_DIMENSION) {
            throw new IllegalArgumentException("Matrix dimension must be between "
                + SandpileConfig.MIN_DIMENSION + " and " + SandpileConfig.MAX_DIMENSION
                + " (found " + n + " rows)");
        }
        for (int[] row : rows) {
            if (row.length != n) {
                throw new IllegalArgumentException(
                    "Matrix must be square: " + n + " rows but a row has " + row.length + " cells");
            }
        }
        return new SandpileMatrix(rows.toArray(new int[0][]));
    }

    private static int parseCell(String text, int line, int column) {
        String t = text.trim();
        try {
            int value = Integer.parseInt(t);
            if (value < 0) {
                throw new IllegalArgumentException(
                    "Negative value at line " + line + ", column " + column);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Not an integer at line " + line + ", column " + column + ": '" + t + "'");
        }
    }
}
