import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    Player testPlayer;
    Player testPlayer2;
    @BeforeEach
    void setUp(){

        testPlayer=new Player("Test", Player.Color.BLACK);
        testPlayer2=new Player("TestW", Player.Color.WHITE);
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
    @Test
    void getPip(){
        Piece blackPiece = new Piece(testPlayer,1);
        Piece blackPiece2 = new Piece(testPlayer,5);
        Piece blackPieceBar = new Piece(testPlayer,0);
        Piece blackPieceOff = new Piece(testPlayer,25);
        Piece whitePiece1 = new Piece(testPlayer2,24);
        Piece whitePiece2 = new Piece(testPlayer2,6);
        Piece whitePieceBar = new Piece(testPlayer2,25);
        Piece whitePieceOff = new Piece(testPlayer2,0);
        assertAll(()->assertEquals(24,blackPiece.getPip()),
                ()->assertEquals(20,blackPiece2.getPip()),
                ()->assertEquals(25,blackPieceBar.getPip()),
                ()->assertEquals(0,blackPieceOff.getPip()),
                ()->assertEquals(24,whitePiece1.getPip()),
                ()->assertEquals(6,whitePiece2.getPip()),
                ()->assertEquals(25,whitePieceBar.getPip()),
                ()->assertEquals(0,whitePieceOff.getPip()));
    }
}