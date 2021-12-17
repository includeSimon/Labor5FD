package Model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person{
    public List<Course> courses;
    private int id;

    public Teacher(int id, String firstName, String lastName) {
        super(firstName, lastName);
        this.courses = new ArrayList<>();
        this.id=id;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course){this.courses.add(course);}

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  " id= " + id +
                " firstName= " + firstName +
                " lastName= " + lastName +
                " courses= " + courses;
    }

    /**
     * Method to check if two objects of type Teacher have the same id
     *
     * @param other the other Object that is compared
     * @return true objects are identical, false otherwise
     */
    public boolean equals(Teacher other) {
        return this.id ==  other.getId();
    }

}
