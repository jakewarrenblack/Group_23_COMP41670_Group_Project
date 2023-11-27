import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    private Point firstPoint;
    private Point secondPoint;
    private Player testPlayer;
    @BeforeEach
    void setUp(){
        firstPoint = new Point(1,22,14,12);
        secondPoint = new Point(2,21,14,11);
        testPlayer = new Player("Test", Player.Color.WHITE);
    }
    @Test
    void addPiece() {
        firstPoint.addPiece(testPlayer.getPiece(1));
        assertEquals(1,firstPoint.numPieces());
    }

    @Test
    void removePiece() {
        firstPoint.addPiece(testPlayer.getPiece(1));
        Piece removed = firstPoint.removePiece();
        assertAll(()->assertEquals(0,firstPoint.numPieces()),
                ()->assertEquals(Player.Color.WHITE,removed.getColor()));
    }

    @Test
    void numPieces() {
        for (int i=0;i<5;i++) {
            firstPoint.addPiece(testPlayer.getPiece(i));
        }
        assertEquals(5,firstPoint.numPieces());
    }

    @Test
    void getPosition() {
        assertAll(()->assertEquals(1,firstPoint.getPositionWhite()),
                ()->assertEquals(2,secondPoint.getPositionWhite()));
    }

    @Test
    void getColour() {
        firstPoint.addPiece(testPlayer.getPiece(1));
        assertEquals(" W ",firstPoint.getColour());
    }
}