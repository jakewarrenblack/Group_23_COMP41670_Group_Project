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
        assertAll(()->assertEquals(2,testBoard.numPieces(24)),
                ()->assertEquals(5,testBoard.numPieces(13)),
                ()->assertEquals(3,testBoard.numPieces(8)),
                ()->assertEquals(5,testBoard.numPieces(6)));
    }
    @Test
    void placeBlackPieces(){
        Player testPlayer = new Player("Test", Player.Color.BLACK);
        testBoard.placePieces(testPlayer);
        assertAll(()->assertEquals(2,testBoard.numPieces(1)),
                ()->assertEquals(5,testBoard.numPieces(12)),
                ()->assertEquals(3,testBoard.numPieces(17)),
                ()->assertEquals(5,testBoard.numPieces(19)));
    }
    @Test
    void placeAllPieces(){
        Player testPlayer = new Player("Test", Player.Color.BLACK);
        Player otherPlayer = new Player("Other", Player.Color.WHITE);
        testBoard.placePieces(testPlayer);
        testBoard.placePieces(otherPlayer);
        assertAll(()->assertEquals(2,testBoard.numPieces(24)),
                ()->assertEquals(" W ",testBoard.getColour(24)),
                ()->assertEquals(5,testBoard.numPieces(13)),
                ()->assertEquals(" W ",testBoard.getColour(13)),
                ()->assertEquals(3,testBoard.numPieces(8)),
                ()->assertEquals(" W ",testBoard.getColour(8)),
                ()->assertEquals(5,testBoard.numPieces(6)),
                ()->assertEquals(" W ",testBoard.getColour(6)),
                ()->assertEquals(2,testBoard.numPieces(1)),
                ()->assertEquals(" B ",testBoard.getColour(1)),
                ()->assertEquals(5,testBoard.numPieces(12)),
                ()->assertEquals(" B ",testBoard.getColour(12)),
                ()->assertEquals(3,testBoard.numPieces(17)),
                ()->assertEquals(" B ",testBoard.getColour(17)),
                ()->assertEquals(5,testBoard.numPieces(19)),
                ()->assertEquals(" B ",testBoard.getColour(19)));

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