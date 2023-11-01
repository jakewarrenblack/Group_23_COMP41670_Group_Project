import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private final static InputStream systemIn = System.in;
    private final static PrintStream systemOut = System.out;
    private ByteArrayInputStream typeIn;
    private static ByteArrayOutputStream typeOut;
    private Game myGame;
    @BeforeEach
    void setUp() {
        myGame = new Game(new Player[2], new Die());
        Player blackPlayer = new Player("R", Player.Color.BLACK);
        Player whitePlayer = new Player("J", Player.Color.WHITE);
        typeOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(typeOut));
    }
    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
    @Test
    @DisplayName("Checks that game has been created")
    void testGame(){
        assertNotNull(myGame);
    }
    @Test
    void handleMove() {
    }

    @Test
    @DisplayName("Checks whether we can check the game state is won")
    void isGameWon() {
        assertFalse(myGame.isGameWon());
    }

    @Test
    @DisplayName("Checks whether we can check the game state is lost")
    void isGameLost() {
        assertFalse(myGame.isGameWon());
    }

    @Test
    @DisplayName("Checks we can update the game state")
    void setGameState() {
        assertAll(()->assertFalse(myGame.isGameWon()),
                ()->assertFalse(myGame.isGameLost()));
        myGame.setGameState(Game.GameState.WON);
        assertAll(()->assertTrue(myGame.isGameWon()),
                ()->assertFalse(myGame.isGameLost()));
        myGame.setGameState(Game.GameState.LOST);
        assertAll(()->assertFalse(myGame.isGameWon()),
                ()->assertTrue(myGame.isGameLost()));
    }

    @Test
    void addPlayer() {

    }

    @Test
    void addPlayers() {
    }

    @Test
    @DisplayName("Checks the correct message is sent to the console. Need to figure out how to capture multiple outputs to test different cases")
    void sendMessage() {
        String[] tests = new String[]{"Try this message"};
        for (String test:tests) {
            Game.sendMessage(test);
            assertEquals(test, typeOut.toString().trim());
        }
    }

    @Test
    void chooseOption() {
    }

    @Test
    void getInput() {
    }
}