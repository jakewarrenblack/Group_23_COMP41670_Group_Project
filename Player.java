public class Player  {
    private final String name;
    private int score;
    private final int[] startPoints = new int[]{24,24,13,13,13,13,13,8,8,8,6,6,6,6,6};

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
            if (color.equals(Color.BLACK)){position = 25-position;}
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

    /**
     * Returns the colour of the pieces used by this player
     * @return          The color of the pieces used by this player
     */
    public Color getColor(){return this.color;}

    /**
     * Returns one of the player's pieced
     * @param index         The index of the piece to returns
     * @return              The returned piece
     */
    public Piece getPiece(int index){
        return pieces[index];
    }
    protected Piece[] getPieces(){return this.pieces;}
    /**
     * How many pieces does the player have?
     * @return              The number of pieces in the player's collection
     */
    public int numPieces(){return pieces.length;}

    /**
     * Gives the player's pip score.
     * The pip score is calculated by taking the pip value of the points on which every piece is positioned
     * @return              The player's pip score
     */
    public int pipScore(){
        int pipScore = 0;
        for (int i=0;i< pieces.length;i++){
            pipScore+=pieces[i].getPip();
        }
        return pipScore;
    }

    /**
     * What is the position of the point a piece is sitting on
     * @param index         The index of the piece
     * @return              The position of the point the piece is on
     */
    public int piecePosition(int index){return pieces[index].getPosition();}
    // TODO Find a way to avoid absolute value
    public boolean canMoveOff(){
        int minPip = 26;
        for (Piece p:pieces){
            minPip = Math.min(minPip,p.getPip());
        }
        return minPip<6;
    }
    // TODO Find a way to avoid absolute value
    public boolean hasWon(){
        int minPip = 0;
        for (Piece p:pieces){
            minPip=Math.max(minPip,p.getPip());
        }
        return minPip<1;
    }
}
