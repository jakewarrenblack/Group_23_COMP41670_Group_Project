import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player testWhite;
    Player testBlack;
    @BeforeEach
    void setUp(){
        testWhite = new Player("Test", Player.Color.WHITE);
        testBlack = new Player("Also Test", Player.Color.BLACK);
    }
    @Test
    void getScore() {
        //NB - this test also covers getGameScore and loseGame methods
        assertEquals(0,testWhite.getScore());
        testBlack.loseGame(2);
        // Player at the start of game hasn't born off any pieces so is "gammoned"
        // doubling of 2 means 167 * 2 * 2 = 668 score
        assertEquals(668,testBlack.getScore());
    }
    @Test
    void printScore(){
        assertEquals("Test match score: 0",testWhite.printScore());
    }
    @Test
    void getName() {
        assertAll(()->assertEquals("Test",testWhite.getName()),
                ()->assertEquals("Also Test",testBlack.getName()));
    }

    @Test
    void getColor() {
        assertAll(()->assertEquals(Player.Color.WHITE,testWhite.getColor()),
                ()->assertEquals(Player.Color.BLACK,testBlack.getColor()));
    }

    @Test
    void getPiece() {
        assertEquals(24,testWhite.getPiece(0).getPosition());
    }
    @Test
    void numPieces(){
        assertEquals(15,testBlack.numPieces());
    }
    @Test
    void pipScore() {
        // Start positions - pip score should be 167
        assertAll(()->assertEquals(167,testWhite.pipScore()),
                ()->assertEquals(167,testBlack.pipScore()));
        Board testBoard = new Board(1,1,"Black score","White score");
        // Move one black piece from 19 to 21, black Pip score should go down to 165
        testBoard.placePieces(testBlack);
        testBoard.addPiece(21,testBoard.removePiece(19));
        assertEquals(165,testBlack.pipScore());
        // Move one black piece to the bar - black Pip score should go up to 168
        testBoard.setColour(0, Player.Color.BLACK);
        testBoard.addPiece(0,testBoard.removePiece(21));
        assertEquals(186,testBlack.pipScore());
    }
    @Test
    void piecePosition(){
        assertEquals(24,testWhite.piecePosition(0));
        assertEquals(1,testBlack.piecePosition(0));
    }
    @Test
    void startPosition(){
        assertEquals(24,testWhite.startPosition(0));
        assertEquals(19,testBlack.startPosition(14));
    }
    @Test
    void canMoveOff(){
        // In the start position the player cannot move any checkers off the board
        assertFalse(testBlack.canMoveOff());
        // When all the player's checkers are in the last quadrant (position>=19 for black) they
        // can start moving their checkers off the board
        int j=19;
        for (int i=0;i<testBlack.numPieces();i++){
            testBlack.getPiece(i).setPosition(j);
            if ((i+1)%6==0){j++;}
        }
        assertTrue(testBlack.canMoveOff());
        // What about when some pieces are already off?
        testBlack.getPiece(1).setPosition(25);
        assertTrue(testBlack.canMoveOff());
    }
    @Test
    void isBarred(){
        // Aren't barred in the start position
        assertFalse(testWhite.isBarred(24));
        // Aren't barred if they're trying to move the piece that's on the bar
        testWhite.getPiece(14).setPosition(25);
        assertFalse(testWhite.isBarred(25));
        // Are barred if they try to move a piece that's not on the bar
        testWhite.getPiece(0).setPosition(1);
        assertTrue(testWhite.isBarred(1));
    }
    @Test
    void hasWon(){
        // Has won when all checkers are in the off position
        for (int i=0;i<testWhite.numPieces();i++){
            assertFalse(testWhite.hasWon());
            testWhite.getPiece(i).setPosition(0);
        }
        assertTrue(testWhite.hasWon());
    }
    @Test
    void hasOff(){
        // True when some pieces are in the off position
        assertFalse(testWhite.hasOff());
        testWhite.getPiece(0).setPosition(0);
        assertTrue(testWhite.hasOff());
    }
    @Test
    void hasBar(){
        // True when some pieces are on the bar
        assertFalse(testWhite.hasBar());
        testWhite.getPiece(0).setPosition(25);
        assertTrue(testWhite.hasBar());
    }
}