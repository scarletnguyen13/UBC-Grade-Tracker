package model;

import java.io.Serializable;

/**
 * Represents a pair of a course and one of its component.
 */
public class CoursePair<N, C> implements Serializable {
    public final Course course;
    public final CourseComponent component;

    public CoursePair(Course course, CourseComponent component) {
        this.course = course;
        this.component = component;
    }

    @Override
    public String toString() {
        return course.getName();
    }

    // EFFECTS: returns true if both fields are null, false otherwise
    public boolean isEmpty() {
        return course == null && component == null;
    }
}
