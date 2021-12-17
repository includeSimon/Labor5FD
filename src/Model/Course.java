package Model;

import Exceptions.SizeException;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private int teacherId;
    private int maxEnrollment;
    private List<Student> studentsEnrolled;
    private int credits;
    private int id;

    public Course(int id, String name, int teacherId, int maxEnrollment, int credits) {
        this.name = name;
        this.teacherId = teacherId;
        this.maxEnrollment = maxEnrollment;
        this.studentsEnrolled= new ArrayList<>();
        this.credits = credits;
        this.id=id;
    }

    public void enrollStudent(Student student) throws SizeException {
        if (studentsEnrolled.size() == maxEnrollment)
            throw new SizeException("The course " + toString() + " has the maximum number of students enrolled");

        studentsEnrolled.add(student);
    }

    public String getName() {return name; }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int id) {
        this.teacherId = id;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<Student> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(List<Student> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  " id= " + id+
                " courseName= " + name +
                " teacherId= " + teacherId +
                " maxEnrollment= " + maxEnrollment +
                " credits= " + credits +
                " studentsEnrolled= " + studentsEnrolled;
    }

    /**
     * Method to check if two objects of type class have the same id
     *
     * @param other the other object that is compared
     * @return true if objects are identical, false otherwise
     */
    public boolean equals(Course other) {
        return this.id == other.getId()
                && this.getName() == other.getName()
                && this.getTeacherId() == other.getTeacherId()
                && this.getCredits() == other.getCredits()
                && this.getMaxEnrollment() == other.getMaxEnrollment()
                && this.getStudentsEnrolled() == other.getStudentsEnrolled();
    }

}
