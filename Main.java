public class Main {
    public static void main(String[] args) {
        Game myGame = new Game(new Player[2], new Die());
        myGame.addPlayers();
        if (Game.chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 2) {myGame.setGameState(Game.GameState.LOST);}
        // roll dice to choose who goes first
        // display board
        while (myGame.isGameOngoing()) {
            //current player
            //roll dice
            //give legal move options
            //player chooses moves
            //make moves
            //display board
        }
    }
}
