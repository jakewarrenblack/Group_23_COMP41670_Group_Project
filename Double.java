public class Double {
    private final int[] values = new int[]{2,4,8,16,32,64};
    private int doublings;
    private Player owner;
    public Double(){
        doublings=0;
    }
    public void doubleScore(Player newOwner){
        Math.max(doublings++,5);
        this.owner=newOwner;
    }
    public void setOwner(Player owner){
        this.owner=owner;
    }
    public Player getOwner(){return owner;}
    public int getDouble(){return values[doublings];}
}
