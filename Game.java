import java.util.*;

public class Game {
    private final Board board;
    protected enum GameState {ONGOING, WON, LOST}
    private boolean isOngoing = true;
    private final Double cube;
    private Player[] players;
    private Player currentPlayer;
    private final Die die;
    private final Log log;
    private Command command;


    // players are an optional parameter
    // if they're provided, use them. otherwise, run addPlayers
    public Game(int gameNumber, int matchGames, Player... players) {
        // Singleton pattern. No need to pass around instances of these, there can be only one of each for a given game.
        this.die = Die.getInstance();
        this.log = Log.getInstance();
        this.cube=new Double();
        this.players = players.length == 2 ? players : addPlayers();
        this.board = new Board(gameNumber,matchGames, this.players);
        board.print(currentPlayer.getColor(), log.recentLog(10));
    }

    public void setCommand(Command command){
        this.command=command;
    }

    public void setScores(int doubleValue){
        for (Player player:players){player.setScore(player.getGameScore(doubleValue));}
    }

    public void setInitialPlayer(){
        // the player to go first is determined by the dice roll
        while (Objects.equals(die.getCurrentValues().get(0), die.getCurrentValues().get(1))) {
            this.die.roll();
        }

        this.log.updateLog(this.players[0].getName() + " has rolled " + die.getCurrentValues().get(0) + ", " + this.players[1].getName() + " has rolled " + die.getCurrentValues().get(1));

        // if leftDie has a greater value, player 0 starts, otherwise, player 1 starts
        this.currentPlayer = this.players[die.getCurrentValues().get(0) > die.getCurrentValues().get(1) ? 0 : 1];

        this.log.updateLog(this.currentPlayer.getName() + " will go first");

        this.cube.setOwner(currentPlayer);
    }

