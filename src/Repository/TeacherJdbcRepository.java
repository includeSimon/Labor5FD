package Repository;

import Exceptions.InputException;
import Exceptions.NullException;
import Model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherJdbcRepository implements ICrudRepository<Teacher>{
    private final Connection connection;

    public TeacherJdbcRepository() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab5", "root", "");
    }

    @Override
    public Teacher findOne(Integer id) throws NullException, SQLException, InputException {
        if (id == null)
            throw new NullException("Id must not be null");

        Teacher teacher = null;

        String teacherQuery = "select t.id, t.firstName, t.lastName from teacher t " +
                "where t.id = " + id;

        Statement statement = connection.createStatement();
        ResultSet resultTeacher = statement.executeQuery(teacherQuery);

        if (resultTeacher.next())
            teacher = new Teacher(resultTeacher.getInt(1),resultTeacher.getString(2),resultTeacher.getString(3));
        else throw new InputException("There is no teacher with id " + id);

        return teacher;
    }

    @Override
    public List<Teacher> findAll() throws SQLException, InputException {
        String query = "select * from teacher";
        Statement statement = connection.createStatement();
        ResultSet resultTeacher = statement.executeQuery(query);

        List<Teacher> teacherList = new ArrayList<>();

        while (resultTeacher.next()){
            Teacher teacher = new Teacher(resultTeacher.getInt(1),resultTeacher.getString(2),resultTeacher.getString(3));
            teacherList.add(teacher);
        }

        if (teacherList.isEmpty())
            throw new InputException("There are no teachers in the database");

        return teacherList;
    }

    @Override
    public Teacher save(Teacher teacher) throws NullException, SQLException {
        if(teacher == null)
            throw new NullException("The teacher to be saved is null");

        try {
            Teacher temp = findOne(teacher.getId());
            if (temp != null)
                throw new InputException("teacher with id " + teacher.getId() + " already exists in database");
        } catch(Exception e){
            //ok, proceed further
        }

        String insertQuery = "insert into teacher(id,firstName,lastName) values ('%d','%s','%s')".formatted(teacher.getId(), teacher.getFirstName(), teacher.getLastName());

        Statement statement = connection.createStatement();
        statement.executeUpdate(insertQuery);

        return null;
    }

    @Override
    public Teacher update(Teacher teacher) throws NullException, SQLException, InputException {
        if(teacher == null)
            throw new NullException("The teacher update object is null");

        if (findOne(teacher.getId()) == teacher)
            return teacher;

        String updateQuery = "update teacher set firstName = '%s', lastName = '%s' where id = '%d'".formatted(teacher.getFirstName(), teacher.getLastName(), teacher.getId());

        Statement statement = connection.createStatement();
        statement.executeUpdate(updateQuery);

        return null;
    }

    @Override
    public Teacher delete(Integer id) throws NullException, SQLException, InputException {
        if(id == null)
            throw new NullException("The teacher delete id is null");

        if ( findOne(id) == null)
            return null;

        Teacher oldCopy = findOne(id);

        String deleteQuery = "delete from teacher where id = '"+id+"'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteQuery);

        return oldCopy;
    }
}
