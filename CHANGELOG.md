# Changelog

All notable changes to this project are documented here.

---

## [2.0.0] — Personal Refactor & Maven Migration (2025)

This release represents a complete personal refactor of the original academic project, undertaken independently for learning and portfolio purposes.

### Added
- **Maven build system** (`pom.xml`) with Apache Commons Math 4, JUnit 5, AssertJ
- **53 JUnit 5 unit tests** across 5 test classes — all passing
- `SandpileConfig.java` — central constants class (no more magic numbers)
- `EigenResult.java` — immutable value object for eigenvalue/eigenvector pairs
- `NeutralElement.java` — neutral element verification and inverse search extracted to own class
- `CHANGELOG.md`, `CONTRIBUTING.md`, `LICENSE` files
- `.gitignore` covering Java build artefacts, IDE files, and generated outputs
- `scripts/run.sh` and `scripts/run.bat` for convenient execution
- `src/` directory tree following standard Java project conventions
- `security/ResilienceAnalyser.java` — cybersecurity-oriented analysis helpers mapping sandpile properties to network resilience concepts
- Comprehensive inline Javadoc documentation on all public methods
- Detailed `README.md` rewritten in English with cybersecurity framing
- `docs/` folder for images and supplementary documentation
- Separate `input/` and `output/` directories at the project root (in addition to release-specific ones)
- `SandpileTests.java` unit test class extracted into `src/test/`

### Changed
- Project renamed from `lapr1-2526-DI02-Repo` to `sandpile-load-balancer` for public clarity
- README completely rewritten: English, professional structure, badges, cybersecurity section, usage examples
- Source code reorganised into logical packages: `core`, `io`, `math`, `cli`, `util`, `security`
- Removed IDE-specific files (`.idea/`, `.iml`) from version control
- Removed macOS `.DS_Store` files
- Standardised line endings (CRLF → LF)
- Improved inline comments throughout `Main.java` for readability
- Typos and inconsistencies in output messages corrected

### Fixed
- `HeatmapImageWriter` extension label mismatch: output files are PNG internally but labelled `.jpg` — documented this behaviour clearly
- `GenerateMatricesSimpleVersion.java` was orphaned with no references — moved to `util/` as a documented helper

---

## [1.0.0] — Final Academic Release (2025)

Original project submission for LAPR1 at ISEP, first semester 2024/2025.

### Features at submission
- 10 functionalities via interactive and non-interactive CLI
- Matrix stabilisation with heatmap export
- Dhar's Burning Algorithm
- Neutral element verification
- Recurrent matrix count (brute-force and Laplacian)
- Inverse matrix search
- Eigenvalue/eigenvector decomposition (numerical + closed-form)
- CSV input/output

**Team:** Bruno Silva (1250800), Afonso Martins (1250698), Martim Pereira (1251260), Eduardo Fernandes (1250907)

---

## [0.2.5] — Pre-release

Progressive development releases leading to final submission. See `releases/` folder for compiled JARs of each version.

- `pre-release_0.1.0` — Initial matrix display and stabilisation
- `pre-release_0.2.0` — CSV output added
- `pre-release_0.2.1` — Large matrix support (up to 1000×1000)
- `pre-release_0.2.2` — Heatmap image export
- `pre-release_0.2.3` — Dhar's Burning Algorithm and neutral element
- `pre-release_0.2.4` — Laplacian and recurrent count
- `pre-release_0.2.5` — Eigenvalues and inverse matrix
