import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    private Game game;
    private Command command;
    private Player[] players;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @BeforeEach
    void setUp() {
        players = new Player[]{new Player("B", Player.Color.BLACK),new Player("W", Player.Color.WHITE)};
        game = new Game(players,new Die());
        command = new Command(game);
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    @Test
    void roll(){
        command.acceptCommand("Roll");
        assertEquals("B rolled",outputStreamCaptor.toString().substring(0,8));
    }
    @Test
    void pip(){
        command.acceptCommand("Pip");
        assertEquals("B has a pip score of",outputStreamCaptor.toString().substring(0,20));
    }
    @Test
    void quit(){}
    @Test
    void move(){
        game.placePieces(players[0]);
        command.acceptCommand("Move 0 2");
        assertEquals("B moved a piece from 0 to 2",outputStreamCaptor.toString().trim());
    }
    @Test
    void noPieceToMove(){
        game.placePieces(players[0]);
        command.acceptCommand("Move 1 2");
        assertEquals("B's checkers are not on Point 1",outputStreamCaptor.toString().trim());
    }
    @Test
    void fullPoint(){
        game.placePieces(players[0]);
        game.placePieces(players[1]);
        command.acceptCommand("Move 0 5");
        assertEquals("Your opponent has too many checkers on Point 5 for you to move there",outputStreamCaptor.toString().trim());
    }
    @Test
    void doubleCmd(){}
    @Test
    void dice(){}
    @Test
    void test(){}
    @Test
    void invalid(){
        command.acceptCommand("?>");
        assertEquals("I do not recognise ?> as a command",outputStreamCaptor.toString().trim());
    }
    @Test
    void blank(){
        command.acceptCommand("");
        assertEquals("I do not recognise  as a command",outputStreamCaptor.toString().trim());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}