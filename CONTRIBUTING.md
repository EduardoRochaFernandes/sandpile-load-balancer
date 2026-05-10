# Contributing

Thank you for your interest in this project. As this is primarily a personal portfolio and learning repository, contributions are welcome in the form of bug reports, suggestions, and improvements.

---

## Reporting Issues

If you find a bug or unexpected behaviour:

1. Check existing issues first to avoid duplicates.
2. Open a new issue with:
   - A clear title and description
   - Steps to reproduce the problem
   - Expected vs. actual behaviour
   - Java version and OS if relevant
   - Sample CSV input that triggers the issue (if applicable)

---

## Suggesting Enhancements

Open an issue with the `enhancement` label. Ideas particularly welcome around:

- Additional matrix operations from sandpile theory
- Performance improvements for large matrices (parallelism, sparse representation)
- Cybersecurity/network-resilience analysis extensions
- A proper build system (Maven or Gradle integration)
- Visualisation improvements (animated GIF output, JavaFX UI)

---

## Pull Requests

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Keep changes focused — one concern per PR.
4. Add or update tests in `SandpileTests.java` for any logic changes.
5. Update `CHANGELOG.md` under an `[Unreleased]` section.
6. Submit a pull request with a clear description of what was changed and why.

---

## Code Style

- Standard Java naming conventions (camelCase methods, PascalCase classes, UPPER_SNAKE constants)
- Javadoc on all public methods
- Prefer `for-each` loops over index-based where the index is not needed
- No magic numbers — use named constants
- All CSV file paths must be validated before reading

---

## Academic Integrity Note

This repository is a personal refactor of an academic project. If you are a student working on a similar assignment, please do not copy this code for submission — use it as a reference for understanding the concepts only.
