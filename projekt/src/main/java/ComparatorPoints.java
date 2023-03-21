import java.util.Comparator;

public class ComparatorPoints
        implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        double result = o1.points-o2.points;
        if(result<0.) return -1;
        else if(result>0.) return 1;
        else return 0;
    }
}
