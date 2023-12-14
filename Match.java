import java.util.ArrayList;
import java.util.List;

public class Match {
    private final int games;
    private int gameIndex;
    private Game game;
    private final Command command;
    private Die die;
    private Player[] players;
    private final Log log;
    private int doubleFace;
    private final int[] doubleValues = new int[]{1,2,4,8,16,32,64};
    private Player currentPlayer;

    public Match(int games){
        this.games=games;
        this.gameIndex=0;
        this.die=new Die();
        this.players=new Player[2];
        this.log=new Log();
        this.doubleFace=0;
        this.command=new Command(this);
    }
    public void addPlayer(int index, Player player, boolean isCurrentPlayer) {
        this.players[index] = player;
        if (isCurrentPlayer) {
            currentPlayer = player;
        }
    }
    public Player[] addPlayers() {
        Player[] players = new Player[2];

        for (int i = 0; i < 2; i++) {
            Player.Color color = Player.Color.values()[i];
            players[i] = new Player(Game.getInput("Please enter the name of the " + color + " player"), color);
        }
        this.players = players;

        return players;
    }

    public void setCurrentPlayer(int PlayerID) {
        this.currentPlayer = players[PlayerID];
        try {
            game.setCurrentPlayer(this.currentPlayer);
        } catch (IllegalArgumentException e) {
            log.updateLog("Cannot set current player of game right now");
        }
    }

    public Game newGame(){
        this.game = new Game(die,log,players,gameIndex,games);
        this.game.setCurrentPlayer(currentPlayer);
        return this.game;
    }
    public Game getGame(){return game;}
    public boolean Play(){
        game=new Game(die,log,players,gameIndex,games);
        command.newGame(game);
        for (int j = 0; j < 2; j++) {
            game.placePieces(this.players[j]);
        }
        currentPlayer = game.setInitialPlayer();
        // game just started, set the initial dice roll value, which the player will have to use
        List<Integer> diceRolls = die.getCurrentValues();
        while (game.isGameOngoing()) {
            List<String> exclude = new ArrayList<>();
            List<Game.Move> validMoves;
            exclude.add("MOVE");
            if(!currentPlayer.hasDouble()){
                exclude.add("DOUBLE");
            }
            String[] commands = game.listCommands(exclude);
            int commandIndex = commands.length-1;
            // Loop until the player chooses to roll
            while (commandIndex!=0) {
                commandIndex = Game.chooseOption(currentPlayer.getName() + " what would you like to do next?", commands);
                command.acceptCommand(commands[commandIndex]);
            }
            game.processRolls(diceRolls);
            if (game.isGameWon()) {
                game.updateLog(currentPlayer.getName() + " has won!");
                game.setScores(doubleValues[doubleFace]);
                game.setGameState(Game.GameState.WON);
            }
            currentPlayer = game.nextTurn();
            diceRolls=die.getCurrentValues();
        }
        gameIndex++;
        log.updateLog("Game "+gameIndex+" of "+games+" complete. "+players[0].getName()+" has a score of "+players[0].getScore()+" and "+players[1].getName()+" has a score of "+players[1].getScore());
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
