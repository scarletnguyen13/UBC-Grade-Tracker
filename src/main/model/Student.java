package model;

import java.util.HashSet;
import java.util.Set;

/*
 * Represents a UBC student - also the main user of the app.
 */
public class Student {
    private String name;
    private String studentId;
    private String csId;
    private String email;
    private String phone;

    private Double gpa;
    private Set<Session> sessions;

    public Student(String name, String studentId, String csId,
                   String email, String phone, Double gpa, Set<Session> sessions) {
        this.name = name;
        this.studentId = studentId;
        this.csId = csId;
        this.email = email;
        this.phone = phone;
        this.gpa  = gpa;
        this.sessions = sessions;
    }

    public Student(String name, String studentId, Set<Session> sessions) {
        this(name, studentId, "", "", "", 0.0, sessions);
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

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    // EFFECTS:  returns all the courses the student is taking / has taken so far in UBC
    public Set<Course> getAllCourses() {
        Set<Course> courses = new HashSet<>();
        for (Session session : this.sessions) {
            for (Term term : session.getTerms()) {
                courses.addAll(term.getCourses());
            }
        }
        return courses;
    }

    // REQUIRES: updated course must exist in the current course list
    // MODIFIES: this
    // EFFECTS: replaces the updated course with the existing course
    public void editCourse(Course updatedCourse) {
        Term oldTerm = new Term("");
        Term newTerm = new Term("");

        Session oldSession = null;
        Session newSession = null;

        for (Session session : this.sessions) {
            oldSession = session.copy();
            newSession = session.copy();

            for (Term term : session.getTerms()) {
                oldTerm = term.copy();
                newTerm = term.copy();

                for (Course course : getAllCourses()) {
                    if (course.equals(updatedCourse)) {
                        newTerm.addCourse(updatedCourse);
                    }
                    oldTerm.addCourse(course);
                }
            }
        }

        newSession.removeTerm(oldTerm);
        newSession.addTerm(newTerm);

        this.sessions.remove(oldSession);
        this.sessions.add(newSession);
    }

    public Course findCourse(String name) {
        Course course = null;
        for (Course c: this.getAllCourses()) {
            if (c.getName().equals(name)) {
                course = c;
            }
        }
        return course;
    }

    @Override
    public String toString() {
        return "Student{"
                + "name='" + name + "\'\n"
                + ", studentId=" + studentId + "\'\n"
                + ", csId='" + csId + "\'\n"
                + ", email='" + email + "\'\n"
                + ", phone='" + phone + "\'\n"
                + ", gpa=" + gpa + "\'\n"
                + ", sessions=" + sessions + "\'\n"
                + '}';
    }
}
