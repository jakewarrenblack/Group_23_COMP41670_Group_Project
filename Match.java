import java.util.ArrayList;
import java.util.List;

public class Match {
    private final int games;
    private int gameIndex;
    private Game game;
    private int doubleFace;
    private final Command command;
    private final int[] doubleValues = new int[]{1,2,4,8,16,32,64};

    Player[] players;

    public Match(int games){
        this.players = new Player[2];
        this.games=games;
        this.gameIndex=0;
        this.doubleFace=0;
        this.command = new Command(this);
    }

    public Game newGame(){
        this.game = new Game(gameIndex,games, players);

        this.players = game.getPlayers();

        return this.game;
    }

    public Game getGame(){return game;}

    public void play(){
        game=new Game(gameIndex,games);
        command.newGame(game);


        // game just started, set the initial dice roll value, which the player will have to use
        List<Integer> diceRolls = Die.getInstance().getCurrentValues();
        while (game.isGameOngoing()) {
            List<String> exclude = new ArrayList<>();
            exclude.add("MOVE");

            if(!game.getCurrentPlayer().hasDouble()){
                exclude.add("DOUBLE");
            }

            String[] commands = command.listCommands(exclude);
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

            diceRolls= Die.getInstance().getCurrentValues();
        }

        gameIndex++;
        Log.getInstance().updateLog("Game "+gameIndex+" of "+games+" complete. " + game.getPlayers()[0].getName() + " has a score of " + game.getPlayers()[0].getScore()+" and " + game.getPlayers()[1].getName() + " has a score of " + game.getPlayers()[1].getScore());
    }

    public void doubleBet(){
        Player otherPlayer = game.getCurrentPlayer() == game.getPlayers()[0] ? game.getPlayers()[1] : game.getPlayers()[0];

        if (Game.chooseOption(otherPlayer.getName() + ", " + game.getCurrentPlayer().getName() + " has offered to double the bet. Do you accept?",new String[]{"Yes","No"})==0) {
            doubleFace = Math.min(6,doubleFace+1);
            otherPlayer.setDouble(true);
            game.getCurrentPlayer().setDouble(false);
            Log.getInstance().updateLog("The doubling cube now shows " + doubleValues[doubleFace] + " and " + otherPlayer.getName() + " has possession");
        } else {
            Log.getInstance().updateLog(otherPlayer.getName()+" has rejected the offer to double the bet and loses the game");
            game.setGameState(Game.GameState.LOST);
        }
    }
}
