import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Die {
    private List<Integer> values;
    public Die(){
        // Initialise to 'empty' die faces
        this.values = new ArrayList<Integer>();
    }

    // Roll, creating two die values -> return them both
    public List<Integer> roll(){
        this.values.clear();
        int value1 = (int)(Math.random() * 6 + 1);
        int value2 = (int)(Math.random() * 6 + 1);
        this.values.add(Math.max(value1,value2));
        int max = value1==value2 ? 4:2;
        for(int i=1;i<max;i++){
            this.values.add(Math.min(value1,value2));
        }
        return this.values;
    }

    public List<Integer> getCurrentValues(){
        // Need to sort the dice rolls so the player can't illegally use the lower number and leave themselves unable to use the larger
        List<Integer> sorted = new ArrayList(this.values);
        Collections.sort(sorted,Collections.reverseOrder());
        return sorted;
    }
}