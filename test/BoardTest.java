import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board testBoard;
    Log testLog;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @BeforeEach
    void setUp(){
        testBoard = new Board();
        testLog=new Log();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    @Test
    void placeWhitePieces(){
        Player testPlayer = new Player("Test", Player.Color.WHITE);
        testBoard.placePieces(testPlayer);
        assertAll(()->assertEquals(2,testBoard.numPieces(23)),
                ()->assertEquals(5,testBoard.numPieces(12)),
                ()->assertEquals(3,testBoard.numPieces(7)),
                ()->assertEquals(5,testBoard.numPieces(5)));
    }
    @Test
    void placeBlackPieces(){
        Player testPlayer = new Player("Test", Player.Color.BLACK);
        testBoard.placePieces(testPlayer);
        assertAll(()->assertEquals(2,testBoard.numPieces(0)),
                ()->assertEquals(5,testBoard.numPieces(11)),
                ()->assertEquals(3,testBoard.numPieces(16)),
                ()->assertEquals(5,testBoard.numPieces(18)));
    }
    @Test
    void placeAllPieces(){
        Player testPlayer = new Player("Test", Player.Color.BLACK);
        Player otherPlayer = new Player("Other", Player.Color.WHITE);
        testBoard.placePieces(testPlayer);
        testBoard.placePieces(otherPlayer);
        assertAll(()->assertEquals(2,testBoard.numPieces(23)),
                ()->assertEquals(" W ",testBoard.getColour(23)),
                ()->assertEquals(5,testBoard.numPieces(12)),
                ()->assertEquals(" W ",testBoard.getColour(23)),
                ()->assertEquals(3,testBoard.numPieces(7)),
                ()->assertEquals(" W ",testBoard.getColour(7)),
                ()->assertEquals(5,testBoard.numPieces(5)),
                ()->assertEquals(" W ",testBoard.getColour(5)),
                ()->assertEquals(2,testBoard.numPieces(0)),
                ()->assertEquals(" B ",testBoard.getColour(0)),
                ()->assertEquals(5,testBoard.numPieces(11)),
                ()->assertEquals(" B ",testBoard.getColour(11)),
                ()->assertEquals(3,testBoard.numPieces(16)),
                ()->assertEquals(" B ",testBoard.getColour(16)),
                ()->assertEquals(5,testBoard.numPieces(18)),
                ()->assertEquals(" B ",testBoard.getColour(18)));

    }
    @Test
    void printBlank(){
        testBoard.print(Player.Color.WHITE,testLog.recentLog(10));
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+--24 OFF\n |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                "                                                \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1- OFF\n",outputStreamCaptor.toString());
    }
    @Test
    void printLogs(){
        for (int i=0;i<15;i++){
            testLog.updateLog("This is my test number "+i);
        }
        outputStreamCaptor.reset();
        testBoard.print(Player.Color.WHITE,testLog.recentLog(10));
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+--24 OFF\n |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 5\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 6\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 7\n" +
                "                                                This is my test number 8\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 9\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 10\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 11\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 12\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 13\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 14\n" +
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1- OFF\n",outputStreamCaptor.toString());
    }
    @Test
    void testBoardWithPieces(){
        Player blackPlayer = new Player("Test", Player.Color.BLACK);
        testBoard.placePieces(blackPlayer);
        Player whitePlayer = new Player("Test", Player.Color.WHITE);
        testBoard.placePieces(whitePlayer);
        testBoard.print(whitePlayer.getColor(), testLog.recentLog(10));
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+--24 OFF\n" +
                " W  |  |  |  B  |     B  |  |  |  |  W    \n" +
                " W  |  |  |  B  |     B  |  |  |  |  W    \n" +
                " W  |  |  |  B  |     B  |  |  |  |  |    \n" +
                " W  |  |  |  |  |     B  |  |  |  |  |         \n" +
                " W  |  |  |  |  |     B  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                "                                                \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " B  |  |  |  |  |     W  |  |  |  |  |         \n" +
                " B  |  |  |  |  |     W  |  |  |  |  |         \n" +
                " B  |  |  |  W  |     W  |  |  |  |  |         \n" +
                " B  |  |  |  W  |     W  |  |  |  |  B         \n" +
                " B  |  |  |  W  |     W  |  |  |  |  B         \n" +
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1- OFF\n",outputStreamCaptor.toString());
    }

    @Test
    void getPoint() {
        assertEquals(10,testBoard.getPoint(10).getPosition());
    }

    @Test
    void getBoardPrint() {
    }
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}