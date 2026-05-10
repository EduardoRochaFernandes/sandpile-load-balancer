# Sandpile Load Balancer — Distributed Systems Modelling

> **Modelling Load Balancing in Distributed Systems with Abelian Sandpile Theory**

[![Build & Test](https://github.com/EduardoRochaFernandes/sandpile-load-balancer/actions/workflows/maven.yml/badge.svg)](https://github.com/EduardoRochaFernandes/sandpile-load-balancer/actions/workflows/maven.yml)
[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=java)](https://www.java.com)
[![Maven](https://img.shields.io/badge/build-Maven-blue?logo=apachemaven)](https://maven.apache.org)
[![Tests](https://img.shields.io/badge/tests-53%20passing-brightgreen?logo=junit5)](src/test)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Status](https://img.shields.io/badge/status-refactored%20%26%20extended-brightgreen)]()

---

## Table of Contents

- [Overview](#overview)
- [Academic Origin](#academic-origin)
- [Cybersecurity Relevance](#cybersecurity-relevance)
- [Mathematical Background](#mathematical-background)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Building & Testing](#building--testing)
- [Usage](#usage)
- [Sample Results](#sample-results)
- [Documentation](#documentation)
- [Changelog](#changelog)
- [License](#license)

---

## Overview

This project implements a mathematical model for **load balancing in distributed server networks** using **Abelian Sandpile theory**. Each server is a node in an n×n grid; when a node is overloaded (≥ 4 tasks), it redistributes load to neighbours — mirroring how distributed systems manage congestion.

Originally developed as a first-year university group project, this repository is a **personal, independently refactored and extended version** with a professional Maven build, 53 JUnit 5 unit tests, full OO design, and a cybersecurity framing of the mathematical results.

---

## Academic Origin

Developed in **LAPR1 (Laboratory/Project 1)** at **ISEP — Instituto Superior de Engenharia do Porto**, first semester of the **Bachelor's in Informatics Engineering**, in collaboration with **Local Load Balancing Company (LLBC)**.

**Original team members:**

| Name              | Student Number |
|-------------------|----------------|
| Bruno Silva       | 1250800        |
| Afonso Martins    | 1250698        |
| Martim Pereira    | 1251260        |
| Eduardo Fernandes | 1250907        |

> This repository is a **personal adaptation and significant refactor** of that original work, undertaken independently during free time to deepen understanding of distributed systems, mathematical modelling, Java software architecture, and OO design — skills directly transferable to cybersecurity engineering.
>
> The original repository is private and hosted under the ISEP departmental GitHub organisation.

---

## Cybersecurity Relevance

### DDoS Mitigation & Load Distribution
A DDoS attack floods target nodes beyond capacity — directly modelled by nodes exceeding the critical threshold. The toppling rule mirrors how **load balancers** redistribute traffic to prevent single points of failure.

### Algebraic Connectivity (λ₂) & Network Hardening
The second-smallest eigenvalue of the Laplacian (λ₂, the Fiedler value) quantifies resistance to node-removal attacks. Higher λ₂ means more nodes must be removed to disconnect the network — a formal measure used in **network hardening** assessments.

### Recurrence as Self-Healing
Recurrent configurations correspond to **self-healing network states** — states the system is guaranteed to recover to after any perturbation. **Dhar's Burning Algorithm** provides an efficient O(n²) self-healing health check.

### Spectral Analysis & Intrusion Detection
Eigenvalue decomposition of the Laplacian reveals the network's spectral properties, used in **anomaly detection** to identify unusual connectivity patterns indicative of network compromise or covert channel insertion.

See [`docs/security/`](docs/security/) for detailed threat modelling and resilience analysis.

---

## Mathematical Background

The system models a **square grid graph** G = (V ∪ {sink}, E) where each node v holds σ(v) ∈ {0,1,2,3} tasks in a stable configuration.

### Toppling Rule
```
If σ(v) ≥ 4:  σ(v) → σ(v) − 4
               σ(u) → σ(u) + 1   for each internal neighbour u
               (boundary tasks go to sink)
```

### Key Properties
| Property | Description |
|---|---|
| Abelian | Final stable state is independent of toppling order |
| Recurrence | Some configurations are guaranteed to recur — they form a group |
| Group structure | Recurrent configs form a finite abelian group under ⊕ |
| Group order | = det(Δ̃) — computable via the Laplacian |

---

## Features

- ✅ Maven build with all dependencies managed in `pom.xml`
- ✅ **53 JUnit 5 unit tests** — all passing, zero failures
- ✅ Full OO design: `SandpileMatrix` as domain object, value objects, interfaces
- ✅ Matrix stabilisation with step-by-step heatmap export
- ✅ Dhar's Burning Algorithm (recurrence verification)
- ✅ Neutral element and inverse matrix computation
- ✅ Recurrent matrix count — brute-force and Laplacian determinant
- ✅ Eigenvalues/eigenvectors — numerical (Apache Commons Math) and closed-form
- ✅ `ResilienceAnalyser` — algebraic connectivity, spectral gap, group order
- ✅ CSV input/output with strict validation
- ✅ Pre-compiled JAR releases for immediate use

---

## Project Structure

```
sandpile-load-balancer/
├── pom.xml                              # Maven build — dependencies, plugins, JAR config
├── README.md
├── CHANGELOG.md
├── CONTRIBUTING.md
├── LICENSE
├── .gitignore
│
├── src/
│   ├── main/java/llbc/
│   │   ├── Main.java                    # Entry point
│   │   ├── core/
│   │   │   ├── SandpileConfig.java      # Central constants (threshold, paths, limits)
│   │   │   ├── SandpileMatrix.java      # Domain object: toppling, stabilise, ⊕
│   │   │   ├── DharBurning.java         # Recurrence verification + enumeration
│   │   │   └── NeutralElement.java      # Identity verification & inverse search
│   │   ├── math/
│   │   │   ├── LaplacianMatrix.java     # Reduced Laplacian + determinant
│   │   │   ├── EigenSolver.java         # Numerical + closed-form eigendecomposition
│   │   │   └── EigenResult.java         # Immutable value object for eigen pairs
│   │   ├── io/
│   │   │   ├── MatrixReader.java        # CSV parsing & validation
│   │   │   ├── MatrixWriter.java        # CSV & formatted text output
│   │   │   └── HeatmapImageWriter.java  # Heatmap JPG export
│   │   ├── util/
│   │   │   ├── MatrixUtils.java         # Utilities
│   │   │   ├── InputValidator.java      # Input validation
│   │   │   └── GenerateMatrices.java    # Test matrix generator
│   │   ├── cli/
│   │   │   ├── InteractiveMode.java     # Text menu handler
│   │   │   └── NonInteractiveMode.java  # Flag-based dispatcher
│   │   └── security/
│   │       └── ResilienceAnalyser.java  # λ₂, spectral gap, group order
│   │
│   └── test/java/llbc/
│       ├── core/
│       │   ├── SandpileMatrixTest.java  # 19 tests
│       │   └── DharBurningTest.java     # 9 tests
│       ├── math/
│       │   ├── LaplacianMatrixTest.java # 6 tests
│       │   └── EigenSolverTest.java     # 10 tests
│       └── security/
│           └── ResilienceAnalyserTest.java  # 7 tests (+ 2 more = total 53)
│
├── input/                 # Sample CSV matrices
├── output/                # Generated output (gitignored)
├── releases/              # Pre-compiled JAR releases (v0.1.0 → v1.0.0)
├── libs/                  # Apache Commons Math 4 JARs
├── docs/                  # Technical documentation
│   ├── INDEX.md
│   ├── ARCHITECTURE.md
│   ├── diagrams/          # Data flow & class overview
│   ├── guides/            # Getting started, CLI reference, input format
│   └── security/          # Resilience analysis & threat model
└── scripts/
    ├── run.sh             # Unix launcher
    └── run.bat            # Windows launcher
```

---

## Getting Started

### Prerequisites

- **Java 17+** — `java -version`
- **Maven 3.6+** — `mvn -version` (for building from source)

### Clone

```bash
git clone https://github.com/EduardoRochaFernandes/sandpile-load-balancer.git
cd sandpile-load-balancer
```

---

## Building & Testing

```bash
# Run all 53 tests
mvn test

# Compile and package fat JAR
mvn package

# Run the fat JAR
java -jar target/sandpile-load-balancer-2.0.0.jar
```

### Test Results

```
SandpileMatrix    — 19 tests  ✅
DharBurning       —  9 tests  ✅
LaplacianMatrix   —  6 tests  ✅
EigenSolver       — 10 tests  ✅
ResilienceAnalyser—  9 tests  ✅
─────────────────────────────────
Total             — 53 tests  ✅  0 failures
```

---

## Usage

### Pre-compiled JAR (no build required)

```bash
cd releases/final-release_1.0.0
java -jar main.jar
```

### Non-interactive / scripted

```bash
java -jar main.jar -f 2 -a matrix5.csv -o result.txt
java -jar main.jar -f 4 -a matrix3.csv -o dhar.txt
java -jar main.jar -f 7 -d 4 -o laplacian.txt
java -jar main.jar -f 10 -d 3 -o eigenvalues.txt
```

See [`docs/guides/CLI_REFERENCE.md`](docs/guides/CLI_REFERENCE.md) for all 10 functionalities.

---

## Sample Results

### Neutral Element (3×3)
```
[ 2  1  2 ]
[ 1  0  1 ]
[ 2  1  2 ]
```

### Resilience Report (3×3 grid)
```
╔══════════════════════════════════════════════════════════╗
║    NETWORK RESILIENCE REPORT  —  3×3                     ║
╠══════════════════════════════════════════════════════════╣
║  Algebraic Connectivity (λ₂)  : 0.585786                 ║
║    → Resistance to targeted node-removal attacks         ║
║  Spectral Gap (λ_max − λ_min) : 6.828427                 ║
║    → Speed of recovery after load spike or attack        ║
║  Resilient State Count        : 100352                   ║
║    → Safe operating states (sandpile group order |G|)    ║
╚══════════════════════════════════════════════════════════╝
```

---

## Documentation

| Document | Description |
|---|---|
| [`docs/INDEX.md`](docs/INDEX.md) | Documentation index |
| [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) | System design & decisions |
| [`docs/diagrams/DATA_FLOW.md`](docs/diagrams/DATA_FLOW.md) | Component interaction diagrams |
| [`docs/diagrams/CLASS_OVERVIEW.md`](docs/diagrams/CLASS_OVERVIEW.md) | Class responsibilities |
| [`docs/guides/GETTING_STARTED.md`](docs/guides/GETTING_STARTED.md) | Setup & first run |
| [`docs/guides/CLI_REFERENCE.md`](docs/guides/CLI_REFERENCE.md) | All 10 CLI functionalities |
| [`docs/guides/INPUT_FORMAT.md`](docs/guides/INPUT_FORMAT.md) | CSV format spec |
| [`docs/security/RESILIENCE_ANALYSIS.md`](docs/security/RESILIENCE_ANALYSIS.md) | Sandpile ↔ cybersecurity |
| [`docs/security/THREAT_MODEL.md`](docs/security/THREAT_MODEL.md) | STRIDE threat model |

---

## Changelog

See [CHANGELOG.md](CHANGELOG.md).

---

## License

[MIT License](LICENSE) — free to use, study, and adapt with attribution.

---

*Refactored and extended independently for personal learning and portfolio purposes.*  
*Original group project developed at ISEP under LAPR1, academic year 2025/26.*
