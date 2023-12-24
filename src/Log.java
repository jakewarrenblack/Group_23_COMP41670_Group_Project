import java.util.ArrayList;

/**
 * Prints to the screen messages sent to the players and then keeps a record of those messages
 */

/*
- Team number: 23
- Team members: Jake Black & Richard Mitchell
- Github IDs: jakewarrenblack & richardmitchell1
*/
public class Log {
    private ArrayList<String> log = new ArrayList<>();

    // Like the dice, we only want one log, so we can use the singleton pattern
    private static Log instance = null;
    private Log(){
        log = new ArrayList<>();
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    /**
     * Clears all entries from the log
     * For use in testing to reset log state
     */
    protected void clearLog(){this.log.clear();}
    /**
     * Prints a message to the screen and then adds that message to the message log
     * @param entry
     */
    public void updateLog(String entry){
        System.out.println(entry);
        log.add(entry);
    }

    /**
     * Returns a list of the most recent messages sent to the players
     * The length of the list is specified as an input parameter
     * @param   entries     The number of messages to return
     * @return              The most recent messages
     */

    public String[] recentLog(int entries){
        String[] extract = new String[entries];
        int blanks = Math.max(entries - this.log.size(), 0);
        int extras = Math.max(this.log.size() - entries, 0);

        for (int i=0; i < blanks; i++) {
            extract[i] = "";
        }

        for (int i=blanks; i < entries; i++){
            extract[i]=this.log.get(i-blanks+extras);
        }

        return extract;
    }
}