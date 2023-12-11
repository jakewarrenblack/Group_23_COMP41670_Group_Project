import java.util.Stack;

public class OffBoard extends Point{
    private boolean returnWhite;
    private Stack<Piece> piecesWhite = new Stack<>();
    private Stack<Piece> piecesBlack = new Stack<>();

    public OffBoard(int positionWhite, int positionBlack, int col, int row) {
        super(positionWhite,positionBlack,col,row);
        returnWhite=true;
    }
    // TODO Not sure we need this one. It seems to be redundant with activeStack?
    public void setColor(boolean isWhite){
        returnWhite=isWhite;
        pieces=activeStack();
    }
    public Stack<Piece> activeStack(){
        return returnWhite? piecesWhite:piecesBlack;
    }

    /**
     * Adds a piece to this point and updates the piece's position to the position of this point
     *
     * @param piece The piece to add
     */
    public void addPiece(Piece piece) {
        if(piece.getColor().equals(Player.Color.WHITE)==returnWhite) {
            super.addPiece(piece);
        }
    }

    public boolean isFull() {return false;}

    public boolean isBlot(){return false;}
    public boolean isOff(){return true;}

    public int[] getCoords() {
        int col = -1;
        int row = this.row;
        if (returnWhite){
            col = (positionWhite == 0 ? this.col:6);
        } else { col = (positionBlack == 0 ? this.col:6);}
        return new int[]{row, col};
    }
}