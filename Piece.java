public class Piece {
    enum Color {BLACK, WHITE};
    public final Color color;

    private int position;

    public Piece(Color color){
        this.color = color;
        this.position = 0;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getPosition(){
        return this.position;
    }

    public Color getColor(){
        return this.color;
    }

}
