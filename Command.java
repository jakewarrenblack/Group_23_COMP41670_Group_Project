import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Command {
    enum Commands{ROLL,QUIT,PIP,HINT,MOVE,DOUBLE,DICE,TEST,}
    private Game game;
    private final Match match;

    private ArrayList<String> moves = new ArrayList<>();

    public Command(Match match) {
        this.match = match;
    }

    public void newGame(Game game){
        this.game=game;
        game.setCommand(this);
    }

    /**
     * Read a text file and print contents to the console.
     * @param filePath The path of the file to be read and printed. This should be a relative path from the classpath.
     */
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

    /**
     * This method processes a given command and performs the corresponding action.
     * It's sole purpose is to allow the user to interact with the game.
     * The actions can be: "ROLL", "QUIT", "PIP", "HINT", "MOVE", "DOUBLE", "DICE", "TEST", "SET_PLAYER".
     *
     * @param command The command to be processed.
     */
    public void acceptCommand(String command){

        String[] alphabet = {"a","b","c","d","e","f"};

        // check if the command is a letter, and present somewhere in the alphabet array
        if (command.length() == 1 && Arrays.asList(alphabet).contains(command.toLowerCase())) {
            // when this is the case, we also need to manually trigger the MOVE command


            // if so, convert it to a number
            int num = Arrays.asList(alphabet).indexOf(command.toLowerCase());
            // and add 1 to it, to get the correct number
            num++;
            // then convert it back to a string
            command = Integer.toString(num);
        }

        String[] cmdTokens = command.split("\\s+");
        switch (cmdTokens[0].toUpperCase()) {
            case "ROLL" -> match.roll();
            case "QUIT" -> {
                System.out.println("Goodbye");
                System.exit(0);
            }
            case "PIP" -> match.pipScore();
            case "HINT" -> printTextFile("res/hints.txt");
            case "MOVE" -> {
                // If we receive a command of just "MOVE" with no co-ordinates this is the player
                // deciding to use the pre-rolled dice from the player selection throws
                // so do nothing
                if (cmdTokens.length>1) {
                    try {
                        game.movePiece(Integer.parseInt(cmdTokens[1]), Integer.parseInt(cmdTokens[2]));
                        match.updateLog("You have moved from " + cmdTokens[1] + " to " + cmdTokens[2]);
                    } catch (IllegalArgumentException e) {
                        match.updateLog(e.getMessage());
                    }
                }
            }
            case "DOUBLE" -> match.doubleBet();
            case "DICE" -> {
                int[] rolls;
                if(cmdTokens.length==1) {
                    int firstRoll = getInteger("Please enter the desired value for the first die");
                    int secondRoll = getInteger("Please enter the desired value for the second die");
                    if (firstRoll == secondRoll) {
                        System.out.println("Since the two die rolls specified are equal your number of rolls will be doubled");
                        rolls = new int[4];
                        Arrays.fill(rolls, firstRoll);
                    } else {
                        rolls = new int[]{firstRoll, secondRoll};
                    }
                } else {
                    rolls = new int[cmdTokens.length-1];
                    for (int i=1;i<cmdTokens.length;i++){
                        rolls[i-1]=Integer.parseInt(cmdTokens[i]);
                    }
                }
                match.setDie(rolls);
                match.updateLog("The values of the dice have been set manually for the next roll");
            }
            case "TEST" -> {
                match.updateLog("Running test script");
                try {
                    moves = importMoves();
                    // might make sense to parse the moves from the text file,
                    // and recursively call acceptCommand() for each move
                    // (this would allow the user to use the same commands as in the console)




                    match.updateLog("Test script executed successfully");
                } catch (IllegalArgumentException e){match.updateLog(e.getMessage());}

            }
            case "SET_PLAYER" -> {
                match.setCurrentPlayer(Integer.parseInt(cmdTokens[1]));
            }
            default -> match.updateLog("I do not recognise " + cmdTokens[0] + " as a command");
        }
    }

    /**
     * List all the commands that the user can use, excluding those in the 'exclude' list
     * @param exclude
     * @return a List of Strings representing the possible commands
     */
    public String[] listCommands(List<String> exclude){
        List<String> commands = new ArrayList<String>();

        for (Commands command : Command.Commands.values()){
            // If the command is not in the exclude list, add it to the list of commands
            if (!exclude.contains(command.toString().toUpperCase())){
                commands.add(command.toString());
            }
        }

        return commands.toArray(new String[0]);
    }

    /**
     * Allow the user to select from a series of options
     *
     * @param message to explain the choices to the user
     * @param options that the user can choose from
     * @return an integer representing the position of the chosen option within the list of options given
     */
    public static int chooseOption(String message, String[] options) {
        System.out.println(message);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ": " + options[i]);
        }

        Scanner in = new Scanner(System.in);
        int opt = -1;
        System.out.println("Please select an option");

        while (opt < 0 || opt >= options.length) {
            while (!in.hasNextInt()) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
            opt = in.nextInt() - 1;
            if (opt < 0 || opt >= options.length) {
                System.out.println("You must enter a number corresponding to one of the options");
            }
        }

        return opt;
    }

    /**
     * Allow the user to provide free text input
     *
     * @param message explaining the input required
     * @return a string representing the user's input
     */
    public static String getInput(String message) {
        Scanner in = new Scanner(System.in);
        String input = "";
        while (input.isEmpty()) {
            System.out.println(message);
            String temp = in.nextLine();
            if (Command.chooseOption("You have entered <" + temp + ">. Confirm?", new String[]{"Yes", "No"}) == 0) {
                input = temp;
            }
        }

        return input;
    }

    /**
     * Allow the user to provide numerical input in the form of an integer
     *
     * @param message explaining the input required
     * @return an int representing the user's input
     */
    public static int getInteger(String message){
        Scanner in = new Scanner(System.in);
        int input=0;
        while (input==0) {
            System.out.println(message);
            while (!in.hasNextInt()) {
                String temp = in.next();
                System.out.println(temp + " is not a number");
            }
            int temp = in.nextInt();
            if (Command.chooseOption("You have entered <" + temp + ">. Confirm?", new String[]{"Yes", "No"}) == 0) {
                input = temp;
            }
        }
        return input;
    }


    public static ArrayList<String> importMoves(){
        ArrayList<String> moves = new ArrayList<>();

        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                File selectedFile = chooser.getSelectedFile();

                Scanner s = new Scanner(selectedFile);

                while(s.hasNextLine()){
                    String line = s.nextLine();
                    moves.add(line);
                }

                return moves;
            }
        } catch (IOException ex){ex.printStackTrace();}
        return null;
    }
}

