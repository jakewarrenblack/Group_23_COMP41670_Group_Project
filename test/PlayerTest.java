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
    void setScore() {
    }

    @Test
    void getScore() {
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
    }

    @Test
    void pipScore() {
        // Start positions - pip score should be 167
        assertAll(()->assertEquals(167,testWhite.pipScore()),
                ()->assertEquals(167,testBlack.pipScore()));
        Board testBoard = new Board();
        // Move one black piece from 19 to 21, black Pip score should go down to 165
        testBoard.placePieces(testBlack);
        testBoard.addPiece(21,testBoard.removePiece(19));
        assertEquals(165,testBlack.pipScore());
        // Move one black piece to the bard - black Pip score should go up to 168
        testBoard.setColour(0, Player.Color.BLACK);
        testBoard.addPiece(0,testBoard.removePiece(21));
        assertEquals(186,testBlack.pipScore());
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
}