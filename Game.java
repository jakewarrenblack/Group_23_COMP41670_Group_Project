import jdk.jshell.spi.ExecutionControl;

import java.util.Scanner;

public class Game {
    private final Board board;
    protected enum GameState {ONGOING, WON, LOST};
    private GameState gameState;
    private Player[] players;
    private Player currentPlayer;

    private Die die;

    private Log log;
    private Command command;
    public Game(){
        this.gameState = GameState.ONGOING;
        this.board = new Board();
        this.players = new Player[2];
        this.die = new Die();
        this.log=new Log();
        this.command=new Command(this);
    }

    public Player setInitialPlayer(){
        // the player to go first is determined by the dice roll
        this.die.roll(); // roll the dice to initialise some values (it starts as 0,0)

        int leftDie = this.die.getCurrentValues()[0];
        int rightDie = this.die.getCurrentValues()[1];

        while(leftDie == rightDie){
            this.die.roll();
            leftDie = this.die.getCurrentValues()[0];
            rightDie = this.die.getCurrentValues()[1];
        }


        log.updateLog(this.players[0].getName()+" has rolled "+leftDie+", "+this.players[1].getName()+" has rolled "+rightDie);

        // if leftDie has a greater value, player 0 starts, otherwise, player 1 starts
        this.currentPlayer = this.players[leftDie > rightDie ? 0 : 1];

        log.updateLog(this.currentPlayer.getName()+" will go first");
        return this.currentPlayer;
    }



    public Player nextTurn(){
        // just switch between 0 and 1, whichever is NOT the current player
        this.currentPlayer = this.players[0] == this.currentPlayer ? this.players[1] : this.players[0];

        return this.currentPlayer;
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
    public void addPlayer(int index, Player player, boolean isCurrentPlayer){
        this.players[index]=player;
        if (isCurrentPlayer){currentPlayer=player;}
    }
    public Player[] addPlayers(){
        Player[] players = new Player[2];

        for(int i=0; i<2; i++){
            Player.Color color = Player.Color.values()[i];
            players[i] = new Player(getInput("Please enter the name of the " + color + " player"), color);
        }
        // Not sure if this should sit here or be in another method?
        this.players = players;
        for (int i=0;i<2;i++){
            board.placePieces(this.players[i]);
            placePieces(this.players[i]);
        }

        return players;
    }
    public void placePieces(Player player){board.placePieces(player);}
    public static int chooseOption(String message, String[] options){
        System.out.println(message);

        for(int i=0; i<options.length; i++){
            System.out.println((i+1) + ": " + options[i]);
        }

        Scanner in = new Scanner(System.in);
        int opt=-1;

        System.out.println("Please select an option");

        while (opt < 0 || opt > options.length) {
            while (!in.hasNextInt()) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
            opt = in.nextInt()-1;
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

    public void movePiece(int from, int to) {
        if (!board.isPlayers(from, currentPlayer)) {
            throw new IllegalArgumentException(currentPlayer.getName() + "'s checkers are not on Point " + from);
        } else if (!board.isEmpty(to)) {
            if (!board.isPlayers(to,currentPlayer)){
                if(!board.isBlot(to)) {
                    throw new IllegalArgumentException("Your opponent has too many checkers on Point " + to + " for you to move there");
                }//TODO functionality to move opponent's blot to their bar
            } else {
                if (board.isFull(to)) {
                    throw new IllegalArgumentException("You cannot place more than six checkers on one point");
                }
            }
        }
        board.addPiece(to,board.removePiece(from));
        log.updateLog(currentPlayer.getName()+" moved a piece from "+from+" to "+to);
    }
    public void print(){

        board.print(currentPlayer.getColor(), log.recentLog(10));
    }
    public void pipScore(){
        for (int i=0;i<2;i++){
            log.updateLog(players[i].getName()+" has a pip score of "+players[i].pipScore());
        }
    }

    public Board getBoard(){
        return this.board;
    }
    public void roll(){
        int[] dice = die.roll();
        log.updateLog(getCurrentPlayer().getName() + " rolled " + dice[0] + ", " + dice[1]);
    }
    public void updateLog(String message){
        log.updateLog(message);
    }
    public boolean acceptCommand(String command){
        return this.command.acceptCommand(command);
    }
}
