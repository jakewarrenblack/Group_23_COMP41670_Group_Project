import java.util.*;

public class Main {
    public static ArrayList<String> importMoves(){
        return Command.getStrings();
    }

    // need a global override for every time chooseOption runs, to automatically agree to the first option
    public static boolean testMode = false;

    public static void main(String[] args) {
        ArrayList<String> moves;

        // it should be possible for the user to enter test mode from here
        if (Command.chooseOption("Play normally or enter test mode?", new String[]{"Begin game", "Test mode"}) == 1) {
            testMode = true;
            moves = importMoves();
            Match match;

            if (moves == null){
                System.out.println("No moves imported. Exiting.");
                System.exit(0);
            }

            System.out.println("Moves imported:");
            // first line is the number of games to play
            int games = Integer.parseInt(moves.get(0));
            match = new Match(games);
            // lines 2 and 3 are players 1 and 2
            match.addPlayers(moves.get(1), moves.get(2));

            // the normal moves start at index 3
            match.play(moves.subList(3, moves.size()));


        }
        // otherwise, play normally
        else{
            int games = Command.getInteger("How many games would you like to play?");

            Match match = new Match(games);
            match.addPlayers();
            // User has opted to quit
            if (Command.chooseOption("Would you like to begin your first game?", new String[]{"Begin Game", "Quit"}) == 1) {
                System.out.println("Goodbye!");
            }
            // Otherwise, user has opted to begin a game
            else {
                match.play();
            }
        }
    }
}
