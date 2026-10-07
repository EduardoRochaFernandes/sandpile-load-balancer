package llbc.io;

import llbc.core.SandpileMatrix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MatrixReader")
class MatrixReaderTest {

    @Test
    @DisplayName("parses a valid square matrix and ignores blank lines")
    void parsesValid() {
        SandpileMatrix m = MatrixReader.parse(List.of("1,5,2", "", "4,4,2", "6,1,0", ""));
        assertThat(m.dimension()).isEqualTo(3);
        assertThat(m.get(0, 1)).isEqualTo(5);
        assertThat(m.get(2, 0)).isEqualTo(6);
    }

    @Test
    @DisplayName("rejects non-square input")
    void rejectsNonSquare() {
        assertThatThrownBy(() -> MatrixReader.parse(List.of("1,2,3", "4,5,6")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("square");
    }

    @Test
    @DisplayName("rejects non-integer and negative cells")
    void rejectsBadCells() {
        assertThatThrownBy(() -> MatrixReader.parse(List.of("1,x", "2,3")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Not an integer");
        assertThatThrownBy(() -> MatrixReader.parse(List.of("1,-2", "2,3")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Negative");
    }

    @Test
    @DisplayName("rejects empty input")
    void rejectsEmpty() {
        assertThatThrownBy(() -> MatrixReader.parse(List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
