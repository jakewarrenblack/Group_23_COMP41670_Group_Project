import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Point {
    private final int position;
    private Stack<Piece> pieces = new Stack<>();

    public Point(int position){
        this.position = position;
    }

    public Piece getTopChecker() {
        if (!this.pieces.isEmpty()) {
            return pieces.peek();
        } else {
            //FIXME: add error handling case where the stack is empty (no checkers on the piece)
            return null;
        }
    }

    public void addPiece(Piece piece){
        this.pieces.push(piece);
        piece.setPosition(this.position);
    }

    public Piece removePiece(){
        return this.pieces.pop();
    }



    public int numPieces(){
        // Need to include error handling for point with no pieces
        return this.pieces.size();
    }

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

    @Override
    public String toString() {
        if (this.numPieces() == 0) {
            return " | ";
        } else {
            return this.getColour();
        }
    }
}

