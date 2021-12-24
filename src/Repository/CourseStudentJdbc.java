package Repository;

import Exceptions.NullException;
import java.sql.*;


public class CourseStudentJdbc{
    private final Connection connection;

    public CourseStudentJdbc() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab5", "root", "");
    }

    /**
     * returns the id of pair<courseId,studentId>
     *
     * @param courseId id of course
     * @param studentId id of enrolled student in course
     * @return id of pair if it exists, -1 otherwise
     * @throws NullException if either of the two ids are null

     */
    public Integer findOne(Integer courseId, Integer studentId) throws NullException {
        if (courseId == null || studentId == null)
            throw new NullException("Id must not be null");
        int id = -1;

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select id, courseId, studentId from coursestudentenrolled where courseId = " + courseId
                    + " and studentId = " + studentId);

        while (result.next()) {
            id = result.getInt("id");
            }
        }
        catch (Exception e){
            return -1;
        }

        return id;
    }

    /**
     * saves a pair of integers to enrolled students table
     *
     * @param courseId course id
     * @param studentId student id
     * @return  null if pair is saved, -1 if pair already exists
     * @throws NullException because we use findOne method
     * @throws SQLException necessary for sql statements
     */
    public Integer save(Integer courseId, Integer studentId) throws NullException,  SQLException {
        if (findOne(courseId,studentId) != -1)
            return -1;

        Statement statement = connection.createStatement();
        //get highest id
        int maxid = 1;
        ResultSet result = statement.executeQuery("select max(id) from coursestudentenrolled");
        while (result.next())
            maxid = result.getInt(1);

        maxid += 1;
        statement.executeUpdate("insert into coursestudentenrolled values (" + maxid + "," + courseId + " ," + studentId + ")");

        return null;
    }

    /**
     * removes a pair from coursestudentenrolled
     *
     * @param id id of pair
     * @return null if it has been successfully deleted, -1 if it doesn't exist
     * @throws SQLException necessary for sql statements
     */
    public Integer delete(Integer id) throws SQLException{
        if(id == null)
            return -1;


        String deleteQuery = "delete from coursestudentenrolled where id = '"+id+"'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteQuery);

        return null;
    }
}
