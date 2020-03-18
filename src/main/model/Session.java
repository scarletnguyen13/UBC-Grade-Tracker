package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a UBC session such as 2018S, 2019W, etc.
 */
public class Session implements Serializable {
    private int year;
    private SessionType type;
    private HashMap<Course, String> courseTermPair;

    public Session(int year, SessionType type, HashMap<Course, String> courseTermPair) {
        this.year = year;
        this.type = type;
        this.courseTermPair = courseTermPair;
    }

    public Session(int year, SessionType type) {
        this(year, type, new HashMap<>());
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public void setCourseTermPair(HashMap<Course, String> terms) {
        this.courseTermPair = terms;
    }

    public HashMap<Course, String> getCourseTermPair() {
        return courseTermPair;
    }

    // MODIFIES: this
    // EFFECTS:  adds the given term to the current term list
    public void addPair(Course course, String term) {
        this.courseTermPair.put(course, term);
    }

    // MODIFIES: this
    // EFFECTS:  removes the given term from the current term list
    public void removeCourse(Course course) {
        this.courseTermPair.remove(course);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return year == session.year
                && type == session.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, type);
    }

    @Override
    public String toString() {
        String type = this.type == SessionType.SUMMER_SESSION ? "S" : "W";
        return year + "" + type;
    }
}
