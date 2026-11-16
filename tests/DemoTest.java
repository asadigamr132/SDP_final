import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class DemoTest {
    @Test
    void mainDemonstratesAllPatternsAndCompletesRental() {
        String output = captureOutput(() -> Main.main(new String[0]));

        for (String pattern : new String[]{"FACTORY METHOD", "STRATEGY", "BUILDER",
                "FACADE", "DECORATOR", "OBSERVER", "STATE"}) {
            assertTrue(output.contains(pattern), pattern);
        }
        assertTrue(output.contains("23500.0"));
        assertTrue(output.contains("Second rental rejected:"));
        assertTrue(output.contains("Olzhas: Toyota Camry (vehicle 1) is now available"));
        assertFalse(output.contains("Alisher: Toyota Camry"));
        assertTrue(output.contains("State after return: Available"));
    }

    @Test
    void customerRejectsMissingName() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(null));
        assertThrows(IllegalArgumentException.class, () -> new Customer(" "));
    }

    private String captureOutput(Runnable action) {
        PrintStream previous = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(output);
            action.run();
        } finally {
            System.setOut(previous);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }
}
