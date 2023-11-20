public class Die {
    private int[] values;
    public Die(){
        // Initialise to 'empty' die faces
        this.values = new int[2];
    }

    // Roll, creating two die values -> return them both
    public int[] roll(){
        // Set the dice face to some random value between 1 and 9 inclusive
        int value1 = (int)(Math.random() * 6 + 1);
        int value2 = (int)(Math.random() * 6 + 1);

        this.values = new int[2];

        this.values[0] = value1;
        this.values[1] = value2;

        return this.values;
    }

    public int[] getCurrentValues(){
        return this.values;
    }
}