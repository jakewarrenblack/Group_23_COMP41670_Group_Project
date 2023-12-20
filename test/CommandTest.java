import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    private Match match;
    private Game game;
    private Command command;
    private Player[] players;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @BeforeEach
    void setUp() {
        players = new Player[]{new Player("B", Player.Color.BLACK),new Player("W", Player.Color.WHITE)};
        match = new Match(1);
        match.addPlayer(0,players[0],true);
        match.addPlayer(1,players[1],false);
        game = match.newGame();
        command = new Command(match);
        command.newGame(game);
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
        command.acceptCommand("Move 1 3");
        assertEquals("You have moved from 1 to 3",outputStreamCaptor.toString().trim());
    }

    @Test
    void doubleCmd(){}
    @Test
    void dice(){
        command.acceptCommand("DICE 3 3 3 3");
        match.roll();
        command.acceptCommand("DICE 5 4");
        match.roll();
        assertEquals("The values of the dice have been set manually for the next roll\r\nB rolled 3, 3, 3, 3\r\nThe values of the dice have been set manually for the next roll\r\nB rolled 5, 4",outputStreamCaptor.toString().trim());
    }
    @Test
    void test(){}
    @Test
    void invalid(){
        command.acceptCommand("?>");
        assertEquals("I do not recognise ?> as a command",outputStreamCaptor.toString().trim());
    }
    @Test
    void listCommands(){
        ArrayList<String> exclude = new ArrayList<String>();
        exclude.add("MOVE");
        String[] expected = new String[]{"ROLL","QUIT","PIP","HINT","DOUBLE","DICE","TEST"};
        for (int i=0;i<expected.length;i++) {
            assertEquals(expected[i], command.listCommands(exclude)[i]);
        }
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