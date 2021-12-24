package Repository;

import Exceptions.InputException;
import Exceptions.NullException;
import Model.Course;
import Model.Student;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseJdbcRepositoryTest {
    private CourseJdbcRepository courseRepo = new CourseJdbcRepository();

    CourseJdbcRepositoryTest() throws SQLException {
    }

    @Test
    void findOne() throws SQLException, InputException, NullException {
        Course course = new Course(1,"Flying", 1,100,5);

        assertEquals(course.getId(), courseRepo.findOne(1).getId());
        assertEquals(course.getName(), courseRepo.findOne(1).getName());
        assertEquals(course.getTeacherId(), courseRepo.findOne(1).getTeacherId());
        assertEquals(course.getMaxEnrollment(), courseRepo.findOne(1).getMaxEnrollment());
        assertEquals(course.getCredits(), courseRepo.findOne(1).getCredits());
    }

    @Test
    void findAll() throws SQLException, InputException {
        Course flying = new Course(1,"Flying", 1,100,5);
        Course potions = new Course(2,"Potions", 2,60,6);
        Course transfiguration = new Course(3,"Transfiguration", 3,200,10);

        List<Course> courseList = new ArrayList<>();
        courseList.add(flying);
        courseList.add(potions);
        courseList.add(transfiguration);

        List<Course> courseListDatabase = courseRepo.findAll();

        assertEquals(courseList.get(0).getId(), courseListDatabase.get(0).getId());
        assertEquals(courseList.get(1).getId(), courseListDatabase.get(1).getId());
        assertEquals(courseList.get(2).getId(), courseListDatabase.get(2).getId());
    }

    @Test
    void save() throws SQLException, NullException, InputException {
        Course test = new Course(4,"test",1,1,1);
        courseRepo.save(test);

        assertEquals(test.getId(),courseRepo.findOne(4).getId());
        assertEquals(test.getName(),courseRepo.findOne(4).getName());
        assertEquals(test.getTeacherId(),courseRepo.findOne(4).getTeacherId());
        assertEquals(test.getMaxEnrollment(),courseRepo.findOne(4).getMaxEnrollment());
        assertEquals(test.getCredits(),courseRepo.findOne(4).getCredits());
    }

    @Test
    void update() throws SQLException, InputException, NullException {
        Course test = new Course(4,"testUpdated",1,1,1);

        courseRepo.update(test);

        assertEquals(test.getName(),courseRepo.findOne(4).getName());
    }

    @Test
    void delete() throws SQLException, InputException, NullException {
        courseRepo.delete(4);

        try {
            courseRepo.findOne(4);
        } catch (Exception e){
            assertEquals(1,1);
        }
    }
}