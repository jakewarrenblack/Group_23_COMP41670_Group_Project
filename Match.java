import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Match class conducts a series of Backgammon games to determine the winner
 * The games are contested between two players
 * After each game the loser's score is calculated based on the number of their pieces still on the board
 *
 */
public class Match {
    /**
     * The number of games in the match
     */
    private final int games;
    /**
     * the position of the game being played currently in the series
     */
    private int gameIndex;
    /**
     * The game being played currently
     */
    private Game game;

    /**
     * The interface to process commands from the players
     */
    private Command command;
    /**
     * The die that the players use to determine how far they can move on each turn
     */
    private Die die;
    /**
     * The two players competing in the match
     */
    private Player[] players;
    /**
     * The log of messages sent to the players
     */
    private final Log log;
    /**
     * The doubling cube that determines how much each piece's score will be multiplied by at the end of the game
     */
    private Cube doublingCube;
    /**
     * The player taking their turn at present
     */
    private Player currentPlayer;
    private int doubleFace;
    private final int[] doubleValues = new int[]{1,2,4,8,16,32,64};

    /**
     * Create a new match of however many games you want
     * @param games
     */
    public Match(int games){
        this.players = new Player[2];
        this.games=games;
        this.gameIndex=0;
        this.die = Die.getInstance();
        this.players=new Player[2];
        this.log = Log.getInstance();
        this.doublingCube=new Cube();
        this.command=new Command(this);
    }


    /**
     * Add a single player to the match
     * For use in testing only
     * @param index
     * @param player
     * @param isCurrentPlayer
     */
    protected void addPlayer(int index, Player player, boolean isCurrentPlayer) {
        this.players[index] = player;
        if (isCurrentPlayer) {
            currentPlayer = player;
        }
    }


    /**
     * Set the current player to the one in a specified position in the Players[] array
     * For use if a test script gives a command to set a particular player
     * Not used in general game play
     * @param PlayerID
     */
    public void setCurrentPlayer(int PlayerID) {
        this.currentPlayer = players[PlayerID];
        try {
            game.setCurrentPlayer(this.currentPlayer);
        } catch (IllegalArgumentException e) {
            log.updateLog("Cannot set current player of game right now");
        }

        this.doubleFace=0;
        this.command = new Command(this);
    }

    /**
     * Create a new instance of the Game class
     * Set the game to have the game position in the series of games comprising the match
     * @return the game object created
     */
    public Game newGame(){
        this.game = new Game(gameIndex,games, players);

        this.players = game.getPlayers();

        return this.game;
    }


    /**
     * Execute the matchplay
     * Keep going until the number of games defined when the match was constructed have been completed
     * @return True if the number of games have been complete, False otherwise
     */
    public void play(){
        game=new Game(gameIndex,games);
        command.newGame(game);
        currentPlayer = setInitialPlayer();
        Player otherPlayer = this.game.getPlayers()[0].equals(currentPlayer) ? this.game.getPlayers()[1] : this.game.getPlayers()[0];


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

                    commandIndex = Command.chooseOption(this.game.getCurrentPlayer().getName() + " what would you like to do next?", commands);
                    command.acceptCommand(commands[commandIndex]);
                }

                game.processRolls(diceRolls,currentPlayer);

                if (game.isGameWon()) {
                    updateLog(currentPlayer.getName() + " has won!");
                    game.finishGame();
                    otherPlayer.loseGame(doublingCube.getDouble());
                }

                currentPlayer = nextTurn();
                otherPlayer = this.game.getPlayers()[0].equals(this.game.getCurrentPlayer()) ? this.game.getPlayers()[1] : this.game.getPlayers()[0];
                diceRolls=die.getCurrentValues();
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

        gameIndex++;
        Log.getInstance().updateLog("Game "+gameIndex+" of "+games+" complete. " + game.getPlayers()[0].getName() + " has a score of " + game.getPlayers()[0].getScore()+" and " + game.getPlayers()[1].getName() + " has a score of " + game.getPlayers()[1].getScore());
    }


    /**
     * Decide which player goes first
     * Both players roll a die. The player who rolls the highest number gets to go first
     * And uses their die and the other players die for their first move
     * Both players keep rolling until one player rolls higher than the other
     *
     * @return the player who will start the game
     */
    public Player setInitialPlayer(){
        // the player to go first is determined by the dice roll
        while (Objects.equals(die.getCurrentValues().get(0), die.getCurrentValues().get(1))) {
            this.die.roll();
        }
        log.updateLog(this.game.getPlayers()[0].getName() + " has rolled " + die.getCurrentValues().get(0) + ", " + this.game.getPlayers()[0].getName() + " has rolled " + die.getCurrentValues().get(1));
        // if leftDie has a greater value, player 0 starts, otherwise, player 1 starts
        this.currentPlayer = this.players[die.getCurrentValues().get(0) > die.getCurrentValues().get(1) ? 0 : 1];

        log.updateLog(this.game.getCurrentPlayer().getName() + " will go first");
        return this.currentPlayer;
    }

    /** Switch between 0 and 1, whichever is NOT the current player
     *
     * @return the Player object representing the new current player
     */
    public Player nextTurn() {
        this.currentPlayer = this.players[0] == this.currentPlayer ? this.players[1] : this.players[0];
        return this.currentPlayer;
    }

    /**
     * Roll the dice and then tell the users what the player has rolled
     * Update the game log
     */
    public void roll() {
        List<Integer> dice = die.roll();
        String message=this.game.getCurrentPlayer().getName() + " rolled " + dice.get(0);

        for (int i=1;i<dice.size();i++){
            message+=", " + dice.get(i);
        }

        log.updateLog(message);
    }

    /**
     * Set the dice to return pre-specified values on the next roll
     * To satisfy the DICE feature required in the game scope
     *
     * @param rolls
     */
    public void setDie(int[] rolls){die.setValues(rolls);}


    /**
     * Allows the current player to offer a doubling of the cube
     * If the other player accepts the next face of the doubling cube is shown
     * If the other player declines they lose the game
     */
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


    /**
     * Update the game log with a message to be delivered to the users
     * The log object will also print the message to the console
     *
     * @param message
     */
    public void updateLog(String message) {
        log.updateLog(message);
    }

}
