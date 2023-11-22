import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Die die = new Die();
        Scanner in = new Scanner(System.in);
        Game myGame = new Game(new Player[2], die);
        Player currentPlayer;
//        Board b = new Board();
        myGame.addPlayers();

        int[] rollValue = new int[2];

        // User has opted to quit
        if (Game.chooseOption("Would you like to begin the game?", new String[]{"Begin Game", "Quit"}) == 1) {
            System.out.println("Goodbye!");
            myGame.setGameState(Game.GameState.LOST);
        }
        // Otherwise, user has opted to begin a game
        else{
            currentPlayer = myGame.setInitialPlayer();

            // game just started, set the initial dice roll value, which the player will have to use
            rollValue[0] = die.getCurrentValues()[0];
            rollValue[1] = die.getCurrentValues()[1];

            while (myGame.isGameOngoing()) {
                // player to go first is determined by the dice roll

                System.out.println(myGame.getCurrentPlayer().getName() + "'s move:");

                String move = in.nextLine().toLowerCase();

                if(move.isEmpty()){
                    System.out.println("Invalid input! Please enter a move.");
                    continue;
                }

                // TODO: Prevent rolling multiple times before making a move
                if(move.equals("roll")){
                    die.roll();
                    System.out.println(myGame.getCurrentPlayer().getName() + " to play " + die.getCurrentValues()[0] + "-" + die.getCurrentValues()[1] + ". Select from:");
                }

                // TODO: Print out possible moves

                // Black player moves from the bottom-right (index 24) to the top-right (index 1) -> (clockwise). In other words, the position index must be moving DOWN from 24...to 1...
                // White player moves from the top-right (index 1) to the bottom-right (index 24) -> (counter-clockwise). In other words, the position index must be moving UP from 1...to 24...

                // possible move distances are dice 1, dice 2, or dice1 + dice2

                // I need to iterate through all the pieces on the board and find the pieces belonging to the relevant player. Of these, the pieces at the top of their respective triangles can be moved.
                // The direction they can move depends on which player we're dealing with.
                Point[] allBoardPoints = myGame.getBoard().getPoints();

                int[] distanceOptions = {die.getCurrentValues()[0], die.getCurrentValues()[1], (die.getCurrentValues()[0] + die.getCurrentValues()[1])};

                for(int option: distanceOptions){

                }


                // FIXME: Actually, we DON'T need to iterate over every point, we need to iterate over every PIECE. The former would be pointlessly inefficient.
                for(Point p: allBoardPoints){
                    System.out.println(p);
                }

                // example scenario:
                //  black player rolls a 5-3
                //  their options are to move one piece 5 places, and another piece 3 places, OR they can move one piece 8 places
                // to give the player their move options, i need to iterate through all the points on the board, finding the relevant (black) pieces
                // the pieces which can be moved are the ones at the top of the pile



//                switch(currentPlayer.getColor()){
//                    case BLACK: {
//                        break;
//                    }
//                    case WHITE: {
//                        break;
//                    }
//                }



                if(move.equals("print")){
                    myGame.print();
                }
                if(move.equals("pip")){
                    myGame.pipScore();
                }



                // TODO: In future, when score is unchanged (non-playing command was given), this won't run
                // FIXME: Commented out for now. Don't actually switch players until the player has finished their moves.
                //  i.e, the player could run 'print', 'pip', 'roll' before actually making a move.

                // before switching turns, clear the dice value for the next player
                rollValue = null;
                currentPlayer = myGame.nextTurn();


            }
        }
    }
}
