package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private int totalCredits = 0;
    private List<Course> enrolledCourses;
    private int id;

    public Student(int id, String firstName, String lastName) {
        super(firstName, lastName);
        this.enrolledCourses = new ArrayList<>();
        this.id = id;
    }

    public void addCourse(Course course) throws IOException{
        if (enrolledCourses.contains(course))
            throw new IOException();

        enrolledCourses.add(course);
        totalCredits += course.getCredits();
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return " First name= " + firstName +
                " last name= " + lastName +
                " studentId= " + id +
                " totalCredits= " + totalCredits;
    }

    /**
     * Method to check if two objects of type Student have the same id
     *
     * @param other the other object that is compared
     * @return true if the objects match, else false
     */
    public boolean equals(Student other) {
        return this.id == other.getId();
    }
}
