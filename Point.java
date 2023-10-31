import java.util.ArrayList;

public class Point {
    private final int position; // where on the board is this point
    // TODO: Limit this to 6 pieces per row
    private ArrayList<Piece> pieces;

    private boolean isOccupied;

    public Point(int position, ArrayList<Piece> pieces){
        this.position = position;
        this.pieces = pieces;
        this.isOccupied = false;
    }

    public void addPiece(Piece piece){
        this.pieces.add(piece);
    }

    public void removePiece(Piece piece){
        this.pieces.remove(piece);
    }

    public void setIsOccupied(boolean isOccupied){
        this.isOccupied = isOccupied;
    }

    public boolean isOccupied(){
        return this.isOccupied;
    }
}
