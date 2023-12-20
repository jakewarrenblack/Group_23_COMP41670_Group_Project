// this seems like a very common use for generics
// it doesn't mind what types the key and value are, just provide them when you instantiate the class
public class Pair<T, T1> {
    private final T key;
    private final T1 value;

    public Pair(T key, T1 value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public T1 getValue() {
        return value;
    }
}