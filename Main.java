import java.util.*;

public class Main {
    private static final String[] rolls = new String[]{"first", "second", "third", "fourth"};

    public static void main(String[] args) {
        Die die = new Die();
        Game myGame = new Game(die);
        Player currentPlayer;
        myGame.addPlayers();

        // User has opted to quit
        if (Game.chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 1) {
            System.out.println("Goodbye!");
            myGame.setGameState(Game.GameState.LOST);
        }

        // Otherwise, user has opted to begin a game
        else {
            currentPlayer = myGame.setInitialPlayer();
            // game just started, set the initial dice roll value, which the player will have to use
            List<Integer> diceRolls = die.getCurrentValues();

            while (myGame.isGameOngoing()) {
                List<String> exclude = new ArrayList<>();
                List<Integer[]> validMoves;

                for (int i = 0; i < diceRolls.size(); i++) {
                    validMoves = myGame.getAvailableValidMoves(diceRolls.get(i));

                    String[] validMoveStrings = myGame.validMovesString(validMoves);

                    int chosenMove = Game.chooseOption(currentPlayer.getName() + ", you rolled " + diceRolls.get(i).toString() + " with your " + rolls[i] + " dice. Choose your move: ", validMoveStrings);

                    // run Command's acceptCommand method with the chosen move
                    new Command(myGame).acceptCommand("move " + validMoves.get(chosenMove)[0] + " " + validMoves.get(chosenMove)[1]);
                    myGame.print();
                }

                if (myGame.isGameWon()) {
                    myGame.updateLog(currentPlayer.getName() + " has won!");
                    myGame.setGameState(Game.GameState.WON);
                }

                exclude.add("MOVE");
                exclude.add("ROLL");

                if(!myGame.hasDouble()){
                    exclude.add("DOUBLE");
                }

                String[] commands = myGame.listCommands(exclude);
                int command = Game.chooseOption(currentPlayer.getName() + " what would you like to do next?", commands);
                new Command(myGame).acceptCommand(commands[command]);
                myGame.nextTurn();
            }
        }
    }
}
