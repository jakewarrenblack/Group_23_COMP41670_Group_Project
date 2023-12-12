import java.util.Stack;

public class OffBoard extends Point{
    private boolean returnWhite;
    private final Stack<Piece> piecesWhite = new Stack<>();
    private final Stack<Piece> piecesBlack = new Stack<>();

    public OffBoard(int positionWhite, int positionBlack, int col, int row) {
        super(positionWhite,positionBlack,col,row);
        returnWhite=true;
    }
    public void setColor(boolean isWhite){
        returnWhite=isWhite;
        pieces=activeStack();
    }
    public void setColor(Player.Color color){
        returnWhite=color== Player.Color.WHITE;
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
        // Cannot add black piece to white off and vice verse
        // Cannot add black piece to white bar and vice verse
        if(piece.getColor().equals(Player.Color.WHITE)==returnWhite) {
            super.addPiece(piece);
        } else {throw new IllegalArgumentException("This point is not focused on "+piece.getColor().name()+" and cannot accept this piece right now");}
    }
    // TODO I'm sure there's a more elegant way to code this
    public Piece removePiece(){
        if(returnWhite){
            if (positionWhite!=0){
                return super.removePiece();
            } else {return null;}
        } else {
            if (positionBlack!=0){
                return super.removePiece();
            } else {return null;}
        }
    }
    public boolean isFull() {return false;}

    public boolean isBlot(){return false;}
    //TODO figure out a way to do this without hard coding
    public boolean isOff(Player.Color color) {
        if (color.equals(Player.Color.WHITE)) {
            return positionWhite == 0;
        } else {
            return positionWhite == 25;
        }
    }
    public boolean isPlayers(Player chkPlayer) {
        if (returnWhite) {
            return chkPlayer.getColor().equals(Player.Color.WHITE);
        } else {
            return chkPlayer.getColor().equals(Player.Color.BLACK);
        }
    }
    // TODO figure out a way to do this without hard coding
    public int[] getCoords() {
        // Always column, row
        int col = -1;
        int row = -1;
        if (returnWhite) {
            row = 13;
            col = (positionWhite == 0 ? this.col : 6);
        } else {
            row=1;
            col = (positionBlack == 0 ? this.col:6);}
        return new int[]{col, row};
    }
}