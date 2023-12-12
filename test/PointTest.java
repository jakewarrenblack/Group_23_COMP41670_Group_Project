import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    private Point firstPoint;
    private Point secondPoint;
    private Player testPlayerW;
    private Player testPlayerB;
    @BeforeEach
    void setUp(){
        firstPoint = new Point(1,24,14,12);
        secondPoint = new Point(2,23,14,11);
        testPlayerW = new Player("Test W", Player.Color.WHITE);
        testPlayerB = new Player("Test B", Player.Color.BLACK);
    }
    @Test
    void addPiece() {
        firstPoint.addPiece(testPlayerW.getPiece(1));
        assertAll(()->assertEquals(1,firstPoint.numPieces()),
                ()->assertEquals(1,testPlayerW.getPiece(1).getPosition()));
        secondPoint.addPiece(testPlayerB.getPiece(1));
        assertAll(()->assertEquals(1,secondPoint.numPieces()),
                ()->assertEquals(23,testPlayerB.getPiece(1).getPip()));
    }

    @Test
    void removePiece() {
        firstPoint.addPiece(testPlayerW.getPiece(1));
        Piece removed = firstPoint.removePiece();
        assertAll(()->assertEquals(0,firstPoint.numPieces()),
                ()->assertEquals(Player.Color.WHITE,removed.getColor()));
        Piece noPiece = firstPoint.removePiece();
        assertNull(noPiece);
    }

    @Test
    void numPieces() {
        assertEquals(0,firstPoint.numPieces());
        for (int i=0;i<5;i++) {
            firstPoint.addPiece(testPlayerW.getPiece(i));
        }
        assertEquals(5,firstPoint.numPieces());
    }

    @Test
    void getPosition() {
        assertAll(()->assertEquals(1,firstPoint.getPosition()),
                ()->assertEquals(2,secondPoint.getPosition()));
    }

    @Test
    void getColour() {
        assertEquals("  ",firstPoint.getColour());
        firstPoint.addPiece(testPlayerW.getPiece(1));
        assertEquals(" W ",firstPoint.getColour());
        secondPoint.addPiece(testPlayerB.getPiece(1));
        assertEquals(" B ",secondPoint.getColour());
    }
    @Test
    void isPlayers(){
        firstPoint.addPiece(testPlayerB.getPiece(0));
        assertTrue(firstPoint.isPlayers(testPlayerB));
        assertFalse(firstPoint.isPlayers(testPlayerW));
        assertFalse(secondPoint.isPlayers(testPlayerB));
        assertFalse(secondPoint.isPlayers(testPlayerW));
    }
    @Test
    void isFull(){
        assertFalse(firstPoint.isFull());
        for (int i=0;i<5;i++) {
            firstPoint.addPiece(testPlayerW.getPiece(i));
        }
        assertFalse(firstPoint.isFull());
        firstPoint.addPiece(testPlayerW.getPiece(5));
        assertTrue(firstPoint.isFull());
    }
    @Test
    void isBlot(){
        assertFalse(firstPoint.isBlot());
        firstPoint.addPiece(testPlayerW.getPiece(0));
        assertTrue(firstPoint.isBlot());
        firstPoint.addPiece(testPlayerW.getPiece(1));
        assertFalse(firstPoint.isBlot());
    }
    @Test
    void isEmpty(){
        assertTrue(firstPoint.isEmpty());
        firstPoint.addPiece(testPlayerW.getPiece(0));
        assertFalse(firstPoint.isEmpty());
    }
    @Test
    void isOff(){
        assertFalse(firstPoint.isOff(testPlayerW.getColor()));
    }
    @Test
    void getPip(){
        firstPoint.getPip();
        assertEquals(0,firstPoint.getPip());
        firstPoint.addPiece(testPlayerW.getPiece(0));
        secondPoint.addPiece(testPlayerB.getPiece(0));
        assertAll(()->assertEquals(1,firstPoint.getPip()),
                ()->assertEquals(23,secondPoint.getPip()));
    }
    @Test
    void getTopChecker(){
        assertNull(firstPoint.getTopChecker());
        firstPoint.addPiece(testPlayerW.getPiece(0));
        assertEquals(testPlayerW.getPiece(0),firstPoint.getTopChecker());
    }
    @Test
    void getCoords(){
        assertArrayEquals(new int[]{14,12},firstPoint.getCoords());
    }

}