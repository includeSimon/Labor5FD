package Repository;

import Exceptions.InputException;
import Exceptions.NullException;
import Model.Student;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class StudentJdbcRepositoryTest {
    private StudentJdbcRepository studentRepo;

    public StudentJdbcRepositoryTest(StudentJdbcRepository studentRepo){
        this.studentRepo = studentRepo;
    }

    @org.junit.jupiter.api.Test
    void findOne() throws SQLException, NullException, InputException {
        Student student = new Student(9999,"test", "test");
        studentRepo.save(student);

        assertEquals(student, studentRepo.findOne(9999));

        studentRepo.delete(9999);
    }

    @org.junit.jupiter.api.Test
    void findAll() {
    }

    @org.junit.jupiter.api.Test
    void save() {
    }

    @org.junit.jupiter.api.Test
    void update() {
    }

    @org.junit.jupiter.api.Test
    void delete() {
    }
}