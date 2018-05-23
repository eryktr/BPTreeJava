/**
 * This auxiliary class helps to represent the structure of a Node.
 * @param <T>
 * @param <K>
 */
public class Pair<T,K> {
    private final T first;
    private final K second;

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first element of the Pair
     * @return
     */
    public T getFirst() {
        return first;
    }

    /**
     * Returns the second element of the Pair
     * @return
     */
    public K getSecond() {
        return second;
    }

}
