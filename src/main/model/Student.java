package model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a UBC student - also the main user of the app.
 */
public class Student implements Serializable {
    private String name;
    private String studentId;
    private String csId;
    private String email;
    private String phone;

    private String gpa;
    private Set<Session> sessions;

    public Student(String name, String studentId, String csId,
                   String email, String phone, String gpa, Set<Session> sessions) {
        this.name = name;
        this.studentId = studentId;
        this.csId = csId;
        this.email = email;
        this.phone = phone;
        this.gpa  = gpa;
        this.sessions = sessions;
    }

    public Student(String name, String studentId, Set<Session> sessions) {
        this(name, studentId, "", "", "", "", sessions);
    }

    public Student() {
        this("", "", "", "", "", "", new HashSet<>());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public String getCsId() {
        return csId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getGpa() {
        return gpa;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    // EFFECTS: returns all the courses the student is taking / has taken so far in UBC
    public Set<Course> getAllCourses() {
        Set<Course> courses = new HashSet<>();
        for (Session session : this.sessions) {
            courses.addAll(session.getCourseTermPair().keySet());
        }
        return courses;
    }

    // EFFECTS: returns the session with given year and type, if not found creates and returns a new session
    public Session findSession(int year, SessionType type) {
        Session session = new Session(year, type);
        for (Session s: this.sessions) {
            if (session.equals(s)) {
                return s;
            }
        }
        return session;
    }

    // MODIFIES: this
    // EFFECTS: add the session the the list of sessions
    public void addSession(Session session) {
        this.sessions.add(session);
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\n"
                + "Student ID: " + this.studentId + "\n"
                + "Sessions: " + this.sessions + "\n";
    }
}
