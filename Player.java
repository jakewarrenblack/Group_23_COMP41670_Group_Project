public class Player  {
    private final String name;
    private int score;
    public enum Color {BLACK, WHITE};
    public final Color color;

    public Player(String name, Color color){
        if(name.isBlank()){
            throw new IllegalArgumentException("Name may not be empty!");
        }
        this.name = name;
        this.color = color;
        this.score = 0;
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

}
