package llbc.math;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("EigenSolver")
class EigenSolverTest {

    private static final double TOLERANCE = 1e-6;

    // ── closed-form ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("closedForm(2) returns 4 eigenvalues")
    void closedForm2ReturnsCorrectCount() {
        EigenResult result = EigenSolver.closedForm(2);
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("closedForm eigenvalues are sorted ascending")
    void closedFormEigenvaluesSorted() {
        EigenResult result = EigenSolver.closedForm(3);
        double[] values = result.eigenvalues();
        for (int i = 0; i < values.length - 1; i++) {
            assertThat(values[i]).isLessThanOrEqualTo(values[i + 1]);
        }
    }

    @Test
    @DisplayName("closedForm minimum eigenvalue for n=2 matches formula")
    void closedFormMinEigenvalue2() {
        // λ_min = 4 - 2*cos(π/3) - 2*cos(π/3) = 4 - 1 - 1 = 2
        EigenResult result = EigenSolver.closedForm(2);
        assertThat(result.eigenvalue(0)).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    @DisplayName("closedForm eigenvalues are all positive")
    void closedFormAllPositive() {
        EigenResult result = EigenSolver.closedForm(4);
        for (double v : result.eigenvalues()) {
            assertThat(v).isGreaterThan(0.0);
        }
    }

    @Test
    @DisplayName("closedForm eigenvectors have correct dimensions")
    void closedFormEigenvectorDimensions() {
        int n = 3;
        EigenResult result = EigenSolver.closedForm(n);
        assertThat(result.size()).isEqualTo(n * n);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.eigenvector(i).length).isEqualTo(n * n);
        }
    }

    // ── numerical ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("numerical(2x2) returns 4 eigenvalues")
    void numerical2x2Count() {
        LaplacianMatrix lap = new LaplacianMatrix(2);
        EigenResult result = EigenSolver.numerical(lap);
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("numerical eigenvalues are sorted ascending")
    void numericalEigenvaluesSorted() {
        LaplacianMatrix lap = new LaplacianMatrix(3);
        EigenResult result = EigenSolver.numerical(lap);
        double[] values = result.eigenvalues();
        for (int i = 0; i < values.length - 1; i++) {
            assertThat(values[i]).isLessThanOrEqualTo(values[i + 1] + TOLERANCE);
        }
    }

    @Test
    @DisplayName("numerical and closed-form eigenvalues agree for n=3")
    void numericalAndClosedFormAgree() {
        int n = 3;
        LaplacianMatrix lap = new LaplacianMatrix(n);
        EigenResult numerical  = EigenSolver.numerical(lap);
        EigenResult closedForm = EigenSolver.closedForm(n);

        double[] numVals = numerical.eigenvalues();
        double[] cfVals  = closedForm.eigenvalues();

        assertThat(numVals.length).isEqualTo(cfVals.length);
        for (int i = 0; i < numVals.length; i++) {
            assertThat(numVals[i]).isCloseTo(cfVals[i], within(1e-4));
        }
    }

    // ── EigenResult immutability ──────────────────────────────────────────────

    @Test
    @DisplayName("EigenResult.eigenvalues() returns a defensive copy")
    void eigenResultDefensiveCopy() {
        EigenResult result = EigenSolver.closedForm(2);
        double[] copy = result.eigenvalues();
        copy[0] = 9999.0;
        assertThat(result.eigenvalue(0)).isNotEqualTo(9999.0);
    }
}