    public void setCurrentPlayer(Player player){
        this.currentPlayer=player;
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

    public boolean isGameWon() {
        return !this.isOngoing;
    }

    public boolean isGameOngoing() {
        return this.isOngoing;
    }

    public void setGameState(GameState gameState) {
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

        this.players = players;

        // Might as well do this here. It has to be done at some stage.
        this.setInitialPlayer();

        return players;
    }

    public static int chooseOption(String message, String[] options) {
        System.out.println(message);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ": " + options[i]);
        }
        Scanner in = new Scanner(System.in);
        int opt = -1;
        System.out.println("Please select an option");
        while (opt < 0 || opt >= options.length) {
            while (!in.hasNextInt()) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
            opt = in.nextInt() - 1;
            if (opt < 0 || opt >= options.length) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
        }
        return opt;
    }

    public static String getInput(String message) {
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

    public static int getInteger(String message){
        Scanner in = new Scanner(System.in);
        int input=0;
        while (input==0) {
            System.out.println(message);
            while (!in.hasNextInt()) {
                String temp = in.next();
                System.out.println(temp + " is not a number");
            }
            int temp = in.nextInt();
            if (chooseOption("You have entered <" + temp + ">. Confirm?", new String[]{"Yes", "No"}) == 0) {
                input = temp;
            }
        }
        return input;
    }

    public void movePiece(int from, int to) {
        try {
            if (board.getPoint(to).isBlot()&!board.getPoint(to).isPlayers(currentPlayer)){
                board.moveToBar(to,log);
            }
            board.setColour(from,currentPlayer.getColor());
            board.setColour(to,currentPlayer.getColor());
            board.addPiece(to, board.removePiece(from));
        }
        catch(IllegalArgumentException e){
            log.updateLog(e.getMessage());
        }
    }

    /**
     * Checks if a move from one point to another is legal.
     *
     * @param from The index of the point from which a piece is being moved.
     * @param to The index of the point to which a piece is being moved.
     * @return true if the move is legal, false otherwise.
     * @throws IllegalArgumentException if the move is not legal. The exception's message provides details about why the move is not legal.
     */
    public boolean isLegalMove(int from, int to){
        if(from<0|to<0|from>25|to>25){throw new IllegalArgumentException("There are only 26 valid points");}
        Point fromPoint = board.getPoint(from);
        Point toPoint = board.getPoint(to);
        if(currentPlayer.isBarred(from)){
            throw new IllegalArgumentException(currentPlayer.getName() + " must move their checkers from the bar first");
        }
        if (!fromPoint.isPlayers(currentPlayer)) {
            throw new IllegalArgumentException(currentPlayer.getName() + "'s checkers are not on Point " + from);
        }
        else if (toPoint.isOff(currentPlayer.getColor())){
            if(!currentPlayer.canMoveOff()){
                throw new IllegalArgumentException("You cannot move a piece off the board until all your checkers are in the final quadrant");
            }
        }
        else if (!toPoint.isEmpty()) {
            if (!toPoint.isPlayers(currentPlayer)) {
                if (!toPoint.isBlot()) {
                    throw new IllegalArgumentException("Your opponent has too many checkers on point " + to);
                }
            }
            else {
                if (toPoint.isFull()) {
                    throw new IllegalArgumentException("Point " + to + " has six checkers on it already");
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
        String message=currentPlayer.getName() + " rolled " + dice.get(0);

        for (int i=1;i<dice.size();i++){
            message+=", " + dice.get(i);
        }

        log.updateLog(message);
    }

    public void setDie(int[] rolls){die.setValues(rolls);}

    public void updateLog(String message) {
        log.updateLog(message);
    }

    public void processRolls(List<Integer> diceRolls){
        if (diceRolls.size()==2){
            processDifferentDiceRolls(diceRolls);
        } else {
            processDoubleDiceRolls(diceRolls);
        }
        isOngoing=!currentPlayer.hasWon();
    }

    public void processDifferentDiceRolls(List<Integer> diceRolls){
        ArrayList<Move> validMoves = getAllAvailableValidMoves(diceRolls);
        String[] validMoveString = validMovesString(validMoves);

        int chosenMove = chooseOption(currentPlayer.getName() + " you rolled " + diceRolls.get(0) + " and " + diceRolls.get(1) + ". Choose your first move: ", validMoveString);

        command.acceptCommand("move " + validMoves.get(chosenMove).getFrom() + " " + validMoves.get(chosenMove).getTo());

        int otherRoll = diceRolls.get(0)==validMoves.get(chosenMove).getFrom() - validMoves.get(chosenMove).getTo() ? diceRolls.get(1) : diceRolls.get(0);

        print();

        validMoves = getAvailableValidMoves(otherRoll);

        if(validMoves.isEmpty()){
            updateLog("You have no valid moves to make with your other die roll of "+otherRoll);
        }
        else {
            validMoveString = validMovesString(validMoves);
            chosenMove = chooseOption(currentPlayer.getName() + " you rolled " + otherRoll + " with your other die. Choose your second move: ", validMoveString);
            command.acceptCommand("move " + validMoves.get(chosenMove).getFrom() + " " + validMoves.get(chosenMove).getTo());
        }

        print();
    }

    public void processDoubleDiceRolls(List<Integer> diceRolls){
        int i=0;
        ArrayList<Move> validMoves=getAvailableValidMoves(diceRolls.get(i));

        while (!validMoves.isEmpty() && i<diceRolls.size()){

            String[] validMoveString=validMovesString(validMoves);
            int chosenMove = chooseOption(currentPlayer.getName() +" you have the following options with dice number "+i+". Please choose: ",validMoveString );
            command.acceptCommand("move "+validMoves.get(chosenMove).getFrom()+" "+validMoves.get(chosenMove).getTo());
            i++;

            if (i<diceRolls.size()){
                validMoves=getAvailableValidMoves(diceRolls.get(i));
            }
        }

        print();
        if (i<diceRolls.size()){updateLog("You have no more valid moves");}
    }

    public ArrayList<Move> getAllAvailableValidMoves(List<Integer> dieRoll){
        // If we have rolled two different values then the player cannot make a move
        // with the smaller die value first if it would leave them with no legal moves
        // with the larger die
        ArrayList<Move> validMoves = getAvailableValidMoves(Math.min(dieRoll.get(0),dieRoll.get(1)));
        ArrayList<Move> invalids = new ArrayList<Move>();
        // Try all the apparently valid moves with the smaller dice value
        // and remove any that leave us with no valid moves with the larger

        for (Move validMove : validMoves) {
            // Make the move as a test
            movePiece(validMove.getFrom(), validMove.getTo());
            ArrayList<Move> testLarge = getAvailableValidMoves(Math.max(dieRoll.get(0), dieRoll.get(1)));
            // If there are no valid moves with the larger dice roll, capture the move for later deletion
            if (testLarge.isEmpty()) {
                invalids.add(validMove);
            }
            // Reverse the test move
            movePiece(validMove.getTo(), validMove.getFrom());
        }
        // Delete the moves with the smaller die that would leave you with no valid moves with the larger
        for (Move invalid : invalids) {
            validMoves.remove(invalid);
        }

        ArrayList<Move> largeMoves = getAvailableValidMoves(Math.max(dieRoll.get(0),dieRoll.get(1)));
        validMoves.addAll(largeMoves);
        return validMoves;
    }


    public ArrayList<Move> getAvailableValidMoves(int dieRoll) {
        // Black player moves from the bottom-right (index 24) to the top-right (index 1) -> (clockwise). In other words, the position index must be moving DOWN from 24...to 1...
        // White player moves from the top-right (index 1) to the bottom-right (index 24) -> (counter-clockwise). In other words, the position index must be moving UP from 1...to 24...
        ArrayList<Move> validMoves = new ArrayList<Move>();

        // for all the occupied points, the pieces which can be moved are the ones whose colour match the player's colour (and are on top of their respective pile)
        ArrayList<Piece> movablePieces = getMovablePieces();
        for (Piece p : movablePieces) {
            // black player will add dice values to determine move
            // white player will subtract dice values to determine move
            int endPosition = calculate(p.getPosition(), dieRoll, currentPlayer.getColor() == Player.Color.WHITE);
            try {
                if (isLegalMove(p.getPosition(), endPosition)) {
                    validMoves.add(new Move(p.getPosition(), endPosition)); // Store the valid move in the Map
                }
            } catch (IllegalArgumentException ignored){}
        }

        return validMoves;
    }

    public ArrayList<Piece> getMovablePieces(){
        ArrayList<Piece> movablePieces = new ArrayList<Piece>();

        if (board.hasBarPieces(currentPlayer)){
            movablePieces.add(board.getBar(currentPlayer).getTopChecker());
        }
        else {
            for (Point p : board.getPoints()) {
                p.setColor(currentPlayer.getColor());
                if (!p.isOff(currentPlayer.getColor())){
                    if (p.isPlayers(currentPlayer)) {
                        movablePieces.add(p.getTopChecker());
                        if(p.getTopChecker()==null) {
                            System.out.println("Break");
                        }
                    }
                }
            }
        }
        return movablePieces;
    }

    public String[] validMovesString(List<Move> validMoves){
        String[] moves = new String[validMoves.size()];

        for (int i=0;i< validMoves.size();i++){
            moves[i] = "From "+ validMoves.get(i).getFromPip(board,currentPlayer) +" to "+ validMoves.get(i).getToPip(board,currentPlayer);
        }

        return moves;
    }

    public static int calculate(int a, int b, boolean subtract) {
        if (subtract) {
            return Math.max(0, a - b);
        } else {
            return Math.min(25, a + b);
        }
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

    public boolean hasDouble() {
        return currentPlayer.equals(cube.getOwner());
    }

    public void doubleBet(){
        Player newPlayer = cube.getOwner() == this.players[0] ? this.players[1] : this.players[0];
        if (chooseOption(newPlayer.getName()+", "+currentPlayer.getName()+" has offered to double the bet. Do you accept?",new String[]{"Yes","No"})==0) {
            cube.doubleScore(newPlayer);
            updateLog("The doubling cube now shows " + cube.getDouble() + " and " + cube.getOwner().getName() + " has possession");
        } else {
            updateLog(newPlayer.getName()+" has rejected the offer to double the bet and loses the game");
            // TODO change this to use the isOngoing boolean
            setGameState(Game.GameState.LOST);
        }
    }

    public static class Move{
        private final int from, to;

        public Move(int from,int to){
            this.from=from;
            this.to = to;
        }

        public boolean equals(Move other){
            // don't need to check individual attributes,
            // we can just check if both objects are literally identical
            return this == other;
        }

        public int getFrom(){return from;}
        public int getTo(){return to;}
        public int getFromPip(Board board, Player player){
            return board.getPoint(from).getPip(player);
        }
        public int getToPip(Board board, Player player){
            return board.getPoint(to).getPip(player);
        }

    }

    public Player[] getPlayers(){
        return this.players;
    }

}

