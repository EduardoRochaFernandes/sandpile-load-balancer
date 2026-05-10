package llbc.security;

import llbc.math.LaplacianMatrix;

/**
 * Cybersecurity-oriented resilience analysis of sandpile network configurations.
 *
 * <p>Maps sandpile-theoretic properties to practical network security metrics:</p>
 * <ul>
 *   <li>Algebraic connectivity (λ₂) — resistance to node-removal attacks</li>
 *   <li>Spectral gap — recovery speed after disruption</li>
 *   <li>Group order (det Δ̃) — diversity of resilient operating states</li>
 * </ul>
 *
 * @author Eduardo Fernandes (refactor) — original team: Bruno Silva, Afonso Martins,
 *         Martim Pereira, Eduardo Fernandes (ISEP LAPR1, 2025/26)
 */
public final class ResilienceAnalyser {

    private final int n;
    private final double lambda2;
    private final double lambdaMax;
    private final double groupOrder;

    /**
     * Constructs a ResilienceAnalyser for an n×n grid.
     * All metrics are computed eagerly at construction time.
     *
     * @param n grid dimension
     */
    public ResilienceAnalyser(int n) {
        this.n = n;
        // Closed-form extremal eigenvalues for the n×n grid Laplacian
        this.lambda2    = 4.0 - 4.0 * Math.cos(Math.PI / (n + 1));
        this.lambdaMax  = 4.0 - 2.0 * Math.cos(n * Math.PI / (n + 1))
                                - 2.0 * Math.cos(n * Math.PI / (n + 1));
        this.groupOrder = new LaplacianMatrix(n).determinant();
    }

    /**
     * Returns the algebraic connectivity (Fiedler value, λ₂).
     *
     * <p>Higher λ₂ → harder to disconnect the network → more resistant to
     * targeted node-removal attacks and BGP hijacking.</p>
     */
    public double algebraicConnectivity() { return lambda2; }

    /**
     * Returns the spectral gap (λ_max − λ_min).
     *
     * <p>Larger gap → faster recovery after DDoS floods or node failures.</p>
     */
    public double spectralGap() { return lambdaMax - lambda2; }

    /**
     * Returns the number of resilient (recurrent) operating states.
     *
     * <p>Equals det(Δ̃). Larger value → more diverse safe states →
     * harder for an attacker to force a specific configuration.</p>
     */
    public double groupOrder() { return groupOrder; }

    /**
     * Prints a formatted resilience report to stdout.
     */
    public void printReport() {
        System.out.printf("%n");
        System.out.printf("╔══════════════════════════════════════════════════════════╗%n");
        System.out.printf("║    NETWORK RESILIENCE REPORT  —  %d×%-22d  ║%n", n, n);
        System.out.printf("╠══════════════════════════════════════════════════════════╣%n");
        System.out.printf("║  Algebraic Connectivity (λ₂)  : %-26.6f ║%n", lambda2);
        System.out.printf("║    → Resistance to targeted node-removal attacks         ║%n");
        System.out.printf("║  Spectral Gap (λ_max − λ_min) : %-26.6f ║%n", spectralGap());
        System.out.printf("║    → Speed of recovery after load spike or attack        ║%n");
        if (groupOrder > 999_999) {
            System.out.printf("║  Resilient State Count        : %-26.3e ║%n", groupOrder);
        } else {
            System.out.printf("║  Resilient State Count        : %-26.0f ║%n", groupOrder);
        }
        System.out.printf("║    → Safe operating states (sandpile group order |G|)    ║%n");
        System.out.printf("╚══════════════════════════════════════════════════════════╝%n");
        System.out.printf("%n");
    }
}
