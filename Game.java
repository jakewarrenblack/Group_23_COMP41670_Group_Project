import jdk.jshell.spi.ExecutionControl;

import java.util.Scanner;

public class Game {

    protected enum GameState {ONGOING, WON, LOST};
    private GameState gameState;

    private Player[] players = new Player[2];

    private Player currentPlayer;

    private Die die;

    public static void main(String[] args) {
        Game myGame = new Game(new Player[2], new Die());
        myGame.addPlayers();
        if (chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 2) {myGame.setGameState(GameState.LOST);}
        // roll dice to choose who goes first
        // display board
        while (myGame.gameState.equals(GameState.ONGOING)) {
            //current player
                //roll dice
                //give legal move options
                //player chooses moves
                //make moves
                //display board
        }
    }
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
    public boolean isGameLost(){
        return this.gameState == GameState.LOST;
    }
    public void setGameState(GameState gameState){
        this.gameState=gameState;
    }
    public Player addPlayer(Player.Color color){
        Scanner in = new Scanner(System.in);
        String name = "";
        while (name.length()==0) {
            name=getInput ("Please enter the name of the " + color + " player");
        }
        return new Player(name,color);
    }

    public Player[] addPlayers(){
        Player[] players = new Player[2];
        int i=0;
        for (Player.Color color:Player.Color.values()){
            players[i] = addPlayer(color);
            i+=1;
        }
        return players;
    }
    public static void sendMessage(String message){System.out.println(message);}
    public static int chooseOption(String message, String[] options){
        sendMessage(message);
        int i=0;
        for (String option: options){
            i+=1;
            sendMessage(String.valueOf(i)+": "+option);
        }
        Scanner in = new Scanner(System.in);
        int opt=-1;
        sendMessage("Please select option");
        while (opt < 0 || opt > options.length) {
            while (!in.hasNextInt()) {
                String temp = in.next();
                sendMessage("You must enter a number corresponding to one of the options");
            }
            opt = in.nextInt();
            if (opt < 0 || opt > options.length) {
                sendMessage("You must enter a number corresponding to one of the options");
            }
        }
        return opt;
    }
    public String getInput(String message){
        Scanner in = new Scanner(System.in);
        String input = "";
        while (input.length()==0) {
            sendMessage(message);
            String temp = in.nextLine();
            if (chooseOption("You have entered <" + temp + ">. Confirm?", new String[]{"Yes","No"})==1){
                input= temp;
            }
        }
        return input;
    }
}
