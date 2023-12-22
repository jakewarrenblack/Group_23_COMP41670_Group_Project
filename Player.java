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

    /**
     * How many pieces does the player have?
     * @return The number of pieces in the player's collection
     */
    public int numPieces(){return pieces.length;}

    /**
     * Gives the player's pip score.
     * The pip score is calculated by taking the pip value of the points on which every piece is positioned
     * @return              The player's pip score
     */
    public int pipScore(){
        int pipScore = 0;
        for (Piece piece : pieces) {
            pipScore += piece.getPip();
        }
        return pipScore;
    }

    /**
     * What is the position of the point a piece is sitting on
     * @param index         The index of the piece
     * @return              The position of the point the piece is on
     */
    public int piecePosition(int index){return pieces[index].getPosition();}

    /**
     * Where should a piece be placed at the start of a game
     * @param index         The index of the piece
     * @return              The position of the point the piece should be placed on
     */
    public int startPosition(int index){
        int position = startPoints[index];
        if (color.equals(Color.BLACK)){position = 25-position;}
        return position;
    }
    /**
     * <b>Checks if the player can move off the board.</b>
     * <hr/>
     * This is determined by comparing the minimum pip value of all pieces to the home pip value.
     * @return true if the player can move off, false otherwise.
     */
    public boolean canMoveOff(){
        int minPip = 1;
        for (Piece p:pieces){
            minPip = Math.max(minPip,p.getPip());
        }
        int homePip = 6;
        return minPip<= homePip;
    }

    /**
     * <b>Checks if the player has any pieces on the bar and returns true if the player is trying to move another piece.</b>
     * <hr/>
     * This is determined by comparing the maximum pip value of all pieces to the bar pip value.
     * @param from the position to check.
     * @return true if the player is barred, false otherwise.
     */
    public boolean isBarred(int from){
        int maxPip=0;
        for (Piece p:pieces){
            if (p.getPip()==25&p.getPosition()==from){return false;}
            maxPip=Math.max(maxPip,p.getPip());
        }
        return maxPip==barPip;
    }


    /**
     * <b>Checks if the player has won the game.</b>
     * <hr/>
     * If all the player's pieces are on the off pip, they have won.
     * @return true if the player has won, false otherwise.
     */
    public boolean hasWon(){
        int maxPip = 0;
        for (Piece p:pieces){
            maxPip=Math.max(maxPip,p.getPip());
        }
        return maxPip==offPip;
    }

    /**
     * <b>Checks if the player has any pieces off the board.</b>
     * <hr/>
     * This is determined by comparing the minimum pip value of all pieces to the off pip value.
     * @return true if the player has pieces off the board, false otherwise.
     */
    public boolean hasOff(){
        int minPip = 25;
        for (Piece p:pieces){
            minPip=Math.min(minPip,p.getPip());
        }
        return minPip==offPip;
    }

    /**
     * <b>Checks if the player has any pieces on the bar.</b>
     * <hr/>
     * This is determined by comparing the maximum pip value of all pieces to the bar pip value.
     * @return true if the player has pieces on the bar, false otherwise.
     */
    public boolean hasBar(){
        int maxPip = 0;
        for (Piece p:pieces){
            maxPip=Math.max(maxPip,p.getPip());
        }
        return maxPip==barPip;
    }

    /**
     * <b>Calculates the game score for the player.</b>
     * <ul>
     *      <li>The score is determined by the pip score multiplied by the double value.</li>
     *      <li>If the player has not moved any pieces off the board, the score is doubled.</li>
     *      <li>If the player has not moved any pieces off the board and has a piece on the bar, the score is quadrupled.</li>
     * </ul>
     * @param doubleValue the value to multiply the pip score by.
     * @return the calculated game score.
     */
    public int getGameScore(int doubleValue){
        int score = pipScore()*doubleValue;
        // If a player loses without bearing off any checkers, they are gammoned and their score is doubled
        if (!hasOff()){
            Log.getInstance().updateLog(name+" you have not born off any checkers and so are gammoned!");
            // If a player loses while they haven't born off any checkers, and they still have a checker on the bar
            // they are backgammoned and their score is quadrupled
            if (hasBar()) {
                Log.getInstance().updateLog(name+" you still have checkers on the bar and so are backgammoned!");
                score *= 4;
            } else { score*=2;}
        }
        return score;
    }

    /**
     * Updates the player's score when they lose a game.
     * The score is increased by the game score calculated with the given double value.
     * @param doubleValue the value to calculate the game score with.
     */
    public void loseGame(int doubleValue){
        Log.getInstance().updateLog("Game over. Your final score is " + getGameScore(doubleValue));
        this.score+=getGameScore(doubleValue);
    }
}
