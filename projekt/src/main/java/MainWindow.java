import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
//import org.hibernate.metamodel.Metadata;
//import org.hibernate.metamodel.MetadataSources;

//import javax.imageio.spi.ServiceRegistry;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.service.ServiceRegistry;


public class MainWindow {

    JFrame menuFrame;
    int selectedIndex;
    SessionFactory sessionFactory = null;

    JPanel createMainMenuPanel() {
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new FlowLayout());

        JButton studentsButton = new JButton("Studenci");
        studentsButton.setActionCommand("displayStudentsList");
        studentsButton.addActionListener(new ButtonClickListener());
        mainMenuPanel.add(studentsButton);

        JButton classesButton = new JButton("Klasy");
        classesButton.setActionCommand("displayClassesList");
        classesButton.addActionListener(new ButtonClickListener());
        mainMenuPanel.add(classesButton);

        JButton saveToCSVButton = new JButton("Zapis do pliku .csv");
        saveToCSVButton.setActionCommand("saveToCSV");
        saveToCSVButton.addActionListener(new ButtonClickListener());
        mainMenuPanel.add(saveToCSVButton);

        selectedIndex = -1;

        return mainMenuPanel;
    }
    public MainWindow() {
        menuFrame = new JFrame("Dziennik v2");
        //JFrame.setDefaultLookAndFeelDecorated(true);


        menuFrame.setSize(500,500);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setBackground(Color.red);
        JPanel mainMenuPanel = createMainMenuPanel();
        menuFrame.setContentPane(mainMenuPanel);
        menuFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(menuFrame, "Chcesz zapisać dane do pliku .csv?",
                        "Zapis danych", JOptionPane.YES_NO_OPTION);
                if(result==JOptionPane.YES_OPTION) saveToCSV();
            }
        });
        menuFrame.setVisible(true);

        //deserializeData();
        //readFromCSV();
        try {
            setUpSessionFactory();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(menuFrame, "Nie działa połączenie z bazą danych!", "Brak połączenia", JOptionPane.WARNING_MESSAGE);
        }
    }
    JPanel createStudentsListMenuPanel() {
        JPanel studentsListMenuPanel = new JPanel();
        studentsListMenuPanel.setLayout(new BoxLayout(studentsListMenuPanel, BoxLayout.Y_AXIS));
        DefaultListModel<Student> listOfStudents = DAO.getAllStudentsDefaultListModel(sessionFactory);
        JList studentsList = new JList(listOfStudents);

        TableStudent genericStudent;
        JTable tableStudent;
        genericStudent = new TableStudent(new Student());
        tableStudent = new JTable(genericStudent);
        final Student[] s = {new Student()};
        studentsList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                    s[0] = (Student)studentsList.getSelectedValue();
                    selectedIndex = s[0].id;
                    genericStudent.set(DAO.getStudent(sessionFactory, selectedIndex));
                    tableStudent.valueChanged(arg0);
            }
        });
        studentsListMenuPanel.add(studentsList);

        JButton addStudent = new JButton("Dodaj studenta");
        addStudent.setAlignmentX(Component.LEFT_ALIGNMENT);
        addStudent.setActionCommand("addStudent");
        addStudent.addActionListener(new ButtonClickListener());
        studentsListMenuPanel.add(addStudent);

        JButton removeStudent = new JButton("Usuń studenta");
        removeStudent.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeStudent.setActionCommand("removeStudent");
        removeStudent.addActionListener(new ButtonClickListener());
        studentsListMenuPanel.add(removeStudent);

        JButton editStudent = new JButton("Edytuj dane studenta");
        editStudent.setAlignmentX(Component.LEFT_ALIGNMENT);
        editStudent.setActionCommand("editStudent");
        editStudent.addActionListener(new ButtonClickListener());
        studentsListMenuPanel.add(editStudent);

        JButton sort = new JButton("Sortuj po imieniu");
        sort.setAlignmentX(Component.LEFT_ALIGNMENT);
        sort.setActionCommand("sortStudents");
        sort.addActionListener(new ButtonClickListener());
        studentsListMenuPanel.add(sort);

        JButton search = new JButton("Znajdź studenta po nazwisku");
        search.setAlignmentX(Component.LEFT_ALIGNMENT);
        search.setActionCommand("searchStudents");
        search.addActionListener(new ButtonClickListener());
        studentsListMenuPanel.add(search);

        JButton backToMainMenu = new JButton("Powrót");
        backToMainMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        backToMainMenu.setActionCommand("backToMainMenu");
        backToMainMenu.addActionListener(new ButtonClickListener());
        studentsListMenuPanel.add(backToMainMenu);

        tableStudent.setAlignmentY(Component.CENTER_ALIGNMENT);
        tableStudent.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentsListMenuPanel.add(tableStudent);

        return studentsListMenuPanel;
    }
    JPanel createClassesListMenuPanel() {
        JPanel classesListMenuPanel = new JPanel();
        classesListMenuPanel.setLayout(new BoxLayout(classesListMenuPanel, BoxLayout.Y_AXIS));

        DefaultListModel<Class> listOfClasses = DAO.getAllClassesDefaultListModel(sessionFactory);
        JList classesList = new JList(listOfClasses);
        JLabel labelTest = new JLabel();
        JLabel labelRating = new JLabel();

        TableClass genericClass;
        JTable tableClass;
        genericClass = new TableClass(new ArrayList<Student>());
        tableClass = new JTable(genericClass);

        final Class[] c = {new Class()};

        classesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                c[0] = (Class)classesList.getSelectedValue();
                selectedIndex = c[0].id;
                labelTest.setText(DAO.getClass(sessionFactory, selectedIndex).toString());
                labelRating.setText(DAO.getRatingForClass(sessionFactory, selectedIndex));
                genericClass.set(DAO.getClass(sessionFactory, selectedIndex).students);
                tableClass.valueChanged(e);
            }
        });
        classesListMenuPanel.add(classesList);

        JButton addStudentToClass = new JButton("Dodaj studenta do klasy");
        addStudentToClass.setAlignmentX(Component.LEFT_ALIGNMENT);
        addStudentToClass.setActionCommand("addStudentToClass");
        addStudentToClass.addActionListener(new ButtonClickListener());
        classesListMenuPanel.add(addStudentToClass);

        JButton removeStudentFromClass = new JButton("Usuń studenta z klasy");
        removeStudentFromClass.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeStudentFromClass.setActionCommand("removeStudentFromClass");
        removeStudentFromClass.addActionListener(new ButtonClickListener());
        classesListMenuPanel.add(removeStudentFromClass);

        JButton addClass = new JButton("Dodaj klasę");
        addClass.setAlignmentX(Component.LEFT_ALIGNMENT);
        addClass.setActionCommand("addClass");
        addClass.addActionListener(new ButtonClickListener());
        classesListMenuPanel.add(addClass);

        JButton editClass = new JButton("Edytuj klasę");
        editClass.setAlignmentX(Component.LEFT_ALIGNMENT);
        editClass.setActionCommand("editClass");
        editClass.addActionListener(new ButtonClickListener());
        classesListMenuPanel.add(editClass);

        JButton removeClass = new JButton("Usuń klasę");
        removeClass.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeClass.setActionCommand("removeClass");
        removeClass.addActionListener(new ButtonClickListener());
        classesListMenuPanel.add(removeClass);

        JButton sort = new JButton("Sortuj");
        sort.setAlignmentX(Component.LEFT_ALIGNMENT);
        sort.setActionCommand("sortClasses");
        sort.addActionListener(new ButtonClickListener());
        classesListMenuPanel.add(sort);

        JButton backToMainMenu = new JButton("Powrót");
        backToMainMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        backToMainMenu.setActionCommand("backToMainMenu");
        backToMainMenu.addActionListener(new ButtonClickListener());
        classesListMenuPanel.add(backToMainMenu);

        labelTest.setAlignmentY(Component.CENTER_ALIGNMENT);
        labelTest.setAlignmentX(Component.CENTER_ALIGNMENT);
        classesListMenuPanel.add(labelTest);

        tableClass.setAlignmentY(Component.CENTER_ALIGNMENT);
        tableClass.setAlignmentX(Component.CENTER_ALIGNMENT);
        classesListMenuPanel.add(tableClass);

        labelRating.setAlignmentY(Component.CENTER_ALIGNMENT);
        labelRating.setAlignmentX(Component.CENTER_ALIGNMENT);
        classesListMenuPanel.add(labelRating);

        return classesListMenuPanel;
    }
    void displayStudentsList() {
        menuFrame.getContentPane().removeAll();
        JPanel studentsListMenuPanel = createStudentsListMenuPanel();
        menuFrame.add(studentsListMenuPanel);
        selectedIndex = -1;
        menuFrame.revalidate();
        menuFrame.repaint();
    }
    void displayClassesList() {
        menuFrame.getContentPane().removeAll();
        JPanel classesListMenuPanel = createClassesListMenuPanel();
        menuFrame.add(classesListMenuPanel);
        selectedIndex = -1;
        menuFrame.revalidate();
        menuFrame.repaint();
    }
    void addStudent() {
        JTextField name = new JTextField(20);
        JTextField surname = new JTextField(20);
        JTextField studiesYear = new JTextField(20);
        JTextField birth = new JTextField(20);
        JTextField points = new JTextField(20);

        JPanel addStudentPanel = new JPanel();
        addStudentPanel.setLayout(new BoxLayout(addStudentPanel, BoxLayout.Y_AXIS));
        addStudentPanel.add(new JLabel("Imię studenta:"));
        addStudentPanel.add(name);
        addStudentPanel.add(new JLabel("Nazwisko studenta:"));
        addStudentPanel.add(surname);
        addStudentPanel.add(new JLabel("Rok studiów:"));
        addStudentPanel.add(studiesYear);
        addStudentPanel.add(new JLabel("Rok urodzenia:"));
        addStudentPanel.add(birth);
        addStudentPanel.add(new JLabel("Punkty:"));
        addStudentPanel.add(points);

        int result = JOptionPane.showConfirmDialog(null, addStudentPanel,
                "Wprowadź dane studenta", JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION)
        {
            if(name.getText().isEmpty() || surname.getText().isEmpty() || birth.getText().isEmpty() || studiesYear.getText().isEmpty() || points.getText().isEmpty())
                JOptionPane.showMessageDialog(menuFrame, "Niektóre z danych są puste!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
            else {
                try {
                    Student s = new Student(name.getText(), surname.getText(), Integer.parseInt(studiesYear.getText()),
                            Integer.parseInt(birth.getText()), Double.parseDouble(points.getText()));
                    DAO.addStudent(sessionFactory, s);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(menuFrame, "Złe argumenty!", "Uwaga", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        displayStudentsList();
    }
    void editStudent() {
        if(selectedIndex==-1) return;
        Student s = DAO.getStudent(sessionFactory, selectedIndex);

        JTextField name = new JTextField(s.name , 20);
        JTextField surname = new JTextField(s.surname , 20);
        JTextField studiesYear = new JTextField(String.valueOf(s.studiesYear), 20);
        JTextField birth = new JTextField(String.valueOf(s.birthYear), 20);
        JTextField points = new JTextField(String.valueOf(s.points), 20);

        JPanel addStudentPanel = new JPanel();
        addStudentPanel.setLayout(new BoxLayout(addStudentPanel, BoxLayout.Y_AXIS));
        addStudentPanel.add(new JLabel("Imię studenta:"));
        addStudentPanel.add(name);
        addStudentPanel.add(new JLabel("Nazwisko studenta:"));
        addStudentPanel.add(surname);
        addStudentPanel.add(new JLabel("Rok studiów:"));
        addStudentPanel.add(studiesYear);
        addStudentPanel.add(new JLabel("Rok urodzenia:"));
        addStudentPanel.add(birth);
        addStudentPanel.add(new JLabel("Punkty:"));
        addStudentPanel.add(points);

        int result = JOptionPane.showConfirmDialog(null, addStudentPanel,
                "Edytuj dane studenta", JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION)
        {
            if(name.getText().isEmpty() || surname.getText().isEmpty() || birth.getText().isEmpty() || studiesYear.getText().isEmpty() || points.getText().isEmpty())
                JOptionPane.showMessageDialog(menuFrame, "Brak argumentów!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
            else {
                try {
                    s.name=name.getText();
                    s.surname=surname.getText();
                    s.studiesYear=Integer.parseInt(studiesYear.getText());
                    s.birthYear=Integer.parseInt(birth.getText());
                    s.points=Double.parseDouble(points.getText());
                    DAO.updateStudent(sessionFactory, s, selectedIndex);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(menuFrame, "Niepoprawne argumenty!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        displayStudentsList();
    }
    void removeStudent() {
        if(selectedIndex==-1) return;
        DAO.deleteStudent(sessionFactory, selectedIndex);
        displayStudentsList();
    }
    void backToMainMenu() {
        menuFrame.getContentPane().removeAll();
        JPanel mainMenuPanel = createMainMenuPanel();
        menuFrame.add(mainMenuPanel);
        menuFrame.revalidate();
        menuFrame.repaint();
    }
    void addStudentToClass() {
        if(selectedIndex==-1) return;
        int classIndex = selectedIndex;
        selectedIndex=-1;
        JPanel addStudentPanel = new JPanel();
        addStudentPanel.setLayout(new BoxLayout(addStudentPanel, BoxLayout.Y_AXIS));
        DefaultListModel<Student> listOfStudents = DAO.getStudentsNOTFromClassDefaultListModel(sessionFactory, classIndex);
        JList studentsList = new JList(listOfStudents);

        final Student[] s = {new Student()};
        studentsList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                s[0] = (Student)studentsList.getSelectedValue();
                selectedIndex = s[0].id;
            }
        });
        addStudentPanel.add(studentsList);

        int result = JOptionPane.showConfirmDialog(null, addStudentPanel,
                "Wybierz studenta w celu dodania do klasy"+ DAO.getClass(sessionFactory, classIndex).name +"=", JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION)
        {
            if(selectedIndex==-1)
                JOptionPane.showMessageDialog(menuFrame, "Nie wybrano studenta!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
            else {
                try {
                    DAO.addStudentToClass(sessionFactory, classIndex, selectedIndex);
                }
                catch(IllegalArgumentException e)
                {
                    JOptionPane.showMessageDialog(menuFrame, "Klasa jest pełna!", "Uwaga", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        displayClassesList();
    }
    void removeStudentFromClass() {
        if(selectedIndex==-1) return;
        int classIndex = selectedIndex;
        selectedIndex=-1;
        JPanel removeStudentPanel = new JPanel();
        removeStudentPanel.setLayout(new BoxLayout(removeStudentPanel, BoxLayout.Y_AXIS));
        DefaultListModel<Student> listOfStudents = DAO.getStudentsFromClassDefaultListModel(sessionFactory, classIndex);
        JList studentsList = new JList(listOfStudents);
        final Student[] s = {new Student()};
        studentsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                s[0] = (Student)studentsList.getSelectedValue();
                selectedIndex = s[0].id;

            }
        });
        removeStudentPanel.add(studentsList);

        int result = JOptionPane.showConfirmDialog(null, removeStudentPanel,
                "Wybierz studenta do usunięcia"+ DAO.getClass(sessionFactory, classIndex).name +"=", JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION)
        {
            if(selectedIndex==-1)
                JOptionPane.showMessageDialog(menuFrame, "Nie wybrano studenta!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
            else {
                DAO.removeStudentFromClass(sessionFactory, classIndex, selectedIndex);
            }
        }

        displayClassesList();
    }
    void addClass() {
        JTextField name = new JTextField(20);
        JTextField limit = new JTextField(20);

        JPanel addClassPanel = new JPanel();
        addClassPanel.setLayout(new BoxLayout(addClassPanel, BoxLayout.Y_AXIS));
        addClassPanel.add(new JLabel("Nazwa klasy"));
        addClassPanel.add(name);
        addClassPanel.add(new JLabel("Limit liczby studentów:"));
        addClassPanel.add(limit);

        int result = JOptionPane.showConfirmDialog(null, addClassPanel,
                "Wprowadź dane klasy", JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION)
        {
            if(name.getText().isEmpty() || limit.getText().isEmpty())
                JOptionPane.showMessageDialog(menuFrame, "Niektóre dane są puste!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
            else {
                try {
                    Class c = new Class(name.getText(), Integer.parseInt(limit.getText()));
                    DAO.addClass(sessionFactory, c);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(menuFrame, "Niepoprawne dane!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        displayClassesList();
    }
    void editClass() {
        if(selectedIndex==-1) return;
        Class c = DAO.getClass(sessionFactory, selectedIndex);

        JTextField name = new JTextField(c.name , 20);
        JTextField limit = new JTextField(String.valueOf(c.limitOfStudents) , 20);

        JPanel editClassPanel = new JPanel();
        editClassPanel.setLayout(new BoxLayout(editClassPanel, BoxLayout.Y_AXIS));
        editClassPanel.add(new JLabel("Nazwa klasy:"));
        editClassPanel.add(name);
        editClassPanel.add(new JLabel("Limit studentów:"));
        editClassPanel.add(limit);

        int result = JOptionPane.showConfirmDialog(null, editClassPanel,
                "Edycja klasy", JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION)
        {
            if(name.getText().isEmpty() || limit.getText().isEmpty())
                JOptionPane.showMessageDialog(menuFrame, "Brak danych!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
            else {
                try {
                    if(Integer.parseInt(limit.getText()) <= 0)
                        JOptionPane.showMessageDialog(menuFrame, "Niepoprawne dane!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
                    else if(Integer.parseInt(limit.getText()) < c.students.size())
                        JOptionPane.showMessageDialog(menuFrame, "Niepoprawne dane!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
                    else {
                        c.name = name.getText();
                        c.limitOfStudents = Integer.parseInt(limit.getText());
                        DAO.updateClass(sessionFactory, c, selectedIndex);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(menuFrame, "Niepoprawne dane!", "Uwaga", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        displayClassesList();
    }
    void removeClass() {
        if(selectedIndex==-1) return;
        DAO.deleteClass(sessionFactory, selectedIndex);
        displayClassesList();
    }
    void sortStudents() {
        JPanel sortedPanel = new JPanel();
        sortedPanel.setLayout(new BoxLayout(sortedPanel, BoxLayout.Y_AXIS));
        DefaultListModel<Student> listOfStudents = DAO.getAllStudentsDefaultListModelSORTED(sessionFactory);
        JList studentsList = new JList(listOfStudents);

        sortedPanel.add(studentsList);
        JOptionPane.showConfirmDialog(null, sortedPanel,
                "Sortowanie listy studentów", JOptionPane.CLOSED_OPTION);
    }
    void sortClasses() {
        JPanel sortedPanel = new JPanel();
        sortedPanel.setLayout(new BoxLayout(sortedPanel, BoxLayout.Y_AXIS));
        DefaultListModel<Class> listOfClasses = DAO.getAllClassesDefaultListModelSORTED(sessionFactory);
        JList classesList = new JList(listOfClasses);

        sortedPanel.add(classesList);
        JOptionPane.showConfirmDialog(null, sortedPanel,
                "Sortowanie listy klass", JOptionPane.CLOSED_OPTION);
    }
    void search() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        JTextField searched = new JTextField(20);
        searchPanel.add(searched);

        int result = JOptionPane.showConfirmDialog(null, searchPanel,
                "Wpisz nazwisko studenta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            JPanel foundPanel = new JPanel();
            foundPanel.setLayout(new BoxLayout(foundPanel, BoxLayout.Y_AXIS));

            List<Student> found = DAO.getStudentsWithGivenSurname(sessionFactory, searched.getText());

            TableClass genericClass = new TableClass(found);
            JTable tableFound;
            tableFound = new JTable(genericClass);
            foundPanel.add(tableFound);
            JOptionPane.showConfirmDialog(null, foundPanel,
                    "Znajdź studenta", JOptionPane.CLOSED_OPTION);
        }
    }
    void serializeID(String filename) {
        try
        {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(Student.idStatic);
            out.close();
            file.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    void deserializeID(String filename) {
        try
        {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            Student.idStatic = (int) in.readObject();
            in.close();
            file.close();
        }
        catch(IOException |ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    void serializeData() {
        String filename = new String("DaneStudenta.txt");
        try
        {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(Student.allStudents);

            filename = "DaneKlas.txt";
            file = new FileOutputStream(filename);
            out = new ObjectOutputStream(file);
            out.writeObject(Class.allClasses);

            out.close();
            file.close();
            JOptionPane.showConfirmDialog(null, "Zapis danych przebiegł pomyślnie!", "Zapisano", JOptionPane.CLOSED_OPTION);

            serializeID("serializedID.txt");
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(menuFrame, "Błąd w trakcie zapisu!", "Niepowodzenie", JOptionPane.WARNING_MESSAGE);
        }
    }

    void saveToCSV() {
        String filename = new String("csvStudent.csv");
        File csvOut = new File(filename);
        List<String> data = new ArrayList<>();

        ArrayList<Student> s = DAO.getAllStudentsArrayList(sessionFactory);
        try (PrintWriter pw = new PrintWriter(csvOut))
        {
            for (int i = 0; i < s.size(); i++)
            {
                data.add(s.get(i).name + "," + s.get(i).surname
                        + "," + s.get(i).birthYear + "," + s.get(i).points
                        + "," + s.get(i).studiesYear + "," + s.get(i).scholarship
                        + "," + s.get(i).id + ",");
            }
            data.stream().forEach(pw::println);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(menuFrame, "Niepowodzenie w trakcie zapisu danych", "Niepowodzenie zapisu", JOptionPane.WARNING_MESSAGE);
        }

        filename = new String("csvKlas.csv");
        csvOut = new File(filename);
        data = new ArrayList<>();

        ArrayList<Class> c = DAO.getAllClassesArrayList(sessionFactory);
        try(PrintWriter pw = new PrintWriter(csvOut))
        {
            for (int i = 0; i < c.size(); i++)
            {
                data.add(c.get(i).name + "," + c.get(i).limitOfStudents+ ",");
            }
            data.stream().forEach(pw::println);


            serializeID("Plik.txt");
            JOptionPane.showConfirmDialog(null, "Zapis do pliku .csv przeprowadzono poprawnie!", "Zapis do pliku .csv zakończono sukcesem", JOptionPane.CLOSED_OPTION);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(menuFrame, "Nie udało się zapisać pliku .csv!", "Saving to .csv failed", JOptionPane.WARNING_MESSAGE);
        }
    }
    void readFromCSV() {
        try
        {
        String filename = new String("csvStudents.csv");
        File csvIn = new File(filename);
        FileReader reader = new FileReader(csvIn);
        LineNumberReader lnr = new LineNumberReader(reader);
        String line;
        List<String> data = new ArrayList<>();
        int counter;

        LineNumberReader getNumberOfLines = new LineNumberReader(new FileReader(new File(filename)));
        int numberOfLines = 0;
        while(getNumberOfLines.readLine()!=null) numberOfLines++;

        for(int k=0; k<numberOfLines; k++)
        {
            line = lnr.readLine();
            counter = 0;
            data.clear();
            for(int i=0; i<line.length(); i++)
            {
                if(line.charAt(i)==',')
                {
                    data.add(line.substring(counter, i));
                    counter = i+1;
                }
            }
            new Student(data.get(0), data.get(1), StudentCondition.Present,
                    Integer.parseInt(data.get(2)), Double.parseDouble(data.get(3)) ,
                    Integer.parseInt(data.get(4)), Boolean.parseBoolean(data.get(5)),
                    Integer.parseInt(data.get(6)));
        }

            filename = new String("csvKlas.csv");
            csvIn = new File(filename);
            reader = new FileReader(csvIn);
            lnr = new LineNumberReader(reader);
            data = new ArrayList<>();
            List<Integer> IDs = new ArrayList<>();
            Class c;

            getNumberOfLines = new LineNumberReader(new FileReader(new File(filename)));
            numberOfLines = 0;
            while(getNumberOfLines.readLine()!=null) numberOfLines++;

            for(int k=0; k<numberOfLines; k++)
            {
                line = lnr.readLine();
                counter = 0;
                data.clear();
                for(int i=0; i<line.length(); i++)
                {
                    if(line.charAt(i)==',')
                    {
                        data.add(line.substring(counter, i));
                        counter = i+1;
                    }
                }

                counter=0;
                IDs.clear();
                for(int i=0; i<data.get(2).length(); i++)
                {
                    if(data.get(2).charAt(i)=='|')
                    {
                        IDs.add(Integer.parseInt(data.get(2).substring(counter, i)));
                        counter = i+1;
                    }
                }

                c = new Class(data.get(0), Integer.parseInt(data.get(1)));
                for(int i=0; i<IDs.size(); i++)
                    for(int j=0; j<Student.allStudents.size(); j++)
                        if(Student.allStudents.get(j).id==IDs.get(i))
                            c.addStudent(Student.allStudents.get(j));
            }

            deserializeID("IDcsv.txt");
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(menuFrame, "Niepowodzenie w trakcie zapisu!", "Niepowodzenie zapisu", JOptionPane.WARNING_MESSAGE);
        }
    }

    void setUpSessionFactory(){
        try {

            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(StudentCondition.class);
            configuration.addAnnotatedClass(Class.class);


            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }


    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent event)
        {
            String command = event.getActionCommand();
            if (command.equals("displayStudentsList"))
            {
                displayStudentsList();
            }
            else if (command.equals("displayClassesList"))
            {
                displayClassesList();
            }
            else if (command.equals("addStudent"))
            {
                addStudent();
            }
            else if (command.equals("editStudent"))
            {
                editStudent();
            }
            else if (command.equals("removeStudent"))
            {
                removeStudent();
            }
            else if (command.equals("backToMainMenu"))
            {
                backToMainMenu();
            }
            else if (command.equals("addStudentToClass"))
            {
                addStudentToClass();
            }
            else if (command.equals("removeStudentFromClass"))
            {
                removeStudentFromClass();
            }
            else if (command.equals("removeClass"))
            {
                removeClass();
            }
            else if (command.equals("addClass"))
            {
                addClass();
            }
            else if (command.equals("editClass"))
            {
                editClass();
            }
            else if (command.equals("sortStudents"))
            {
                sortStudents();
            }
            else if (command.equals("sortClasses"))
            {
                sortClasses();
            }
            else if (command.equals("searchStudents"))
            {
                search();
            }
            else if (command.equals("serializeData"))
            {
                serializeData();
            }
            else if (command.equals("saveToCSV"))
            {
                saveToCSV();
            }
        }
    }

}
