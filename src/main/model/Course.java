package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a UBC Course.
 */
public class Course implements Serializable {
    private String name;
    private String section;
    private Instructor instructor;
    private Set<CourseComponent> components;
    private Session session;
    private Term term;

    public Course(String name, String section,
                  Instructor instructor,
                  Set<CourseComponent> components) {
        this.name = name;
        this.section = section;
        this.instructor = instructor;
        this.components = components;
    }

    public Course(String name, String section) {
        this(name, section, new Instructor(), new HashSet<>());
    }

    public Course(String name) {
        this(name, "", new Instructor(), new HashSet<>());
    }

    public Course(String name, Set<CourseComponent> components, Term term, Session session) {
        this(name, "", new Instructor(), components);
        this.term = term;
        this.session = session;
    }

    public void setComponents(Set<CourseComponent> components) {
        this.components = components;
    }

    public Set<CourseComponent> getComponents() {
        return components;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Term getTerm() {
        return term;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return name.equals(course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
