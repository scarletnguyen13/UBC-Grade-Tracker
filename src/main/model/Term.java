package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a UBC term.
 */
public class Term implements Serializable {
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
        return this.name;
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
    // and removes the term from its session if the term is empty
    public void removeCourse(Course course) {
        this.courses.remove(course);
        if (this.courses.size() == 0) {
            Session session = course.getSession();
            session.removeTerm(this);
        }
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    @Override
    public String toString() {
        return name;
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
        return name.equals(term.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
