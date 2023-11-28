import java.util.Stack;

public class OffBoard extends Point{
    private boolean returnWhite;
    private Stack<Piece> piecesWhite = new Stack<>();
    private Stack<Piece> piecesBlack = new Stack<>();

    public OffBoard(int positionWhite, int positionBlack, int col, int row) {
        super(positionWhite,positionBlack,col,row);
        returnWhite=true;
    }
    public void setColor(boolean isWhite){
        returnWhite=isWhite;
    }
    private Stack<Piece> activeStack(){
        return returnWhite? piecesWhite:piecesBlack;
    }
    public Piece getTopChecker() {
        Stack<Piece> activeStack = activeStack();
        if (!activeStack.isEmpty()) {
            return activeStack.peek();
        } else {throw new IndexOutOfBoundsException("No checkers on this point");}
    }

    /**
     * Adds a piece to this point and updates the piece's position to the position of this point
     *
     * @param piece The piece to add
     *              TODO Should probably add an exception for if we try to add a piece of a different colour
     */
    public void addPiece(Piece piece) {
        Stack<Piece> activeStack = activeStack();
        activeStack.push(piece);
        piece.setPosition(this.positionWhite);
    }
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
        throw new IllegalArgumentException("Bar and Off do not have color attributes");
    }
    public boolean isPlayers(Player chkPlayer){
        return false;
    }

    public boolean isFull() {return false;}

    public boolean isBlot(){return false;}
    public boolean isEmpty(){
        Stack<Piece> activeStack = activeStack();
        return activeStack.size()==0;
    }
    public int getPip(Player.Color color){
        return returnWhite ? positionWhite:positionBlack;
    }
    public int[] getCoords() {
        int col = -1;
        int row = this.row;
        if (returnWhite){
            col = (positionWhite == 0 ? this.col:6);
        } else { col = (positionBlack == 0 ? this.col:6);}
        return new int[]{row, col};
    }
}