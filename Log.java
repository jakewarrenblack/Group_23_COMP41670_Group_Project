import java.util.ArrayList;

public class Log {
    private ArrayList<String> log;
    public Log(){
        log = new ArrayList<String>();
    }
    public void updateLog(String entry){
        System.out.println(entry);
        log.add(entry);
    }
    public String[] recentLog(int entries){
        String[] extract = new String[entries];
        int blanks = entries-log.size()>0 ? entries-log.size():0;
        int extras = log.size()-entries>0 ? log.size()-entries:0;
        for (int i=0;i<blanks;i++) {
            extract[i] = "";
        }
        for (int i=blanks;i<entries;i++){
            extract[i]=log.get(i-blanks+extras);
        }
        return extract;
    }
}
