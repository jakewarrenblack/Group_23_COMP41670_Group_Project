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
        typeOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(typeOut));
    }
    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
    @Test
    void handleMove() {
    }

    @Test
    void isGameWon() {
    }

    @Test
    void isGameLost() {
    }

    @Test
    void setGameState() {
    }

    @Test
    void addPlayer() {

    }

    @Test
    void addPlayers() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void chooseOption() {
    }

    @Test
    void getInput() {
    }
}