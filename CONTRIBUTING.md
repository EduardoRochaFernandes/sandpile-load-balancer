# Contributing

This is a small portfolio and learning project. Bug reports, questions and suggestions are welcome as issues.

## Development

```bash
mvn verify        # compile, run the 62 tests, build the fat JAR (Java 17+, Maven 3.6+)
```

Or open the repository in GitHub Codespaces (see the README); the dev container has everything installed.

## Pull requests

1. Keep each PR focused on one change.
2. Add or update JUnit 5 tests next to the code (`src/test/java/llbc/...`).
3. Make sure `mvn verify` passes; CI runs the same on Java 17 and 21.
4. Add a line under `[Unreleased]` in `CHANGELOG.md`.
5. Use [Conventional Commit](https://www.conventionalcommits.org/) messages (`feat:`, `fix:`, `docs:`, ...).

## Style

Standard Java naming, Javadoc on public methods, named constants instead of magic numbers (`SandpileConfig`),
English for code, comments and documentation. See `.editorconfig`.

## Academic integrity

The project started as a university assignment. If you are a student with a similar assignment, use this repository as
a reference for the concepts only; do not submit it as your own work.
