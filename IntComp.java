import java.util.Comparator;

class IntComp implements Comparator<Integer> {
    public int compare(Integer i, Integer j) {
        return i - j;
    }
}

