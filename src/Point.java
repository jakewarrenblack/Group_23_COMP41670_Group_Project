import java.util.Stack;

/**
 * The Backgammon board is made up of Points which can hold Pieces
 * The can only hold Pieces of one colour at a time
 * And can only hold a maximum of six Pieces at once
 */

/*
- Team number: 23
- Team members: Jake Black & Richard Mitchell
- Github IDs: jakewarrenblack & richardmitchell1
*/
public class Point {
    /**
     * The position of the Point on the board from the perspective of the WHITE player
     */
    protected final int positionWhite;
    /**
     * The position of the Point on the board from the perspective of the BLACK player
     */
    protected final int positionBlack;
    /**
     * The row the representation of the point should start from when being printed to the console
     */
    protected final int row;
    /**
     * The column the representation of the Point should be shown in when being printed to the console
     */
    protected final int col;

    /**
     * The pieces currently on the point
     */
    protected Stack<Piece> pieces = new Stack<>();

    /**
     * Construct a new Point instance
     * @param positionWhite The position of the Point on the board from the perspective of the WHITE player
     * @param positionBlack The position of the Point on the board from the perspective of the BLACK player
     * @param col The column the representation of the Point should be shown in when being printed to the console
     * @param row The row the representation of the point should start from when being printed to the console
     */
    public Point(int positionWhite, int positionBlack, int col, int row) {
        this.positionWhite = positionWhite;
        this.positionBlack = positionBlack;
        this.row = row;
        this.col = col;
    }

    /**
     * Return the piece currently on top of the point
     * @return a Piece instance
     */
    public Piece getTopChecker() {
        return !this.pieces.isEmpty() ? pieces.peek() : null;
    }

    /**
     * Adds a piece to this point and updates the piece's position to the position of this point
     *
     * @param piece The piece to add
     */
    // No need for validation - this is handled in the getAvailableValidMoves methods of Game
    public void addPiece(Piece piece) {
        this.pieces.push(piece);
        piece.setPosition(this.positionWhite);
    }

    /**
     * Removes the top piece from this point
     *
     * @return The piece which has been removed
     */
    public Piece removePiece() {
        return !this.pieces.isEmpty() ? this.pieces.pop() : null;
    }

    /**
     * The number of pieces currently placed on this point
     *
     * @return Integer value of number of pieces
     */
    public int numPieces() {
        return this.pieces.size();
    }

    /**
     * The position of the point on the board
     * Used in unit testing
     *
     * @return Integer representing the position of the Point on the Board
     */
    protected int getPosition() {
        return positionWhite;
    }

    /**
     * The colour of the pieces placed on this point
     *
     * @return String indicating the colour of the pieces on the point
     */
    public String getColour() {
        // Need to include error handling for point with no pieces
        String printCol = "  ";
        if (!this.pieces.isEmpty()) {
            printCol = switch (this.pieces.peek().getColor()) {
                case BLACK -> " B ";
                case WHITE -> " W ";
            };
        }
        return printCol;
    }

    /**
     * Checks if the point holds Pieces of the player of interest
     * @param chkPlayer
     * @return true if the pieces belong to the player, false if they don't or there are no pieces
     */
    public boolean isPlayers(Player chkPlayer){
        if (!pieces.isEmpty()) {
            return chkPlayer.equals(pieces.peek().getPlayer());
        } else {return false;}
    }

    /**
     * The position of the point in the board representation to be printed to the console
     * @return an integer array. First entry is the column, second entry is the row
     */
    public int[] getCoords(){
        // Always col, row
        return new int[]{col, row};}

    /**
     * Checks if the point can hold more pieces
     * @return true if there are six pieces on the point, false otherwise
     */
    public boolean isFull(){return pieces.size()==6;}

    /**
     * A blot is a point with only one piece on it
     *
     * @return true if the Point has only one piece, false if it has none or more than one
     */
    public boolean isBlot(){
        return pieces.size()==1;
    }

    /**
     * Checks if the Point holds any pieces
     * @return true if there are no pieces, false otherwise
     */
    public boolean isEmpty(){return pieces.isEmpty();}

    /**
     * Checks if the point holds the Off Board for the player of interest
     *
     * @param color of the player of interest
     * @return always returns false for normal on-board points
     */
    public boolean isOff(Player.Color color){
        return false;
    }

    /**
     * Calculates the pip score for pieces of the player of interest on this point
     *
     * @param player
     * @return an integer representing the pip score of this point for a given player
     */
    public int getPip(Player player){
        return player.getColor().equals(Player.Color.WHITE) ? positionWhite:positionBlack;
    }
    /**
     * Set the focus colour of the point
     * Only really works for OffBoard Points - other points have colours
     * defined by the pieces they hold
     *
     * @param color the colour on which to focus the point
     */
    public void setColor(Player.Color color){}
}