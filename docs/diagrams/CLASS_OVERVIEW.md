# Class Overview

## Package Map

```
llbc/
├── core/
│   ├── SandpileMatrix      The sandpile model — toppling, stabilisation, ⊕ operation
│   └── DharBurning         Recurrence verification + exhaustive enumeration
│
├── math/
│   ├── LaplacianMatrix     Graph Laplacian construction and determinant
│   └── EigenSolver         Eigenvalues/eigenvectors (numerical + closed-form)
│
├── io/
│   ├── MatrixReader        CSV file parsing with strict validation
│   ├── MatrixWriter        CSV and formatted text output
│   └── HeatmapImageWriter  Per-step heatmap JPG export
│
├── util/
│   ├── MatrixUtils         Deep copy, add, equal, stable matrix enumeration
│   ├── InputValidator      String-to-integer parsing, extension checking
│   └── GenerateMatrices    Interactive helper to generate test CSV matrices
│
├── cli/
│   ├── InteractiveMode     Text menu loop (no flags)
│   └── NonInteractiveMode  Flag-based dispatcher (-f, -a, -b, -d, -o)
│
├── security/
│   └── ResilienceAnalyser  Algebraic connectivity, spectral gap, state count
│
└── Main                    Entry point — routes to Interactive or NonInteractive
```

---

## Class Responsibilities

### `SandpileMatrix` — `llbc.core`

| Method | Description |
|---|---|
| `isValid(matrix)` | All entries ≥ 0 |
| `isStable(matrix)` | No entry ≥ 4 |
| `toppleStep(matrix)` | One pass of the toppling rule (in-place) |
| `stabilise(matrix)` | Repeat toppleStep until stable (in-place) |
| `countStabilisationSteps(matrix)` | Number of steps to reach stability |
| `stabilisedAddition(A, B)` | Returns stabilise(A + B) as new matrix |

**Constants:** `CRITICAL_THRESHOLD = 4`, `TOPPLE_DECREMENT = 4`, `TOPPLE_INCREMENT = 1`

---

### `DharBurning` — `llbc.core`

| Method | Description |
|---|---|
| `isRecurrent(matrix)` | Runs Dhar's Burning Algorithm — O(n²) |
| `countRecurrentMatrices(n)` | Exhaustive count — O(4^(n²)), feasible n ≤ 3 |
| `indexToBase4Array(size, k)` | Converts integer index to base-4 flat array |
| `arrayToMatrix(n, data)` | Reshapes flat array to n×n matrix |

---

### `LaplacianMatrix` — `llbc.math`

| Method | Description |
|---|---|
| `buildVertexList(n)` | n² vertices as {row, col} coordinates |
| `buildLaplacian(n)` | n²×n² reduced Laplacian matrix |
| `determinant(matrix)` | det via EigenDecomposition (= sandpile group order) |

**Laplacian entries:** diagonal = 4, off-diagonal = −1 for adjacent vertices, 0 otherwise.

---

### `EigenSolver` — `llbc.math`

| Method | Description |
|---|---|
| `numericalEigenvalues(laplacian)` | Via Apache Commons Math |
| `numericalEigenvector(laplacian, i)` | i-th eigenvector numerically |
| `closedFormEigenvalues(n)` | λ_{k,l} = 4 − 2cos(kπ/n+1) − 2cos(lπ/n+1) |
| `closedFormEigenvectors(n)` | v_{k,l}(i,j) = sin(kiπ/n+1)·sin(ljπ/n+1) |
| `sortEigenPairs(values, vectors)` | In-place ascending sort keeping correspondence |

---

### `ResilienceAnalyser` — `llbc.security`

| Method | Description |
|---|---|
| `algebraicConnectivity(n)` | λ₂ — resistance to node removal attacks |
| `spectralGap(n)` | λ_max − λ_min — recovery speed after disruption |
| `resilientStateCount(n)` | det(Δ̃) — number of safe operating states |
| `printResilienceReport(n)` | Formatted security report to stdout |

---

## Dependencies

```
Main
 └── cli/InteractiveMode, cli/NonInteractiveMode
      ├── core/SandpileMatrix
      ├── core/DharBurning
      ├── math/LaplacianMatrix  ──→  Apache Commons Math 4
      ├── math/EigenSolver      ──→  Apache Commons Math 4
      ├── io/MatrixReader
      ├── io/MatrixWriter
      ├── io/HeatmapImageWriter ──→  javax.imageio (JDK built-in)
      ├── util/MatrixUtils
      ├── util/InputValidator
      └── security/ResilienceAnalyser
```

**External library:** Apache Commons Math 4 (JAR included in `libs/`)
- `commons-math4-core-4.0-beta1.jar`
- `commons-math4-legacy-4.0-beta1.jar`
- `commons-math4-legacy-exception-4.0-beta1.jar`
- `commons-numbers-core-1.2.jar`
