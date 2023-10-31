import jdk.jshell.spi.ExecutionControl;

public class Game {
    private enum GameState {ONGOING, WON, LOST};
    private GameState gameState;

    private Player[] players = new Player[2];

    private Player currentPlayer;

    private Die die;

    public Game(Player[] players, Die die){
        this.gameState = GameState.ONGOING;
        this.players = players;
        this.die = die;
    }

    private void nextTurn(){
        // just switch between 0 and 1, whichever is NOT the current player
        this.currentPlayer = this.players[0] == this.currentPlayer ? this.players[1] : this.players[0];
    }

    public void handleMove() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public boolean isGameWon(){
        return this.gameState == GameState.WON;
    }

}
