package Controller;

import Exceptions.InputException;
import Exceptions.NullException;
import Exceptions.SizeException;
import Model.Course;
import Model.Student;
import Model.Teacher;
import Repository.CourseJdbcRepository;
import Repository.CourseStudentJdbc;
import Repository.StudentJdbcRepository;
import Repository.TeacherJdbcRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationSystem {
    private final Connection connection;
    private final StudentJdbcRepository studentRepo;
    private final TeacherJdbcRepository teacherRepo;
    private final CourseJdbcRepository courseRepo;
    private CourseStudentJdbc enrollRepo;

    public RegistrationSystem(StudentJdbcRepository studentRepo, TeacherJdbcRepository teacherRepo, CourseJdbcRepository courseRepo) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab5", "root", "");
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.courseRepo = courseRepo;
    }

    /**
     * Enroll a student to a course
     *
     * @param courseId,  Course (id) where student will be enrolled
     * @param studentId, Student (id) to be enrolled
     * @return true if successfully enrolled, else false
     * @throws NullException if course or student do not exist in repo,
     *                       if student credits exceeds maximum amount of 30 credits,
     *                       if course has no free places or if student is already enrolled
     */
    public boolean register(Integer courseId, Integer studentId) throws SQLException, InputException, NullException, SizeException {
        //get course and student
        Course course = courseRepo.findOne(courseId);
        Student student = studentRepo.findOne(studentId);

        //check if the course provided as a parameter exists in repository
        try {
            if (courseRepo.findOne(courseId) == null) {
                throw new NullException("The course" + course.getName() + " doesn't exist");
            }
        } catch (NullException | SQLException | InputException e) {
            e.printStackTrace();
            return false;
        }

        //check if the student provided as a parameter exists in repository
        try {
            if (studentRepo.findOne(studentId) == null)
                throw new NullException("The student " + student.getFirstName() + " " + student.getLastName() + " doesn't exist");
        } catch (NullException | SQLException | InputException e) {
            e.printStackTrace();
            return false;
        }

        //check if course has free places
        List<Student> enrolledStudents = course.getStudentsEnrolled();

        if (enrolledStudents.size() >= course.getMaxEnrollment())
            throw new SizeException("Course" + course.getName() + " has no free places!");


        //check if student is already enrolled
        try {
            for (Student stud : enrolledStudents)
                if (stud.equals(student))
                    throw new InputException("Student " + student.getFirstName()
                            + " " + student.getLastName() + " is already enrolled");
        } catch (InputException e) {
            System.out.println(e.getMessage());
            return false;
        }

        //if student has over 30 credits after enrolling to this course
        int studentCredits = student.getTotalCredits() + course.getCredits();
        try {
            if (studentCredits > 30)
                throw new InputException("Student " + student.getFirstName()
                        + " " + student.getLastName() + " has exceeded the number of 30 credits");

        } catch (InputException e) {
            System.out.println(e.getMessage());
            return false;
        }

        //update total credits of student
        student.setTotalCredits(studentCredits);

        //add new course to student courses list
        List<Course> studentCourses = student.getEnrolledCourses();
        studentCourses.add(course);
        student.setEnrolledCourses(studentCourses);

        //update course and student
        courseRepo.update(course);
        studentRepo.update(student);

        //add pair to coursestudentenrolled
        enrollRepo.save(courseId, studentId);

        return true;
    }

    /**
     * Find all the courses with free spots
     *
     * @return courses with free places
     */
    public List<Course> retrieveCoursesWithFreePlaces() throws SQLException, InputException {

        return courseRepo.findAll()
                .stream()
                .filter(c -> c.getStudentsEnrolled().size() < c.getMaxEnrollment())
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all students enrolled to a course
     *
     * @param course Course object
     * @return list of students enrolled to the given course, or null if course is NULL
     */
    public List<Student> retrieveStudentsEnrolledForACourse(Course course) {
        /* find course in the course repo */
        try {
            if (courseRepo.findOne(course.getId()) != null) {
                return course.getStudentsEnrolled();
            }
        } catch (NullException | SQLException | InputException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Delete a course from a teacher. Removing course from the teacher's courses list, from the students' enrolled lists and from the course repo
     * Update number of credits of certain students
     *
     * @param teacherId Teacher id from whom we delete a course
     * @param courseId  Course id, from the teacher's list, to be deleted
     * @return true if successfully deleted
     * @throws InputException if teacher or course do not exist in te repo lists,
     *                        or if the course does not correspond to that teacher
     *                        deleting course from the teacher's teaching list, from the students enrolled list and from the courses repo
     */
    public boolean deleteCourseFromTeacher(Integer teacherId, Integer courseId) throws InputException, SQLException, NullException {
        Teacher teacher = teacherRepo.findOne(teacherId);
        Course course = courseRepo.findOne(courseId);

        //check if course exists in repository
        try {
            if (courseRepo.findOne(course.getId()) == null) {
                throw new InputException("Course " + course.getName() + " doesn't exist");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //check if teacher exists in repository
        try {
            if (teacherRepo.findOne(teacher.getId()) == null) {
                throw new InputException("Teacher " + teacher.getFirstName()
                        + " " + teacher.getLastName() + " doesn't exist");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //check if course exists in teacher
        List<Course> teacherCourses = teacher.getCourses();
        if (!teacherCourses.contains(course))
            throw new InputException("Teacher " + teacher.getFirstName() + " "
                    + teacher.getLastName() + " has no course " + course.getName());

        // remove course from teacher
        teacherCourses.remove(course);
        teacher.setCourses(teacherCourses);

        //updating teacher in teacherRepo
        try {
            teacherRepo.update(teacher);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    /**
     * desc: Recalculate the sum of credits provided from the enrolled courses of the students
     * Update the credits sum for each student
     */
    public void updateStudentCredits() throws SQLException, InputException {
        List<Student> studentList = studentRepo.findAll();

        for (Student student : studentList) {
            List<Course> coursesEnrolled = student.getEnrolledCourses();
            int sum = 0;

            for (Course course : coursesEnrolled) {
                //calculate the total sum of credits for each student
                sum += course.getCredits();
            }

            //update the total sum of credits for the student
            student.setTotalCredits(sum);

            //update in the repo
            try {
                studentRepo.update(student);
            } catch (NullException | SQLException | InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * desc: modifying credit number for a course, that leads to updating repo with the updated course and updating students' credits
     *
     * @param c Course object, which credits were updated
     */
    public void modifyCredits(Course c) throws SQLException, InputException {
        /* update course in the repo */
        try {
            this.courseRepo.update(c);
        } catch (NullException | SQLException | InputException e) {
            System.out.println(e.getMessage());
        }

        /*update all students*/
        this.updateStudentCredits();
    }


    /**
     * desc: get all students from the repo
     *
     * @return student list from the student repo
     */
    public List<Student> getAllStudents() throws SQLException, InputException {
        return new ArrayList<>(this.studentRepo.findAll());
    }

    /**
     * desc: get all courses from the repo
     *
     * @return courses list from the course repo
     */
    public List<Course> getAllCourses() throws SQLException, InputException {
        return new ArrayList<>(this.courseRepo.findAll());
    }

    /**
     * get all teachers from the repo
     *
     * @return teachers list from teh teacher repo
     */
    public List<Teacher> getAllTeachers() throws SQLException, InputException {
        return new ArrayList<>(this.teacherRepo.findAll());
    }


    /**
     * searching for a student in the repo by the id
     *
     * @param id of a Student object
     * @return Student object from the student repo list with the given id
     */
    public Student findOneStudent(Integer id) {
        try {
            return this.studentRepo.findOne(id);
        } catch (NullException | SQLException | InputException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    /**
     * desc: searching for a course in the repo by the id
     *
     * @param id of a Course object
     * @return Course object from the course repo list with the given id
     */
    public Course findOneCourse(Integer id) {
        try {
            return this.courseRepo.findOne(id);
        } catch (NullException | SQLException | InputException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * desc: searching for a teacher in the repo by the id
     *
     * @param id of a Teacher object
     * @return Teacher object from the teacher repo list with the given id
     */
    public Teacher findOneTeacher(Integer id) {
        try {
            return this.teacherRepo.findOne(id);
        } catch (NullException | SQLException | InputException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

}
