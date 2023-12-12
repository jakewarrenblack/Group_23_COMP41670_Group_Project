public class Piece {

    public Player player;

    private int position;
    private int pip;
    // TODO Find way to do this without absolute value
    public Piece(Player player, int position){
        this.player = player;
        this.position = position;
        if (player.getColor().equals(Player.Color.BLACK)){
            this.pip=25-position;
        } else {this.pip=position;}
    }
    // TODO Find way to do this without absolute value
    public void setPosition(int position){
        this.position = position;
        if (this.getColor().equals(Player.Color.BLACK)){
            this.pip=25-position;}
        else {this.pip=position;}
    }
//    public void setPip(int pip){this.pip=pip;}

    public int getPosition(){
        return this.position;
    }
    // TODO We have two getPip methods! There's also one in the Point class
    //The one in Point works as expected,this one doesn't
    public int getPip(){
        return this.pip;
    }

    public Player.Color getColor(){
        return player.getColor();
    }

    public Player getPlayer() {
        return player;
    }
}
