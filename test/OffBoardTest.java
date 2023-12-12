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
        // Add a piece of the focus colour
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        assertAll(()->assertEquals(0,blackBar.size()),
                ()->assertEquals(0,testPlayerW.getPiece(1).getPosition()),
                ()->assertEquals(1,whiteOff.size()),
                ()->assertEquals(0,testPlayerW.getPiece(1).getPip()));
        // Now try to add one of the non-focus colour
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));}
        );
        // Now change focus colour and try again
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        assertAll(()->assertEquals(1,blackBar.size()),
                ()->assertEquals(0,testPlayerB.getPiece(1).getPosition()),
                ()->assertEquals(25,testPlayerB.getPiece(1).getPip()));
    }

    @Test
    void removePiece() {
        // Add a piece of each colour
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        // Now try to remove one. It should be black as that's the focus colour
        // The white piece should still be there
        Piece removed = offWhiteBarBlack.removePiece();
        assertAll(()->assertEquals(0, offWhiteBarBlack.numPieces()),
                ()->assertEquals(Player.Color.BLACK,removed.getColor()),
                ()->assertEquals(0,blackBar.size()),
                ()->assertEquals(1,whiteOff.size()));
        // Now try removing another piece - should remove null
        // as there are no black pieces left
        Piece noPiece = offWhiteBarBlack.removePiece();
        assertNull(noPiece);
        // Add a piece of each colour to the other OffBoard
        offBlackBarWhite.setColor(Player.Color.BLACK);
        offBlackBarWhite.addPiece(testPlayerB.getPiece(2));
        offBlackBarWhite.setColor(Player.Color.WHITE);
        offBlackBarWhite.addPiece(testPlayerW.getPiece(2));
        // Remove a piece. It should be white, the black one should still be there
        Piece removedW = offBlackBarWhite.removePiece();
        assertAll(()->assertEquals(0, offBlackBarWhite.numPieces()),
                ()->assertEquals(Player.Color.WHITE,removedW.getColor()),
                ()->assertEquals(0,whiteBar.size()),
                ()->assertEquals(1,blackOff.size()));
        // And if we try to remove another one it should return null
        Piece noPieceW = offBlackBarWhite.removePiece();
        assertNull(noPieceW);
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
    void getPosition() {
        // Position is the same, whatever the focus colour
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        offBlackBarWhite.setColor(Player.Color.WHITE);
        assertAll(()->assertEquals(0, offWhiteBarBlack.getPosition()),
                ()->assertEquals(25, offBlackBarWhite.getPosition()));
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        offBlackBarWhite.setColor(Player.Color.BLACK);
        assertAll((()->assertEquals(25,offBlackBarWhite.getPosition())),
                ()->assertEquals(0,offWhiteBarBlack.getPosition()));
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
    void isPlayers(){
        // Return true if the player is the focus at the moment and there are pieces on the bar/off
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(1));
        assertTrue(offWhiteBarBlack.isPlayers(testPlayerW));
        assertFalse(offWhiteBarBlack.isPlayers(testPlayerB));
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(1));
        assertTrue(offWhiteBarBlack.isPlayers(testPlayerB));
        assertFalse(offWhiteBarBlack.isPlayers(testPlayerW));
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
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        assertFalse(offWhiteBarBlack.isOff(Player.Color.BLACK));
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        assertTrue(offWhiteBarBlack.isOff(Player.Color.WHITE));
        offBlackBarWhite.setColor(Player.Color.BLACK);
        assertTrue(offBlackBarWhite.isOff(Player.Color.BLACK));
        offBlackBarWhite.setColor(Player.Color.WHITE);
        assertFalse(offBlackBarWhite.isOff(Player.Color.WHITE));
    }
    @Test
    void getPip(){
        // Return 25 for BarBlack if Focus is Black, 0 if focus is White, vice verse
        // Return 0 in any case if point is empty of the focus colour
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        assertEquals(0,offWhiteBarBlack.getPip());
        offWhiteBarBlack.addPiece(testPlayerB.getPiece(0));
        offBlackBarWhite.setColor(Player.Color.BLACK);
        offBlackBarWhite.addPiece(testPlayerB.getPiece(1));
        assertEquals(25, offWhiteBarBlack.getPip());
        assertEquals(0,offBlackBarWhite.getPip());
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        assertEquals(0,offWhiteBarBlack.getPip());
        offWhiteBarBlack.addPiece(testPlayerW.getPiece(0));
        offBlackBarWhite.setColor(Player.Color.WHITE);
        offBlackBarWhite.addPiece(testPlayerW.getPiece(1));
        assertEquals(0, offWhiteBarBlack.getPip());
        assertEquals(25,offBlackBarWhite.getPip());
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
    void getCoords(){
        // Always column, row
        int[] offB = new int[]{13,1};
        int[] offW = new int[]{13,13};
        int[] barB = new int[]{6,1};
        int[] barW = new int[]{6,13};
        offBlackBarWhite.setColor(Player.Color.BLACK);
        offWhiteBarBlack.setColor(Player.Color.BLACK);
        assertArrayEquals(offB,offBlackBarWhite.getCoords());
        assertArrayEquals(barB,offWhiteBarBlack.getCoords());
        offBlackBarWhite.setColor(Player.Color.WHITE);
        offWhiteBarBlack.setColor(Player.Color.WHITE);
        assertArrayEquals(offW,offWhiteBarBlack.getCoords());
        assertArrayEquals(barW,offBlackBarWhite.getCoords());
    }


}