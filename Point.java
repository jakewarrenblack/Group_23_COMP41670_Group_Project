import java.util.ArrayList;

public class Point {
    private final int position;
    private Piece[] pieces;

    public Point(int position){
        this.position = position;
        this.pieces = new Piece[1];
    }

    public void addPiece(Piece piece){
        this.pieces[0] = piece;
    }

    public void removePiece(){
        this.pieces = new Piece[1];
    }

    public Piece getPiece(){
        return this.pieces[0];
    }

    public boolean isOccupied(){
        return this.pieces[0] != null;
    }

    public int getPosition(){
        return this.position;
    }
}
