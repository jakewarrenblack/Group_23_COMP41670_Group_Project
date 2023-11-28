import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private Player blackPlayer;
    private Player whitePlayer;
    private Game myGame;
    @BeforeEach
    void setUp() {
        blackPlayer = new Player("B", Player.Color.BLACK);
        whitePlayer = new Player("W", Player.Color.WHITE);
        myGame = new Game(new Die());
        myGame.addPlayer(0,blackPlayer,true);
        myGame.addPlayer(1,whitePlayer,false);
        myGame.placePieces(blackPlayer);
        myGame.placePieces(whitePlayer);
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
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

//    @Test
//    @DisplayName("Checks we can update the game state")
//    void setGameState() {
//        assertFalse(myGame.isGameWon()),
//        myGame.setGameState(Game.GameState.WON);
//        assertAll(()->assertTrue(myGame.isGameWon()),
//                ()->assertFalse(myGame.isGameWon()));
//        myGame.setGameState(Game.GameState.LOST);
//        assertAll(()->assertFalse(myGame.isGameWon()),
//                ()->assertTrue(myGame.isGameWon()));
//    }

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
            System.out.println(test);
            assertEquals(test, outputStreamCaptor.toString().trim());
        }
    }
    @Test
    void move(){
        myGame.movePiece(1,3);
        assertEquals("B moved a piece from 1 to 3",outputStreamCaptor.toString().trim());
    }
    @Test
    void noPieceToMove(){
        myGame.movePiece(2, 3);
        assertEquals("B's checkers are not on Point 2",outputStreamCaptor.toString().trim());
    }
    @Test
    void fullPoint(){
        myGame.movePiece(1,12);
        myGame.movePiece(1,12);
        assertEquals("B moved a piece from 1 to 12\r\nPoint 12has six checkers on it already",outputStreamCaptor.toString().trim());
    }
    @Test
    void chooseOption() {
    }

    @Test
    void getInput() {
    }
    @Test
    void getMovablePieces() {
        Piece[] blackMovable = new Piece[]{myGame.getBoard().getPoint(1).getTopChecker(), myGame.getBoard().getPoint(12).getTopChecker(), myGame.getBoard().getPoint(17).getTopChecker(), myGame.getBoard().getPoint(19).getTopChecker()};
        Object[] result = myGame.getMovablePieces().toArray();
        for (int i = 0; i < 4; i++) {
            assertEquals(blackMovable[i], result[i]);
        }
    }
}