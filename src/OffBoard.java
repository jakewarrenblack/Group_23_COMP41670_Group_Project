import java.util.Stack;

/**
 * At the beginning and end of the board there are Off Board spaces, which double as Bar spaces
 * An off-board point contains a white point and a black point
 * The point which interacts with other classes can be defined by the color of the player we're
 * interested in
 */

/*
- Team number: 23
- Team members: Jake Black & Richard Mitchell
- Github IDs: jakewarrenblack & richardmitchell1
*/
public class OffBoard extends Point{
    /**
     * Whether the colour we're interested in at present is white
     */
    private boolean returnWhite;
    /**
     * The active point at present. Can be the wh
     */
    private Point activePoint;
    /**
     * The white point. If the position is 0, this will be the Off Board. If it's 25, this will be the Bar
     */
    private final Point pointWhite;
    /**
     * The black point. If the position is 0, this will be the bar. If it's 25, this will be the off point
     */
    private final Point pointBlack;
    // TODO Fix the absolute value on row

    /**
     * Construct a new OffBoard instance
     * @param positionWhite The position of the Point on the board from the perspective of the WHITE player
     * @param positionBlack The position of the Point on the board from the perspective of the BLACK player
     * @param col The column the representation of the Point should be shown in when being printed to the console
     * @param row The row the representation of the point should start from when being printed to the console
     */
    public OffBoard(int positionWhite, int positionBlack, int col, int row) {
        super(positionWhite,positionBlack,col,row);
        int blackCol = positionBlack==0?col: (col/2);
        int whiteCol = positionBlack==0? (col/2) :col;
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
    /**
     * Set the focus colour of the point
     * Only really works for OffBoard Points - other points have colours
     * defined by the pieces they hold
     *
     * @param color the colour on which to focus the point
     */
    public void setColor(Player.Color color){
        returnWhite=color== Player.Color.WHITE;
        activePoint=activePoint();
    }

    /**
     * Set the active point based on the returnWhite boolean
     * @return the white point if returnWhite is true, the black point otherwise
     */
    public Point activePoint(){
        return returnWhite?pointWhite:pointBlack;
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

    /**
     * Checks if the point is full. Off Board and Bar points are never full
     * @return false
     */
    public boolean isFull() {return false;}
    /**
     * A blot is a point with only one piece on it
     *
     * @return always false as blots are not relevant to OffBoard points
     */
    public boolean isBlot(){return false;}
    /**
     * Checks if the activePoint holds any pieces
     * @return true if there are no pieces, false otherwise
     */
    public boolean isEmpty(){return activePoint.isEmpty();}
    //TODO figure out a way to do this without hard coding

    /**
     * Checks if this point is the off board area for the player of interest
     * @param color of the player of interest
     * @return true if the column of the activePoint is 13
     */
    public boolean isOff(Player.Color color) {
        setColor(color);
        return activePoint.col == 13;
    }
    /**
     * Checks if the activePoint holds Pieces of the player of interest
     * @param chkPlayer
     * @return true if the pieces belong to the player, false if they don't or there are no pieces
     */
    public boolean isPlayers(Player chkPlayer) {
        setColor(chkPlayer.getColor());
        return !activePoint.isEmpty();
    }
    /**
     * The position of the activePoint in the board representation to be printed to the console
     * @return an integer array. First entry is the column, second entry is the row
     */
    public int[] getCoords() {
        return activePoint.getCoords();
    }
    /**
     * Return the piece currently on top of the activePoint
     * @return a Piece instance
     */
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

}