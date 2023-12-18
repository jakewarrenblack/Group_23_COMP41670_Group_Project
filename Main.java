import java.util.*;

public class Main {
    private static final String[] rolls = new String[]{"first", "second", "third", "fourth"};

    public static void main(String[] args) {
        int games = Command.getInteger("How many games would you like to play?");
        Match match = new Match(games);
        match.addPlayers();
        // User has opted to quit
        if (Command.chooseOption("Would you like to begin your first game?", new String[]{"Begin Game", "Quit"}) == 1) {
            System.out.println("Goodbye!");
        }
        // Otherwise, user has opted to begin a game
        else {
            while (!match.Play()){
            }
        }
    }
}
