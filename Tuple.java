public class Tuple {
    private final int[] values = new int[2];

    public Tuple(int value1, int value2){
        this.values[0] = value1;
        this.values[1] = value2;
    }

    public int getLeft(){
        return this.values[0];
    }

    public int getRight(){
        return this.values[1];
    }

    @Override
    public String toString() {
        return this.values[0] + " " + this.values[1];
    }
}
