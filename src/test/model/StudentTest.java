package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Student class.
 */
class StudentTest {
    private Student student;
    private Set<Session> sessions;
    private Session session1;
    private Session session2;

    private HashMap<Course, String> winterCourses;
    private HashMap<Course, String> summerCourses;

    @BeforeEach
    void runBefore() {
        HashMap<Course, String> term1Courses = new HashMap<>();
        term1Courses.put(new Course("MATH100", "L35"), "Term 1");
        term1Courses.put(new Course("BIOL111", "L35"), "Term 1");
        term1Courses.put(new Course("ASTR101", "L35"), "Term 1");
        term1Courses.put(new Course("ECON101", "L35"), "Term 1");
        term1Courses.put(new Course("CPSC110", "L35"), "Term 1");

        HashMap<Course, String> term2Courses = new HashMap<>();
        term2Courses.put(new Course("CPSC121", "L35"), "Term 2");
        term2Courses.put(new Course("CPSC210", "L35"), "Term 2");
        term2Courses.put(new Course("MATH101", "L35"), "Term 2");
        term2Courses.put(new Course("DSCI100", "L35"), "Term 2");

        winterCourses = new HashMap<>();
        winterCourses.putAll(term1Courses);
        winterCourses.putAll(term2Courses);

        summerCourses = new HashMap<>();
        summerCourses.put(new Course("CPSC310", "L35"), "Summer Term");
        summerCourses.put(new Course("ASTR102", "L35"), "Summer Term");
        summerCourses.put(new Course("ECON102", "L35"), "Summer Term");

        sessions = new HashSet<>();
        session1 = new Session(2019, SessionType.SUMMER_SESSION, winterCourses);
        session2 = new Session(2018, SessionType.WINTER_SESSION, summerCourses);
        sessions.add(session1);
        sessions.add(session2);

        student = new Student("Scarlet Nguyen", "44304491", sessions);
    }

    @Test
    void testInit1() {
        student = new Student(
                "Scarlet Nguyen", "44304491", "d6x2b",
                "scarlet.nguyen01@gmail.com", "(604) 369-9123",
                "3.8", sessions, new TreeSet<>()
        );

        assertEquals("Scarlet Nguyen", student.getName());
        assertEquals("44304491", student.getStudentId());
        assertEquals("d6x2b", student.getCsId());
        assertEquals("scarlet.nguyen01@gmail.com", student.getEmail());
        assertEquals("(604) 369-9123", student.getPhone());
        assertEquals("3.8", student.getGpa());
        assertEquals(sessions, student.getSessions());
        assertEquals(new TreeSet<>(), student.getTodoList());
    }

    @Test
    void testInit2() {
        assertEquals("Scarlet Nguyen", student.getName());
        assertEquals("44304491", student.getStudentId());
        assertTrue(student.getCsId().isEmpty());
        assertTrue(student.getEmail().isEmpty());
        assertTrue(student.getPhone().isEmpty());
        assertEquals("", student.getGpa());
        assertEquals(sessions, student.getSessions());
    }

    @Test
    void testInit3() {
        this.student = new Student();
        assertTrue(student.getName().isEmpty());
        assertTrue(student.getStudentId().isEmpty());
        assertTrue(student.getCsId().isEmpty());
        assertTrue(student.getEmail().isEmpty());
        assertTrue(student.getPhone().isEmpty());
        assertTrue(student.getGpa().isEmpty());
        assertEquals(0, student.getSessions().size());
    }

    @Test
    void testSetters() {
        student = new Student("Scarlet Nguyen", "443", new HashSet<>());

        student.setName("Scarlet");
        student.setStudentId("44304491");
        student.setCsId("d6x2b");
        student.setEmail("scarlet.nguyen01@gmail.com");
        student.setPhone("(604) 369-9123");
        student.setGpa("3.8");
        student.setSessions(sessions);

        assertEquals("Scarlet", student.getName());
        assertEquals("44304491", student.getStudentId());
        assertEquals("d6x2b", student.getCsId());
        assertEquals("scarlet.nguyen01@gmail.com", student.getEmail());
        assertEquals("(604) 369-9123", student.getPhone());
        assertEquals("3.8", student.getGpa());
        assertEquals(sessions, student.getSessions());
    }

    @Test
    void testGetAllCourses() {
        assertEquals(12, student.getAllCourses().size());
    }

