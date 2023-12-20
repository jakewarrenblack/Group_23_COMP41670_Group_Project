import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    Die testDie;

    @BeforeEach
    void setUp() {
        testDie = Die.getInstance();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

    @Test
    void roll() {
        List<Integer> rolls = testDie.roll();
        assertAll(()->assertInstanceOf(ArrayList.class,rolls),
                ()->assertTrue(rolls.get(0) > 0 && rolls.get(0) < 7));
    }

    @Test
    void setManual() {
        // Loop over the possible test cases
        // Previously we had redundant calls to setValues and roll
        int[][] testCases = new int[][]{{1, 2}, {3, 3, 3, 3}};

        for (int[] rolls : testCases) {
            testDie.setValues(rolls);
            List<Integer> currentValues = testDie.roll();
            assertAll(
                    () -> assertEquals(rolls[0], currentValues.get(0)),
                    () -> assertEquals(rolls.length, currentValues.size())
            );
        }
    }

    @Test
    void getCurrentValues() {
        List<Integer> rolls = testDie.roll();
        assertAll(()->assertTrue(rolls.contains(testDie.getCurrentValues().get(0))),
                ()->assertTrue(rolls.contains(testDie.getCurrentValues().get(1))));
    }
}