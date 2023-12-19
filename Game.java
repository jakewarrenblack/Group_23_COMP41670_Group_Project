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
    private Player currentPlayer;
    private final Log log;
    private Command command;

    public Game(Player[] players, int gameNumber, int matchGames) {
        this.board = new Board(gameNumber,matchGames,players[0].printScore(),players[1].printScore());
        this.log = Log.getInstance();
    }
    public void setCommand(Command command){this.command=command;}


    public void setCurrentPlayer(Player player){this.currentPlayer=player;}


    public boolean isGameWon() {

        return !this.isOngoing;
    }

    public boolean isGameOngoing() {
        return this.isOngoing;
    }

    public void finishGame(Player loser,int doubleValue) {
        this.isOngoing = false;
        loser.loseGame(doubleValue);
    }


    /** Place the pieces of a player in the start positions on the board
     *
     * @param player whose pieces are to be placed on the board
     */
    public void placePieces(Player player) {
        board.placePieces(player);
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
        if(from<0|to<0|from>25|to>25){
            throw new IllegalArgumentException("There are only 26 valid points");
        }

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
    public void print(String doubleStatus) {
        board.print(currentPlayer.getColor(), log.recentLog(10),doubleStatus);
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
    public void processRolls(List<Integer> diceRolls,Player currentPlayer,String doubleStatus, String... selectedTestOption){
        this.currentPlayer=currentPlayer;
        processDiceRolls(diceRolls,doubleStatus, selectedTestOption);

        isOngoing = !currentPlayer.hasWon();
    }

    /**
     * Allow the current player to choose between the different moves available to them
     * from their dice roll
     *
     * NB - they cannot use the smaller roll first if that would mean they're unable to use the larger roll on this turn
     *
     * @param diceRolls
     */
    public void processDiceRolls(List<Integer> diceRolls, String doubleStatus, String... selectedTestOption) {
        for(int i=0; i<diceRolls.size(); i++){
            ArrayList<Move> validMoves = getAvailableValidMoves(diceRolls.get(i));

            if (!validMoves.isEmpty()) {
                String[] validMoveString = validMovesString(validMoves);
                board.print(currentPlayer.getColor(), log.recentLog(10), doubleStatus);

                int chosenMove = Command.chooseOption(currentPlayer.getName() + " you rolled " + diceRolls.get(i) + ". Choose your move: ", validMoveString);


                // **They cannot use the smaller roll first if that would mean they're unable to use the larger roll on this turn**
                int otherRoll = diceRolls.get(0)==validMoves.get(chosenMove).getFrom()-validMoves.get(chosenMove).getTo()?diceRolls.get(1):diceRolls.get(0);
                validMoves = getAvailableValidMoves(otherRoll);

                if(validMoves.isEmpty()){
                    log.updateLog("You have no valid moves to make with your other die roll of "+otherRoll);
                }
                else{
                    // use the option we got from the text file. this will have been converted from a letter to a number (in string format)
                    if (selectedTestOption.length > 0) {
                        chosenMove = Integer.parseInt(selectedTestOption[0]);
                    }

                    command.acceptCommand("move " + validMoves.get(chosenMove).getFrom() + " " + validMoves.get(chosenMove).getTo());
                }

            } else {
                log.updateLog("You have no valid moves to make with your die roll of " + diceRolls.get(i));
            }

            if (i < diceRolls.size()) {
                log.updateLog("You have no more valid moves");
            }
        }
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
        for (Move validMove:validMoves){
            // Make the move as a test
            movePiece(validMove.getFrom(),validMove.getTo());
            ArrayList<Move> testLarge = getAvailableValidMoves(Math.max(dieRoll.get(0),dieRoll.get(1)));
            // If there are no valid moves with the larger dice roll, capture the move for later deletion
            if (testLarge.isEmpty()){
                invalids.add(validMove);
            }
            // Reverse the test move
            movePiece(validMove.getTo(),validMove.getFrom());
        }
        // Delete the moves with the smaller die that would leave you with no valid moves with the larger
        for (Move invalid:invalids){
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
                    validMoves.add(new Move(p.getPosition(), endPosition,dieRoll)); // Store the valid move in the Map
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
        /**
         * The index of the point the piece would be moving from
         */
        private int from;
        /**
         * The index of the point the piece would be moving to
         */
        private int to;
        /**
         * The value of the die roll which generated the move
         * This needs to be tracked as if a move is bearing a piece off the difference
         * between from and to may not be equal to the value of the die roll
         */
        private int roll;

        public Move(int from,int to, int roll){
            this.from=from;
            this.to = to;
            this.roll = roll;
        }

        /**
         * Two moves are only considered to be equal if their from, to, and roll values are equal
         * I was initially relying on the equals() method but it caused some bugs towards the end of the game so
         * I implemented this overriding method
         * @param other the other Move instance to compare
         * @return a boolean indicating whether the other Move is equal to this instance
         */
        public boolean equals(Move other){
            return this.from==other.from & this.to==other.to & this.roll==other.roll;
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
}

