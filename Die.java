public class Die {
    private Tuple values;
    public Die(){
        // Initialise to 'empty' die faces
        this.values = new Tuple(0, 0);
    }

    // Roll, creating two die values -> return them both
    public Tuple roll(){
        // Set the dice face to some random value between 1 and 9 inclusive
        int value1 = (int)(Math.random() * 6 + 1);
        int value2 = (int)(Math.random() * 6 + 1);

        this.values = new Tuple(value1, value2);

        return this.values;
    }

    public Tuple getCurrentValues(){
        return this.values;
    }
}