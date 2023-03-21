public class TableStudent extends GenericTable{

    Student student;

    public TableStudent(Student s)
    {
        student=s;
    }

    @Override
    public int getRowCount()
    {
        return 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch(col)
        {
            case(0): return student.name;
            case(1): return student.surname;
            case(2): return student.studiesYear;
            case(3): return student.birthYear;
            case(4): return student.points;
        }
        return null;
    }

    public void set(Student s)
    {
        student = s;
    }

}
