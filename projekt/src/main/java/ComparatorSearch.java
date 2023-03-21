import java.util.Comparator;

public class ComparatorSearch
        implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        if(o1.equals(o2)) return 1;
        return 0;
    }
}
