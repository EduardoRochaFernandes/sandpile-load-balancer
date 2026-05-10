package llbc.math;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LaplacianMatrix")
class LaplacianMatrixTest {

    @Test
    @DisplayName("2x2 Laplacian has correct structure")
    void laplacian2x2Structure() {
        LaplacianMatrix lap = new LaplacianMatrix(2);
        double[][] m = lap.toArray();

        // Diagonal = 4
        assertThat(m[0][0]).isEqualTo(4.0);
        assertThat(m[1][1]).isEqualTo(4.0);
        assertThat(m[2][2]).isEqualTo(4.0);
        assertThat(m[3][3]).isEqualTo(4.0);

        // Adjacent entries = -1 (vertex 0 is adjacent to 1 and 2)
        assertThat(m[0][1]).isEqualTo(-1.0);
        assertThat(m[0][2]).isEqualTo(-1.0);

        // Non-adjacent entries = 0 (vertex 0 is not adjacent to 3)
        assertThat(m[0][3]).isEqualTo(0.0);
    }

    @Test
    @DisplayName("2x2 Laplacian is symmetric")
    void laplacian2x2Symmetric() {
        LaplacianMatrix lap = new LaplacianMatrix(2);
        double[][] m = lap.toArray();
        int size = 4;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assertThat(m[i][j]).isEqualTo(m[j][i]);
            }
        }
    }

    @Test
    @DisplayName("det(Laplacian 2x2) == 192")
    void determinant2x2() {
        LaplacianMatrix lap = new LaplacianMatrix(2);
        assertThat(Math.round(lap.determinant())).isEqualTo(192L);
    }

    @Test
    @DisplayName("det(Laplacian 3x3) == 100352")
    void determinant3x3() {
        LaplacianMatrix lap = new LaplacianMatrix(3);
        assertThat(Math.round(lap.determinant())).isEqualTo(100_352L);
    }

    @Test
    @DisplayName("Laplacian size is n*n x n*n")
    void laplacianSize() {
        for (int n = 2; n <= 5; n++) {
            LaplacianMatrix lap = new LaplacianMatrix(n);
            assertThat(lap.toArray().length).isEqualTo(n * n);
            assertThat(lap.toArray()[0].length).isEqualTo(n * n);
        }
    }

    @Test
    @DisplayName("row sums of 2x2 Laplacian are non-negative (boundary rows have sum > 0)")
    void rowSumsNonNegative() {
        LaplacianMatrix lap = new LaplacianMatrix(2);
        double[][] m = lap.toArray();
        for (double[] row : m) {
            double sum = 0;
            for (double v : row) sum += v;
            assertThat(sum).isGreaterThanOrEqualTo(0.0);
        }
    }
}
