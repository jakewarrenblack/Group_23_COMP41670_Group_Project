public class Player  {
    private final String name;
    private int score;
    private final int[] startPoints = new int[]{23,23,12,12,12,12,12,7,7,7,5,5,5,5,5};

    private Piece[] pieces;
    public enum Color {BLACK, WHITE};
    public final Color color;

    public Player(String name, Color color){
        if(name.isBlank()){
            throw new IllegalArgumentException("Name may not be empty!");
        }
        this.name = name;
        this.color = color;
        this.score = 0;
        this.pieces = new Piece[startPoints.length];
        for (int i=0;i<startPoints.length;i++){
            int position = startPoints[i];
            if (color.equals(Color.BLACK)){position = 23-position;}
            pieces[i]=new Piece(this,position);
        }
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return this.score;
    }

    public String getName(){
        return this.name;
    }
    public Color getColor(){return this.color;}

    public Piece getPiece(int index){
        return pieces[index];
    }
    public int pipScore(){
        int pipScore = 0;
        for (int i=0;i< pieces.length;i++){
            pipScore+=pieces[i].getPip()+1;
        }
        return pipScore;
    }


}
