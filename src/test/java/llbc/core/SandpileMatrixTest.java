package llbc.core;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SandpileMatrix")
class SandpileMatrixTest {

    // ── Construction ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("constructor deep-copies the input array")
    void constructorDeepCopiesInput() {
        int[][] raw = {{1, 2}, {3, 0}};
        SandpileMatrix m = new SandpileMatrix(raw);
        raw[0][0] = 99;
        assertThat(m.get(0, 0)).isEqualTo(1); // original unchanged
    }

    @Test
    @DisplayName("constructor rejects dimension < 2")
    void constructorRejectsTooSmall() {
        assertThatThrownBy(() -> new SandpileMatrix(1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("constructor rejects non-square array")
    void constructorRejectsNonSquare() {
        assertThatThrownBy(() -> new SandpileMatrix(new int[][]{{1, 2, 3}, {4, 5}}))
            .isInstanceOf(IllegalArgumentException.class);
    }

    // ── isValid ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isValid returns true for all non-negative values")
    void isValidPositive() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{0, 1}, {2, 3}});
        assertThat(m.isValid()).isTrue();
    }

    @Test
    @DisplayName("isValid returns false when any value is negative")
    void isValidNegative() {
        SandpileMatrix m = new SandpileMatrix(2);
        m.set(0, 0, -1);
        assertThat(m.isValid()).isFalse();
    }

    // ── isStable ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isStable returns true when all values < 4")
    void isStableTrue() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{3, 2}, {1, 0}});
        assertThat(m.isStable()).isTrue();
    }

    @Test
    @DisplayName("isStable returns false when any value >= 4")
    void isStableFalse() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{4, 0}, {0, 0}});
        assertThat(m.isStable()).isFalse();
    }

    // ── toppleStep ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("toppleStep fires top-left cell correctly")
    void toppleStepTopLeft() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{4, 0}, {0, 0}});
        m.toppleStep();
        // top-left: 4-4=0, right gains 1, below gains 1
        assertThat(m.get(0, 0)).isEqualTo(0);
        assertThat(m.get(0, 1)).isEqualTo(1);
        assertThat(m.get(1, 0)).isEqualTo(1);
        assertThat(m.get(1, 1)).isEqualTo(0);
    }

    @Test
    @DisplayName("toppleStep fires centre cell correctly on 3x3")
    void toppleStepCentre() {
        int[][] grid = {
            {0, 0, 0},
            {0, 4, 0},
            {0, 0, 0}
        };
        SandpileMatrix m = new SandpileMatrix(grid);
        m.toppleStep();
        assertThat(m.get(1, 1)).isEqualTo(0);
        assertThat(m.get(0, 1)).isEqualTo(1);
        assertThat(m.get(2, 1)).isEqualTo(1);
        assertThat(m.get(1, 0)).isEqualTo(1);
        assertThat(m.get(1, 2)).isEqualTo(1);
    }

    @Test
    @DisplayName("toppleStep boundary cell loses tasks to sink")
    void toppleStepBoundarySink() {
        // Corner cell has only 2 neighbours; 2 tasks go to sink
        SandpileMatrix m = new SandpileMatrix(new int[][]{{4, 0}, {0, 0}});
        m.toppleStep();
        int sum = m.get(0,0) + m.get(0,1) + m.get(1,0) + m.get(1,1);
        // Started with 4, 2 went to internal neighbours, 2 to sink → sum = 2
        assertThat(sum).isEqualTo(2);
    }

    // ── stabilise ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("stabilise produces a stable matrix")
    void stabiliseResultIsStable() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{6, 5}, {4, 7}});
        m.stabilise();
        assertThat(m.isStable()).isTrue();
    }

    @Test
    @DisplayName("stabilise of known 2x2 produces expected result")
    void stabilise2x2Known() {
        // {{4,5},{2,3}} → {{2,3},{0,1}}
        SandpileMatrix m = new SandpileMatrix(new int[][]{{4, 5}, {2, 3}});
        m.stabilise();
        assertThat(m.get(0, 0)).isEqualTo(2);
        assertThat(m.get(0, 1)).isEqualTo(3);
        assertThat(m.get(1, 0)).isEqualTo(0);
        assertThat(m.get(1, 1)).isEqualTo(1);
    }

    @Test
    @DisplayName("stabilise of already-stable matrix is a no-op")
    void stabiliseNoop() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{2, 1}, {1, 3}});
        SandpileMatrix copy = m.copy();
        m.stabilise();
        assertThat(m).isEqualTo(copy);
    }

    // ── add (⊕) ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("add is commutative")
    void addIsCommutative() {
        SandpileMatrix a = new SandpileMatrix(new int[][]{{2, 1}, {0, 3}});
        SandpileMatrix b = new SandpileMatrix(new int[][]{{1, 2}, {3, 0}});
        assertThat(a.add(b)).isEqualTo(b.add(a));
    }

    @Test
    @DisplayName("add result is always stable")
    void addResultIsStable() {
        SandpileMatrix a = new SandpileMatrix(new int[][]{{3, 3}, {3, 3}});
        SandpileMatrix b = new SandpileMatrix(new int[][]{{3, 3}, {3, 3}});
        assertThat(a.add(b).isStable()).isTrue();
    }

    @Test
    @DisplayName("add throws when dimensions differ")
    void addThrowsOnDimensionMismatch() {
        SandpileMatrix a = new SandpileMatrix(new int[][]{{1, 0}, {0, 1}});
        SandpileMatrix b = new SandpileMatrix(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        assertThatThrownBy(() -> a.add(b)).isInstanceOf(IllegalArgumentException.class);
    }

    // ── copy & equals ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("copy produces an equal but independent matrix")
    void copyIsIndependent() {
        SandpileMatrix original = new SandpileMatrix(new int[][]{{1, 2}, {3, 0}});
        SandpileMatrix copy = original.copy();
        assertThat(copy).isEqualTo(original);
        copy.set(0, 0, 99);
        assertThat(original.get(0, 0)).isEqualTo(1); // original unchanged
    }

    @Test
    @DisplayName("equals is symmetric")
    void equalsSymmetric() {
        SandpileMatrix a = new SandpileMatrix(new int[][]{{1, 2}, {3, 0}});
        SandpileMatrix b = new SandpileMatrix(new int[][]{{1, 2}, {3, 0}});
        assertThat(a).isEqualTo(b);
        assertThat(b).isEqualTo(a);
    }

    // ── countStabilisationSteps ───────────────────────────────────────────────

    @Test
    @DisplayName("countStabilisationSteps returns 0 for stable matrix")
    void stepsZeroIfStable() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{1, 2}, {0, 3}});
        assertThat(m.countStabilisationSteps()).isEqualTo(0);
    }

    @Test
    @DisplayName("countStabilisationSteps returns positive for unstable matrix")
    void stepsPositiveIfUnstable() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{4, 0}, {0, 0}});
        assertThat(m.countStabilisationSteps()).isGreaterThan(0);
    }

    @Test
    @DisplayName("countStabilisationSteps does not modify original matrix")
    void stepsDoesNotModifyOriginal() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{4, 0}, {0, 0}});
        SandpileMatrix before = m.copy();
        m.countStabilisationSteps();
        assertThat(m).isEqualTo(before);
    }
}
