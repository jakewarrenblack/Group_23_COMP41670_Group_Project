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
        testDie = Die.getInstance();
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
    void setManual(){
        int[] rolls = new int[]{1,2};
        testDie.setValues(rolls);
        List<Integer> currentValues = testDie.getCurrentValues();
        assertAll(()->assertEquals(rolls[0],currentValues.get(0)),
                ()->assertEquals(rolls[1],currentValues.get(1)));
        testDie.setValues(rolls);
        List<Integer> currentValues2 = testDie.roll();
        assertAll(()->assertEquals(rolls[0],currentValues2.get(0)),
                ()->assertEquals(rolls[1],currentValues2.get(1)));
        int[] rolls2 =new int[]{3,3,3,3};
        testDie.setValues(rolls2);
        List<Integer> currentValues3 = testDie.roll();
        assertAll(()->assertEquals(3,currentValues2.get(0)),
                ()->assertEquals(4,currentValues2.size()));
    }
    @Test
    void getCurrentValues() {
        List rolls = testDie.roll();
        assertAll(()->assertTrue(rolls.contains(testDie.getCurrentValues().get(0))),
                ()->assertTrue(rolls.contains(testDie.getCurrentValues().get(1))));
    }
}