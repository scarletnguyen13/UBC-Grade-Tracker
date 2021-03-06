package model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a UBC session such as 2018S, 2019W, etc.
 */
public class Session implements Serializable, Comparable<Session> {
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

    public Session() {
        this(0, SessionType.SUMMER_SESSION);
    }

    public boolean isEmpty() {
        return this.year == 0;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public HashMap<Course, String> getCourseTermPair() {
        return courseTermPair;
    }

    public void setCourseTermPair(HashMap<Course, String> terms) {
        this.courseTermPair = terms;
    }

    // MODIFIES: this
    // EFFECTS:  adds the given course to the current course list
    public void addPair(Course course, String term) {
        this.courseTermPair.put(course, term);
    }

    // MODIFIES: this
    // EFFECTS:  removes the given course from the current course list
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

    @Override
    public int compareTo(Session other) {
        return Comparator
                .comparing(Session::toString, Comparator.nullsFirst(Comparator.reverseOrder()))
                .compare(this, other);
    }
}
