import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    Player testPlayer, testPlayer2;
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
    void getPip(){
        Piece[] blackPieces = new Piece[4], whitePieces = new Piece[4];
        int[] p1Values = {1,5,0,25}, p2Values = {24,6,25,0};
        int[] endValues = {24,20,25,0,24,6,25,0};

        for(int i=0; i < p1Values.length; i++) blackPieces[i] = new Piece(testPlayer,p1Values[i]);
        for(int i=0; i < p2Values.length; i++) whitePieces[i] = new Piece(testPlayer2,p2Values[i]);

        for(int i=0; i < endValues.length; i++) assertEquals(endValues[i],(i<4) ? blackPieces[i].getPip() : whitePieces[i-4].getPip());
    }

    @Test
    void getColor() {
        Piece testPiece = new Piece(testPlayer,1);
        assertEquals(Player.Color.BLACK,testPiece.getColor());
    }
    @Test
    void getPlayer(){
        assertEquals(testPlayer,new Piece(testPlayer,1).getPlayer());
        assertEquals(testPlayer2,new Piece(testPlayer2,1).getPlayer());
    }
}