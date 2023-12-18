import java.util.*;

public class Main {

    public static void main(String[] args) {
        int games = Game.getInteger("How many games would you like to play?");

        Match match = new Match(games);

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
