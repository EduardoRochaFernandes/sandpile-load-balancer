# Threat Model

## Scope

This threat model maps the sandpile distributed system model to real-world attack scenarios relevant to network infrastructure security. It is intended to contextualise the mathematical results of this project within a practical cybersecurity framework.

The modelled system is an **n×n grid of interconnected servers** (nodes) with a boundary sink, as described in [RESILIENCE_ANALYSIS.md](RESILIENCE_ANALYSIS.md).

---

## Assets

| Asset | Description |
|---|---|
| Server nodes | Individual compute units handling tasks |
| Network links | Communication edges between adjacent nodes |
| System stability | The property that no node is overloaded |
| Recurrent state | Self-healing operational configuration |
| Task throughput | Volume of work processed per unit time |

---

## Threat Actors

| Actor | Capability | Goal |
|---|---|---|
| External attacker | Can inject arbitrary load to any node | Crash servers, deny service |
| Insider threat | Can modify task routing policies | Redirect load, cause imbalance |
| Nation-state / APT | Can target infrastructure topology | Partition network, persistent disruption |

---

## Threat Scenarios

### T1 — Volumetric DDoS (Load Flooding)

**Description:** An attacker floods one or more nodes with tasks beyond their capacity (σ(v) ≥ 4), forcing repeated toppling and potentially cascading failures across the network.

**Sandpile mapping:** Corresponds to injecting a large number of grains at a target node, causing a chain reaction of topplings.

**Mitigation via model:**
- Recurrent configurations guarantee the network stabilises after any finite injection
- The number of toppling steps is bounded and predictable
- Functionality 2 (stabilisation) models the recovery process

**Detection signal:** Monitor for nodes repeatedly reaching the critical threshold in short time windows — a pattern consistent with sustained flooding rather than organic load spikes.

---

### T2 — Targeted Node Removal (Server Takedown)

**Description:** An attacker takes down specific servers (e.g., via exploitation, physical access, or BGP blackholing) to partition or degrade the network.

**Sandpile mapping:** Removing a node from the grid graph reduces algebraic connectivity λ₂. If λ₂ → 0, the graph becomes disconnectable.

**Mitigation via model:**
- Compute λ₂ for the network topology before deployment
- Higher λ₂ → more nodes must be removed to disconnect the network
- Use the closed-form eigenvalue formula (functionality 10) to rapidly assess topology changes

**Key metric:** `ResilienceAnalyser.algebraicConnectivity(n)` — monitor this value as topology changes. Set alerting thresholds based on acceptable disconnection risk.

---

### T3 — State Inference Attack

**Description:** An attacker observes the network's load distribution over time and attempts to infer the internal state, enabling targeted disruption (e.g., knowing when a node is near-critical).

**Sandpile mapping:** The total number of recurrent states is det(Δ̃). The larger this number, the harder it is for an attacker to predict the system's current state from partial observations.

**Mitigation via model:**
- Prefer network topologies with large group orders (higher det(Δ̃))
- The sandpile group order grows super-exponentially with n — larger grids are exponentially harder to infer
- For 5×5: ≈ 1.7 × 10²⁴ possible safe states — infeasible to enumerate

---

### T4 — Load Imbalance Exploitation (Routing Manipulation)

**Description:** An insider or compromised load balancer routes tasks unfairly, causing some nodes to be consistently overloaded while others are idle — reducing effective capacity.

**Sandpile mapping:** A non-recurrent configuration may be transient — the system may never return to it naturally. An attacker who can force the system into a transient state disrupts the normal recovery guarantees.

**Mitigation via model:**
- Use Dhar's Burning Algorithm (functionality 4) to verify that the current load distribution is a recurrent (recoverable) configuration
- Automate this check as a periodic health probe in the monitoring stack
- Alert when the current state is detected as non-recurrent

---

### T5 — Slow Recovery Attack (Spectral Gap Exploitation)

**Description:** An attacker repeatedly injects small amounts of load just below the threshold, exploiting a low spectral gap to keep the network in a prolonged recovery state without triggering obvious alarms.

**Sandpile mapping:** A small spectral gap means the network converges slowly to equilibrium. Repeated sub-critical injections can maintain the network in a degraded state.

**Mitigation via model:**
- Compute and monitor the spectral gap: `ResilienceAnalyser.spectralGap(n)`
- Set minimum acceptable spectral gap thresholds based on SLA recovery time requirements
- Consider denser topologies (higher connectivity) to increase spectral gap

---

## Residual Risks

| Risk | Notes |
|---|---|
| Grid topology assumption | Real networks are rarely perfect grids — the model is an approximation |
| Discrete task model | Real traffic is continuous, not integer-valued |
| Boundary sink simplification | Real networks have multiple exit points |
| Brute-force feasibility | Exact inverse computation only practical for n ≤ 4 |

These residual risks do not invalidate the model's qualitative insights but should be accounted for when applying results to production systems.

---

## STRIDE Mapping

| Threat category | Relevant scenario | Sandpile metric |
|---|---|---|
| **S**poofing | Fake task injection | Load validation at ingress |
| **T**ampering | Routing table manipulation | Recurrence check (Dhar) |
| **R**epudiation | Log tampering during attack | Out of scope |
| **I**nformation disclosure | State inference attack | Group order (det Δ̃) |
| **D**enial of service | Volumetric DDoS | Stabilisation steps, λ₂ |
| **E**levation of privilege | Insider routing manipulation | Transient state detection |
