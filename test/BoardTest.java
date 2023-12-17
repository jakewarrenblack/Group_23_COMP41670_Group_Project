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
        testBoard = new Board(1,1,"Black score","White score");
        testLog=new Log();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void constructor(){
        Board board = new Board(24,6,1,1,1,1,"Black score","White score");
        board.print(Player.Color.WHITE, testLog.recentLog(10));
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+-24-OFF    Black score\n |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         Game 1 of 1\n" +
                " |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         \n" +
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
    void colNames(){
        assertEquals("-1-",testBoard.colNames(1,6));
        assertEquals("-6-",testBoard.colNames(6,6));
        assertEquals("-7-",testBoard.colNames(7,6));
        assertEquals("-12",testBoard.colNames(12,6));
        assertEquals("-13",testBoard.colNames(13,6));
        assertEquals("18-",testBoard.colNames(18,6));
        assertEquals("-19",testBoard.colNames(19,6));
        assertEquals("24-",testBoard.colNames(24,6));
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
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+--24 OFF    Black score\n |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         Game 1 of 1\n" +
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
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1- OFF    White score\n",outputStreamCaptor.toString());
    }
    @Test
    void printLogs(){
        for (int i=0;i<15;i++){
            testLog.updateLog("This is my test number "+i);
        }
        outputStreamCaptor.reset();
        testBoard.print(Player.Color.WHITE,testLog.recentLog(10));
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+--24 OFF    Black score\n |  |  |  |  |  |     |  |  |  |  |  |    \n" +
                " |  |  |  |  |  |     |  |  |  |  |  |         Game 1 of 1\n" +
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
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1- OFF    White score\n",outputStreamCaptor.toString());
    }
    @Test
    void testBoardWithPieces(){
        Player blackPlayer = new Player("Test", Player.Color.BLACK);
        testBoard.placePieces(blackPlayer);
        Player whitePlayer = new Player("Test", Player.Color.WHITE);
        testBoard.placePieces(whitePlayer);
        testBoard.print(whitePlayer.getColor(), testLog.recentLog(10));
        assertEquals("-13-+--+--+--+-18-BAR-19-+--+--+--+--24 OFF    Black score\n" +
                " W  |  |  |  B  |     B  |  |  |  |  W    \n" +
                " W  |  |  |  B  |     B  |  |  |  |  W         Game 1 of 1\n" +
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
                "-12-+--+--+--+--7-BAR-6--+--+--+--+--1- OFF    White score\n",outputStreamCaptor.toString());
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
        // 14 Dec 23: Removed this validation - this is duplicating validation done in the Game class
        // Try it with off - expect to get null, cannot take pieces back on the board again
//        testBoard.setColour(25, Player.Color.BLACK);
//        testBoard.setColour(0, Player.Color.WHITE);
//        testBoard.addPiece(25,bPlayer.getPiece(1));
//        testBoard.addPiece(0,wPlayer.getPiece(1));
//        assertNull(testBoard.removePiece(25));
//        assertNull(testBoard.removePiece(0));
    }
    @Test
    void setColour(){
        // First check we get the right coordinates when setting colour of off and bar points
        testBoard.setColour(25, Player.Color.BLACK);
        assertArrayEquals(new int[]{13,1},testBoard.getPoint(25).getCoords());
        testBoard.setColour(0, Player.Color.WHITE);
        assertArrayEquals(new int[]{13,13},testBoard.getPoint(0).getCoords());

    }
    @Test
    void moveToBar(){
        Player bPlayer = new Player("Test", Player.Color.BLACK);
        Player wPlayer = new Player("Other", Player.Color.WHITE);
        testBoard.placePieces(bPlayer);
        testBoard.placePieces(wPlayer);
        // First try moving a piece that isn't a blot
        Point barB = testBoard.getBar(bPlayer);
        testBoard.moveToBar(1,new Log());
        barB.setColor(wPlayer.getColor());
        assertEquals(0,barB.numPieces());
        // Now try one that is a blot
        testBoard.addPiece(2,testBoard.removePiece(1));
        testBoard.moveToBar(1,new Log());
        assertEquals(1,barB.numPieces());
        assertEquals(0,testBoard.numPieces(1));

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
    void getBoardPrint() {
    }
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}