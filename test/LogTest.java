import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {
    private Log testLog;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @BeforeEach
    void setUp(){
        testLog = Log.getInstance();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    @Test
    void updateLog() {
        testLog.updateLog("This is my test");
        assertEquals("This is my test", outputStreamCaptor.toString()
                .trim());
    }
    @Test
    void recentLogEmpty(){
        String[] tests = new String[10];
        for (int i=0;i<10;i++){
            tests[9-i]="";
        }
        assertArrayEquals(tests,testLog.recentLog(10));
    }
    @Test
    void recentLogHalfFull(){
        String[] tests = new String[10];
        for (int i=0;i<5;i++){
            tests[i]="";
        }
        for (int i=5;i<10;i++){
            tests[i]="This is my test number "+i;
            testLog.updateLog("This is my test number "+i);
        }
        assertArrayEquals(tests,testLog.recentLog(10));
    }
    @Test
    void recentLogFull() {
        String[] tests = new String[10];
        for (int i=0;i<10;i++){
            testLog.updateLog("This is my test number "+i);
            tests[i]="This is my test number "+i;
        }
        assertArrayEquals(tests,testLog.recentLog(10));
    }
    @Test
    void recentLogFulltoBursting() {
        String[] tests = new String[10];
        for (int i=0;i<15;i++){
            testLog.updateLog("This is my test number "+i);
            if (i>4) {
                tests[i-5] = "This is my test number " + i;
            }
        }
        assertArrayEquals(tests,testLog.recentLog(10));
    }
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}