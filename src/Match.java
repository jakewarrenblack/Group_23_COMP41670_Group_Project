import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Match class conducts a series of Backgammon games to determine the winner
 * The games are contested between two players
 * After each game the loser's score is calculated based on the number of their pieces still on the board
 *
 */

/*
- Team number: 23
- Team members: Jake Black & Richard Mitchell
- Github IDs: jakewarrenblack & richardmitchell1
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
    private final Command command;
    /**
     * The die that the players use to determine how far they can move on each turn
     */
    private final Die die;
    /**
     * The two players competing in the match
     *
     * Protected so can be accessed by unit tests
     */
    protected Player[] players;
    /**
     * The log of messages sent to the players
     */
    private final Log log;
    /**
     * The doubling cube that determines how much each piece's score will be multiplied by at the end of the game
     */
    private final Cube doublingCube;
    /**
     * The player taking their turn at present
     *
     * protected so accessible by unit tests
     */
    protected Player currentPlayer;

    /**
     * Create a new match of however many games you want
     *
     * @param games The number of games to play in the match
     */
    public Match(int games){
        this.games=games;
        this.gameIndex=0;
        this.die=Die.getInstance();
        this.players=new Player[2];
        this.log=Log.getInstance();
        this.doublingCube=new Cube();
        this.command=new Command(this);
    }

    /**
     * Add a single player to the match
     * For use in testing only
     *
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
     * Construct two players with names entered by the users
     */
    public void addPlayers(String...providedPlayers) {
        if(providedPlayers.length > 0){
            for(int i = 0; i < providedPlayers.length; i++){
                this.players[i] = new Player(providedPlayers[i], Player.Color.values()[i]);
            }

            return;
        }

        Player[] players = new Player[2];

        for (int i = 0; i < 2; i++) {
            Player.Color color = Player.Color.values()[i];
            players[i] = new Player(Command.getInput("Please enter the name of the " + color + " player"), color);
        }
        this.players = players;

    }

    /**
     * Set the current player to the one in a specified position in the Players[] array
     *
     * For use if a test script gives a command to set a particular player
     * Not used in general game play
     *
     * @param PlayerID
     */
    public void setCurrentPlayer(int PlayerID) {
        this.currentPlayer = players[PlayerID];
        try {
            game.setCurrentPlayer(this.currentPlayer);
        } catch (IllegalArgumentException e) {
            log.updateLog("Cannot set current player of game right now");
        } catch (IndexOutOfBoundsException e){
            log.updateLog(PlayerID + " is not a valid player ID");
        }
    }

    /**
     * Create a new instance of the Game class
     * Set the game to have the game position in the series of games comprising the match
     *
     * @return the game object created
     */
    public Game newGame(){
        this.game = new Game(players,gameIndex,games);
        this.game.setCurrentPlayer(currentPlayer);
        return this.game;
    }

    public Game getGame(){return game;}

    /**
     * Set up and play a new game
     *
     * @return True if the specified number of games in the match series have been complete, False otherwise
     */
    public boolean play(List<String>...moves){
        System.out.println("\n**Currently playing game number " + (gameIndex +1) + " of " + games + " in the match**\n");

        game=new Game(players,gameIndex,games);
        command.newGame(game);

        for (int j = 0; j < 2; j++) {
            game.placePieces(this.players[j]);
        }

        currentPlayer = setInitialPlayer();

        Player otherPlayer = players[0].equals(currentPlayer) ? players[1] : players[0];

        // game just started, set the initial dice roll value, which the player will have to use
        List<Integer> diceRolls = die.getCurrentValues();
        List<String> exclude = new ArrayList<>();


        List<String> selectedTestOption = new ArrayList<>();



        if(moves.length > 0){
            for (int i=0;i<moves[0].size();i++){
                if (moves[0].get(i).length()==1){
                    if (moves[0].get(i-1).length()>1){
                        selectedTestOption.clear();
                    }
                    selectedTestOption.add(letterToNumber(moves[0].get(i)));
                    if (moves[0].get(i+1).length()>1){
                        game.processRolls(die.getCurrentValues(),currentPlayer,doublingCube.doubleStatus(),selectedTestOption.toArray(new String[0]));
                        currentPlayer=nextTurn();
                    }
                } else {
                    command.acceptCommand(moves[0].get(i));
                }
            }
        }


        // You cannot roll the dice on your first turn - you need to use the dice rolls from deciding the initial players
        exclude.add("ROLL");

        // The player can execute commands until they choose MOVE
        acceptCommand("MOVE",exclude);

        // Once the player has chosen to move, allow the game to process the dice rolls
        game.processRolls(diceRolls,currentPlayer, doublingCube.doubleStatus(), selectedTestOption.toArray(new String[0]));
        currentPlayer=nextTurn();

        while (game.isGameOngoing()) {
            exclude.clear();
            // You can only use the MOVE command on your first turn because you don't have to roll the dice
            exclude.add("MOVE");

            // The DOUBLE command can be offered if no-one owns the doubling cube yet or if the current player owns it
            if(doublingCube.hasOwner() && !doublingCube.isOwnedBy(currentPlayer)){
                exclude.add("DOUBLE");
            }

            acceptCommand("ROLL",exclude);
            // The other player may have lost the game if they rejected an offer to double
            if(game.isGameOngoing()) {
                // Once the player has rolled the dice, allow the game to process the dice rolls
                game.processRolls(diceRolls, currentPlayer, doublingCube.doubleStatus());
                if (!game.isGameOngoing()) {
                    updateLog(currentPlayer.getName() + " has won!");
                    game.finishGame(otherPlayer, doublingCube.getDouble());
                } else {
                    currentPlayer = nextTurn();
                    otherPlayer = players[0].equals(currentPlayer) ? players[1] : players[0];
                    diceRolls = die.getCurrentValues();
                }
            }
        }

        gameIndex++;
        if (gameIndex==games){
            log.updateLog("Match complete. "+players[0].getName()+" has a score of "+players[0].getScore()+" and "+players[1].getName()+" has a score of "+players[1].getScore());
            return true;
        } else {
            log.updateLog("Game " + gameIndex + " of " + games + " complete. " + players[0].getName() + " has a score of " + players[0].getScore() + " and " + players[1].getName() + " has a score of " + players[1].getScore());
            return false;
        }
    }

    /**
     * The test script uses letters to represent the dice rolls
     * But the game uses numbers, so convert the letters to numbers
     * @param letter
     * @return the number corresponding to the letter
     */
    private String letterToNumber(String letter){
        // ASCII value of 'a' is 97, so subtract 96 to get the number corresponding to the letter
        int num = letter.charAt(0) - 'a' + 1;
        return Integer.toString(num);
    }

    private void acceptCommand(String target, List<String> exclude){
        String[] commands = command.listCommands(exclude);
        int commandIndex = commands.length-1;
        // The player can continue executing commands until they choose the target command
        // Some commands (like DOUBLE) can result in the game ending. if this happens don't accept any more commands
        while (!commands[commandIndex].equals(target) && game.isGameOngoing()) {
            commandIndex = Command.chooseOption(currentPlayer.getName() + " what would you like to do next?", commands);
            command.acceptCommand(commands[commandIndex]);
        }
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
        log.updateLog(this.players[0].getName() + " has rolled " + die.getCurrentValues().get(0) + ", " + this.players[1].getName() + " has rolled " + die.getCurrentValues().get(1));
        // if leftDie has a greater value, player 0 starts, otherwise, player 1 starts
        this.currentPlayer = this.players[die.getCurrentValues().get(0) > die.getCurrentValues().get(1) ? 0 : 1];

        log.updateLog(this.currentPlayer.getName() + " will go first");
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
        StringBuilder message= new StringBuilder(currentPlayer.getName() + " rolled " + dice.get(0));

        for (int i=1;i<dice.size();i++){
            message.append(", ").append(dice.get(i));
        }

        log.updateLog(message.toString());
    }

    /**
     * Allows the current player to offer a doubling of the cube
     * If the other player accepts the next face of the doubling cube is shown
     * If the other player declines they lose the game
     */
    public void doubleBet(String...testCommand){
        Player otherPlayer = currentPlayer == this.players[0] ? this.players[1] : this.players[0];


        if(testCommand.length > 0){
            if(testCommand[0].equals("accept")){
                doublingCube.doubleScore(otherPlayer);
                log.updateLog("The doubling cube now shows " + doublingCube.getDouble() + " and " + otherPlayer.getName() + " has possession");
            }
            else if(testCommand[0].equals("refuse")){
                log.updateLog(otherPlayer.getName()+" has rejected the offer to double the bet and loses the game");
                game.finishGame(otherPlayer,doublingCube.getDouble());
            }
            return;
        }


        if (Command.chooseOption(otherPlayer.getName() + ", " + currentPlayer.getName() + " has offered to double the bet. Do you accept?", new String[]{"Yes","No"})==0) {
            doublingCube.doubleScore(otherPlayer);
            log.updateLog("The doubling cube now shows " + doublingCube.getDouble() + " and " + otherPlayer.getName() + " has possession");
        }
        else {
            log.updateLog(otherPlayer.getName()+" has rejected the offer to double the bet and loses the game");
            game.finishGame(otherPlayer,doublingCube.getDouble());
        }
    }
    /**
     * Calculate the pip score of both players, print it to the screen and add it to the game log
     */
    public void pipScore() {
        for (int i = 0; i < 2; i++) {
            log.updateLog(players[i].getName() + " has a pip score of " + players[i].pipScore());
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

    /**
     * To add a contrived game for use in unit testing
     *
     * @param game
     */
    protected void addGame(Game game){
        this.game = game;
        this.command.newGame(game);
    }

    public Match nextGame() {
        if(gameIndex < games){
            gameIndex++;
            // make a new game
            this.game = newGame();
            this.play();
        }
        else{
            return null;
        }
        return this;
    }
}
