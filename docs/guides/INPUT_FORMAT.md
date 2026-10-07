# Input Format Specification

## CSV Matrix Files

All matrix inputs are square CSV files containing non-negative integers.

### Rules

- One row per line
- Values separated by commas (`,`) — no spaces around commas
- All values must be non-negative integers (≥ 0)
- The matrix must be square: n rows × n columns
- No headers, no labels — data only
- File must have the `.csv` extension
- Minimum dimension: 2×2
- Maximum dimension: 1000×1000 (functionality-dependent — see [CLI_REFERENCE.md](CLI_REFERENCE.md))

### Stable vs Unstable

Some functionalities (e.g., Dhar's Burning Algorithm) require a **stable** matrix — all values must be in {0, 1, 2, 3}. Others (e.g., Stabilise) accept unstable matrices with values ≥ 4.

---

## Examples

### 3×3 stable matrix
```
2,1,2
1,0,1
2,1,2
```
This is the **neutral element** of the 3×3 sandpile group.

### 4×4 unstable matrix (will be stabilised)
```
5,3,2,1
0,4,3,2
2,1,6,0
3,2,1,4
```

### 5×5 matrix for heatmap addition test
```
3,2,1,0,3
2,1,0,3,2
1,0,3,2,1
0,3,2,1,0
3,2,1,0,3
```

---

## File Placement

With the Maven build (`java -jar target/sandpile-load-balancer-2.0.0.jar stabilise FILE.csv`) any path is accepted, for example `input/matrix5.csv`. The rules below apply to the original JAR:

Place input files in the `input/` folder relative to the JAR you are running:

```
releases/final-release_1.0.0/
├── main.jar
├── input/          ← place your CSV files here
│   ├── matrix3.csv
│   └── mymatrix.csv
└── output/         ← results appear here
```

When using the scripts from the project root, the working directory changes automatically to the release folder — no manual navigation needed.

---

## Generating Test Matrices

The original JAR bundles a `GenerateMatrices` helper (random matrices with values 0-6, or a recurrent pattern with corners = 2 and everything else = 3). Its source is not part of this repository; the sample files in `input/` can be used instead.

---

## Common Errors

| Error message | Cause | Fix |
|---|---|---|
| `Only .csv files are permitted` | Wrong file extension | Rename file to `.csv` |
| `The file 'x' doesn't exist` | File not in `input/` folder | Move file to `input/` |
| `The given matrix in file 'x' isn't valid` | Negative values or wrong column count | Check the CSV format |
| `The matrices have different dimensions` | Files A and B have different n | Use matrices of the same size |
| `The given matrix isn't stable` | Values ≥ 4 when stability required | Stabilise first (functionality 2) |
