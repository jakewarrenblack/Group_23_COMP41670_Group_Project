import java.util.ArrayList;
import java.util.List;

public class Match {
    private final int games;
    private int gameIndex;
    private Game game;
    private int doubleFace;
    private final Command command;
    private final int[] doubleValues = new int[]{1,2,4,8,16,32,64};

    public Match(int games){
        this.games=games;
        this.gameIndex=0;
        this.doubleFace=0;
        this.command = new Command(this);
    }

    public Game newGame(){
        this.game = new Game(gameIndex,games);
        return this.game;
    }

    public Game getGame(){return game;}

    public boolean Play(){
        game=new Game(gameIndex,games);


        // game just started, set the initial dice roll value, which the player will have to use
        List<Integer> diceRolls = Die.getCurrentValues();
        while (game.isGameOngoing()) {
            List<String> exclude = new ArrayList<>();
            List<Game.Move> validMoves;
            exclude.add("MOVE");
            if(!game.getCurrentPlayer().hasDouble()){
                exclude.add("DOUBLE");
            }
            String[] commands = game.listCommands(exclude);
            int commandIndex = commands.length-1;
            // Loop until the player chooses to roll
            while (commandIndex!=0) {
                commandIndex = Game.chooseOption(game.getCurrentPlayer().getName() + " what would you like to do next?", commands);
                command.acceptCommand(commands[commandIndex]);
            }
            game.processRolls(diceRolls);
            if (game.isGameWon()) {
                game.updateLog(game.getCurrentPlayer().getName() + " has won!");
                game.setScores(doubleValues[doubleFace]);
                game.setGameState(Game.GameState.WON);
            }
            game.nextTurn();

            diceRolls= Die.getCurrentValues();
        }
        gameIndex++;
        Log.updateLog("Game "+gameIndex+" of "+games+" complete. " + players[0].getName()+" has a score of "+players[0].getScore()+" and "+players[1].getName()+" has a score of "+players[1].getScore());
        return gameIndex==games;
    }

    public void doubleBet(){
        Player otherPlayer = currentPlayer == this.players[0] ? this.players[1] : this.players[0];
        if (Game.chooseOption(otherPlayer.getName()+", "+currentPlayer.getName()+" has offered to double the bet. Do you accept?",new String[]{"Yes","No"})==0) {
            doubleFace = Math.min(6,doubleFace+1);
            otherPlayer.setDouble(true);
            currentPlayer.setDouble(false);
            log.updateLog("The doubling cube now shows " + doubleValues[doubleFace] + " and " + otherPlayer.getName() + " has possession");
        } else {
            log.updateLog(otherPlayer.getName()+" has rejected the offer to double the bet and loses the game");
            game.setGameState(Game.GameState.LOST);
        }
    }
}
