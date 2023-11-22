import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Die die = new Die();
        Scanner in = new Scanner(System.in);
        Game myGame = new Game();
//        Board b = new Board();
        myGame.addPlayers();

        // User has opted to quit
        if (Game.chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 1) {
            System.out.println("Goodbye!");
            myGame.setGameState(Game.GameState.LOST);
        }
        // Otherwise, user has opted to begin a game
        else{
            myGame.setInitialPlayer();

            while (myGame.isGameOngoing()) {
                // player to go first is determined by the dice roll

                System.out.println(myGame.getCurrentPlayer().getName() + "'s move:");

                String move = in.nextLine().toLowerCase();

                if(move.isEmpty()){
                    System.out.println("Invalid input! Please enter a move.");
                    continue;
                }

                if (!myGame.acceptCommand(move)){return;}

                // TODO: In future, when score is unchanged (non-playing command was given), this won't run
                // This alternates between the players
                myGame.nextTurn();


            }
        }
    }
}
