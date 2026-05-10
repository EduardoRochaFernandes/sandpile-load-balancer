# Data Flow & Component Interaction

## End-to-End Data Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                        USER INPUT                               │
│                                                                 │
│   Interactive mode          Non-interactive (flags)             │
│   java -jar main.jar        java -jar main.jar -f 2 -a m.csv   │
│          │                              │                        │
└──────────┼──────────────────────────────┼───────────────────────┘
           │                              │
           ▼                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                       CLI LAYER                                 │
│                                                                 │
│   InteractiveMode.java          NonInteractiveMode.java         │
│   • Menu loop                   • Argument parsing              │
│   • Input validation            • Flag dispatch                 │
│   • User prompts                • Output file creation          │
└──────────────────────────────┬──────────────────────────────────┘
                               │
           ┌───────────────────┼────────────────────┐
           ▼                   ▼                    ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐
│   llbc.io       │  │   llbc.core     │  │   llbc.math         │
│                 │  │                 │  │                     │
│ MatrixReader    │  │ SandpileMatrix  │  │ LaplacianMatrix     │
│ • CSV parsing   │  │ • toppleStep()  │  │ • buildLaplacian()  │
│ • validation    │  │ • stabilise()   │  │ • determinant()     │
│                 │  │ • stabilised    │  │                     │
│ MatrixWriter    │  │   Addition()    │  │ EigenSolver         │
│ • CSV output    │  │                 │  │ • numerical         │
│ • formatted txt │  │ DharBurning     │  │ • closed-form       │
│                 │  │ • isRecurrent() │  │ • sortEigenPairs()  │
│ HeatmapWriter   │  │ • countRecurrent│  │                     │
│ • JPG export    │  │                 │  └─────────────────────┘
└────────┬────────┘  └────────┬────────┘
         │                    │
         └──────────┬─────────┘
                    ▼
┌─────────────────────────────────────────────────────────────────┐
│                      llbc.security                              │
│                                                                 │
│   ResilienceAnalyser                                            │
│   • algebraicConnectivity()   λ₂ — resistance to node removal  │
│   • spectralGap()             recovery speed after attack       │
│   • resilientStateCount()     number of safe operating states   │
│   • printResilienceReport()   formatted security report         │
└─────────────────────────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────────────────────────┐
│                        OUTPUT                                   │
│                                                                 │
│   output/result.txt          Formatted text results            │
│   output/stabilized_*.csv    Stabilised matrix as CSV          │
│   output/*_step_N.jpg        Heatmap image per topple step     │
└─────────────────────────────────────────────────────────────────┘
```

---

## Stabilisation Flow (Functionality 2 & 3)

```
CSV file
   │
   ▼
MatrixReader.read()
   │
   ├─ isValid()? ──── NO ──→ Error message
   │
   YES
   │
   ▼
SandpileMatrix.stabilise()
   │
   ├── isStable()? ── YES ──→ Return matrix
   │
   NO
   │
   ▼
toppleStep()  ◄──────────────────────┐
   │                                 │
   ├── save heatmap? ── YES ──→ HeatmapImageWriter.writeArrayAsImage()
   │
   └── isStable()? ── NO ───────────┘
```

---

## Recurrence Check Flow (Functionality 4)

```
CSV file → MatrixReader → int[][] matrix
                               │
                               ▼
                    DharBurning.isRecurrent()
                               │
                    ┌──────────┴──────────┐
                    │                     │
                   YES                    NO
                    │                     │
              "IS recurrent"        "NOT recurrent"
              (self-healing         (transient state —
               network state)        not guaranteed
                                      to recover)
```

---

## Eigenvalue Pipeline (Functionalities 9 & 10)

```
Dimension n (integer input)
        │
        ▼
LaplacianMatrix.buildLaplacian(n)    →    n²×n² matrix
        │
        ├── Functionality 9 (numerical):
        │       EigenSolver.numericalEigenvalues()
        │       EigenSolver.numericalEigenvector()
        │       [uses Apache Commons Math EigenDecomposition]
        │
        └── Functionality 10 (closed-form):
                EigenSolver.closedFormEigenvalues()
                EigenSolver.closedFormEigenvectors()
                EigenSolver.sortEigenPairs()
                [uses formula: λ_{k,l} = 4 - 2cos(kπ/n+1) - 2cos(lπ/n+1)]
```
