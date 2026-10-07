package llbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Main CLI")
class MainTest {

    private final ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

    private int run(String... args) {
        return Main.run(args,
            new PrintStream(outBuf, true, StandardCharsets.UTF_8),
            new PrintStream(errBuf, true, StandardCharsets.UTF_8));
    }

    private String out() { return outBuf.toString(StandardCharsets.UTF_8); }
    private String err() { return errBuf.toString(StandardCharsets.UTF_8); }

    @Test
    @DisplayName("demo exits 0 and reports 100352 recurrent 3x3 configurations both ways")
    void demoRuns() {
        assertThat(run()).isZero();
        assertThat(out())
            .contains("262144 stable grids : 100352")
            .contains("reduced Laplacian            : 100352");
    }

    @Test
    @DisplayName("stabilise reads the sample CSV and prints a stable result")
    void stabiliseSample() {
        assertThat(run("stabilise", "input/matrix3.csv")).isZero();
        assertThat(out()).contains("Stabilised after").contains("Recurrent");
    }

    @Test
    @DisplayName("stabilise reports a missing file with exit code 1")
    void stabiliseMissingFile() {
        assertThat(run("stabilise", "input/does-not-exist.csv")).isEqualTo(1);
        assertThat(err()).contains("Cannot read file");
    }

    @Test
    @DisplayName("resilience rejects out-of-range dimensions")
    void resilienceRange() {
        assertThat(run("resilience", "99")).isEqualTo(1);
        assertThat(err()).contains("between 2 and 20");
    }

    @Test
    @DisplayName("unknown command exits 2")
    void unknownCommand() {
        assertThat(run("bogus")).isEqualTo(2);
    }
}
