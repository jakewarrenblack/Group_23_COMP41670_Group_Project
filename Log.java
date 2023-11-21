import java.util.ArrayList;

/**
 * Prints to the screen messages sent to the players and then keeps a record of those messages
 */
public class Log {
    private ArrayList<String> log;
    public Log(){
        log = new ArrayList<String>();
    }

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
        for (int i=blanks;i<entries;i++){
            extract[i]=log.get(i-blanks+extras);
        }
        return extract;
    }
}
