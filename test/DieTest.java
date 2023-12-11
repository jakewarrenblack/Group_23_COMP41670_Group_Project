import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    Die testDie;
    List<Integer> rolls;
    @BeforeEach
    void setUp() {
        testDie = new Die();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }
    @Test
    void roll() {
        List rolls = testDie.roll();
        assertAll(()->assertInstanceOf(ArrayList.class,rolls),
                ()->assertTrue((int)rolls.get(0)>0&(int)rolls.get(0)<7));
    }

    @Test
    void getCurrentValues() {
        List rolls = testDie.roll();
        assertAll(()->assertTrue(rolls.contains(testDie.getCurrentValues().get(0))),
                ()->assertTrue(rolls.contains(testDie.getCurrentValues().get(1))));
    }
}