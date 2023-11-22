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
        return switch (color) {
            case BLACK -> 23 - position;
            case WHITE -> position;
        };

    }

    public Player.Color getColor(){
        return player.getColor();
    }

}
