import com.sun.source.tree.BreakTree;

public class Player  {
    protected final String name;
    protected int score;
    protected final int[] startPoints = new int[]{24,24,13,13,13,13,13,8,8,8,6,6,6,6,6};
    protected boolean hasDouble;
    protected Piece[] pieces;
    public enum Color {BLACK, WHITE};
    protected final Color color;

    private final int offPip = 0;
    private final int homePip = 6;
    private final int barPip = 25;

    public Player(String name, Color color){
        if(name.isBlank()){
            throw new IllegalArgumentException("Name may not be empty!");
        }
        this.name = name;
        this.color = color;
        this.score = 0;
        this.hasDouble=true;
        this.pieces = new Piece[startPoints.length];
        for (int i=0;i<startPoints.length;i++){
            int position = startPoints[i];
            if (color.equals(Color.BLACK)){position = 25-position;}
            pieces[i]=new Piece(this,position);
        }
    }
    public void setDouble(boolean hasDouble){this.hasDouble=hasDouble;}

    public boolean hasDouble() {
        return hasDouble;
    }

    public void setScore(int score){
        this.score += score;
    }

    public int getScore(){
        return this.score;
    }

    public String printScore(){return getName()+" match score: "+getScore();}
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

    public boolean canMoveOff(){
        int minPip = 1;
        for (Piece p:pieces){
            minPip = Math.max(minPip,p.getPip());
        }
        return minPip<=homePip;
    }

    public boolean isBarred(int from){
        int maxPip=0;
        for (Piece p:pieces){
            if (p.getPip()==25&p.getPosition()==from){return false;}
            maxPip=Math.max(maxPip,p.getPip());
        }
        return maxPip==barPip;
    }

    public boolean hasWon(){
        int maxPip = 0;
        for (Piece p:pieces){
            maxPip=Math.max(maxPip,p.getPip());
        }
        return maxPip==offPip;
    }

    public boolean hasOff(){
        int minPip = 25;
        for (Piece p:pieces){
            minPip=Math.min(minPip,p.getPip());
        }
        return minPip==offPip;
    }
    public boolean hasBar(){
        int maxPip = 0;
        for (Piece p:pieces){
            maxPip=Math.max(maxPip,p.getPip());
        }
        return maxPip==barPip;
    }
    public int getGameScore(int doubleValue){
        int score = pipScore()*doubleValue;
        // If a player loses without bearing off any checkers, they are gammoned and their score is doubled
        if (!hasOff()){
            // If a player loses while they haven't born off any checkers and they still have a checker on the bar
            // they are backgammoned and their score is quadrupled
            if (hasBar()) {
                score *= 4;
            } else { score*=2;}
        }
        return score;
    }
    public void loseGame(int doubleValue){
        this.score+=getGameScore(doubleValue);
    }
}
