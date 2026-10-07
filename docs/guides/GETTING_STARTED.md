# Getting Started

## Option A - GitHub Codespaces (no install)

Click *Open in GitHub Codespaces* in the README. When the Codespace is ready, run:

```bash
java -jar target/sandpile-load-balancer-2.0.0.jar
```

## Option B - local build

Prerequisites: Java 17+ (`java -version`) and Maven 3.6+ (`mvn -version`).

```bash
git clone https://github.com/EduardoRochaFernandes/sandpile-load-balancer.git
cd sandpile-load-balancer
mvn package                                                   # builds and runs the tests
java -jar target/sandpile-load-balancer-2.0.0.jar             # demo
java -jar target/sandpile-load-balancer-2.0.0.jar stabilise input/matrix5.csv
java -jar target/sandpile-load-balancer-2.0.0.jar resilience 5
```

To stabilise your own load matrix, save it as CSV (see [INPUT_FORMAT.md](INPUT_FORMAT.md)), for example:

```
2,1,0,3
0,3,2,1
1,0,3,2
3,2,1,0
```

and run `stabilise mymatrix.csv`.

## Option C - the original pre-compiled CLI

Requires **Java 21+** (the JAR is compiled for class-file version 65).

```bash
./scripts/run.sh                                    # interactive menu (Windows: scripts\run.bat)
./scripts/run.sh -f 2 -a matrix5.csv -o result.txt  # non-interactive; results go to releases/final-release_1.0.0/output/
```

See the [CLI reference](CLI_REFERENCE.md).

## Troubleshooting

| Problem | Solution |
|---|---|
| `java` or `mvn` not found | Install a JDK 17+ and Maven, or use Codespaces |
| `UnsupportedClassVersionError ... 65.0` when running `main.jar` | The original JAR needs Java 21+ |
| `Cannot read file` | Check the path to the CSV (relative to where you run the command) |
| `Matrix must be square` / `Not an integer` | Fix the CSV; see [INPUT_FORMAT.md](INPUT_FORMAT.md) |