    @Test
    void testAddSession() {
        assertEquals(2, this.student.getSessions().size());
        this.student.addSession(new Session(2020, SessionType.WINTER_SESSION, winterCourses));
        assertEquals(3, this.student.getSessions().size());
        this.student.addSession(new Session(2020, SessionType.WINTER_SESSION, summerCourses));
        assertEquals(3, this.student.getSessions().size());

        this.student.setSessions(new HashSet<>());
        this.student.addSession(new Session(2020, SessionType.WINTER_SESSION));
        assertEquals(0, this.student.getSessions().size());
        assertTrue(this.student.getCurrentSession().isEmpty());

        this.student.addSession(session1);
        assertEquals(session1, this.student.getCurrentSession());
    }

    @Test
    void testRemoveSession() {
        Session session3 = new Session(2020, SessionType.WINTER_SESSION, winterCourses);
        this.student.addSession(session3);

        assertEquals(3, this.student.getSessions().size());
        this.student.setCurrentSession(session1);
        assertEquals(session1, this.student.getCurrentSession());

        student.removeSession(session1);
        assertEquals(2, this.student.getSessions().size());
        assertEquals(session3, this.student.getCurrentSession());

        student.removeSession(session3);
        assertEquals(1, this.student.getSessions().size());
        assertEquals(session2, this.student.getCurrentSession());

        student.removeSession(session2);
        assertEquals(0, this.student.getSessions().size());
        assertEquals(new Session(), this.student.getCurrentSession());
    }

    @Test
    void testToString() {
        String expectedResult = "Name: " + this.student.getName() + "\n"
                + "Student ID: " + this.student.getStudentId() + "\n"
                + "Sessions: " + this.sessions + "\n";
        assertEquals(expectedResult, student.toString());
    }

    @Test
    void testFindSession() {
        Session session = this.student.findSession(2019, SessionType.SUMMER_SESSION);
        assertEquals(session1, session);

        session = this.student.findSession(2018, SessionType.WINTER_SESSION);
        assertEquals(session2, session);

        session = this.student.findSession(2020, SessionType.WINTER_SESSION);
        assertEquals(new Session(2020, SessionType.WINTER_SESSION), session);
    }

    @Test
    void testAddTodoItem() {
        Course course = new Course("CPSC 210", "201");
        CourseComponent component = new CourseComponent("Quizzes", 40);
        CoursePair coursePair = new CoursePair(course, component);

        TodoItem item = new TodoItem("Quiz #1", coursePair, new Grade(0.0, 0.0));

        CoursePair coursePair2 = new CoursePair(
                new Course("CPSC 110", "L21"),
                new CourseComponent("Homework", 20)
        );
        TodoItem item2 = new TodoItem("Homework #2", coursePair2, new Grade(0.0, 0.0));

        assertTrue(student.getTodoList().isEmpty());
        student.addTodoItem(item);
        assertEquals(1, student.getTodoList().size());

        // add again
        student.addTodoItem(item);
        assertEquals(1, student.getTodoList().size());

        student.addTodoItem(item2);
        assertEquals(2, student.getTodoList().size());
    }

    @Test
    void testRemoveTodoItem() {
        CoursePair coursePair1 = new CoursePair(null, null);
        TodoItem item1 = new TodoItem("Quiz #1", coursePair1, new Grade(0.0, 0.0));

        CourseComponent component = new CourseComponent("Homework", 20);
        component.setTotalMarkGained(80);
        component.setMaxMark(100);
        CoursePair coursePair2 = new CoursePair(
                new Course("CPSC 110", "L21"),
                component
        );
        TodoItem item2 = new TodoItem("Homework #2", coursePair2, new Grade(40.0, 60.0));

        student.addTodoItem(item1);
        student.addTodoItem(item2);

        assertEquals(2, student.getTodoList().size());
        student.removeTodoItem(item1);
        assertEquals(1, student.getTodoList().size());
        student.removeTodoItem(item1);
        assertEquals(1, student.getTodoList().size());
        student.removeTodoItem(item2);
        assertEquals(0, student.getTodoList().size());
        assertEquals(40.0, component.getTotalMarkGained());
        assertEquals(40.0, component.getMaxMark());

        HashSet<TodoItem> items = new HashSet<>();
        items.add(item1);
        items.add(item2);
        student.setTodoList(items);
    }
}