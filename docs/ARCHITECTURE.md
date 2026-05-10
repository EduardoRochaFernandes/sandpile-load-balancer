# Architecture Notes

## System Design

The sandpile system is structured around four distinct concerns, each mapped to a Java package:

### `llbc.core`
The mathematical heart of the system. Contains:
- `SandpileMatrix` — toppling rule, stabilisation, stable addition
- `DharBurning` — recurrence verification and exhaustive enumeration

These two classes implement the complete Abelian Sandpile model and are independent of I/O, enabling straightforward unit testing.

### `llbc.math`
Higher-level algebraic computations:
- `LaplacianMatrix` — constructs the graph Laplacian and computes its determinant
- `EigenSolver` — eigenvalue/eigenvector decomposition (numerical and closed-form)

Depends on Apache Commons Math 4 for matrix operations.

### `llbc.io`
All file system interaction:
- `MatrixReader` — CSV parsing with strict validation
- `MatrixWriter` — CSV and formatted text output
- `HeatmapImageWriter` — JPEG heatmap generation for each stabilisation step

### `llbc.util`
Shared utilities:
- `MatrixUtils` — deep copy, element-wise addition, equality, stable matrix enumeration
- `InputValidator` — string-to-integer parsing with whitespace/letter rejection
- `GenerateMatrices` — helper tool for generating test matrices

### `llbc.cli`
Command-line interface:
- `InteractiveMode` — text menu loop with user prompts
- `NonInteractiveMode` — flag-based argument parsing for scripted execution

### `llbc.security`
Cybersecurity-oriented extensions:
- `ResilienceAnalyser` — algebraic connectivity, spectral gap, resilient state count

---

## Data Flow

```
CSV file → MatrixReader → int[][] matrix
                                │
                    ┌───────────┼───────────┐
                    ▼           ▼           ▼
             SandpileMatrix  DharBurning  LaplacianMatrix
             (stabilise)     (isRecurrent)(determinant)
                    │                       │
                    ▼                       ▼
             MatrixWriter            ResilienceAnalyser
             HeatmapImageWriter      (security report)
```

---

## Limitations & Future Work

- **Performance**: brute-force enumeration (functionalities 5, 6, 8) is O(4^(n²)) and only feasible for n ≤ 3. For n ≥ 4, the Laplacian-based approach (functionality 7) must be used.
- **Build system**: Currently uses a pre-compiled JAR. A Maven or Gradle build would enable reproducible compilation and dependency management.
- **Parallelism**: The inverse matrix search (functionality 8) iterates over up to 4^(n²) candidates sequentially. A parallel stream or thread pool would accelerate this for n ≥ 4.
- **Visualisation**: Heatmaps are exported as individual JPEGs. An animated GIF output would make the stabilisation process clearer for presentations.
- **CLI**: Argument parsing is hand-written. Apache Commons CLI or picocli would provide more robust argument handling with auto-generated help text.
