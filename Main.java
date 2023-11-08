import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game myGame = new Game(new Player[2], new Die());
        myGame.addPlayers();
        if (Game.chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 2) {myGame.setGameState(Game.GameState.LOST);}
        // roll dice to choose who goes first
        // display board
        while (myGame.isGameOngoing()) {
            // player to go first is determined by the dice roll
            Player currentPlayer = myGame.setInitialPlayer();

            System.out.println(currentPlayer.getName() + "'s move:");
            String move = in.nextLine();
        }

        Board b = new Board();
        b.print();
    }
}
