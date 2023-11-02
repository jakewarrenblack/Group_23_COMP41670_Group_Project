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


    private void nextTurn(){
        // just switch between 0 and 1, whichever is NOT the current player
        this.currentPlayer = this.players[0] == this.currentPlayer ? this.players[1] : this.players[0];
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
            if (chooseOption("You have entered <" + temp + ">. Confirm?", new String[]{"Yes","No"})==1){
                input= temp;
            }
        }
        return input;
    }
}
