public class Cube {
    private final int[] values = new int[]{1,2,4,8,16,32,64};
    private int doublings;
    private Player owner;
    public Cube(){
        doublings=0;
    }
    public void doubleScore(Player newOwner){
        Math.max(doublings++,values.length);
        this.owner=newOwner;
    }
    public void setOwner(Player owner){
        this.owner=owner;
    }
    public boolean hasOwner(){return owner!=null;}
    public boolean isOwnedBy(Player player) {
        if (owner != null) {
            return owner.equals(player);
        } else {
            return false;
        }
    }
    public Player getOwner(){return owner;}
    public int getDouble(){return values[doublings];}

    public String doubleStatus(){
        String status = "Double: "+String.valueOf(getDouble());
        if (owner==null){
            return status+" no owner";
        } else {
            return status+" "+owner.getName()+" in possession";
        }
    }
}
