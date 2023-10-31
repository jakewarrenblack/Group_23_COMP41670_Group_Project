import java.util.List;
import java.util.Map;

public class Die {
    public List<Map.Entry<Integer, Integer>> values = new java.util.ArrayList<>();

    public Die(){
        this.values = new java.util.ArrayList<>();
    }

    // Roll, creating two die values -> return them both
    public List<Map.Entry<Integer, Integer>> roll(){
        // Set the dice face to some random value between 1 and 9 inclusive
        int value1 = (int)(Math.random() * 6 + 1);
        int value2 = (int)(Math.random() * 6 + 1);

        List<Map.Entry<Integer,Integer>> dieValues= new java.util.ArrayList<>();
        dieValues.add(new java.util.AbstractMap.SimpleEntry<>(value1, value2));

        this.values = dieValues;

        return dieValues;
    }
}