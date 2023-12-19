/**
 * Each player gets 15 pieces of a particular colour - either black or white
 *
 */
public class Piece {
    /**
     * The player this piece belongs to
     */
    public Player player;
    /**
     * The piece's current position on the board
     */
    private int position;
    /**
     * The "pip" score for the player. Same for white, in reverse for black
     */
    private int pip;

    /**
     * Construct a new piece for White and place it in its initial position
     * Calculating the PIP uses a hard coded value at the moment.
     * Probably not an issue since there don't seem to be any moves in play to increase the size of the backgammon board
     * but could be an enhancement to link this in some way to the boardSize parameter in the Board class
     * just in case we ever decide to launch !!!SUPERBACKGAMMON!!!
     *
     * @param player
     * @param position
     */
    public Piece(Player player, int position){
        this.player = player;
        this.position = position;
        if (player.getColor().equals(Player.Color.BLACK)){
            this.pip=25-position;
        } else {this.pip=position;}
    }

    /**
     * Update the position of the piece
     * Again, replacing the hard code in the calculation of PIP could be an enhancement
     *
     * @param position
     */
    public void setPosition(int position){
        this.position = position;
        if (this.getColor().equals(Player.Color.BLACK)){
            this.pip=25-position;}
        else {this.pip=position;}
    }

    /**
     * Give the current position of the piece on the board
     *
     * @return integer representing the position of the point the piece is placed on
     */
    public int getPosition(){
        return this.position;
    }
    /**
     * Give the current PIP score of the piece
     *
     * @return integer representing the pip score of the piece
     */
    public int getPip(){
        return this.pip;
    }

    /**
     * Give the colour of the piece
     *
     * @return Player.Color of the piece
     */
    public Player.Color getColor(){
        return player.getColor();
    }

    /**
     * Give the player who owns the piece
     *
     * @return Player object the piece belongs to
     */
    public Player getPlayer() {
        return player;
    }
}
