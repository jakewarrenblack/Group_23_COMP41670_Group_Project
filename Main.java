import java.sql.Array;
import java.util.*;

public class Main {
    private static final String[] rolls = new String[]{"first", "second", "third", "fourth"};

    public static void main(String[] args) {
        Die die = new Die();
        Scanner in = new Scanner(System.in);
        Game myGame = new Game(die);
        Player currentPlayer;
        myGame.addPlayers();

        List<Integer> rollValue = die.roll();

        // User has opted to quit
        if (Game.chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 1) {
            System.out.println("Goodbye!");
            myGame.setGameState(Game.GameState.LOST);
        }
        // Otherwise, user has opted to begin a game
        else {
            currentPlayer = myGame.setInitialPlayer();
            // game just started, set the initial dice roll value, which the player will have to use
            List diceRolls = die.getCurrentValues();
            while (myGame.isGameOngoing()) {
                List<String> exclude = new ArrayList<String>();
                List<Integer[]> validMoves;
                for (int i = 0; i < diceRolls.size(); i++) {
                    validMoves = myGame.getAvailableValidMoves((Integer) diceRolls.get(i));
                    String[] validMoveStrings = myGame.validMovesString(validMoves);
                    int chosenMove = myGame.chooseOption(currentPlayer.getName() + ", you rolled " + diceRolls.get(i).toString() + " with your " + rolls[i] + " dice. Choose your move: ", validMoveStrings);
                    myGame.acceptCommand("move " + Integer.toString(validMoves.get(chosenMove)[0]) + " " + Integer.toString(validMoves.get(chosenMove)[1]));
                    myGame.print();
                }
                if (myGame.isGameWon()) {
                    myGame.updateLog(currentPlayer.getName() + " has won!");
                    myGame.setGameState(Game.GameState.WON);
                }
                exclude.add("MOVE");
                exclude.add("ROLL");
                if(!myGame.hasDouble()){exclude.add("DOUBLE");}
                String[] commands = myGame.listCommands(exclude);
                int command = myGame.chooseOption(currentPlayer.getName() + " what would you like to do next?", commands);
                myGame.acceptCommand(commands[command]);
                myGame.nextTurn();
            }
        }
    }
}
//                if (move.equals("moves")) {
//                    myGame.chooseOption("Choose from the following available moves:", myGame.getAvailableValidMoves());
//
//
//                    if (chosenMove != null) {
//                        // Perform the move by removing and adding the piece
//                        int startPosition = chosenMove.getStartPoint();
//                        int endPosition = chosenMove.getEndPoint();
//                        Piece movedPiece = myGame.getBoard().removePiece(startPosition);
//                        myGame.getBoard().addPiece(endPosition, movedPiece);
//
//                        // Update the board
//                        myGame.getBoard().updateBoard();
//
//                        // Print the updated board
//                        myGame.print();
//
//                        // Switch to the next player
//                        currentPlayer = myGame.nextTurn();
//                    } else {
//                        System.out.println("Invalid move choice. Please try again.");
//                    }
//
//                }
//
//
//                if (move.equals("print")) {
//                    myGame.print();
//                }
//                if (move.equals("pip")) {
//                    myGame.pipScore();
//                }
//
//                // TODO: In future, when score is unchanged (non-playing command was given), this won't run
//                // FIXME: Commented out for now. Don't actually switch players until the player has finished their moves.
//                //  i.e, the player could run 'print', 'pip', 'roll' before actually making a move.

                // before switching turns, clear the dice value for the next player
//                rollValue = null;
                //currentPlayer = myGame.nextTurn();


                ///////////////////////////////////////////////////////////////////////////////////////////////
                // Making moves:

//              // Switch to the next player
//              currentPlayer = myGame.nextTurn();


