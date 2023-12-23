import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
    import java.io.ByteArrayInputStream;

class MatchTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private Match testMatch;
    private Player playerB;
    private Player playerW;
    private Game testGame;

    @BeforeEach
    void setUp(){
        testMatch = new Match(3);
        playerB = new Player("B", Player.Color.BLACK);
        playerW = new Player("W", Player.Color.WHITE);
        testMatch.addPlayer(1, playerW,false);
        testMatch.addPlayer(0,playerB,true);
        assertEquals(playerB,testMatch.players[0]);
        assertEquals(playerW,testMatch.players[1]);
        testGame=testMatch.newGame();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
    @Test
    void addPlayer() {
        testMatch.addPlayer(1, playerW,false);
        testMatch.addPlayer(0,playerB,true);
        assertEquals(playerB,testMatch.players[0]);
        assertEquals(playerW,testMatch.players[1]);
    }

    @Test
    void addPlayers() {
        testMatch.addPlayers(new String[]{"B","W"});
        assertEquals("B",testMatch.players[0].getName());
        assertEquals("W",testMatch.players[1].getName());
    }

    @Test
    void setCurrentPlayer() {
        testMatch.setCurrentPlayer(1);
        assertEquals(testMatch.currentPlayer,playerW);
        assertThrows(IndexOutOfBoundsException.class,
                () -> {testMatch.setCurrentPlayer(19);});
    }
    // This is included in setUp so don't test separately
    @Test
    void newGame() {
    }

    @Test
    void getGame() {
        assertEquals(testGame,testMatch.getGame());
    }

    @Test
    void setInitialPlayer() {
        Die.getInstance().setValues(new int[]{3,4});
        testMatch.setInitialPlayer();
        assertEquals(playerW,testMatch.currentPlayer);
    }

    @Test
    void nextTurn() {
        testMatch.nextTurn();
        assertEquals(playerW,testMatch.currentPlayer);
    }

    @Test
    void roll() {
        Die.getInstance().setValues(new int[]{3,4});
        testMatch.roll();
        assertEquals("B rolled 3, 4",outputStreamCaptor.toString().trim());
    }


    @Test
    void doubleBet() {
        provideInput("1");
        testMatch.doubleBet();
        assertEquals("The doubling",outputStreamCaptor.toString().split("\r\n")[4].substring(0,12));
        outputStreamCaptor.reset();
        provideInput("2");
        testMatch.doubleBet();
        assertEquals("W has reject",outputStreamCaptor.toString().split("\r\n")[4].substring(0,12));
    }

    @Test
    void pipScore() {
        testMatch.pipScore();
        assertEquals("B has a pip score of 167",outputStreamCaptor.toString().split("\r\n")[0].trim());
        assertEquals("W has a pip score of 167",outputStreamCaptor.toString().split("\r\n")[1].trim());

    }
}