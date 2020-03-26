package model;

import java.io.Serializable;

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

    public boolean isEmpty() {
        return course == null && component == null;
    }
}
