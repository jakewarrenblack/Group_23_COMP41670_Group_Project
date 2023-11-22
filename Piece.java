public class Piece {

    public Player player;

    private int position;

    public Piece(Player player, int position){
        this.player = player;
        this.position = position;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getPosition(){
        return this.position;
    }
    public int getPip(){
        Player.Color color = getColor();
        int pip = -1;
        switch (color) {
            case BLACK:
                pip = 23 - position;
                break;
            case WHITE:
                pip = position;
        }
        return pip;

    }

    public Player.Color getColor(){
        return player.getColor();
    }

    public Player getPlayer() {
        return player;
    }
}
