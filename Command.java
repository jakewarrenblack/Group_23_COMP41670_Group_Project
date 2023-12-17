import java.io.FileReader;
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
    public Command(Match match) {
        this.match = match;
    }

    public void newGame(Game game){
        this.game=game;
        game.setCommand(this);
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
        String[] cmdTokens = command.split("\\s+");
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
                    game.updateLog("You have moved from "+cmdTokens[1]+" to "+cmdTokens[2]);
                } catch (IllegalArgumentException e) {
                    game.updateLog(e.getMessage());
                }
            }
            case "DOUBLE" -> match.doubleBet();
            case "DICE" -> {
                int[] rolls;
                if(cmdTokens.length==1) {
                    int firstRoll = Game.getInteger("Please enter the desired value for the first die");
                    int secondRoll = Game.getInteger("Please enter the desired value for the second die");

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
                game.setDie(rolls);
                game.updateLog("The values of the dice have been set manually for the next roll");
            }
            case "TEST" -> {
                game.updateLog("Running test script");
                try {
                    test();
                    game.updateLog("Test script executed successfully");
                } catch (IllegalArgumentException e){game.updateLog(e.getMessage());}

            }
            default -> game.updateLog("I do not recognise " + cmdTokens[0] + " as a command");
        }
        return true;
    }

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

    public void test(){
        Scanner testMoves = importMoves();
        while (testMoves.hasNextLine()){
            String line = testMoves.nextLine();
            if (!acceptCommand(line)){throw new IllegalArgumentException(line+" is not a valid command");}
        }
    }

    public void test(Game game, String path){
        File f = new File(path);
        Scanner scanner;
        try {scanner = new Scanner(new FileReader(f));
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.equals("SET_PLAYER\t1\t")){
                    System.out.println("Break");
                }
                if (!acceptCommand(line)){throw new IllegalArgumentException(line+" is not a valid command");}
            }
        } catch (Exception e) {
            game.updateLog("Error reading file: " + e);
        }
    }

    public static Scanner importMoves(){
        try {
            JFileChooser chooser = new JFileChooser();
            Scanner in;
            if (chooser.showOpenDialog(null)==
                JFileChooser.APPROVE_OPTION){
                File selectedFile = chooser.getSelectedFile();
                return new Scanner(selectedFile);
            }
        } catch (IOException ex){ex.printStackTrace();}
        return null;
    }

    public static Scanner importMoves(String path){
        File f = new File(path);
        try (Scanner scanner = new Scanner(new FileReader(f))) {
            return scanner;
        } catch (Exception e) {
            System.out.println("Error reading file: " + e);
            return null;
        }
    }
}

