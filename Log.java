import java.util.ArrayList;

public class Log {
    private final ArrayList<String> log;

    public Log(){
        this.log = new ArrayList<>();
    }

    public void updateLog(String entry){
        System.out.println(entry);
        this.log.add(entry);
    }

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
