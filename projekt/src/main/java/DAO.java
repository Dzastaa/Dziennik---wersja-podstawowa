import hibernateClasses.ClassesEntity;
import hibernateClasses.RatingsEntity;
import hibernateClasses.RelationclassstudentsEntity;
import hibernateClasses.StudentsEntity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DAO {
    public static DefaultListModel<Student> getAllStudentsDefaultListModel(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        //session.beginTransaction();
        List<StudentsEntity> list = session.createQuery("FROM StudentsEntity", StudentsEntity.class).getResultList();
        DefaultListModel<Student> result = new DefaultListModel<Student>();

        for(int i=0; i<list.size(); i++)
            result.add(i, new Student(list.get(i).getId(), list.get(i).getName(), list.get(i).getSurname(),
                    list.get(i).getStudiesYear(), list.get(i).getBirthYear(), list.get(i).getPoints()));
        //session.save();
        //session.getTransaction().commit();

        session.close();
        return result;
    }
    public static DefaultListModel<Student> getAllStudentsDefaultListModelSORTED(SessionFactory sessionFactory) {Session session = sessionFactory.openSession();
        List<StudentsEntity> list = session.createQuery("FROM StudentsEntity", StudentsEntity.class).getResultList();
        List<Student> s = new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            s.add(new Student(list.get(i).getId(), list.get(i).getName(), list.get(i).getSurname(),
                    list.get(i).getStudiesYear(), list.get(i).getBirthYear(), list.get(i).getPoints()));
        Collections.sort(s);
        DefaultListModel<Student> result = new DefaultListModel<Student>();
        for(int i=0; i<s.size(); i++)
            result.add(i, s.get(i));
        session.close();
        return result;
    }
    public static ArrayList<Student> getAllStudentsArrayList(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        List<StudentsEntity> list = session.createQuery("FROM StudentsEntity", StudentsEntity.class).getResultList();
        ArrayList<Student> result = new ArrayList<Student>();
        for(int i=0; i<list.size(); i++)
            result.add(new Student(list.get(i).getId(), list.get(i).getName(), list.get(i).getSurname(),
                    list.get(i).getStudiesYear(), list.get(i).getBirthYear(), list.get(i).getPoints()));
        session.close();
        return result;
    }
    public static ArrayList<Student> getStudentsFromClass(SessionFactory sessionFactory, int id) {
        Session session = sessionFactory.openSession();
        List<RelationclassstudentsEntity> list = session.createQuery("FROM RelationclassstudentsEntity WHERE idClass="+id, RelationclassstudentsEntity.class).getResultList();
        ArrayList<Student> result = new ArrayList<Student>();
        for(int i=0; i<list.size(); i++)
            result.add(getStudent(sessionFactory, list.get(i).getIdStudent()));
        session.close();
        return result;
    }
    public static DefaultListModel<Student> getStudentsFromClassDefaultListModel(SessionFactory sessionFactory, int id) {
        Session session = sessionFactory.openSession();
        List<RelationclassstudentsEntity> listID = session.createQuery("FROM RelationclassstudentsEntity WHERE idClass="+id, RelationclassstudentsEntity.class).getResultList();
        DefaultListModel<Student> result = new DefaultListModel<Student>();
        for(int i=0; i<listID.size(); i++)
            result.add(i, getStudent(sessionFactory, listID.get(i).getIdStudent()));
        session.close();
        return result;
    }
    public static DefaultListModel<Student> getStudentsNOTFromClassDefaultListModel(SessionFactory sessionFactory, int id) {
        Session session = sessionFactory.openSession();
        List<StudentsEntity> list = session.createQuery("FROM StudentsEntity", StudentsEntity.class).getResultList();
        List<RelationclassstudentsEntity> listID = session.createQuery("FROM RelationclassstudentsEntity WHERE idClass="+id, RelationclassstudentsEntity.class).getResultList();
        DefaultListModel<Student> result = new DefaultListModel<Student>();
        int j=0;
        boolean shouldBeAdded;
        for(int i=0; i<list.size(); i++)
        {
            shouldBeAdded = true;
            for(int k=0; k<listID.size(); k++)
            {
                if (list.get(i).getId() == listID.get(k).getIdStudent()) shouldBeAdded = false;
            }
            if(shouldBeAdded==true)
            {
                result.add(j, new Student(list.get(i).getId(), list.get(i).getName(), list.get(i).getSurname(),
                        list.get(i).getStudiesYear(), list.get(i).getBirthYear(), list.get(i).getPoints()));
                j++;
            }
        }
        session.close();
        return result;
    }
    public static Student getStudent(SessionFactory sessionFactory, int id) {
        try {
            Session session = sessionFactory.openSession();
            StudentsEntity s = session.createQuery("FROM StudentsEntity WHERE id=" + id, StudentsEntity.class).getSingleResult();
            Student result = new Student(s.getId(), s.getName(), s.getSurname(),
                    s.getStudiesYear(), s.getBirthYear(), s.getPoints());
            session.close();
            return result;
        }catch (NoResultException e){
            System.out.println(e);
        }
        return null;
    }
    public static ArrayList<Student> getStudentsWithGivenSurname(SessionFactory sessionFactory, String surname) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<StudentsEntity> cq = cb.createQuery(StudentsEntity.class);
        Root<StudentsEntity> root = cq.from(StudentsEntity.class);
        cq.select(root).where(cb.like(root.get("surname"), "%"+surname+"%"));
        Query<StudentsEntity> query = session.createQuery(cq);
        List<StudentsEntity> list = query.getResultList();
        session.close();
        ArrayList<Student> result = new ArrayList<Student>();
        for(int i=0; i<list.size(); i++)
            result.add(new Student(list.get(i).getId(), list.get(i).getName(), list.get(i).getSurname(),
                    list.get(i).getStudiesYear(), list.get(i).getBirthYear(), list.get(i).getPoints()));
        return result;
    }
    public static void addStudent(SessionFactory sessionFactory, Student student) {
        Session session = sessionFactory.openSession();
        StudentsEntity s = new StudentsEntity();
        s.setName(student.name);
        s.setSurname(student.surname);
        s.setBirthYear(student.birthYear);
        s.setStudiesYear(student.studiesYear);
        s.setPoints(student.points);
        session.beginTransaction();
        session.persist(s);
        session.flush();
        session.close();
    }
    public static void updateStudent(SessionFactory sessionFactory, Student student, int id) {
        Session session = sessionFactory.openSession();
        StudentsEntity s = (StudentsEntity) session.get(StudentsEntity.class, id);
        s.setName(student.name);
        s.setSurname(student.surname);
        s.setBirthYear(student.birthYear);
        s.setStudiesYear(student.studiesYear);
        s.setPoints(student.points);
        session.beginTransaction();
        session.merge(s);
        session.flush();
        session.close();
    }
    public static void deleteStudent(SessionFactory sessionFactory, int id) {
        Session session = sessionFactory.openSession();
        StudentsEntity s = (StudentsEntity) session.get(StudentsEntity.class, id);
        session.beginTransaction();
        session.delete(s);
        List<RelationclassstudentsEntity> list = session.createQuery("FROM RelationclassstudentsEntity WHERE idStudent="+id, RelationclassstudentsEntity.class).getResultList();
        for(int i=0; i<list.size(); i++) session.delete(list.get(i));
        session.flush();
        session.close();
    }
    public static DefaultListModel<Class> getAllClassesDefaultListModel(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        List<ClassesEntity> list = session.createQuery("FROM ClassesEntity", ClassesEntity.class).getResultList();
        DefaultListModel<Class> result = new DefaultListModel<>();
        for(int i=0; i<list.size(); i++)
            result.add(i, new Class(list.get(i).getId(), list.get(i).getName(), list.get(i).getLimitOfStudents(), getStudentsFromClass(sessionFactory, list.get(i).getId())));
        session.close();
        return result;
    }
    public static DefaultListModel<Class> getAllClassesDefaultListModelSORTED(SessionFactory sessionFactory) {Session session = sessionFactory.openSession();
        List<ClassesEntity> list = session.createQuery("FROM ClassesEntity", ClassesEntity.class).getResultList();
        List<Class> c = new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            c.add(new Class(list.get(i).getId(), list.get(i).getName(), list.get(i).getLimitOfStudents()));
        Collections.sort(c);
        DefaultListModel<Class> result = new DefaultListModel<Class>();
        for(int i=0; i<c.size(); i++)
            result.add(i, c.get(i));
        session.close();
        return result;
    }
    public static ArrayList<Class> getAllClassesArrayList(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        List<ClassesEntity> list = session.createQuery("FROM ClassesEntity", ClassesEntity.class).getResultList();
        ArrayList<Class> result = new ArrayList<Class>();
        for(int i=0; i<list.size(); i++)
            result.add(new Class(list.get(i).getId(), list.get(i).getName(), list.get(i).getLimitOfStudents()));
        session.close();
        return result;
    }
    public static Class getClass(SessionFactory sessionFactory, int id) {
        try{
        Session session = sessionFactory.openSession();
        ClassesEntity s = session.createQuery("FROM ClassesEntity WHERE id="+id, ClassesEntity.class).getSingleResult();
        Class result = new Class(s.getId(), s.getName(), s.getLimitOfStudents(), getStudentsFromClass(sessionFactory, s.getId()));
        session.close();
        return result;}
        catch (NoResultException e){
            System.out.println(e);
        }
        return null;
    }
    public static void addClass(SessionFactory sessionFactory, Class c) {
        Session session = sessionFactory.openSession();
        ClassesEntity cl = new ClassesEntity();
        cl.setName(c.name);
        cl.setLimitOfStudents(c.limitOfStudents);
        session.beginTransaction();
        session.persist(cl);
        session.flush();
        session.close();
    }
    public static void updateClass(SessionFactory sessionFactory, Class c, int id) {
        Session session = sessionFactory.openSession();
        ClassesEntity cl = (ClassesEntity) session.get(ClassesEntity.class, id);
        cl.setName(c.name);
        cl.setLimitOfStudents(c.limitOfStudents);
        session.beginTransaction();
        session.merge(cl);
        session.flush();
        session.close();
    }
    public static void deleteClass(SessionFactory sessionFactory, int id) {
        Session session = sessionFactory.openSession();
        ClassesEntity cl = (ClassesEntity) session.get(ClassesEntity.class, id);
        session.beginTransaction();
        session.delete(cl);
        List<RelationclassstudentsEntity> list = session.createQuery("FROM RelationclassstudentsEntity WHERE idClass="+id, RelationclassstudentsEntity.class).getResultList();
        for(int i=0; i<list.size(); i++) session.delete(list.get(i));
        session.flush();
        session.close();
    }
    public static void addStudentToClass(SessionFactory sessionFactory, int idClass, int idStudent) {
        Session session = sessionFactory.openSession();
        if(session.createQuery("FROM RelationclassstudentsEntity WHERE idClass="+idClass, RelationclassstudentsEntity.class).getResultList().size() >= getClass(sessionFactory, idClass).limitOfStudents)
            throw new IllegalArgumentException();
        else {
            RelationclassstudentsEntity r = new RelationclassstudentsEntity();
            r.setIdClass(idClass);
            r.setIdStudent(idStudent);
            session.beginTransaction();
            session.persist(r);
            session.flush();
        }
        session.close();
    }
    public static void removeStudentFromClass(SessionFactory sessionFactory, int idClass, int idStudent) {
        Session session = sessionFactory.openSession();
        RelationclassstudentsEntity r = session.createQuery("FROM RelationclassstudentsEntity WHERE idClass="+idClass+" AND idStudent="+idStudent, RelationclassstudentsEntity.class).getSingleResult();
        session.beginTransaction();
        session.delete(r);
        session.flush();
        session.close();
    }
    public static String getRatingForClass(SessionFactory sessionFactory, int id) {
        Session session = sessionFactory.openSession();
        String result = new String("Liczba ocen: ");

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<RatingsEntity> root = cq.from(RatingsEntity.class);
        cq.select(cb.count(root)).where(cb.equal(root.get("idClass"), id));
        Query<Long> query = session.createQuery(cq);
        result += query.getSingleResult();

        CriteriaQuery<Double> cq1 = cb.createQuery(Double.class);
        Root<RatingsEntity> root1 = cq1.from(RatingsEntity.class);
        cq1.select(cb.avg(root1.get("points"))).where(cb.equal(root.get("idClass"), id));
        Query<Double> query1 = session.createQuery(cq1);
        result += "   Średnia: " + query1.getSingleResult();

        session.close();
        return result;
    }
}