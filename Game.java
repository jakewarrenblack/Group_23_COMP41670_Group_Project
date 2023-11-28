import jdk.jshell.spi.ExecutionControl;

import java.sql.Array;
import java.util.*;

public class Game {
    private final Board board;

    protected enum GameState {ONGOING, WON, LOST}
    private Double cube;
    ;
    private GameState gameState;
    private Player[] players;
    private Player currentPlayer;

    private Die die;

    private Log log;
    private Command command;

    public Game(Die die) {
        this.gameState = GameState.ONGOING;
        this.board = new Board();
        this.players = new Player[2];
        this.die = die;
        this.log = new Log();
        this.command = new Command(this);
        this.cube=new Double();
    }

    public Player setInitialPlayer() {
        // the player to go first is determined by the dice roll
        while (die.getCurrentValues().get(0) == die.getCurrentValues().get(1)) {
            this.die.roll();
        }
        log.updateLog(this.players[0].getName() + " has rolled " + die.getCurrentValues().get(0) + ", " + this.players[1].getName() + " has rolled " + die.getCurrentValues().get(1));
        // if leftDie has a greater value, player 0 starts, otherwise, player 1 starts
        this.currentPlayer = this.players[die.getCurrentValues().get(0) > die.getCurrentValues().get(1) ? 0 : 1];

        log.updateLog(this.currentPlayer.getName() + " will go first");
        board.print(currentPlayer.getColor(), log.recentLog(10));
        this.cube.setOwner(currentPlayer);
        return this.currentPlayer;
    }


    public Player nextTurn() {
        // just switch between 0 and 1, whichever is NOT the current player
        this.currentPlayer = this.players[0] == this.currentPlayer ? this.players[1] : this.players[0];
        board.print(currentPlayer.getColor(), log.recentLog(10));
        return this.currentPlayer;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void handleMove() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public boolean isGameWon() {

        return this.gameState == GameState.WON;
    }

    public boolean isGameOngoing() {
        return this.gameState == GameState.ONGOING;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void addPlayer(int index, Player player, boolean isCurrentPlayer) {
        this.players[index] = player;
        if (isCurrentPlayer) {
            currentPlayer = player;
        }
    }

    public Player[] addPlayers() {
        Player[] players = new Player[2];

        for (int i = 0; i < 2; i++) {
            Player.Color color = Player.Color.values()[i];
            players[i] = new Player(getInput("Please enter the name of the " + color + " player"), color);
        }
        // Not sure if this should sit here or be in another method?
        this.players = players;
        for (int i = 0; i < 2; i++) {
            placePieces(this.players[i]);
        }

        return players;
    }

    public void placePieces(Player player) {
        board.placePieces(player);
    }

    public static int chooseOption(String message, String[] options) {
        System.out.println(message);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ": " + options[i]);
        }
        Scanner in = new Scanner(System.in);
        int opt = -1;
        System.out.println("Please select an option");
        while (opt < 0 || opt > options.length) {
            while (!in.hasNextInt()) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
            opt = in.nextInt() - 1;
            if (opt < 0 || opt > options.length) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
        }
        return opt;
    }

    public String getInput(String message) {
        Scanner in = new Scanner(System.in);
        String input = "";
        while (input.isEmpty()) {
            System.out.println(message);
            String temp = in.nextLine();
            if (chooseOption("You have entered <" + temp + ">. Confirm?", new String[]{"Yes", "No"}) == 0) {
                input = temp;
            }
        }
        return input;
    }

    public void movePiece(int from, int to) {
        try {if (isLegalMove(from, to)) {
                board.addPiece(to, board.removePiece(from));
                log.updateLog(currentPlayer.getName() + " moved a piece from " + from + " to " + to);
            }
        } catch (IllegalArgumentException e)  {log.updateLog(e.getMessage());}
    }
    public boolean isLegalMove(int from, int to){
        if (!board.isPlayers(from, currentPlayer)) {
            throw new IllegalArgumentException(currentPlayer.getName() + "'s checkers are not on Point " + from);
        } else if (board.isOff(to,currentPlayer)){
            if(!currentPlayer.canMoveOff()){throw new IllegalArgumentException("You cannot move a piece off the board until all your checkers are in the final quadrant");}
        } else if (!board.isEmpty(to)) {
            if (!board.isPlayers(to, currentPlayer)) {
                if (!board.isBlot(to)) {
                    throw new IllegalArgumentException("Your opponent has too many checkers on point " + to);
                }
            } else {
                if (board.isFull(to)) {
                    throw new IllegalArgumentException("Point " + to + "has six checkers on it already");
                }
            }
        }
        return true;
    }
    public void print() {
        board.print(currentPlayer.getColor(), log.recentLog(10));
    }

