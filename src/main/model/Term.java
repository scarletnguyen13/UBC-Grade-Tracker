package model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
 * Represents a UBC term.
 */
public class Term {
    private String name;
    private Set<Course> courses;

    public Term(String name, Set<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public Term(String name) {
        this(name, new HashSet<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // MODIFIES: this
    // EFFECTS:  adds the given course to the current course list
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    // MODIFIES: this
    // EFFECTS:  removes the given course from the current course list
    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    // EFFECTS:  returns an instance of this term
    public Term copy() {
        return this;
    }

    @Override
    public String toString() {
        return name + " [" + courses + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Term term = (Term) o;
        return name.equals(term.name)
                && courses.equals(term.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, courses);
    }
}
