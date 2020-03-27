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
    private String term;
    private Grade finalGrade;

    public Course(String name, String section,
                  Instructor instructor,
                  Set<CourseComponent> components) {
        this.name = name;
        this.section = section;
        this.instructor = instructor;
        this.components = components;
        this.finalGrade = new Grade(0.0, 100.0);
    }

    public Course(String name, String section) {
        this(name, section, new Instructor(), new HashSet<>());
    }

    public Course(String name, Set<CourseComponent> components, String term, Session session) {
        this(name, "", new Instructor(), components);
        this.term = term;
        this.session = session;
    }

    public Set<CourseComponent> getComponents() {
        return components;
    }

    public void setComponents(Set<CourseComponent> components) {
        this.components = components;
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

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Grade getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Grade finalGrade) {
        this.finalGrade = finalGrade;
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

    // EFFECTS: returns the component given its name, null if not found
    public CourseComponent findComponentByName(String name) {
        for (CourseComponent component : this.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }

    // EFFECTS: return the estimated overall grade so far in this course
    public double getEstimatedGrade() {
        double gradeAchieved = 0.0;
        double overallGrade = 0.0;
        for (CourseComponent component : this.components) {
            if (component.getMaxMark() != 0.0) {
                gradeAchieved += component.getTotalMarkGained() / component.getMaxMark() * component.getPercentage();
            }
            if (component.getTotalMarkGained() != 0.0) {
                overallGrade += component.getPercentage();
            }
        }
        if (overallGrade != 0.0) {
            return gradeAchieved / overallGrade * 100.0;
        } else {
            return 0.0;
        }
    }
}
