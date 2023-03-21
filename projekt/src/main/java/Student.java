

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

public class Student
        implements Comparable<Student>, Serializable {

    //=========
    static List<Student> allStudents = new ArrayList<>();
    static int idStatic = 0;
    //=========

    String name;
    String surname;
    StudentCondition state;
    int birthYear;
    double points;
    int studiesYear;        //rok rozpoczęcia studiów
    boolean scholarship;    //stypendium
    int id;

    Student(String Name, String Surname, StudentCondition State, int Birth, double Points, int Studies, boolean Scholarship)
    {
        name = Name;
        surname = Surname;
        state = State;
        birthYear = Birth;
        points = Points;
        studiesYear = Studies;
        scholarship = Scholarship;

        id = idStatic++;
        allStudents.add(this);
    }

    Student(String Name, String Surname, StudentCondition State, int Birth, double Points, int Studies, boolean Scholarship, int Id)
    {
        name = Name;
        surname = Surname;
        state = State;
        birthYear = Birth;
        points = Points;
        studiesYear = Studies;
        scholarship = Scholarship;

        id = Id;
        allStudents.add(this);
    }

    //przeładowany konstruktor do szybszego i łatwego inicjalizowania
    Student(String Name, String Surname, StudentCondition State, int Birth, double Points)
    {
        name = Name;
        surname = Surname;
        state = State;
        birthYear = Birth;
        points = Points;
        studiesYear = 2019;
        scholarship = false;

        id = idStatic++;
        allStudents.add(this);
    }

    //konstruktor do Swinga
    Student(String Name, String Surname, int StudiesYear, int Birth, double Points)
    {
        name = Name;
        surname = Surname;
        state = StudentCondition.Present;
        birthYear = Birth;
        points = Points;
        studiesYear = StudiesYear;
        scholarship = false;

        id = idStatic++;
        allStudents.add(this);
    }

    //konstruktor do Hibernate'a
    Student(int Id, String Name, String Surname, int StudiesYear, int Birth, double Points)
    {
        name = Name;
        surname = Surname;
        state = StudentCondition.Present;
        birthYear = Birth;
        points = Points;
        studiesYear = StudiesYear;
        scholarship = false;

        id = Id;
        allStudents.add(this);
    }

    Student()
    {
        ;
    }

    void print()
    {
        if(scholarship==true)
            System.out.println("Student: "+name+" "+surname+"\nState: "+state+"\nYear of birth: "
                    +birthYear+"\nPoints: "+points+"\nFirst year of studies: "+studiesYear+"\nScholarship: yes");
        else
            System.out.println("Student: "+name+" "+surname+"\nState: "+state+"\nYear of birth: "
                    +birthYear+"\nPoints: "+points+"\nFirst year of studies: "+studiesYear+"\nScholarship: no");
    }

    String convertToString()
    {
        //return (name + " " + surname + " " + birthYear);
        return toString();
    }
    public String toString()
    {
        return (name + " " + surname + " " + birthYear);
    }

    @Override
    public int compareTo(Student o) {

        return surname.compareTo(o.surname);
    }
}
