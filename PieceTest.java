import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    Player testPlayer;
    @BeforeEach
    void setUp(){
        testPlayer=new Player("Test", Player.Color.BLACK);
    }

    @Test
    void setPosition() {
        Piece testPiece = new Piece(testPlayer,1);
        testPiece.setPosition(5);
        assertEquals(5,testPiece.getPosition());
    }

    @Test
    void getPosition() {
        Piece testPiece = new Piece(testPlayer,5);
        assertEquals(5,testPiece.getPosition());
    }

    @Test
    void getColor() {
        Piece testPiece = new Piece(testPlayer,1);
        assertEquals(Player.Color.BLACK,testPiece.getColor());
    }
}