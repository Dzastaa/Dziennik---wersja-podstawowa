import java.util.ArrayList;
import java.util.List;

public class TableClass extends GenericTable {

    List<Student> students = new ArrayList<>();

    public TableClass(List<Student> s)
    {
        for(Student student : s) students.add(student);
    }

    @Override
    public int getRowCount()
    {
        return students.size();
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        switch(col)
        {
            case(0): return students.get(row).name;
            case(1): return students.get(row).surname;
            case(2): return students.get(row).studiesYear;
            case(3): return students.get(row).birthYear;
            case(4): return students.get(row).points;
        }
        return null;
    }

    public void set(List<Student> s)
    {
        students = new ArrayList<>();
        for(Student student : s) students.add(student);
    }

}
