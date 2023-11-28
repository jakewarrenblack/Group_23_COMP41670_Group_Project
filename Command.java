import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

public class Command {
    enum Commands{ROLL,QUIT,PIP,HINT,MOVE,DOUBLE,DICE,TEST}
    private Game game;
    public Command(Game game) {
        this.game = game;
    }
    public boolean acceptCommand(String command){
        String[] cmdTokens = command.split(" ");
        switch (cmdTokens[0].toUpperCase()) {
            case "ROLL":
                game.roll();
                break;
            case "QUIT":
                System.out.println("Goodbye");
                return false;
            case "PIP":
                game.pipScore();
                break;
            case "HINT":
                hint();
                break;
            case "MOVE":
                try {
                    game.movePiece(Integer.parseInt(cmdTokens[1]), Integer.parseInt(cmdTokens[2]));
                } catch (IllegalArgumentException e){game.updateLog(e.getMessage());}
                break;
            case "DOUBLE":
                game.doubleBet();
                break;
            case "DICE":
                game.updateLog("The Dice command hasn't been implemented yet");
                break;
            case "TEST":
                game.updateLog("The Test command hasn't been implemented yet");
                break;
            default:
                game.updateLog("I do not recognise "+cmdTokens[0]+" as a command");
        }
        return true;
    }
    public String[] listCommands(List<String> exclude){
        List<String> commands = new ArrayList<String>();
        for (Commands command:Command.Commands.values()){
            if (!(exclude.contains(command.name())|exclude.contains(command.name().toLowerCase())|exclude.contains(command.name().toUpperCase()))){commands.add(command.name());}
        }
        String[] commandsOut = commands.toArray(new String[0]);
        return commandsOut;
    }
    public void hint(){
        System.out.println("ROLL rolls two dice for the current player");
        System.out.println("PIP gives the current PIP score for each player");
        System.out.println("MOVE <from> <to> moves a checker from the <from>th point to the <to>th point");
        System.out.println("DOUBLE proposes doubling the stakes");
        System.out.println("DICE <one> <two> fixes the dice scores for the next roll");
        System.out.println("TEST <location> runs the series of commands defined by the text file at <location>");
        System.out.println("HINT prints a list of available commands to the console");
        System.out.println("QUIT exits the game");
    }
}

