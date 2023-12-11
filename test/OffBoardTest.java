import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class OffBoardTest {
    private OffBoard offWhiteBarBlack;
    private OffBoard offBlackBarWhite;
    private Player testPlayerW;
    private Player testPlayerB;
    private Stack whiteOff;
    private Stack blackBar;
    private Stack blackOff;
    private Stack whiteBar;

    @BeforeEach
    void setUp(){
        offWhiteBarBlack = new OffBoard(0,25,13,13);
        offBlackBarWhite = new OffBoard(25,0,13,1);
        testPlayerW = new Player("Test W", Player.Color.WHITE);
        testPlayerB = new Player("Test B", Player.Color.BLACK);
        offWhiteBarBlack.setColor(false);
        blackBar = offWhiteBarBlack.activeStack();
        offWhiteBarBlack.setColor(true);
        whiteOff = offWhiteBarBlack.activeStack();
        offBlackBarWhite.setColor(false);
        blackOff = offBlackBarWhite.activeStack();
        offBlackBarWhite.setColor(true);
        whiteBar = offBlackBarWhite.activeStack();
    }
    @Test
    void addPiece() {
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        assertAll(()->assertEquals(0,blackBar.size()),
                ()->assertEquals(0,testPlayerW.getPiece(1).getPosition()),
                ()->assertEquals(1,whiteOff.size()));
        offWhiteBarBlack.setColor(false);
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        assertAll(()->assertEquals(1,blackBar.size()),
                ()->assertEquals(0,testPlayerB.getPiece(1).getPosition()),
                ()->assertEquals(25,testPlayerB.getPiece(1).getPip()));
    }

    @Test
    void removePiece() {
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        Piece removed = offWhiteBarBlack.removePiece();
        assertAll(()->assertEquals(0, offWhiteBarBlack.numPieces()),
                ()->assertEquals(Player.Color.BLACK,removed.getColor()),
                ()->assertEquals(0,whiteOff.size()));
        Piece noPiece = offWhiteBarBlack.removePiece();
        assertNull(noPiece);
        offBlackBarWhite.addPiece(testPlayerW.getPiece(2));
        offBlackBarWhite.addPiece(testPlayerB.getPiece(2));
        Piece removedW = offBlackBarWhite.removePiece();
        assertAll(()->assertEquals(0, offBlackBarWhite.numPieces()),
                ()->assertEquals(Player.Color.WHITE,removedW.getColor()),
                ()->assertEquals(0,blackOff.size()));
        Piece noPieceW = offBlackBarWhite.removePiece();
        assertNull(noPieceW);
    }
    // TODO All tests below here need to be scrubbed
    @Test
    void numPieces() {
        assertEquals(0, offWhiteBarBlack.numPieces());
        for (int i=0;i<5;i++) {
            offWhiteBarBlack.addPiece(testPlayerW.getPiece(i));
        }
        assertEquals(5, offWhiteBarBlack.numPieces());
    }

    @Test
    void getPosition() {
        assertAll(()->assertEquals(1, offWhiteBarBlack.getPosition(true)),
                ()->assertEquals(2, offBlackBarWhite.getPosition(true)),
                ()->assertEquals(24, offWhiteBarBlack.getPosition(false)),
                ()->assertEquals(23, offBlackBarWhite.getPosition(false)));
    }

    @Test
    void getColour() {
        assertEquals("  ", offWhiteBarBlack.getColour());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        assertEquals(" W ", offWhiteBarBlack.getColour());
        offBlackBarWhite.addPiece(testPlayerB.getPiece(1));
        assertEquals(" B ", offBlackBarWhite.getColour());
    }
    @Test
    void isPlayers(){
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(0));
        assertTrue(offWhiteBarBlack.isPlayers(testPlayerB));
        assertFalse(offWhiteBarBlack.isPlayers(testPlayerW));
        assertFalse(offBlackBarWhite.isPlayers(testPlayerB));
        assertFalse(offBlackBarWhite.isPlayers(testPlayerW));
    }
    @Test
    void isFull(){
        assertFalse(offWhiteBarBlack.isFull());
        for (int i=0;i<5;i++) {
            offWhiteBarBlack.addPiece(testPlayerW.getPiece(i));
        }
        assertFalse(offWhiteBarBlack.isFull());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(5));
        assertTrue(offWhiteBarBlack.isFull());
    }
    @Test
    void isBlot(){
        assertFalse(offWhiteBarBlack.isBlot());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(0));
        assertTrue(offWhiteBarBlack.isBlot());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        assertFalse(offWhiteBarBlack.isBlot());
    }
    @Test
    void isEmpty(){
        assertTrue(offBlackBarWhite.isEmpty());
        offBlackBarWhite.addPiece(testPlayerW.getPiece(0));
        assertFalse(offBlackBarWhite.isEmpty());
    }
    @Test
    void isOff(){
        assertFalse(offWhiteBarBlack.isOff());
    }
    @Test
    void getPip(){
        offWhiteBarBlack.getPip();
        assertEquals(0, offWhiteBarBlack.getPip());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(0));
        offBlackBarWhite.addPiece(testPlayerB.getPiece(0));
        assertAll(()->assertEquals(1, offWhiteBarBlack.getPip()),
                ()->assertEquals(23, offBlackBarWhite.getPip()));
    }
    @Test
    void getTopChecker(){
        assertNull(offWhiteBarBlack.getTopChecker());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(0));
        assertEquals(testPlayerW.getPiece(0), offWhiteBarBlack.getTopChecker());
    }

}