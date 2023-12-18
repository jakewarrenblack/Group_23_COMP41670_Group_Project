import java.util.*;

/**
 * Conduct a single game of Backgammon
 */
public class Game {
    /**
     * The board the game will be played on
     */
    private final Board board;
    /**
     * Whether the game is finished
     */
    private boolean isOngoing = true;
    private final Cube cube;

    private Player[] players;
    private Player currentPlayer;
    private final Die die;
    private final Log log;
    private Command command;

    public enum GameState {
        ONGOING,
        WON,
        LOST
    }


    /**
     * Create a new game
     *
     * @param gameNumber the number of the game in the match
     * @param matchGames the number of games in the match
     * @param players the players in the game
     */
    // players are an optional parameter
    // if they're provided, use them. otherwise, run addPlayers
    public Game(int gameNumber, int matchGames, Player... players) {
        // Singleton pattern. No need to pass around instances of these, there can be only one of each for a given game.
        this.die = Die.getInstance();
        this.log = Log.getInstance();
        this.cube = new Cube();
        this.players = players.length == 2 ? players : addPlayers();
        this.board = new Board(gameNumber,matchGames, this.players);

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

    public void nextTurn() {
        // just switch between 0 and 1, whichever is NOT the current player
        this.currentPlayer = this.players[0] == this.currentPlayer ? this.players[1] : this.players[0];
        board.print(currentPlayer.getColor(), log.recentLog(10));
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

    public void finishGame() {
        this.isOngoing = false;
    }

    public void setGameState(GameState gameState) {
        this.isOngoing = gameState == GameState.ONGOING;
    }

    // TODO can probably delete this as duplicating functionality in Match
    public void addPlayer(int index, Player player, boolean isCurrentPlayer) {
        this.players[index] = player;
        if (isCurrentPlayer) {
            currentPlayer = player;
        }
    }


    /**
     * Construct two players with names entered by the users
     *
     * @return  an array of 2 Player objects
     */
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

    /**
     * Move a piece from one point to another
     *
     * @param from the index of the point the piece is being moved from
     * @param to the index of the point the piece is being moved to
     */
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

    /**
     * Print a text representation of the current game to the screen
     */
    public void print() {
        board.print(currentPlayer.getColor(), log.recentLog(10));
    }

    /**
     * Calculate the pip score of both players, print it to the screen and add it to the game log
     */
    // NB - this should happen here rather than in Match as it's the pip score of the current game, not the overall
    // match score
    public void pipScore() {
        for (int i = 0; i < 2; i++) {
            log.updateLog(players[i].getName() + " has a pip score of " + players[i].pipScore());
        }
    }
    // TODO would we need this if we moved most of the move validation functionality to the
    // Board class? Seems to only be used in the unit tests
    protected Board getBoard() {
        return this.board;
    }


    /**
     * Allow the current player to choose what to do with their dice rolls
     * If they roll a double they get to use each die twice - ie, they get to make 4 moves
     *
     * @param diceRolls the values of their dice rolls
     */
    public void processRolls(List<Integer> diceRolls,Player currentPlayer) {
        this.currentPlayer = currentPlayer;
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


    /**
     * Allow the current player to choose between the different moves available to them
     * from their dice roll
     *
     * NB - they cannot use the smaller roll first if that would mean they're unable to use the larger roll on this turn
     *
     * @param diceRolls
     */

    public void processDifferentDiceRolls(List<Integer> diceRolls) {
        ArrayList<Move> validMoves = getAllAvailableValidMoves(diceRolls);
        String[] validMoveString = validMovesString(validMoves);

        int chosenMove = chooseOption(currentPlayer.getName() + " you rolled " + diceRolls.get(0) + " and " + diceRolls.get(1) + ". Choose your first move: ", validMoveString);

        command.acceptCommand("move " + validMoves.get(chosenMove).getFrom() + " " + validMoves.get(chosenMove).getTo());

        int otherRoll = diceRolls.get(0) == validMoves.get(chosenMove).getFrom() - validMoves.get(chosenMove).getTo() ? diceRolls.get(1) : diceRolls.get(0);

        print();

        validMoves = getAvailableValidMoves(otherRoll);

        if (validMoves.isEmpty()) {
            log.updateLog("You have no valid moves to make with your other die roll of " + otherRoll);
        } else {
            validMoveString = validMovesString(validMoves);
            chosenMove = Command.chooseOption(currentPlayer.getName() + " you rolled " + otherRoll + " with your other die. Choose your second move: ", validMoveString);
            command.acceptCommand("move " + validMoves.get(chosenMove).getFrom() + " " + validMoves.get(chosenMove).getTo());


            if (validMoves.isEmpty()) {
                updateLog("You have no valid moves to make with your other die roll of " + otherRoll);
            } else {
                validMoveString = validMovesString(validMoves);
                chosenMove = chooseOption(currentPlayer.getName() + " you rolled " + otherRoll + " with your other die. Choose your second move: ", validMoveString);
                command.acceptCommand("move " + validMoves.get(chosenMove).getFrom() + " " + validMoves.get(chosenMove).getTo());
            }
            print();
        }
    }



    /**
     * Allow the current player to choose between the different moves available to them
     * from their dice roll
     *
     * @param diceRolls
     */
    public void processDoubleDiceRolls(List<Integer> diceRolls){
        int i=0;
        ArrayList<Move> validMoves=getAvailableValidMoves(diceRolls.get(i));

        while (!validMoves.isEmpty() && i<diceRolls.size()){

            String[] validMoveString=validMovesString(validMoves);
            int chosenMove = Command.chooseOption(currentPlayer.getName() +" you have the following options with dice number "+i+". Please choose: ",validMoveString );
            command.acceptCommand("move "+validMoves.get(chosenMove).getFrom()+" "+validMoves.get(chosenMove).getTo());
            i++;

            if (i<diceRolls.size()){
                validMoves=getAvailableValidMoves(diceRolls.get(i));
            }
        }

        print();
        if (i<diceRolls.size()){log.updateLog("You have no more valid moves");}
    }


    /**
     * Identify all moves available to a player when they're rolled different values on their dice
     *
     * NB - they cannot use the smaller roll first if that would mean they're unable to use the larger roll on this turn
     *
     * @param dieRoll the values of the two dice rolled
     * @return an ArrayList of valid moves covering both dice
     */

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

    /**
     * Identify all moves available to a player with a single die
     *
     * NB - they cannot use the smaller roll first if that would mean they're unable to use the larger roll on this turn
     *
     * @param dieRoll
     * @return an ArrayList of valid moves
     */
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

    /**
     * Identify the pieces of the current player which can be moved at the moment
     * If they have a piece on the bar they have to move that one first
     *
     * @return a list of Pieces which can be moved
     */
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
                    }
                }
            }
        }
        return movablePieces;
    }

    /**
     * Translate a list of Move objects into a string array that can be passed to chooseOption
     *
     * @param validMoves a List of Move objects
     * @return a String array representing the moves in text
     */
    public String[] validMovesString(List<Move> validMoves){
        String[] moves = new String[validMoves.size()];

        for (int i=0;i< validMoves.size();i++){
            moves[i] = "From "+ validMoves.get(i).getFromPip(board,currentPlayer) +" to "+ validMoves.get(i).getToPip(board,currentPlayer);
        }

        return moves;
    }

    /**
     * Calculate where a dice roll would move a piece
     * If the player is black it will increase the position of the piece by however much is rolled
     * If the player is white it will decrease the position of the piece by however much is rolled
     *
     * @param a the index of the point the piece is on
     * @param b the value of the die roll
     * @param subtract whether the player is white (true) or black (false)
     * @return an integer showing the index of the point the piece would move to
     *
     */
    public static int calculate(int a, int b, boolean subtract) {
        if (subtract) {
            return Math.max(0, a - b);
        } else {
            return Math.min(25, a + b);
        }
    }


    /**
     * Captures the details of a potential move
     */

    public static class Move{
        private final int from, to;

        public Move(int from,int to){
            this.from=from;
            this.to = to;
        }


        /**
         * Two moves are only considered to be equal if their from, to, and roll values are equal
         * @param other the other Move instance to compare
         * @return a boolean indicating whether the other Move is equal to this instance
         */

        public boolean equals(Move other){
            // don't need to check individual attributes,
            // we can just check if both objects are literally identical
            return this == other;
        }

        public int getFrom(){return from;}
        public int getTo(){return to;}

        /**
         * Get the pip value of the from point for the specified player
         * @param board
         * @param player
         * @return an int representing the pip score of the from ppint
         */
        public int getFromPip(Board board, Player player){
            return board.getPoint(from).getPip(player);
        }
        /**
         * Get the pip value of the to point for the specified player
         * @param board
         * @param player
         * @return an int representing the pip score of the to ppint
         */
        public int getToPip(Board board, Player player){
            return board.getPoint(to).getPip(player);
        }

    }

    public Player[] getPlayers(){
        return this.players;
    }

}