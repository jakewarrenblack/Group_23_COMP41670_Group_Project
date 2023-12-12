import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Die {
    private final List<Integer> values;
    public Die(){
        // Initialise to 'empty' die faces
        this.values = new ArrayList<Integer>();

        // Roll to initialise when instantiated
        this.roll();
    }

    // Roll, creating two die values -> return them both
    public List<Integer> roll(){
        this.values.clear();

        int value1 = (int)(Math.random() * 6 + 1);
        int value2 = (int)(Math.random() * 6 + 1);

        this.values.add(value1);
        this.values.add(value2);
        int max = value1==value2 ? 4:2;

        for(int i=2;i<max;i++){
            this.values.add(Math.min(value1,value2));
        }

        return this.values;
    }

    public List<Integer> getCurrentValues(){
        return this.values;
    }
}