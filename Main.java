import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Die die = new Die();
        Scanner in = new Scanner(System.in);
        Game myGame = new Game(new Player[2], die);
        myGame.addPlayers();
        if (Game.chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 2) {myGame.setGameState(Game.GameState.LOST);}


        while (myGame.isGameOngoing()) {
            // player to go first is determined by the dice roll

            System.out.println(myGame.getCurrentPlayer().getName() + "'s move:");

            String move = in.nextLine().toLowerCase();

            if(move.isEmpty()){
                System.out.println("Invalid input! Please enter a move.");
                continue;
            }

            // TODO: Obviously there'll be more of these commands
            if(move.equals("roll")){
                die.roll();
                System.out.println(myGame.getCurrentPlayer().getName() + " rolled " + die.getCurrentValues().toString());
           }

            // This alternates between the players
            myGame.nextTurn();
        }

        Board b = new Board();
        b.print();
    }
}
