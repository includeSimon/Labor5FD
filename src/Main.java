import Exceptions.InputException;
import Exceptions.NullException;
import Model.Course;
import Model.Student;
import Model.Teacher;
import Repository.CourseJdbcRepository;
import Repository.StudentJdbcRepository;
import Repository.TeacherJdbcRepository;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InputException, NullException, IOException {
CourseJdbcRepository courseJdbcRepository = new CourseJdbcRepository();
    Course course = new Course(77,"a",6,6,6);

    courseJdbcRepository.save(course);
    }
}
