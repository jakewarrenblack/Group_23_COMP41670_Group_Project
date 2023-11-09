import jdk.jshell.spi.ExecutionControl;

import java.util.Scanner;

public class Game {

    protected enum GameState {ONGOING, WON, LOST};
    private GameState gameState;

    private Player[] players = new Player[2];

    private Player currentPlayer;

    private Die die;


    public Game(Player[] players, Die die){
        this.gameState = GameState.ONGOING;
        this.players = players;
        this.die = die;
    }

    public Player setInitialPlayer(){
        // the player to go first is determined by the dice roll
        this.die.roll(); // roll the dice to initialise some values (it starts as 0,0)

        int leftDie = this.die.getCurrentValues().getLeft();
        int rightDie = this.die.getCurrentValues().getRight();

        while(leftDie == rightDie){
            this.die.roll();
            leftDie = this.die.getCurrentValues().getLeft();
            rightDie = this.die.getCurrentValues().getRight();
        }
        System.out.println(this.players[0].getName()+" has rolled "+leftDie+", "+this.players[1].getName()+" has rolled "+rightDie);

        // if leftDie has a greater value, player 0 starts, otherwise, player 1 starts
        this.currentPlayer = this.players[leftDie > rightDie ? 0 : 1];

        System.out.println(this.currentPlayer.getName()+" will go first");
        return this.currentPlayer;
    }



    public void nextTurn(){
        // just switch between 0 and 1, whichever is NOT the current player
        this.currentPlayer = this.players[0] == this.currentPlayer ? this.players[1] : this.players[0];
    }

    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    public void handleMove() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public boolean isGameWon(){
        return this.gameState == GameState.WON;
    }

    public boolean isGameOngoing(){
        return this.gameState == GameState.ONGOING;
    }
    public void setGameState(GameState gameState){
        this.gameState=gameState;
    }

    public Player[] addPlayers(){
        Player[] players = new Player[2];

        for(int i=0; i<2; i++){
            Player.Color color = Player.Color.values()[i];

            players[i] = new Player(getInput("Please enter the name of the " + color + " player"), color);
        }

        this.players = players;

        // Roll dice to determine who plays first
        this.setInitialPlayer();

        return players;
    }

    public static int chooseOption(String message, String[] options){
        System.out.println(message);

        for(int i=0; i<options.length; i++){
            System.out.println(i + ": " + options[i]);
        }

        Scanner in = new Scanner(System.in);
        int opt=-1;

        System.out.println("Please select an option");

        while (opt < 0 || opt > options.length) {
            while (!in.hasNextInt()) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
            opt = in.nextInt();
            if (opt < 0 || opt > options.length) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
        }

        return opt;
    }
    public String getInput(String message){
        Scanner in = new Scanner(System.in);
        String input = "";
        while (input.isEmpty()) {
            System.out.println(message);
            String temp = in.nextLine();
            if (chooseOption("You have entered <" + temp + ">. Confirm?", new String[]{"Yes","No"})==0){
                input= temp;
            }
        }
        return input;
    }
}
