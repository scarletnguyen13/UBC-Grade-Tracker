package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Term class.
 */
public class TermTest {
    private static final String TERM_NAME = "Term";
    private Term term;
    private Set<Course> courses;

    @BeforeEach
    void runBefore() {
        courses = new HashSet<>();
        courses.add(new Course("CPSC121"));
        courses.add(new Course("CPSC210"));
        courses.add(new Course("MATH101"));
        courses.add(new Course("DSCI100"));

        term = new Term(TERM_NAME);
    }

    @Test
    void testInit1() {
        term = new Term(TERM_NAME, courses);

        assertEquals(TERM_NAME, term.getName());
        assertEquals(courses, term.getCourses());
    }

    @Test
    void testInit2() {
        assertEquals(TERM_NAME, term.getName());
        assertTrue(term.getCourses().isEmpty());
    }

    @Test
    void testSetters() {
        term.setName("Term 1");
        term.setCourses(courses);

        assertEquals("Term 1", term.getName());
        assertEquals(courses, term.getCourses());
    }

    @Test
    void testAddCourse() {
        assertEquals(0, term.getCourses().size());

        Course cpsc210 = new Course("CPSC210");
        term.addCourse(cpsc210);

        assertEquals(1, term.getCourses().size());
        assertTrue(term.getCourses().contains(cpsc210));

        // Add again, check for duplication
        term.addCourse(cpsc210);
        assertEquals(1, term.getCourses().size());

        Course cpsc210Duplicated = new Course("CPSC210");
        term.addCourse(cpsc210Duplicated);
        assertEquals(1, term.getCourses().size());

        Course cpsc121 = new Course("CPSC121");
        term.addCourse(cpsc121);
        assertEquals(2, term.getCourses().size());
    }

    @Test
    void testRemoveTerm() {
        assertEquals(0, term.getCourses().size());

        Course cpsc210 = new Course("CPSC210");
        Course cpsc121 = new Course("CPSC121");

        term.addCourse(cpsc210);
        term.addCourse(cpsc121);

        assertEquals(2, term.getCourses().size());

        term.removeCourse(cpsc210);
        assertEquals(1, term.getCourses().size());
        assertTrue(term.getCourses().contains(cpsc121));
        assertFalse(term.getCourses().contains(cpsc210));
    }

    @Test
    void testToString() {
        String result = term.getName();
        assertEquals(result, term.toString());
    }

    @Test
    void testEquals() {
        term = new Term(TERM_NAME, courses);

        assertTrue(term.equals(term));
        assertTrue(term.equals(new Term(TERM_NAME, courses)));

        assertFalse(term.equals(new Instructor()));
        assertFalse(term.equals(new Term(TERM_NAME)));
        assertFalse(term.equals(null));
        assertFalse(term.equals(new Term("Term 1", courses)));
    }
}
