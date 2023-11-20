import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Point {
    private final int position;
//    private Piece[] pieces;
    private Stack<Piece> pieces = new Stack<>();

    public Point(int position){
        this.position = position;

    }

    /**
     * Adds a piece to this point and updates the piece's position to the position of this point
     * @param piece         The piece to add
     * TODO Should probably add an exception for if we try to add a piece of a different colour
     */
    public void addPiece(Piece piece){
        this.pieces.push(piece);
        piece.setPosition(this.position);
    }

    /**
     * Removes the top piece from this point
     * @return              The piece which has been removed
     */
    public Piece removePiece(){
        return this.pieces.pop();
    }

//    public Piece getPiece(){return this.pieces[0];}

    /**
     * The number of pieces currently placed on this point
     * @return              Integer value of number of pieces
     */
    public int numPieces(){
        // Need to include error handling for point with no pieces
        return this.pieces.size();}
//    public boolean isOccupied(){return this.pieces[0] != null;}

    public int getPosition(){
        return this.position;
    }

    /**
     * The colour of the pieces placed on this point
     * @return              String indicating the colour of the pieces on the point
     */
    public String getColour(){
        // Need to include error handling for point with no pieces
        String printCol="  ";
        switch (this.pieces.peek().getColor()) {
            case BLACK:
                printCol = " B ";
                break;
            case WHITE:
                printCol = " W ";
                break;
        }
        return printCol;
        }
    }

