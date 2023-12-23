import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class OffBoardTest {
    private OffBoard offWhiteBarBlack, offBlackBarWhite;
    private Player testPlayerW, testPlayerB;
    private Point whiteOff, blackOff, whiteBar, blackBar;

    @BeforeEach
    void setUp(){
        offWhiteBarBlack = new OffBoard(0,25,13,14);
        offBlackBarWhite = new OffBoard(25,0,13,1);
        testPlayerW = new Player("Test W", Player.Color.WHITE);
        testPlayerB = new Player("Test B", Player.Color.BLACK);
        offWhiteBarBlack.setColor(false);
        blackBar = offWhiteBarBlack.activePoint();
        offWhiteBarBlack.setColor(true);
        whiteOff = offWhiteBarBlack.activePoint();
        offBlackBarWhite.setColor(false);
        blackOff = offBlackBarWhite.activePoint();
        offBlackBarWhite.setColor(true);
        whiteBar = offBlackBarWhite.activePoint();
    }
    @Test
    void setColor(){
        offWhiteBarBlack.setColor(false);
        assertEquals(blackBar,offWhiteBarBlack.activePoint());
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        assertEquals(whiteOff,offWhiteBarBlack.activePoint());
    }
    @Test
    void numPieces() {
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        // Should be no pieces to start
        assertEquals(0, offWhiteBarBlack.numPieces());
        // Then add some pieces
        for (int i=0;i<5;i++) {
            offWhiteBarBlack.addPiece(testPlayerW.getPiece(i));
        }
        assertEquals(5, offWhiteBarBlack.numPieces());
        // Change focus colour
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        assertEquals(0,offWhiteBarBlack.numPieces());
        for (int i=0;i<4;i++) {
            offWhiteBarBlack.addPiece(testPlayerB.getPiece(i));
        }
        assertEquals(4,offWhiteBarBlack.numPieces());

    }

    @Test
    void removePiece() {
        // Add a piece of each colour
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        // Now try to remove one. It should be black as that's the focus colour
        // The white piece should still be there
        Piece removed = offWhiteBarBlack.removePiece();
        assertAll(()->assertEquals(0, offWhiteBarBlack.numPieces()),
                ()->assertEquals(Player.Color.BLACK,removed.getColor()),
                ()->assertEquals(0,blackBar.numPieces()),
                ()->assertEquals(1,whiteOff.numPieces()));
        // Now try removing another piece - should remove null
        // as there are no black pieces left
        Piece noPiece = offWhiteBarBlack.removePiece();
        assertNull(noPiece);
        // Add a piece of each colour to the other OffBoard
        offBlackBarWhite.addPiece(testPlayerB.getPiece(2));
        offBlackBarWhite.addPiece(testPlayerW.getPiece(2));
        // Remove a piece. It should be white, the black one should still be there
        Piece removedW = offBlackBarWhite.removePiece();
        assertAll(()->assertEquals(0, offBlackBarWhite.numPieces()),
                ()->assertEquals(Player.Color.WHITE,removedW.getColor()),
                ()->assertEquals(0,whiteBar.numPieces()),
                ()->assertEquals(1,blackOff.numPieces()));
        // And if we try to remove another one it should return null
        Piece noPieceW = offBlackBarWhite.removePiece();
        assertNull(noPieceW);
    }


    @Test
    void isFull(){
        // Return false - you can put as many of your pieces on this as you like
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        assertFalse(offWhiteBarBlack.isFull());
        for (int i=0;i<5;i++) {
            offWhiteBarBlack.addPiece(testPlayerW.getPiece(i));
        }
        assertFalse(offWhiteBarBlack.isFull());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(5));
        assertFalse(offWhiteBarBlack.isFull());
    }
    @Test
    void isBlot(){
        // Return false - these can never be a blot
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        assertFalse(offWhiteBarBlack.isBlot());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(0));
        assertFalse(offWhiteBarBlack.isBlot());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        assertFalse(offWhiteBarBlack.isBlot());
    }
    @Test
    void isEmpty(){
        offBlackBarWhite.setColor(Player.Color.BLACK);
        assertTrue(offBlackBarWhite.isEmpty());
        offBlackBarWhite.addPiece(testPlayerB.getPiece(0));
        assertFalse(offBlackBarWhite.isEmpty());
    }
    @Test
    void isOff(){
        assertFalse(offWhiteBarBlack.isOff(Player.Color.BLACK));
        assertTrue(offWhiteBarBlack.isOff(Player.Color.WHITE));
        assertTrue(offBlackBarWhite.isOff(Player.Color.BLACK));
        assertFalse(offBlackBarWhite.isOff(Player.Color.WHITE));
    }
    @Test
    void isPlayers(){
        // Return true if the player is the focus at the moment and there are pieces on the bar/off
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        assertTrue(offWhiteBarBlack.isPlayers(testPlayerW));
        assertFalse(offWhiteBarBlack.isPlayers(testPlayerB));
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        assertTrue(offWhiteBarBlack.isPlayers(testPlayerB));
        assertFalse(offBlackBarWhite.isPlayers(testPlayerW));
    }
    @Test
    void getCoords(){
        // Always column, row
        int[] offB = new int[]{13,1};
        int[] offW = new int[]{13,14};
        int[] barB = new int[]{6,1};
        int[] barW = new int[]{6,14};
        offBlackBarWhite.setColor(Player.Color.BLACK);
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        assertArrayEquals(offB,offBlackBarWhite.getCoords());
        assertArrayEquals(barB,offWhiteBarBlack.getCoords());
        offBlackBarWhite.setColor(Player.Color.WHITE);
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        assertArrayEquals(offW,offWhiteBarBlack.getCoords());
        assertArrayEquals(barW,offBlackBarWhite.getCoords());
    }
    @Test
    void getTopChecker(){
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        // If it's empty, return null
        assertNull(offWhiteBarBlack.getTopChecker());
        // If there's a piece there return the top piece
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(0));
        assertEquals(testPlayerW.getPiece(0), offWhiteBarBlack.getTopChecker());
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        assertNull(offWhiteBarBlack.getTopChecker());
    }
    @Test
    void getColour() {
        // If there are no pieces, expect blank string
        assertEquals("  ", offWhiteBarBlack.getColour());
        // If there are white pieces and focus is white, expect W
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        assertEquals(" W ", offWhiteBarBlack.getColour());
        // If there are no black pieces and focus is black, expect blank string
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        assertEquals("  ",offWhiteBarBlack.getColour());
        // If focus is black and there are black pieces, expect B
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        assertEquals(" B ", offWhiteBarBlack.getColour());
    }
    @Test
    void getPip(){
        // Return 25 for BarBlack if Focus is Black, 0 if focus is White, vice verse
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(0));
        offBlackBarWhite.setColor(Player.Color.BLACK);
        offBlackBarWhite.addPiece(testPlayerB.getPiece(1));
        assertEquals(25, offWhiteBarBlack.getPip(testPlayerB));
        assertEquals(0,offBlackBarWhite.getPip(testPlayerB));
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        assertEquals(0,offWhiteBarBlack.getPip(testPlayerW));
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(0));
        offBlackBarWhite.setColor(Player.Color.WHITE);
        offBlackBarWhite.addPiece(testPlayerW.getPiece(1));
        assertEquals(0, offWhiteBarBlack.getPip(testPlayerW));
        assertEquals(25,offBlackBarWhite.getPip(testPlayerW));
    }


    @Test
    void getPosition() {
        // Position is the same, whatever the focus colour
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        offBlackBarWhite.setColor(Player.Color.WHITE);
        assertAll(()->assertEquals(0, offWhiteBarBlack.getPosition()), ()->assertEquals(25, offBlackBarWhite.getPosition()));
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        offBlackBarWhite.setColor(Player.Color.BLACK);
        assertAll((()->assertEquals(25,offBlackBarWhite.getPosition())), ()->assertEquals(0,offWhiteBarBlack.getPosition()));
    }
}