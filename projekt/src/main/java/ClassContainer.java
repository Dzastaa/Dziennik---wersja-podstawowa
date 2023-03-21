import java.util.*;

public class ClassContainer {

    Map<String, Class> container;

    ClassContainer()
    {
        container = new HashMap<String, Class>();
    }

    void addClass(String name, int limit)
    {
        container.put(name, new Class(name, limit));
    }

    void removeClass(String name)
    {
        container.remove(name);
    }

    List<Class> findEmpty()
    {
        List<Class> result = new ArrayList<Class>();
        for(Class c : container.values()) if(c.students.isEmpty()) result.add(c);
        return result;
    }

    void summary()
    {
        for(Class c : container.values()) System.out.println("\nName of Class: "+c.name+"\nPercentage filling: "+(((double)c.students.size()/(double)c.limitOfStudents)*100.)+"%");
    }
}
