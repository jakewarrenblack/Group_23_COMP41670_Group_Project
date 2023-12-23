import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board testBoard;
    Log testLog;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @BeforeEach
    void setUp(){
        testBoard = new Board(1,1,"Black score","White score");
        testLog=Log.getInstance();
        testLog.clearLog();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void createPoints(){
        Point[] points = testBoard.createPoints();
        assertEquals(26,points.length);
        assertInstanceOf(OffBoard.class, points[0]);
        for (int i=0;i<points.length;i++){
            assertEquals(points.length-i-1,points[i].getPip(new Player("B", Player.Color.BLACK)));
        }
    }

    @Test
    void createPrintOut(){
        String[][] printOut = testBoard.createPrintOut("Black Test","White Test");
        assertEquals(16,printOut.length);
        assertEquals(15,printOut[0].length);
        assertEquals("    White Test",printOut[15][14]);
    }

    @Test
    void colNames(){
        Map<Integer, String> testValues = Map.of(1,"-1-",6,"-6-",7,"-7-",12,"-12",13,"-13",18,"18-",19,"-19",24,"24-");

        for (Map.Entry<Integer, String> testCase : testValues.entrySet()) {
            assertEquals(testCase.getValue(), testBoard.colNames(testCase.getKey(), 6));
        }
    }

    // we can use the @EnumSource annotation to test all the possible values of the enum, combining the black and white test cases
    @Test
    void colIncrement(){
        assertEquals(-1,testBoard.colIncrement(0,12,6));
        assertEquals(-2,testBoard.colIncrement(7,12,6));
        assertEquals(0,testBoard.colIncrement(13,12,6));
        assertEquals(1,testBoard.colIncrement(14,12,6));
        assertEquals(2,testBoard.colIncrement(19,12,6));
    }
    @Test
    void removePiece(){
        Player bPlayer = new Player("Test", Player.Color.BLACK);
        Player wPlayer = new Player("Other", Player.Color.WHITE);
        testBoard.placePieces(bPlayer);
        testBoard.placePieces(wPlayer);
        // Remove a piece from a normal point
        // Expect to get the piece and be left with one piece on the point
        Piece testPiece = testBoard.removePiece(24);
        assertEquals(Player.Color.WHITE,testPiece.getColor());
        assertEquals(1,testBoard.numPieces(24));
        // Now do bars - expect to get the piece we've placed there back again
        // and be left with no pieces on the bar
        testBoard.setColour(25, Player.Color.WHITE);
        testBoard.setColour(0, Player.Color.BLACK);
        testBoard.addPiece(25,wPlayer.getPiece(0));
        testBoard.addPiece(0,bPlayer.getPiece(0));
        Piece wPiece = testBoard.removePiece(25);
        Piece bPiece = testBoard.removePiece(0);
        assertEquals(Player.Color.WHITE,wPiece.getColor());
        assertEquals(0,testBoard.numPieces(25));
        assertEquals(Player.Color.BLACK,bPiece.getColor());
        assertEquals(0,testBoard.numPieces(0));
    }

    @Test
    void addPiece(){
        Piece testPiece = new Piece(new Player("B",Player.Color.BLACK),1);
        testBoard.addPiece(1,testPiece);
        assertEquals(testPiece,testBoard.getPoint(1).getTopChecker());
    }

    // we can use the @EnumSource annotation to test all the possible values of the enum,
    // combining the black and white test cases
    @ParameterizedTest
    @EnumSource(Player.Color.class)
    void placePiecesTest(Player.Color color){
        Player testPlayer = new Player("Test", color);
        testBoard.placePieces(testPlayer);


        if (color == Player.Color.WHITE) {
            assertAll(()->assertEquals(2,testBoard.numPieces(24)),
                    ()->assertEquals(5,testBoard.numPieces(13)),
                    ()->assertEquals(3,testBoard.numPieces(8)),
                    ()->assertEquals(5,testBoard.numPieces(6)));
        } else {
            assertAll(()->assertEquals(2,testBoard.numPieces(1)),
                    ()->assertEquals(5,testBoard.numPieces(12)),
                    ()->assertEquals(3,testBoard.numPieces(17)),
                    ()->assertEquals(5,testBoard.numPieces(19)));
        }
    }


    @Test
    void mumPieces(){
        Player testPlayer = new Player("Test", Player.Color.BLACK);
        testBoard.placePieces(testPlayer);
        assertAll(()->assertEquals(2,testBoard.numPieces(1)),
                ()->assertEquals(5,testBoard.numPieces(12)),
                ()->assertEquals(3,testBoard.numPieces(17)),
                ()->assertEquals(5,testBoard.numPieces(19)));
    }
    @Test
    void emptySpace(){
        assertEquals(" | ",testBoard.emptySpace(1));
        assertEquals("   ",testBoard.emptySpace(0));
        assertEquals("   ",testBoard.emptySpace(25));
    }
    // NB - the updateBoard method is only called from the print() method so is fully tested by this set of tests
    @Test
    void printBlank(){
        testBoard.print(Player.Color.WHITE,testLog.recentLog(10),"");
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+-24-OFF    Black score\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         Game 1 of 1\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                "                                               \n" +
                "                                               \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1-OFF    White score\n",outputStreamCaptor.toString());
    }
    @Test
    void printLogs(){
        for (int i=0;i<15;i++){
            testLog.updateLog("This is my test number "+i);
        }
        outputStreamCaptor.reset();
        testBoard.print(Player.Color.WHITE,testLog.recentLog(10),"");
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+-24-OFF    Black score\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         Game 1 of 1\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 5\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 6\n" +
                "                                               This is my test number 7\n" +
                "                                               This is my test number 8\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 9\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 10\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 11\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 12\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 13\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         This is my test number 14\n" +
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1-OFF    White score\n",outputStreamCaptor.toString());
    }
    @Test
    void testBoardWithPieces(){
        Player blackPlayer = new Player("Test", Player.Color.BLACK);
        testBoard.placePieces(blackPlayer);
        Player whitePlayer = new Player("Test", Player.Color.WHITE);
        testBoard.placePieces(whitePlayer);
        testBoard.print(whitePlayer.getColor(), testLog.recentLog(10),"Double Status");
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+-24-OFF    Black score\n" +
                " W  |  |  |  B  |     B  |  |  |  |  W         Double Status\n" +
                " W  |  |  |  B  |     B  |  |  |  |  W         Game 1 of 1\n" +
                " W  |  |  |  B  |     B  |  |  |  |  |    \n" +
                " W  |  |  |  |  |     B  |  |  |  |  |    \n" +
                " W  |  |  |  |  |     B  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                "                                               \n" +
                "                                               \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " B  |  |  |  |  |     W  |  |  |  |  |         \n" +
                " B  |  |  |  |  |     W  |  |  |  |  |         \n" +
                " B  |  |  |  W  |     W  |  |  |  |  |         \n" +
                " B  |  |  |  W  |     W  |  |  |  |  B         \n" +
                " B  |  |  |  W  |     W  |  |  |  |  B         \n" +
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1-OFF    White score\n",outputStreamCaptor.toString());
    }
    @Test
    void getPoint() {
        assertEquals(10,testBoard.getPoint(10).getPosition());
    }
    @Test
    void getBar(){
        Player playerB = new Player("Black", Player.Color.BLACK);
        Player playerW = new Player("White", Player.Color.WHITE);
        Point barB = testBoard.getBar(playerB);
        Point barW = testBoard.getBar(playerW);
        assertEquals(0,barB.getPosition());
        assertEquals(25,barW.getPosition());
    }
    @Test
    void getOff(){
        Player playerB = new Player("Black", Player.Color.BLACK);
        Player playerW = new Player("White", Player.Color.WHITE);
        Point offB = testBoard.getOff(playerB);
        Point offW = testBoard.getOff(playerW);
        assertEquals(0,offW.getPosition());
        assertEquals(25,offB.getPosition());
    }
    @Test
    void hasBarPieces(){
        Player playerB = new Player("Test", Player.Color.BLACK);
        Player playerW = new Player("White", Player.Color.WHITE);
        assertFalse(testBoard.hasBarPieces(playerB));
        testBoard.addPiece(25,playerB.getPiece(1));
        assertFalse(testBoard.hasBarPieces(playerB));
        testBoard.addPiece(0,playerB.getPiece(0));
        assertTrue(testBoard.hasBarPieces(playerB));
        assertFalse(testBoard.hasBarPieces(playerW));
    }
    @Test
    void setColour(){
        // First check we get the right coordinates when setting colour of off and bar points
        testBoard.setColour(25, Player.Color.BLACK);
        assertArrayEquals(new int[]{13,1},testBoard.getPoint(25).getCoords());
        testBoard.setColour(0, Player.Color.WHITE);
        assertArrayEquals(new int[]{13,14},testBoard.getPoint(0).getCoords());

    }
    @Test
    void moveToBar(){
        Player bPlayer = new Player("Test", Player.Color.BLACK);
        Player wPlayer = new Player("Other", Player.Color.WHITE);
        testBoard.placePieces(bPlayer);
        testBoard.placePieces(wPlayer);
        // First try moving a piece that isn't a blot
        Point barB = testBoard.getBar(bPlayer);
        testBoard.moveToBar(1,Log.getInstance());
        barB.setColor(wPlayer.getColor());
        assertEquals(0,barB.numPieces());
        // Now try one that is a blot
        testBoard.addPiece(2,testBoard.removePiece(1));
        testBoard.moveToBar(1,Log.getInstance());
        assertEquals(1,barB.numPieces());
        assertEquals(0,testBoard.numPieces(1));

    }
}