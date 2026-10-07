# Changelog

All notable changes to this project are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added
- `demo`, `stabilise FILE.csv`, `resilience N` and `help` commands in `llbc.Main` (previously it only printed a pointer to the release JAR).
- `llbc.io.MatrixReader` (strict CSV parsing) and tests for it and for the CLI (62 tests in total).
- Dev container (Java 21 + Maven) for GitHub Codespaces.
- CI on Java 17 and 21 that builds, tests, smoke-tests the packaged JAR and exercises the original release JAR.
- Issue/PR templates, Dependabot configuration, `SECURITY.md`, `.editorconfig`.

### Changed
- README, architecture and guide documents rewritten to describe only what the repository actually contains; the
  security documents now state that they are conceptual and corrected their numeric tables.
- The original release JAR is documented as requiring Java 21+.
- `LICENSE` is now a plain MIT text with a copyright holder; the academic-origin note moved to the README.
- Fixed the `groupId` typo in `pom.xml` and removed the unused Apache snapshot repository.
- Removed personal data (student numbers) from the documentation.

### Fixed
- `ResilienceAnalyser.printReport`: value rows were two characters wider than the box border.

### Removed
- Superseded pre-release JARs (0.1.0 to 0.2.5), the backup release copy and generated output files from the working
  tree. They remain available in the git history (commit `30f1e42`).

## [2.0.0] - 2026-05-10

Independent refactor of the original academic project.

### Added
- Maven build with Apache Commons Math 4, JUnit 5 and AssertJ.
- 53 JUnit 5 tests for `core`, `math` and `security`.
- Domain classes `SandpileMatrix`, `DharBurning`, `NeutralElement`, `SandpileConfig`, `LaplacianMatrix`,
  `EigenSolver`, `EigenResult` and `ResilienceAnalyser`.
- Documentation under `docs/`, launcher scripts under `scripts/`, GitHub Actions workflow.

## [1.0.0] - Final academic release

Original LAPR1 (ISEP) submission by a team of four, distributed as `releases/final-release_1.0.0/main.jar`.
Ten functionalities through an interactive menu and command-line flags: display, stabilise, stabilised addition with
heatmap export, Dhar's burning algorithm, neutral-element check, recurrent count (brute force and Laplacian), inverse
search, eigen-decomposition (numerical and closed form).

## 0.1.0 - 0.2.5 - Pre-releases

Progressive builds leading to 1.0.0 (matrix display and stabilisation, CSV output, large matrices, heatmap export,
Dhar's algorithm, Laplacian and recurrent count, eigenvalues and inverse). Their JARs were removed from the working
tree; see git history.
