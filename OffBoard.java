import java.util.Stack;

public class OffBoard extends Point{
    private boolean returnWhite;
    private Point activePoint;
    private final Point pointWhite;

    private final Point pointBlack;
    private final Stack<Piece> piecesWhite = new Stack<>();
    private final Stack<Piece> piecesBlack = new Stack<>();
    // TODO Fix the absolute value on row
    public OffBoard(int positionWhite, int positionBlack, int col, int row) {
        super(positionWhite,positionBlack,col,row);
        int blackCol = positionBlack==0?col:(int)(col/2);
        int whiteCol = positionBlack==0?(int)(col/2):col;
        int blackRow = 1;
        int whiteRow = 14;
        pointBlack=new Point(positionWhite,positionBlack,blackCol,blackRow);
        pointWhite=new Point(positionWhite,positionBlack,whiteCol,whiteRow);
        returnWhite=false;
        activePoint=pointBlack;
    }
    public void setColor(boolean isWhite){
        returnWhite=isWhite;
        activePoint=activePoint();
    }
    public void setColor(Player.Color color){
        returnWhite=color== Player.Color.WHITE;
        activePoint=activePoint();
    }
    public Point activePoint(){
        return returnWhite?pointWhite:pointBlack;
    }
    public Stack<Piece> activeStack(){
        return returnWhite? piecesWhite:piecesBlack;
    }
    /**
     * The number of pieces currently placed on this point
     *
     * @return Integer value of number of pieces
     */
    public int numPieces() {
        // Need to include error handling for point with no pieces
        return activePoint.pieces.size();
    }
    /**
     * Adds a piece to this point and updates the piece's position to the position of this point
     *
     * @param piece The piece to add
     */
    public void addPiece(Piece piece) {
        // Cannot add black piece to white off and vice verse
        // Cannot add black piece to white bar and vice verse
        setColor(piece.getColor());
        activePoint.addPiece(piece);
    }

    /**
     * Removes the top piece from this point
     *
     * @return The piece which has been removed
     */
    public Piece removePiece() {

        if (!activePoint.pieces.isEmpty()){
            return activePoint.pieces.pop();
        } else {
            return null;
        }
    }
    public boolean isFull() {return false;}

    public boolean isBlot(){return false;}
    //TODO figure out a way to do this without hard coding
    public boolean isOff(Player.Color color) {
        setColor(color);
        return activePoint.col == 13;
    }
    public boolean isPlayers(Player chkPlayer) {
        setColor(chkPlayer.getColor());
        return !activePoint.isEmpty();
    }
    // TODO figure out a way to do this without hard coding
    public int[] getCoords() {
        return activePoint.getCoords();
    }
    public Piece getTopChecker() {
        return activePoint.getTopChecker();
    }
    /**
     * The colour of the pieces placed on this point
     *
     * @return String indicating the colour of the pieces on the point
     */
    public String getColour() {
        // Need to include error handling for point with no pieces
        String printCol = "  ";
        if (!activePoint.pieces.isEmpty()) {
            printCol = switch (activePoint.pieces.peek().getColor()) {
                case BLACK -> " B ";
                case WHITE -> " W ";
            };
        }
        return printCol;
    }

    public boolean isEmpty(){return activePoint.isEmpty();}
}