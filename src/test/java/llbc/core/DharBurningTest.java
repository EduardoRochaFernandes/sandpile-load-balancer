package llbc.core;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DharBurning")
class DharBurningTest {

    // ── isRecurrent ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("neutral element of 3x3 is recurrent")
    void neutralElement3x3IsRecurrent() {
        SandpileMatrix neutral = new SandpileMatrix(new int[][]{
            {2, 1, 2},
            {1, 0, 1},
            {2, 1, 2}
        });
        assertThat(DharBurning.isRecurrent(neutral)).isTrue();
    }

    @Test
    @DisplayName("all-zero matrix is NOT recurrent for n=3")
    void allZerosNotRecurrent() {
        SandpileMatrix zeros = new SandpileMatrix(new int[][]{
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        });
        assertThat(DharBurning.isRecurrent(zeros)).isFalse();
    }

    @Test
    @DisplayName("all-zero 2x2 is NOT recurrent")
    void allZeros2x2NotRecurrent() {
        SandpileMatrix m = new SandpileMatrix(new int[][]{{0, 0}, {0, 0}});
        assertThat(DharBurning.isRecurrent(m)).isFalse();
    }

    @Test
    @DisplayName("max-value stable matrix for 2x2 is recurrent")
    void maxStable2x2IsRecurrent() {
        // {3,3},{3,3} is recurrent for 2×2
        SandpileMatrix m = new SandpileMatrix(new int[][]{{3, 3}, {3, 3}});
        assertThat(DharBurning.isRecurrent(m)).isTrue();
    }

    @Test
    @DisplayName("known recurrent 2x2 matrix")
    void knownRecurrent2x2() {
        // Verified manually: {2,1},{1,2} is recurrent for 2×2
        SandpileMatrix m = new SandpileMatrix(new int[][]{{2, 1}, {1, 2}});
        assertThat(DharBurning.isRecurrent(m)).isTrue();
    }

    // ── countRecurrent ────────────────────────────────────────────────────────

    @Test
    @DisplayName("countRecurrent(2) == 192")
    void countRecurrent2x2() {
        assertThat(DharBurning.countRecurrent(2)).isEqualTo(192);
    }

    // ── fromIndex ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("fromIndex(0, n) produces all-zero matrix")
    void fromIndexZero() {
        SandpileMatrix m = DharBurning.fromIndex(0, 2);
        assertThat(m.get(0, 0)).isEqualTo(0);
        assertThat(m.get(0, 1)).isEqualTo(0);
        assertThat(m.get(1, 0)).isEqualTo(0);
        assertThat(m.get(1, 1)).isEqualTo(0);
    }

    @Test
    @DisplayName("fromIndex(255, 2) produces all-3 matrix (max stable 2x2)")
    void fromIndexMax2x2() {
        // 4^4 - 1 = 255 → {3,3,3,3}
        SandpileMatrix m = DharBurning.fromIndex(255L, 2);
        assertThat(m.get(0, 0)).isEqualTo(3);
        assertThat(m.get(0, 1)).isEqualTo(3);
        assertThat(m.get(1, 0)).isEqualTo(3);
        assertThat(m.get(1, 1)).isEqualTo(3);
    }

    @Test
    @DisplayName("fromIndex produces distinct matrices for distinct indices")
    void fromIndexDistinct() {
        SandpileMatrix a = DharBurning.fromIndex(0, 2);
        SandpileMatrix b = DharBurning.fromIndex(1, 2);
        assertThat(a).isNotEqualTo(b);
    }

    // ── neutral element property ──────────────────────────────────────────────

    @Test
    @DisplayName("neutral element 3x3: e ⊕ σ = σ for all recurrent σ")
    void neutralElementProperty3x3() {
        SandpileMatrix e = new SandpileMatrix(new int[][]{
            {2, 1, 2},
            {1, 0, 1},
            {2, 1, 2}
        });
        int n = 3;
        int size = n * n;
        long total = (long) Math.pow(4, size);

        for (long k = 0; k < total; k++) {
            SandpileMatrix sigma = DharBurning.fromIndex(k, n);
            if (DharBurning.isRecurrent(sigma)) {
                SandpileMatrix result = e.copy().add(sigma);
                assertThat(result)
                    .as("e ⊕ σ should equal σ for index %d", k)
                    .isEqualTo(sigma);
            }
        }
    }
}
