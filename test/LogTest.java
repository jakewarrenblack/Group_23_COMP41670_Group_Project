import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {
    private Log testLog;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @BeforeEach
    void setUp(){
        testLog = Log.getInstance();
        testLog.clearLog();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    @Test
    void updateLog() {
        testLog.updateLog("This is my test");
        assertEquals("This is my test", outputStreamCaptor.toString().trim());
    }

    @ParameterizedTest
    @CsvSource({"10, 10", "10, 5", "10, 10", "10, 15"})
    void testRecentLog(int arraySize, int logUpdates) {
        String[] tests = new String[arraySize];

        // Initialize the array with empty strings
        // this prevents an error: <null> but was: <> when comparing the arrays
        Arrays.fill(tests, "");

        for (int i = 0; i < logUpdates; i++) {
            testLog.updateLog("This is my test number " + i);
            if (i >= logUpdates - arraySize) {
                tests[i - (logUpdates - arraySize)] = "This is my test number " + i;
            }
        }

        assertArrayEquals(tests, testLog.recentLog(arraySize));
    }

}