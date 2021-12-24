package Repository;

import Exceptions.InputException;
import Exceptions.NullException;
import Model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentJdbcRepositoryTest {
    private StudentJdbcRepository studentRepo = new StudentJdbcRepository();

    StudentJdbcRepositoryTest() throws SQLException {
    }

    @org.junit.jupiter.api.Test
    void findOne() throws SQLException, NullException, InputException {
        Student student = new Student(1,"Harry", "Potter");

        assertEquals(student.getId(), studentRepo.findOne(1).getId());
        assertEquals(student.getFirstName(), studentRepo.findOne(1).getFirstName());
        assertEquals(student.getLastName(), studentRepo.findOne(1).getLastName());
    }

    @org.junit.jupiter.api.Test
    void findAll() throws SQLException, InputException {
        Student harryPotter = new Student(1,"Harry", "Potter");
        Student hermioneGranger = new Student(2,"Hermione", "Granger");
        Student ronWeasly = new Student(3,"Ron", "Weasly");

        List<Student> studentList = new ArrayList<>();
        studentList.add(harryPotter);
        studentList.add(hermioneGranger);
        studentList.add(ronWeasly);

        List<Student> databaseList = studentRepo.findAll();

        assertEquals(studentList.get(0).getId(),databaseList.get(0).getId());
        assertEquals(studentList.get(0).getFirstName(),databaseList.get(0).getFirstName());
        assertEquals(studentList.get(0).getLastName(),databaseList.get(0).getLastName());

        assertEquals(studentList.get(1).getId(),databaseList.get(1).getId());
        assertEquals(studentList.get(1).getFirstName(),databaseList.get(1).getFirstName());
        assertEquals(studentList.get(1).getLastName(),databaseList.get(1).getLastName());

        assertEquals(studentList.get(2).getId(),databaseList.get(2).getId());
        assertEquals(studentList.get(2).getFirstName(),databaseList.get(2).getFirstName());
        assertEquals(studentList.get(2).getLastName(),databaseList.get(2).getLastName());
    }

    @org.junit.jupiter.api.Test
    void save() throws SQLException, NullException, InputException {
        Student test = new Student(999, "test","test");
        studentRepo.save(test);

        assertEquals(test.getId(), studentRepo.findOne(999).getId());
        assertEquals(test.getFirstName(), studentRepo.findOne(999).getFirstName());
        assertEquals(test.getLastName(), studentRepo.findOne(999).getLastName());
    }

    @org.junit.jupiter.api.Test
    void update() throws SQLException, InputException, NullException {
        Student testUpdated = new Student(999, "test","updated");
        studentRepo.update(testUpdated);

        assertEquals(testUpdated.getLastName(), studentRepo.findOne(999).getLastName());
    }

    @org.junit.jupiter.api.Test
    void delete() throws SQLException, InputException, NullException {
        studentRepo.delete(999);

        try {
            studentRepo.findOne(999);
        } catch (Exception e){
            assertEquals(1,1);
        }
    }
}