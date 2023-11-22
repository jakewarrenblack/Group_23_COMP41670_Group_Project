import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /**
     *
     * This is for figuring out where the player's piece would end up based on which dice value they use.
     * White moves counter-clockwise (subtract)
     * Black moves clockwise (add)
     */
    public static int calculate(int a, int b, boolean subtract) {
        return subtract ? a - b : a + b;
    }

    public static void main(String[] args) {
        Die die = new Die();
        Scanner in = new Scanner(System.in);
        Game myGame = new Game(new Player[2], die);
        Player currentPlayer;
//        Board b = new Board();
        myGame.addPlayers();

        int[] rollValue = new int[2];

        // User has opted to quit
        if (Game.chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 1) {
            System.out.println("Goodbye!");
            myGame.setGameState(Game.GameState.LOST);
        }
        // Otherwise, user has opted to begin a game
        else{
            currentPlayer = myGame.setInitialPlayer();

            // game just started, set the initial dice roll value, which the player will have to use
            rollValue[0] = die.getCurrentValues()[0];
            rollValue[1] = die.getCurrentValues()[1];

            while (myGame.isGameOngoing()) {
                // player to go first is determined by the dice roll

                System.out.println(myGame.getCurrentPlayer().getName() + "'s move:");

                String move = in.nextLine().toLowerCase();

                if(move.isEmpty()){
                    System.out.println("Invalid input! Please enter a move.");
                    continue;
                }

                // TODO: Prevent rolling multiple times before making a move
                if(move.equals("roll")){
                    die.roll();
                    System.out.println(myGame.getCurrentPlayer().getName() + " to play " + die.getCurrentValues()[0] + "-" + die.getCurrentValues()[1] + ". Select from:");
                }

                // TODO: Print out possible moves

                // Black player moves from the bottom-right (index 24) to the top-right (index 1) -> (clockwise). In other words, the position index must be moving DOWN from 24...to 1...
                // White player moves from the top-right (index 1) to the bottom-right (index 24) -> (counter-clockwise). In other words, the position index must be moving UP from 1...to 24...

                // possible move distances are dice 1, dice 2, or dice1 + dice2

                // I need to iterate through all the pieces on the board and find the pieces belonging to the relevant player. Of these, the pieces at the top of their respective triangles can be moved.
                // The direction they can move depends on which player we're dealing with.
                Point[] allBoardPoints = myGame.getBoard().getPoints();

                int[] diceOptions = {die.getCurrentValues()[0], die.getCurrentValues()[1], (die.getCurrentValues()[0] + die.getCurrentValues()[1])};

                ArrayList<Point> occupiedPoints = new ArrayList<>();

                for(Point p: allBoardPoints){
                    System.out.println(p);
                    if(p.numPieces() != 0){
                        occupiedPoints.add(p);
                    }
                }

                // for all the occupied points, the pieces which can be moved are the
                // ones whose colour match the player's colour
                // (and are on top of their respective pile)
                ArrayList<Piece> movablePieces = new ArrayList<>();

                for(Point p: occupiedPoints){
                    Piece topPiece = p.getTopChecker();

                    if(topPiece.getColor() == currentPlayer.getColor()){
                     movablePieces.add(topPiece);
                    }
                }

                for(Piece p: movablePieces){
                    //System.out.println("Movable piece " + p.getPosition());

                    // black player will add dice values to determine move
                    // white player will subtract dice values to determine move
                    for(int option: diceOptions){
                        System.out.println(
                                "Potential move: " + p.getPosition() + "-" +
                                calculate(p.getPosition(), option, currentPlayer.getColor() == Player.Color.WHITE)
                        );

                    }
                }

                // example scenario, jake (black player) goes first, options:
                // 0, 11, 16, 18

                // example, rolled a 5-3

                // black moves counter clockwise

                // options would be:
                // use the 5:
                // move 0-5
                // move 11-16
                // move 16-21
                // move 18-23

                // use the 3:
                // move 0-3
                // 11-14
                // 16-19
                // 18-21





                // allBoardPoints is like this:
                // 0:
                    // position = 0
                    // pieces = Stack of size 2
                    // meaning column 0 has 2 pieces, so [0][0] and [0][1] are occupied

                // example scenario:
                //  black player rolls a 5-3
                //  their options are to move one piece 5 places, and another piece 3 places, OR they can move one piece 8 places
                // to give the player their move options, i need to iterate through all the points on the board, finding the relevant (black) pieces


                if(move.equals("print")){
                    myGame.print();
                }
                if(move.equals("pip")){
                    myGame.pipScore();
                }



                // TODO: In future, when score is unchanged (non-playing command was given), this won't run
                // FIXME: Commented out for now. Don't actually switch players until the player has finished their moves.
                //  i.e, the player could run 'print', 'pip', 'roll' before actually making a move.

                // before switching turns, clear the dice value for the next player
                rollValue = null;
                currentPlayer = myGame.nextTurn();


            }
        }
    }
}
