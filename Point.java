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

    public void addPiece(Piece piece){
        this.pieces.push(piece);
        piece.setPosition(this.position);
    }

    public Piece removePiece(){
        return this.pieces.pop();
    }

//    public Piece getPiece(){return this.pieces[0];}

    public int numPieces(){
        // Need to include error handling for point with no pieces
        return this.pieces.size();}
//    public boolean isOccupied(){return this.pieces[0] != null;}

    public int getPosition(){
        return this.position;
    }
    public String getColour(){
        // Need to include error handling for point with no pieces
        return switch (this.pieces.peek().getColor()) {
            case BLACK -> " B ";
            case WHITE -> " W ";
        };
        }
    }

