# CLI Reference

## Modes

The application supports two execution modes:

### Interactive mode
```bash
java -jar main.jar
```
Presents a numbered menu. Each option prompts for the required inputs.

### Non-interactive mode
```bash
java -jar main.jar -f <func> [-a <file_a.csv>] [-b <file_b.csv>] [-d <dim>] -o <output.txt>
```

| Flag | Required | Description |
|------|----------|-------------|
| `-f` | Always | Functionality number (1–10) |
| `-a` | For 1,2,3,4,5,8 | First input CSV matrix filename |
| `-b` | For 3,8 | Second input CSV matrix filename |
| `-d` | For 6,7,9,10 | Matrix dimension (integer) |
| `-o` | Always | Output filename (must end in `.txt`) |

---

## Functionality Reference

### `1` — Display Matrix
Reads and prints the matrix to the output file.

```bash
java -jar main.jar -f 1 -a matrix3.csv -o result.txt
```
- **Input:** one CSV matrix (`-a`)
- **Max dimension:** 1000×1000

---

### `2` — Stabilise Matrix
Applies the toppling rule repeatedly until stable. Saves the stabilised matrix as a CSV alongside the text result.

```bash
java -jar main.jar -f 2 -a matrix5.csv -o result.txt
```
- **Input:** one CSV matrix (`-a`)
- **Output:** `output/result.txt` + `output/stabilized_<filename>.csv`
- **Max dimension:** 1000×1000 (warning shown above 902×902)

---

### `3` — Stabilised Addition with Heatmap Export
Computes stabilise(A + B) and exports up to 20 heatmap JPG snapshots of the stabilisation process.

```bash
java -jar main.jar -f 3 -a matrix5.csv -b matrix5_2.csv -o result.txt
```
- **Input:** two CSV matrices of the same dimension (`-a`, `-b`)
- **Output:** `output/result.txt` + `output/<a>-plus-<b>_step_N.jpg` images
- **Max dimension:** 1000×1000 (warning above 750×750)

---

### `4` — Dhar's Burning Algorithm
Tests whether the given stable matrix is a recurrent configuration.

```bash
java -jar main.jar -f 4 -a matrix3.csv -o result.txt
```
- **Input:** one stable CSV matrix (`-a`) — must have all values in {0,1,2,3}
- **Output:** "IS recurrent" or "NOT recurrent"
- **Max dimension:** 1000×1000

---

### `5` — Neutral Element Verification
Tests whether the given matrix is the identity element of the sandpile group (⊕).

```bash
java -jar main.jar -f 5 -a matrix3.csv -o result.txt
```
- **Input:** one CSV matrix (`-a`)
- **Warning:** exhaustive check — only feasible for n ≤ 3

---

### `6` — Count Recurrent Matrices (Brute-force)
Counts all recurrent configurations of dimension n by exhaustive enumeration.

```bash
java -jar main.jar -f 6 -d 3 -o result.txt
```
- **Input:** dimension (`-d`)
- **Max dimension:** 5 (feasibility limit — 4^25 ≈ 1.1 × 10¹⁵)
- **Note:** use functionality 7 for larger n

---

### `7` — Count Recurrent Matrices (Laplacian)
Computes the number of recurrent configurations as det(Δ̃) — exact and fast.

```bash
java -jar main.jar -f 7 -d 10 -o result.txt
```
- **Input:** dimension (`-d`)
- **Max dimension:** 93 (numerical precision limit)
- **Note:** result may lose precision above dimension 23

---

### `8` — Find Inverse Matrix
Finds the inverse of matrix A with respect to the neutral element E under stabilised addition: finds X such that stabilise(A + X) = E.

```bash
java -jar main.jar -f 8 -a matrixA.csv -b matrixE.csv -o result.txt
```
- **Input:** matrix A (`-a`) and neutral element E (`-b`)
- **Max dimension:** 6×6
- **Note:** exhaustive search — may take several minutes for n = 5 or 6

---

### `9` — Eigenvalues & Eigenvectors (Numerical)
Computes eigenvalues and eigenvectors of the reduced Laplacian numerically via Apache Commons Math.

```bash
java -jar main.jar -f 9 -d 5 -o result.txt
```
- **Input:** dimension (`-d`)
- **Max dimension:** 100

---

### `10` — Eigenvalues & Eigenvectors (Closed-form)
Computes eigenvalues and eigenvectors using the exact formula:
`λ_{k,l} = 4 − 2cos(kπ/(n+1)) − 2cos(lπ/(n+1))`

```bash
java -jar main.jar -f 10 -d 20 -o result.txt
```
- **Input:** dimension (`-d`)
- **Max dimension:** 130

---

## Dimension Limits Summary

| Functionality | Max dimension | Notes |
|---|---|---|
| 1 — Display | 1000 | — |
| 2 — Stabilise | 1000 | Warning above 902 |
| 3 — Heatmap addition | 1000 | Warning above 750 |
| 4 — Dhar | 1000 | Matrix must be stable |
| 5 — Neutral element | ~3 | Exhaustive — slow above n=3 |
| 6 — Brute-force count | 5 | Exponential time |
| 7 — Laplacian count | 93 | Precision warning above 23 |
| 8 — Inverse | 6 | Exhaustive — slow above n=4 |
| 9 — Eigenvalues (numerical) | 100 | — |
| 10 — Eigenvalues (closed-form) | 130 | — |
