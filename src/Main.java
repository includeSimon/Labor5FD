import Exceptions.InputException;
import Exceptions.NullException;
import Model.Course;
import Model.Student;
import Model.Teacher;
import Repository.CourseJdbcRepository;
import Repository.CourseStudentJdbc;
import Repository.StudentJdbcRepository;
import Repository.TeacherJdbcRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InputException, NullException, IOException {
        CourseStudentJdbc relationshipRepo = new CourseStudentJdbc();


    }
}
