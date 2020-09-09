package model;

import java.util.*;

/**
 * Represents a UBC student - also the main user of the app.
 */
public class Student extends Person {
    private String studentId;
    private String csId;
    private String phone;

    private String gpa;
    private Session currentSession;
    private Set<Session> sessions;
    private Set<TodoItem> todoList;

    public Student(String name, String studentId, String csId, String email, String phone,
                   String gpa, Set<Session> sessions, Set<TodoItem> todoList) {
        super(name, email);
        this.studentId = studentId;
        this.csId = csId;
        this.phone = phone;
        this.gpa = gpa;
        this.sessions = sessions;
        this.todoList = todoList;
        this.currentSession = new Session();
    }

    public Student(String name, String studentId, Set<Session> sessions) {
        this(name, studentId, "", "", "", "", sessions, new TreeSet<>());
    }

    public Student() {
        this("", "", "", "", "", "", new HashSet<>(), new TreeSet<>());
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public Set<Session> getSessions() {
        return this.sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public List<Session> getSortedSessions() {
        List<Session> sortedList = new ArrayList<>(this.sessions);
        Collections.sort(sortedList);
        return sortedList;
    }

    public Set<TodoItem> getTodoList() {
        return todoList;
    }

    public void setTodoList(Set<TodoItem> todoList) {
        this.todoList = todoList;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    // EFFECTS: returns all the courses the student is taking / has taken so far in UBC
    public Set<Course> getAllCourses() {
        Set<Course> courses = new HashSet<>();
        for (Session session : this.sessions) {
            courses.addAll(session.getCourseTermPair().keySet());
        }
        return courses;
    }

    // EFFECTS: returns all the courses the student is taking at the moment
    public Set<Course> getCurrentSessionCourses() {
        return this.currentSession.getCourseTermPair().keySet();
    }

    // EFFECTS: returns the session with given year and type, if not found creates and returns a new session
    public Session findSession(int year, SessionType type) {
        Session session = new Session(year, type);
        for (Session s : this.sessions) {
            if (session.equals(s)) {
                return s;
            }
        }
        return session;
    }

    // MODIFIES: this
    // EFFECTS: add the session to the list of sessions
    public void addSession(Session session) {
        if (session.getCourseTermPair().size() > 0) {
            this.sessions.add(session);
            if (this.currentSession.isEmpty()) {
                this.currentSession = session;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: remove the session from the list of sessions and replace the current session with another
    // session if the session being removed is the current one
    public void removeSession(Session session) {
        this.sessions.remove(session);
        if (this.currentSession.equals(session)) {
            if (this.getSortedSessions().size() > 0) {
                this.currentSession = this.getSortedSessions().get(0);
            } else {
                this.currentSession = new Session();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: add the item the todolist
    public void addTodoItem(TodoItem item) {
        this.todoList.add(item);
    }

    // MODIFIES: this, component
    // EFFECTS: remove the item from the todolist
    // and reduce the total marked gain and max mark in associated course's component
    public void removeTodoItem(TodoItem item) {
        CourseComponent component = item.getCoursePair().component;
        if (component != null) {
            component.setTotalMarkGained(component.getTotalMarkGained() - item.getGrade().mark);
            component.setMaxMark(component.getMaxMark() - item.getGrade().outOf);
        }
        todoList.remove(item);
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\n"
                + "Student ID: " + this.studentId + "\n"
                + "Sessions: " + this.sessions + "\n";
    }
}
