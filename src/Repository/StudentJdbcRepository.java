package Repository;

import Exceptions.InputException;
import Exceptions.NullException;
import Model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentJdbcRepository implements ICrudRepository<Student>{
    private final Connection connection;

    public StudentJdbcRepository() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab5", "root", "");
    }

    @Override
    public Student findOne(Integer id) throws NullException, SQLException, InputException {
        if (id == null) throw new NullException("Id must not be null");

        Student student = null;

        String studentQuery = "select s.id, s.firstName, s.lastName, s.totalCredits from student s " +
                "where s.id = " + id;

        Statement statement = connection.createStatement();
        ResultSet resultStudent = statement.executeQuery(studentQuery);

        if (resultStudent.next())
            student = new Student(resultStudent.getInt(1),resultStudent.getString(2),resultStudent.getString(3));
        else throw new InputException("There is no student with id " + id);

        return student;
    }

    @Override
    public List<Student> findAll() throws SQLException, InputException {
        String query = "select * from student";
        Statement statement = connection.createStatement();
        ResultSet resultStudent = statement.executeQuery(query);

        List<Student> studentList = new ArrayList<>();

        while (resultStudent.next()){
            Student student = new Student(resultStudent.getInt(1),resultStudent.getString(2),resultStudent.getString(3));
            studentList.add(student);
        }

        if (studentList.isEmpty())
            throw new InputException("There are no students in the database");

        return studentList;
    }

    @Override
    public Student save(Student student) throws NullException, SQLException {
        if(student == null)
            throw new NullException("The Student to be saved is null");

        try {
            Student temp = findOne(student.getId());
            if (temp != null)
                throw new InputException("Student with id " + student.getId() + " already exists in database");
        } catch(Exception e){
            //ok, proceed further
        }

        String insertQuery = "insert into student(id,firstName,lastName,totalCredits) values ('%d','%s','%s','%d')".formatted(student.getId(), student.getFirstName(), student.getLastName(), student.getTotalCredits());

        Statement statement = connection.createStatement();
        statement.executeUpdate(insertQuery);

        return null;
    }


    @Override
    public Student update(Student student) throws NullException, SQLException, InputException {
        if(student == null)
            throw new NullException("The Student update object is null");

        if (findOne(student.getId()) == student)
            return student;

        String updateQuery = "update student set firstName = '%s', lastName = '%s', totalCredits = '%d' where id = '%d'".formatted(student.getFirstName(), student.getLastName(), student.getTotalCredits(), student.getId());

        Statement statement = connection.createStatement();
        statement.executeUpdate(updateQuery);

        return null;
    }

    @Override
    public Student delete(Integer id) throws NullException, SQLException, InputException {
        if(id == null)
            throw new NullException("The student delete id is null");

        if ( findOne(id) == null)
            return null;

        Student oldCopy = findOne(id);

        String deleteQuery = "delete from student where id = '"+id+"'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteQuery);

        return oldCopy;
    }
}
