/**
 * The Doubling Cube controls the multiple of a player's pip score they
 * incur when they use
 * It repeatedly doubles from 1 to 64
 * At the start of a match it isn't owned by either player, but once a player claims it
 * it continually passes back and forth
 */
public class Cube {
    /**
     * The values the doubling cube can take
     */
    private final int[] values = new int[]{1,2,4,8,16,32,64};
    /**
     * The number of doublings the players have agreed
     */
    private int doublings;
    /**
     * The player who owns the doubling cube at present
     */
    private Player owner;
    public Cube(){
        doublings=0;
    }

    /**
     * Increase the doubling value and put a new player in possession
     *
     * @param newOwner
     */
    public void doubleScore(Player newOwner){
        Math.max(doublings++,values.length);
        this.owner=newOwner;
    }

    /**
     * Set a new owner
     * Mostly used for testing
     *
     * @param owner
     */
    protected void setOwner(Player owner){
        this.owner=owner;
    }

    /**
     * Check if a player has taken ownership of the cube yet
     *
     * @return true if a player owns the cube, false if not
     */
    public boolean hasOwner(){return owner!=null;}

    /**
     * Who is a particular player owns the cube
     *
     * @param player to check
     * @return true if the player does own the cube, false otherwise
     */
    public boolean isOwnedBy(Player player) {
        return owner != null && owner.equals(player);
    }

    /**
     * Return the player who owns the cube
     *
     * @return Player object representing the owner of the cube
     */
    public Player getOwner(){return owner;}

    /**
     * The current value of the cube
     *
     * @return an integer representing the value of the doubling cube
     */
    public int getDouble(){return values[doublings];}

    /**
     * The cube ownership status and face value of the cube
     * @return a String saying who owns the cube and what its face value is
     */
    public String doubleStatus(){
        return owner == null ? "Double: " + getDouble() + " no owner" : "Double: " + getDouble() + " " + owner.getName() + " in possession";
    }
}
