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

    public static boolean bearOffAllowed(ArrayList<Piece> movablePieces, Player currentPlayer){
        // if every one of a player's movable pieces is in their home board, they're allowed to begin bearing off
        boolean bearOffAllowed = true;

        for(Piece p: movablePieces){
                // If ANY of the player's pieces are not in their home board, they're not allowed to bear off
                if(p.getPosition() <= 18 && currentPlayer.getColor() == Player.Color.WHITE){
                    bearOffAllowed = false;
                }
                else if(p.getPosition() >= 7 && currentPlayer.getColor() == Player.Color.BLACK){
                    bearOffAllowed = false;
                }
            }

        return bearOffAllowed;
    }

    public static void main(String[] args) {
        Die die = new Die();
        Scanner in = new Scanner(System.in);
        Game myGame = new Game();
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
                System.out.println(myGame.getCurrentPlayer().getName() + "'s move:");
                String move = in.nextLine().toLowerCase();

                if(move.isEmpty()){
                    System.out.println("Invalid input! Please enter a move.");
                    continue;
                }

                if (!myGame.acceptCommand(move)){return;}

                if(rollValue == null){
                    // TODO: Prevent rolling multiple times before making a move
                    if(move.equals("roll")){
                        die.roll();
                        System.out.println(myGame.getCurrentPlayer().getName() + " to play " + die.getCurrentValues()[0] + "-" + die.getCurrentValues()[1] + ". Select from:");
                    }
                }

                // Black player moves from the bottom-right (index 24) to the top-right (index 1) -> (clockwise). In other words, the position index must be moving DOWN from 24...to 1...
                // White player moves from the top-right (index 1) to the bottom-right (index 24) -> (counter-clockwise). In other words, the position index must be moving UP from 1...to 24...
                Point[] allBoardPoints = myGame.getBoard().getPoints();

                int[] diceOptions = {die.getCurrentValues()[0], die.getCurrentValues()[1], (die.getCurrentValues()[0] + die.getCurrentValues()[1])};

                ArrayList<Point> occupiedPoints = new ArrayList<>();

                for(Point p: allBoardPoints){
                    if(p.numPieces() != 0){
                        occupiedPoints.add(p);
                    }
                }

                // for all the occupied points, the pieces which can be moved are the ones whose colour match the player's colour (and are on top of their respective pile)
                ArrayList<Piece> movablePieces = new ArrayList<>();

                for(Point p: occupiedPoints){
                    Piece topPiece = p.getTopChecker();

                    if(topPiece.getColor() == currentPlayer.getColor()){
                     movablePieces.add(topPiece);
                    }
                }

                for(Piece p: movablePieces){
                    // for assigning a letter label to each move option
                    char moveLabel = 'A';

                    // black player will add dice values to determine move
                    // white player will subtract dice values to determine move
                    for(int option: diceOptions){
                        int endPosition = calculate(p.getPosition(), option, currentPlayer.getColor() == Player.Color.WHITE);

                        // NOTE we only bother showing the valid moves. There'd be (num dice moves, which is 3 * num movable pieces) potential moves otherwise. E.g pieces in 4 Points would yield 4 * 3 = 12 potential moves.
                        // Create a 'potential' Move object for the current potential move
                        Move potentialMove = new Move(p.getPosition(), endPosition, currentPlayer, die, bearOffAllowed(movablePieces, currentPlayer), myGame);

                        if (potentialMove.validateMove()) {

                            // if this runs, bearing off has been allowed
                            if(endPosition > allBoardPoints.length || endPosition < 0){
                                System.out.println(moveLabel + ") Play: " + p.getPosition() + "-off");
                            }
                            else{
                                System.out.println(moveLabel + ") Play: " + p.getPosition() + "-" + calculate(p.getPosition(), option, currentPlayer.getColor() == Player.Color.WHITE));
                            }


                            moveLabel++;
                        }
                    }
                }

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
                //currentPlayer = myGame.nextTurn();


            }
        }
    }
}
