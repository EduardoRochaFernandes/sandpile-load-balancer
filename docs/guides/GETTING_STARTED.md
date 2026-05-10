# Getting Started

## Prerequisites

- **Java 17 or later** — check with `java -version`
- No additional installation required — the JAR is pre-compiled and all dependencies are bundled

---

## 1. Clone the repository

```bash
git clone https://github.com/EduardoRochaFernandes/sandpile-load-balancer.git
cd sandpile-load-balancer
```

---

## 2. Run the application

Navigate to the final release folder:

```bash
cd releases/final-release_1.0.0
java -jar main.jar
```

You will see the main menu:

```
========= Modelling Load Balancing in Distributed Systems with Sandpiles =========

      0. Close Application
      1. Display given Matrix
      2. Stabilise given Matrix
      3. Stabilised addition of two matrices (exports heatmap JPGs)
      4. Verify recurrence using Dhar's Burning Algorithm
      5. Check whether the matrix is the neutral element of ⊕
      6. Count recurrent matrices (brute-force)
      7. Count recurrent matrices (via Laplacian determinant)
      8. Find inverse matrix w.r.t. stabilised addition
      9. Eigenvalues and eigenvectors (numerical)
     10. Eigenvalues and eigenvectors (closed-form)
```

---

## 3. Quick run with the helper scripts

From the project root:

**Unix/macOS/Linux:**
```bash
chmod +x scripts/run.sh
./scripts/run.sh
```

**Windows:**
```bat
scripts\run.bat
```

---

## 4. Try your first operation

**Display and stabilise a matrix:**

1. Run the application
2. Type `2` and press Enter
3. Type `matrix3.csv` and press Enter
4. The application will display the initial matrix, stabilise it, and save the result to `output/stabilized_matrix3.csv`

**Non-interactive (scripted) example:**

```bash
java -jar main.jar -f 2 -a matrix3.csv -o result.txt
```

Output appears in `output/result.txt`.

---

## 5. Add your own matrix

Create a CSV file in `releases/final-release_1.0.0/input/` with non-negative integers:

```
2,1,0,3
0,3,2,1
1,0,3,2
3,2,1,0
```

Save it as e.g. `mymatrix.csv` and use it as input in any functionality.

---

## Troubleshooting

| Problem | Solution |
|---|---|
| `java: command not found` | Install Java 17+ and ensure it's on your PATH |
| `The file 'x.csv' doesn't exist` | Make sure the file is in the `input/` folder relative to the JAR |
| `Only .csv files are permitted` | Rename your file to end in `.csv` |
| `Only .txt files are permitted for -o` | Output file must end in `.txt` |
| Matrix dimension too large warning | Some functionalities have size limits — see [CLI_REFERENCE.md](CLI_REFERENCE.md) |
