package hibernateClasses;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "relationclassstudents", schema = "PAOiM")
public class RelationclassstudentsEntity {
    private int id;
    private int idClass;
    private int idStudent;

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
    @Column(name = "idClass", insertable = false, updatable = false)
    public int getIdClass() {
        return idClass;
    }

    public void setIdClass(int idClass) {
        this.idClass = idClass;
    }

    @Basic
    @Column(name = "idStudent", insertable = false, updatable = false)
    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationclassstudentsEntity that = (RelationclassstudentsEntity) o;
        return id == that.id && idClass == that.idClass && idStudent == that.idStudent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idClass, idStudent);
    }
}
