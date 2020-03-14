package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Course class.
 */
public class CourseTest {

    private Course course;
    private Set<CourseComponent> courseComponents;
    private Term term;
    private Session session;

    @BeforeEach
    void runBefore() {
        term = new Term("Term 1");
        Set<Term> terms = new HashSet<>();
        terms.add(term);
        terms.add(new Term("Term 2"));

        session = new Session(2013, SessionType.WINTER_SESSION, terms);

        courseComponents = new HashSet<>();
        courseComponents.add(new CourseComponent("Homework", 20));
        courseComponents.add(new CourseComponent("Quizzes", 30));
        courseComponents.add(new CourseComponent("Exams", 50));
    }

    @Test
    void testInit1() {
        course = new Course("CPSC 121", "L56",
                new Instructor("Dirk"), courseComponents);

        assertEquals("CPSC 121", course.getName());
        assertEquals("L56", course.getSection());
        assertEquals(new Instructor("Dirk"), course.getInstructor());
        assertEquals(courseComponents, course.getComponents());

        course.setTerm(term);
        course.setSession(session);
        assertEquals(term, course.getTerm());
        assertEquals(session, course.getSession());
    }

    @Test
    void testInit2() {
        course = new Course("CPSC 210", "L13");

        assertEquals("CPSC 210", course.getName());
        assertEquals("L13", course.getSection());
        assertEquals(new Instructor(), course.getInstructor());
        assertEquals(new HashSet<>(), course.getComponents());
    }

    @Test
    void testInit3() {
        course = new Course("CPSC 110");

        assertEquals("CPSC 110", course.getName());
        assertTrue(course.getSection().isEmpty());
        assertTrue(course.getInstructor().isEmpty());
        assertTrue(course.getComponents().isEmpty());
    }

    @Test
    void testInit4() {
        course = new Course("CPSC 210", courseComponents, term, session);

        assertEquals("CPSC 210", course.getName());
        assertTrue(course.getSection().isEmpty());
        assertTrue(course.getInstructor().isEmpty());
        assertEquals(courseComponents, course.getComponents());
        assertEquals(term, course.getTerm());
        assertEquals(session, course.getSession());
    }

    @Test
    void testSetters() {
        course = new Course("CPSC121", "L56");

        assertEquals("CPSC121", course.getName());
        assertEquals("L56", course.getSection());

        course.setName("CPSC210");
        course.setSection("L46");
        course.setInstructor(new Instructor("VandePol"));
        course.setComponents(courseComponents);

        assertEquals("CPSC210", course.getName());
        assertEquals("L46", course.getSection());
        assertEquals(new Instructor("VandePol"), course.getInstructor());
        assertEquals(courseComponents, course.getComponents());
    }

    @Test
    void testToString() {
        course = new Course("CPSC210", "L13");
        assertEquals("CPSC210", course.toString());
    }

    @Test
    void testEquals() {
        course = new Course("CPSC210", "L13");

        assertTrue(course.equals(course));
        assertTrue(course.equals(new Course("CPSC210", "L53")));

        assertFalse(course.equals(new Instructor("Dirk")));
        assertFalse(course.equals(null));
        assertFalse(course.equals(new Course("ECON110", "L53")));
    }
}
