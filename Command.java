import javax.print.DocFlavor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Command {
    enum Commands{ROLL,QUIT,PIP,HINT,MOVE,DOUBLE,DICE,TEST}
    private final Game game;
    public Command(Game game) {
        this.game = game;
    }

    static void printTextFile(String filePath) {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Error reading file: " + e);
            }
        } else {
            System.out.println("File not found: " + filePath);
        }
    }

    public boolean acceptCommand(String command){
        String[] cmdTokens = command.split(" ");
        switch (cmdTokens[0].toUpperCase()) {
            case "ROLL" -> game.roll();
            case "QUIT" -> {
                System.out.println("Goodbye");
                return false;
            }
            case "PIP" -> game.pipScore();
            case "HINT" -> printTextFile("res/hints.txt");
            case "MOVE" -> {
                try {
                    game.movePiece(Integer.parseInt(cmdTokens[1]), Integer.parseInt(cmdTokens[2]));
                } catch (IllegalArgumentException e) {
                    game.updateLog(e.getMessage());
                }
            }
            case "DOUBLE" -> game.doubleBet();
            case "DICE" -> game.updateLog("The Dice command hasn't been implemented yet");
            case "TEST" -> game.updateLog("The Test command hasn't been implemented yet");
            default -> game.updateLog("I do not recognise " + cmdTokens[0] + " as a command");
        }
        return true;
    }

    public String[] listCommands(List<String> exclude){
        List<String> commands = new ArrayList<String>();

        for (Commands command : Command.Commands.values()){
            // If the command is not in the exclude list, add it to the list of commands
            if (!exclude.contains(command.toString().toLowerCase())){
                commands.add(command.toString());
            }
        }

        return commands.toArray(new String[0]);
    }
}

