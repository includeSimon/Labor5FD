package Repository;
import java.sql.*;

import Exceptions.InputException;
import Exceptions.NullException;
import Model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseJdbcRepository implements ICrudRepository<Course> {
    private final Connection connection;

    public CourseJdbcRepository() throws  SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab5", "root", "");
    }

    @Override
    public Course findOne(Integer id) throws NullException, SQLException, InputException {
        if (id == null)
            throw new NullException("Id must not be null");

        Course course;

        String courseQuery = "select c.id, c.name, c.teacherId, c.maxEnrollment, c.credits from course c " +
                "where c.id = " + id;

        Statement statement = connection.createStatement();
        ResultSet resultCourse = statement.executeQuery(courseQuery);

        if (resultCourse.next())
            course = new Course(resultCourse.getInt(1),resultCourse.getString(2),resultCourse.getInt(3),resultCourse.getInt(4),resultCourse.getInt(5));
        else throw new InputException("There is no course with id " + id);

        return course;
    }

    @Override
    public List<Course> findAll() throws SQLException, InputException {
        String query = "select * from course";
        Statement statement = connection.createStatement();
        ResultSet resultCourse = statement.executeQuery(query);

        List<Course> courseList = new ArrayList<>();

        while (resultCourse.next()){
            Course course = new Course(resultCourse.getInt(1),resultCourse.getString(2),resultCourse.getInt(3),resultCourse.getInt(4),resultCourse.getInt(5));
            courseList.add(course);
        }

        if (courseList.isEmpty())
            throw new InputException("There are no courses in the database");

        return courseList;
    }

    @Override
    public Course save(Course course) throws NullException, SQLException {
        if(course == null)
            throw new NullException("The course to be saved is null");

        try {
            Course temp = findOne(course.getId());
            if (temp != null)
                throw new InputException("course with id " + course.getId() + " already exists in database");
        } catch(Exception e){
            //ok, proceed further
        }

        String insertQuery = "insert into course(id,name,teacherId,maxenrollment,credits) values ('%d','%s','%d','%d','%d')".formatted(course.getId(), course.getName(), course.getTeacherId(), course.getMaxEnrollment(), course.getCredits());

        Statement statement = connection.createStatement();
        statement.executeUpdate(insertQuery);

        return null;
    }

    @Override
    public Course update(Course course) throws NullException, SQLException, InputException {
        if(course == null)
            throw new NullException("The course update object is null");

        if (findOne(course.getId()) == course)
            return course;

        String updateQuery = "update course set name = '%s', teacherId = '%d', maxEnrollment = '%d', credits = '%d' where id = '%d'".formatted(course.getName(), course.getTeacherId(), course.getMaxEnrollment(), course.getCredits(), course.getId());

        Statement statement = connection.createStatement();
        statement.executeUpdate(updateQuery);

        return null;
    }

    @Override
    public Course delete(Integer id) throws NullException, SQLException, InputException {
        if(id == null)
            throw new NullException("The course delete id is null");

        if ( findOne(id) == null)
            return null;

        Course oldCopy = findOne(id);

        String deleteQuery = "delete from course where id = '"+id+"'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteQuery);

        return oldCopy;
    }
}
