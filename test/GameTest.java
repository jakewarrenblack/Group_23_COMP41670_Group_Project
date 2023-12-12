import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

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
        myGame.setCurrentPlayer(blackPlayer);
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
        assertEquals(3,blackPlayer.getPiece(1).getPosition());
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
        assertEquals("Point 12 has six checkers on it already",outputStreamCaptor.toString().trim());
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
        assertEquals(blackMovable.length,result.length);
        for (int i = 0; i < 4; i++) {
            assertEquals(blackMovable[i], result[i]);
        }
    }
    // TODO Scrub all tests above this one
    @Test
    void isLegalMove(){
        // Illegal moves first
        // Try to move when the player has players on the bar
        myGame.movePiece(19,0);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {myGame.isLegalMove(19,20);});
        myGame.movePiece(0,19);
        // Try a move from a point where the player has no checkers
        thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {myGame.isLegalMove(20,21);});
        // Try to move a piece off before all your pieces are in the final quadrant
        thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {myGame.isLegalMove(19,25);});
        // Try to move a piece to a point with too many of the opponent's checkers
        thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {myGame.isLegalMove(19,24);});
        // Try to move a piece to a point with too many of your own checkers
        myGame.movePiece(1,19);
        thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {myGame.isLegalMove(1,19);});
        // Try to move with some of your pieces on the bar
        // Move a piece to an empty point
        assertTrue(myGame.isLegalMove(19,2));
        // Move a piece to a point with less than six of your own checkers
        assertTrue(myGame.isLegalMove(19,1));
        // Move a piece to a point with just one of your opponent's checkers
        myGame.movePiece(24,23);
        assertTrue(myGame.isLegalMove(19,23));
        // Move a piece off when all your pieces are in the final quadrant
        int to = 19;
        for (int i=0;i<19;i++) {
            if(myGame.getBoard().getPoint(i).isPlayers(blackPlayer)){
                int j=myGame.getBoard().numPieces(i);
                while (j>0){
                    while (myGame.getBoard().getPoint(to).isFull()){
                        to++;
                    }
                    myGame.movePiece(i,to);
                    j--;
                }
            }
        }
        assertTrue(myGame.isLegalMove(19,25));
    }

    @Test
    void getAvailableValidMoves(){
        // In the start position a throw of 6 will allow the black player to move from 1 to 7, from 12 to 18
        // from 17 to 23. They can't move their pieces on 6 because they can't move off board yet
        ArrayList<Game.Move> expected = new ArrayList<Game.Move>();
        expected.add(new Game.Move(1,7));
        expected.add(new Game.Move(12,18));
        expected.add(new Game.Move(17,23));
        ArrayList<Game.Move> moves = myGame.getAvailableValidMoves(6);
        assertEquals(expected.size(),moves.size());
        boolean contains=false;
        for (int i=0;i<expected.size();i++){
            int j=0;
            while (!contains) {
                contains = expected.get(i).equals(moves.get(j));
                j++;
            }
            assertTrue(contains);
            contains=false;
        }
    }
    @Test
    void getAvailableValidMovesReplica(){
        // App crashes when black starts with roll of 1 and 2
        ArrayList<Game.Move> expected = new ArrayList<Game.Move>();
        expected.add(new Game.Move(1,3));
        expected.add(new Game.Move(12,14));
        expected.add(new Game.Move(17,19));
        expected.add(new Game.Move(19,21));
        ArrayList<Game.Move> moves = myGame.getAvailableValidMoves(2);
        assertEquals(expected.size(),moves.size());
        boolean contains=false;
        for (int i=0;i<expected.size();i++){
            int j=0;
            while (!contains) {
                contains = expected.get(i).equals(moves.get(j));
                j++;
            }
            assertTrue(contains);
            contains=false;
        }
    }
    @Test
    void getAllAvailableValidMoves(){
        // A move using the smalled die roll is not valid if you're left unable to use the larger die roll
        // A contrived example of this is where black rolls a 5 and a 3 with the following configuration
        // 1 checker on point 9
        // 1 checker on point 12
        // 5 checkers on point 15
        // 6 checkers on point 20
        testPlayer contrivedP = new testPlayer("Contrived", Player.Color.BLACK);
        Game contrived = new Game(new Die());
        contrived.addPlayer(0,contrivedP,true);
        contrived.placePieces(contrivedP);
        ArrayList<Integer> rolls = new ArrayList<Integer>();
        rolls.add(3);
        rolls.add(5);
        ArrayList<Game.Move> validMoves = contrived.getAllAvailableValidMoves(rolls);
        Game.Move[] expected = new Game.Move[]{new Game.Move(20,23),new Game.Move(15,18),new Game.Move(10,13),new Game.Move(12,17),new Game.Move(10,15)};
        assertEquals(expected.length,validMoves.size());
        boolean contains=false;
        for (int i=0;i<expected.length;i++){
            int j=0;
            while (!contains) {
                contains = expected[i].equals(validMoves.get(j));
                j++;
            }
            assertTrue(contains);
            contains=false;
        }
    }
    @Test
    void getAllAvailableValidMovesReplica(){
        // App crashes when black starts with roll of 1 and 2
        ArrayList<Integer> rolls = new ArrayList<Integer>();
        rolls.add(1);
        rolls.add(2);
        ArrayList<Game.Move> validMoves = myGame.getAllAvailableValidMoves(rolls);
        Game.Move[] expected = new Game.Move[]{new Game.Move(1,2),new Game.Move(1,3),new Game.Move(12,14),new Game.Move(17,18),new Game.Move(17,19),new Game.Move(19,20),new Game.Move(19,21)};
        assertEquals(expected.length,validMoves.size());
        boolean contains=false;
        for (int i=0;i<expected.length;i++){
            int j=0;
            while (!contains) {
                contains = expected[i].equals(validMoves.get(j));
                j++;
            }
            assertTrue(contains);
            contains=false;
        }
    }
}