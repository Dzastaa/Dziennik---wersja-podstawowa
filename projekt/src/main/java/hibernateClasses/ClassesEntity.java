package hibernateClasses;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "classes", schema = "PAOiM")
public class ClassesEntity {
    private int id;
    private String name;
    private int limitOfStudents;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "limitOfStudents")
    public int getLimitOfStudents() {
        return limitOfStudents;
    }

    public void setLimitOfStudents(int limitOfStudents) {
        this.limitOfStudents = limitOfStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassesEntity that = (ClassesEntity) o;
        return id == that.id && limitOfStudents == that.limitOfStudents && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, limitOfStudents);
    }
}