import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Die {
    private final List<Integer> values;
    private boolean setManual = false;

    private static Die instance = null;


    // this can use the singleton pattern. there can only be one die
    // we don't need to pass the die around, we can just call Die.getInstance()
    private Die(){
        // Initialise to 'empty' die faces
        this.values = new ArrayList<Integer>();

        // Roll to initialise when instantiated
        this.roll();
    }

    public static Die getInstance() {
        if (instance == null) {
            instance = new Die();
        }
        return instance;
    }

    // Roll, creating two die values -> return them both
    public List<Integer> roll(){
        if (!setManual) {
            this.values.clear();

            int value1 = (int) (Math.random() * 6 + 1);
            int value2 = (int) (Math.random() * 6 + 1);

            this.values.add(value1);
            this.values.add(value2);
            int max = value1 == value2 ? 4 : 2;

            for (int i = 2; i < max; i++) {
                this.values.add(Math.min(value1, value2));
            }
        }
        setManual=false;
        return this.values;
    }

    public void setValues(int[] rolls){
        this.values.clear();
        for (int i=0;i<rolls.length;i++){
            this.values.add(rolls[i]);
        }
        this.setManual=true;
    }

    public static List<Integer> getCurrentValues(){
        this.setManual=false;
        return this.values;
    }
}