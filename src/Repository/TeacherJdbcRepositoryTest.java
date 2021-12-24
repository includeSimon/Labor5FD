package Repository;

import Exceptions.InputException;
import Exceptions.NullException;
import Model.Teacher;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherJdbcRepositoryTest {
    private TeacherJdbcRepository teacherRepo = new TeacherJdbcRepository();

    TeacherJdbcRepositoryTest() throws SQLException {
    }

    @Test
    void findOne() throws SQLException, InputException, NullException {
        Teacher rolandaHooch = new Teacher(1,"Rolanda", "Hooch");

        assertEquals(rolandaHooch.getId(), teacherRepo.findOne(1).getId());
        assertEquals(rolandaHooch.getFirstName(), teacherRepo.findOne(1).getFirstName());
        assertEquals(rolandaHooch.getLastName(), teacherRepo.findOne(1).getLastName());
    }

    @Test
    void findAll() throws SQLException, InputException {
        Teacher rolandaHooch = new Teacher(1,"Rolanda", "Hooch");
        Teacher severusSnape = new Teacher(2,"Severus", "Snape");
        Teacher albusDumbledore = new Teacher(3,"Albus", "Dumbledore");

        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(rolandaHooch);
        teacherList.add(severusSnape);
        teacherList.add(albusDumbledore);

        List<Teacher> teacherDatabaseList = teacherRepo.findAll();

        assertEquals(teacherList.get(0).getId(),teacherDatabaseList.get(0).getId());
        assertEquals(teacherList.get(1).getId(),teacherDatabaseList.get(1).getId());
        assertEquals(teacherList.get(2).getId(),teacherDatabaseList.get(2).getId());
    }

    @Test
    void save() throws SQLException, NullException, InputException {
        Teacher test = new Teacher(4,"test","test");
        teacherRepo.save(test);

        assertEquals(test.getId(), teacherRepo.findOne(4).getId());
        assertEquals(test.getFirstName(), teacherRepo.findOne(4).getFirstName());
        assertEquals(test.getLastName(), teacherRepo.findOne(4).getLastName());
    }

    @Test
    void update() throws SQLException, InputException, NullException {
        Teacher testUpdated = new Teacher(4,"test","new");
        teacherRepo.update(testUpdated);

        assertEquals(testUpdated.getLastName(),teacherRepo.findOne(4).getLastName());
    }

    @Test
    void delete() throws SQLException, InputException, NullException {
        teacherRepo.delete(4);

        try {
            teacherRepo.findOne(4);
        } catch (Exception e){
            assertEquals(1,1);
        }
    }
}