    public void pipScore() {
        for (int i = 0; i < 2; i++) {
            log.updateLog(players[i].getName() + " has a pip score of " + players[i].pipScore());
        }
    }

    public Board getBoard() {
        return this.board;
    }

    public void roll() {
        List<Integer> dice = die.roll();
        String message=currentPlayer.getName() + " rolled " + Integer.toString(dice.get(0));
        for (int i=1;i<dice.size();i++){
            message+=", " + Integer.toString(dice.get(i));
        }
        log.updateLog(message);
    }

    public void updateLog(String message) {
        log.updateLog(message);
    }

    public boolean acceptCommand(String command) {
        return this.command.acceptCommand(command);
    }

    public List getAvailableValidMoves(int dieRoll) {
        // Black player moves from the bottom-right (index 24) to the top-right (index 1) -> (clockwise). In other words, the position index must be moving DOWN from 24...to 1...
        // White player moves from the top-right (index 1) to the bottom-right (index 24) -> (counter-clockwise). In other words, the position index must be moving UP from 1...to 24...
        List<Integer[]> validMoves = new ArrayList<Integer[]>();

        // for all the occupied points, the pieces which can be moved are the ones whose colour match the player's colour (and are on top of their respective pile)
        ArrayList<Piece> movablePieces = getMovablePieces();
        for (Piece p : movablePieces) {
            // black player will add dice values to determine move
            // white player will subtract dice values to determine move
            int endPosition = calculate(p.getPosition(), dieRoll, currentPlayer.getColor() == Player.Color.WHITE);
            try {
                if (isLegalMove(p.getPosition(), endPosition)) {
                    validMoves.add(new Integer[]{p.getPosition(), endPosition}); // Store the valid move in the Map
                }
            } catch (IllegalArgumentException e){}
        }
        return validMoves;
    }
    public ArrayList<Piece> getMovablePieces(){
        ArrayList<Piece> movablePieces = new ArrayList<Piece>();
        if (board.hasBarPieces(currentPlayer)){
            movablePieces.add(board.getBar(currentPlayer).getTopChecker());
        } else {
            for (Point p : board.getPoints()) {
                if (!p.isOff()&p.isPlayers(currentPlayer)) {
                    movablePieces.add(p.getTopChecker());
                }
            }
        }
        return movablePieces;
    }
    public String[] validMovesString(List<Integer[]> validMoves){
        String[] moves = new String[validMoves.size()];
        for (int i=0;i< validMoves.size();i++){
            moves[i] = "From "+Integer.toString(validMoves.get(i)[0])+" to "+Integer.toString(validMoves.get(i)[1]);
        }
        return moves;
    }
    public static int calculate(int a, int b, boolean subtract) {
        return subtract ? a - b : a + b;
    }
    public String[] listCommands(List<String> exclude){
        return command.listCommands(exclude);
    }
    public static boolean bearOffAllowed(ArrayList<Piece> movablePieces, Player currentPlayer){
        // if every one of a player's movable pieces is in their home board, they're allowed to begin bearing off
        boolean bearOffAllowed = true;

        for(Piece p: movablePieces){
            // If ANY of the player's pieces are not in their home board, they're not allowed to bear off
            if(p.getPosition() <= 18 && currentPlayer.getColor() == Player.Color.WHITE){
                bearOffAllowed = false;
            }
            else if(p.getPosition() >= 7 && currentPlayer.getColor() == Player.Color.BLACK){
                bearOffAllowed = false;
            }
        }

        return bearOffAllowed;
    }
    public boolean hasDouble(){return currentPlayer.equals(cube.getOwner());}
    public void doubleBet(){
        Player newPlayer = cube.getOwner() == this.players[0] ? this.players[1] : this.players[0];
        if (chooseOption(newPlayer.getName()+", "+currentPlayer.getName()+" has offered to double the bet. Do you accept?",new String[]{"Yes","No"})==0) {
            cube.doubleScore(newPlayer);
            updateLog("The doubling cube now shows " + cube.getDouble() + " and " + cube.getOwner().getName() + " has possession");
        } else {
            updateLog(newPlayer.getName()+" has rejected the offer to double the bet and loses the game");
            setGameState(Game.GameState.LOST);
        }
    }
}

