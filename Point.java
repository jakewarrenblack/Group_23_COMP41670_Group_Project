import java.util.Stack;

public class Point {
    protected final int positionWhite;
    protected final int positionBlack;
    protected final int row;
    protected final int col;

    //    private Piece[] pieces;
    protected Stack<Piece> pieces = new Stack<>();

    public Point(int positionWhite, int positionBlack, int col, int row) {
        this.positionWhite = positionWhite;
        this.positionBlack = positionBlack;
        this.row = row;
        this.col = col;
    }
    public Piece getTopChecker() {
        if (!this.pieces.isEmpty()) {
            return pieces.peek();
        } else {
            //FIXME: add error handling case where the stack is empty (no checkers on the piece)
            return null;
        }
    }

    /**
     * Adds a piece to this point and updates the piece's position to the position of this point
     *
     * @param piece The piece to add
     *              TODO Should probably add an exception for if we try to add a piece of a different colour
     */
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

        if ( this.pieces.size()>0){
            return this.pieces.pop();
        } else {
            return null;
        }
    }


    /**
     * The number of pieces currently placed on this point
     *
     * @return Integer value of number of pieces
     */
    public int numPieces() {
        // Need to include error handling for point with no pieces
        return this.pieces.size();
    }
    // TODO This seems to only be used in unit tests. Do we really need it?
    public int getPosition(boolean isWhite) {
        return isWhite ? positionWhite:positionBlack;
    }

    /**
     * The colour of the pieces placed on this point
     *
     * @return String indicating the colour of the pieces on the point
     */
    public String getColour() {
        // Need to include error handling for point with no pieces
        String printCol = "  ";
        if (this.pieces.size()>0) {
            switch (this.pieces.peek().getColor()) {
                case BLACK:
                    printCol = " B ";
                    break;
                case WHITE:
                    printCol = " W ";
                    break;
            }
        }
        return printCol;
    }
    public boolean isPlayers(Player chkPlayer){
        if (pieces.size()>0) {
            return chkPlayer.equals(pieces.peek().getPlayer());
        } else {return false;}
    }

    public int[] getCoords(){
        return new int[]{row, col};}
    public boolean isFull(){return pieces.size()==6;}
    public boolean isBlot(){return pieces.size()==1;}
    public boolean isEmpty(){return pieces.size()==0;}
    public boolean isOff(){return false;}
    public int getPip(){
        if (isEmpty()){ return 0;}
        else {return this.pieces.peek().equals(Player.Color.WHITE) ? positionWhite:positionBlack;}
    }
}