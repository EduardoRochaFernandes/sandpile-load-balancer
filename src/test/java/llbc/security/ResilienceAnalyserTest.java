package llbc.security;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ResilienceAnalyser")
class ResilienceAnalyserTest {

    private static final double TOLERANCE = 1e-4;

    @Test
    @DisplayName("algebraicConnectivity(2) matches closed-form: 4 - 4*cos(π/3)")
    void algebraicConnectivity2() {
        ResilienceAnalyser analyser = new ResilienceAnalyser(2);
        double expected = 4.0 - 4.0 * Math.cos(Math.PI / 3.0);
        assertThat(analyser.algebraicConnectivity()).isCloseTo(expected, within(TOLERANCE));
    }

    @Test
    @DisplayName("algebraicConnectivity increases as n decreases (smaller grids more connected)")
    void algebraicConnectivityMonotone() {
        double lambda2_2 = new ResilienceAnalyser(2).algebraicConnectivity();
        double lambda2_5 = new ResilienceAnalyser(5).algebraicConnectivity();
        assertThat(lambda2_2).isGreaterThan(lambda2_5);
    }

    @Test
    @DisplayName("algebraicConnectivity is always positive")
    void algebraicConnectivityPositive() {
        for (int n = 2; n <= 8; n++) {
            assertThat(new ResilienceAnalyser(n).algebraicConnectivity()).isGreaterThan(0.0);
        }
    }

    @Test
    @DisplayName("spectralGap is positive for all n >= 2")
    void spectralGapPositive() {
        for (int n = 2; n <= 6; n++) {
            assertThat(new ResilienceAnalyser(n).spectralGap()).isGreaterThan(0.0);
        }
    }

    @Test
    @DisplayName("groupOrder(2) == 192")
    void groupOrder2x2() {
        ResilienceAnalyser analyser = new ResilienceAnalyser(2);
        assertThat(Math.round(analyser.groupOrder())).isEqualTo(192L);
    }

    @Test
    @DisplayName("groupOrder(3) == 100352")
    void groupOrder3x3() {
        ResilienceAnalyser analyser = new ResilienceAnalyser(3);
        assertThat(Math.round(analyser.groupOrder())).isEqualTo(100_352L);
    }

    @Test
    @DisplayName("groupOrder increases with n")
    void groupOrderMonotone() {
        double g2 = new ResilienceAnalyser(2).groupOrder();
        double g3 = new ResilienceAnalyser(3).groupOrder();
        assertThat(g3).isGreaterThan(g2);
    }
}
