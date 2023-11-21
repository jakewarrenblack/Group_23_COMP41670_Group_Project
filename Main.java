import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Die die = new Die();
        Scanner in = new Scanner(System.in);
        Game myGame = new Game();
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
            myGame.setInitialPlayer();

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

                if (!myGame.acceptCommand(move)){return;}

                // TODO: Print out possible moves

                // TODO: In future, when score is unchanged (non-playing command was given), this won't run
                // FIXME: Commented out for now. Don't actually switch players until the player has finished their moves.
                //  i.e, the player could run 'print', 'pip', 'roll' before actually making a move.

                // before switching turns, clear the dice value for the next player
                rollValue = null;
                //myGame.nextTurn();


            }
        }
    }
}
