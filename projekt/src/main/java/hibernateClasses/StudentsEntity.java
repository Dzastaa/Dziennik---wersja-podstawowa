package hibernateClasses;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "StudentsEntity")
@Table(name = "students")

public class StudentsEntity {
    private int id;
   private int birthYear;
    private int studiesYear;
    private double points;
    private String name;
    private String surname;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "student")
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
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "birthYear")
    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    @Basic
    @Column(name = "studiesYear")
    public int getStudiesYear() {
        return studiesYear;
    }

    public void setStudiesYear(int studiesYear) {
        this.studiesYear = studiesYear;
    }

    @Basic
    @Column(name = "points")
    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsEntity that = (StudentsEntity) o;
        return id == that.id && birthYear == that.birthYear && studiesYear == that.studiesYear && Double.compare(that.points, points) == 0 && Objects.equals(name, that.name) && Objects.equals(surname, that.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, birthYear, studiesYear, points);
    }
}