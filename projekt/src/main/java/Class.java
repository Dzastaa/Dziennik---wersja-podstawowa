import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Class implements Comparable<Class>, Serializable {

    //=========
    static List<Class> allClasses = new ArrayList<>();
    //=========

    int id=0;
    String name;
    ArrayList<Student> students;
    //List<Student> students;
    int limitOfStudents;

    Class(String Name, int limit)
    {
        name = Name;
        students = new ArrayList<Student>();
        if(limit<=0) limit = 1;
        limitOfStudents = limit;

        allClasses.add(this);
    }

    Class(int Id, String Name, int limit)
    {
        id = Id;
        name = Name;
        students = new ArrayList<Student>();
        if(limit<=0) limit = 1;
        limitOfStudents = limit;

        allClasses.add(this);
    }

    Class(int Id, String Name, int limit, ArrayList<Student> listOfStudents)
    {
        id = Id;
        name = Name;
        students = new ArrayList<Student>();
        if(limit<=0) limit = 1;
        limitOfStudents = limit;
        students = listOfStudents;

        allClasses.add(this);
    }

    Class() {};

    void addStudent(Student s)
    {
        if(students.size() >= limitOfStudents)
            //System.err.println("\nERROR: class "+name+" can't have more students!\n");
            throw new IllegalArgumentException();
        else
        {
            for(Student student : students)
            {
                if(s.compareTo(student)==0)
                {
                    System.out.println("\nStudent with given surname is already in this class\n");
                    return;
                }
            }
            students.add(s);
        }
    }

    void addPoints(Student s, double p)
    {
        if(students.contains(s))
        {
            s.points += p;
            return;
        }
        System.err.println("\nERROR: Tn this class there's no such student!\n");
    }

    void getStudent(Student s)
    {
        if(students.contains(s))
        {
            students.remove(s);
            //niemożliwe jest całkowite usunięcie studenta w tym miejscu (???)
            if(s.points<=0.) s = null;
        }
    }

    void changeCondition(Student s, StudentCondition state)
    {
        if(students.contains(s)) s.state = state;
    }

    void removePoints(Student s, double p)
    {
        addPoints(s, -p);
    }

    Student search(String surname)
    {
        ComparatorSearch comp = new ComparatorSearch();
        for(Student student : students)
        {
            if(comp.compare(student.surname, surname)==1)
                return student;
        }
        System.out.println("There's no student with such surname in this class");
        return null;
    }

    List<Student> searchPartial(String sur)
    {
        List<Student> result = new ArrayList<Student>();
        for(Student student : students)
        {
            if(student.surname.contains(sur))
                result.add(student);
        }
        return result;
    }

    int countByCondition(StudentCondition state)
    {
        int result = 0;
        for(Student student : students)
        {
            if(student.state==state)
                result++;
        }
        return result;
    }

    void summary()
    {
        for(Student student : students)
        {
            student.print();
            System.out.println("\n===\n");
        }
    }

    List<Student> sortByName()
    {
        List<Student> result = new ArrayList<Student>();
        for(Student student : students) result.add(student);
        Collections.sort(result);
        return result;
    }

    List<Student> sortByPoints()
    {
        List<Student> result = new ArrayList<Student>();
        for(Student student : students) result.add(student);
        Collections.sort(result, Collections.reverseOrder(new ComparatorPoints()));
        return result;
    }

    Student max()
    {
        return Collections.max(students);
    }

    String convertToString()
    {
        //return name + " | limit: " + limitOfStudents;
        return toString();
    }

    public String toString()
    {
        return name + " | limit: " + limitOfStudents;
        /*String result = new String("|");
        for(Student student : students)
        {
            result += " " + student.toString() + " |";
        }
        return result;*/
    }

    @Override
    public int compareTo(Class o) {

        return name.compareTo(o.name);
    }
}