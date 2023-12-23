import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The pair of dice which the players use in the games to see how far they can move their
 * pieces on each turn
 */
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

    /**
     * Roll a pair of dice and return the face values
     * If the dice both have the same face value, return double the number
     * If the face values of the dice have been set manually, return them and turn off the set manual flag
     *
     * @return List of Integers representing the face value of the dice rolled
     */
    public List<Integer> roll() {
        if (!setManual) {
            this.values.clear();

            int value1 = (int) (Math.random() * 6 + 1);
            int value2 = (int) (Math.random() * 6 + 1);

            // add both arrays to the list
            this.values.addAll(Arrays.asList(value1, value2));

            // Use a single loop to add values based on the condition
            for (int i = 2; i < (value1 == value2 ? 4 : 2); i++) this.values.add(value1);
        }

        setManual = false;
        return this.values;
    }


    /**
     * Manually set the value to return the next time the dice are rolled
     *
     * @param rolls integer values to set the return
     */
    public void setValues(int[] rolls){
        this.values.clear();
        for (int roll : rolls) {
            this.values.add(roll);
        }
        this.setManual=true;
    }

    /**
     * Give the value of the dice the last time they were rolled
     * Or what they have been manually set to
     *
     * @return List of Integers representing the most recent dice roll
     */
    public List<Integer> getCurrentValues(){
        this.setManual=false;
        return this.values;
    }
}