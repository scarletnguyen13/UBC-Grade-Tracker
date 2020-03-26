package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Session class.
 */
public class SessionTest {
    private Session session;
    private HashMap<Course, String> terms;

    @BeforeEach
    void runBefore() {
        terms = new HashMap<>();
        terms.put(new Course("CPSC 121", "201"), "Term 1");
        terms.put(new Course("CPSC 210", "201"), "Term 2");
    }

    @Test
    void testInit1() {
        session = new Session(2013, SessionType.WINTER_SESSION, terms);

        assertEquals(2013, session.getYear());
        assertEquals(SessionType.WINTER_SESSION, session.getType());
        assertEquals(terms, session.getCourseTermPair());
    }

    @Test
    void testInit2() {
        session = new Session(2019, SessionType.SUMMER_SESSION);

        assertEquals(2019, session.getYear());
        assertEquals(SessionType.SUMMER_SESSION, session.getType());
        assertEquals(0, session.getCourseTermPair().size());
    }

    @Test
    void testSetters() {
        session = new Session(2019, SessionType.SUMMER_SESSION);

        session.setYear(2014);
        session.setType(SessionType.WINTER_SESSION);
        session.setCourseTermPair(terms);

        assertEquals(2014, session.getYear());
        assertEquals(SessionType.WINTER_SESSION, session.getType());
        assertEquals(terms, session.getCourseTermPair());
    }

    @Test
    void testAddTerm() {
        session = new Session(2019, SessionType.SUMMER_SESSION);
        assertEquals(0, session.getCourseTermPair().size());

        String term1 = "Term 1";
        session.addPair(new Course("CPSC 121", "210"), term1);

        assertEquals(1, session.getCourseTermPair().size());
        assertTrue(session.getCourseTermPair().containsValue(term1));

        // Add again, check for duplication
        session.addPair(new Course("CPSC 121", "210"), term1);
        assertEquals(1, session.getCourseTermPair().size());

        String term1Duplicated = "Term 1";
        session.addPair(new Course("CPSC 121", "210"), term1Duplicated);
        assertEquals(1, session.getCourseTermPair().size());

        String term2 = "Term 2";
        session.addPair(new Course("CPSC 210", "121"), term2);
        assertEquals(2, session.getCourseTermPair().size());
    }

    @Test
    void testRemoveTerm() {
        session = new Session(2019, SessionType.SUMMER_SESSION);
        assertEquals(0, session.getCourseTermPair().size());

        String term1 = "Term 1";
        String term2 = "Term 2";

        Course course1 = new Course("CPSC 121", "121");
        Course course2 = new Course("CPSC 210", "121");

        session.addPair(course1, term1);
        session.addPair(course2, term2);

        assertEquals(2, session.getCourseTermPair().size());

        session.removeCourse(course1);
        assertEquals(1, session.getCourseTermPair().size());
        assertFalse(session.getCourseTermPair().containsKey(course1));
        assertTrue(session.getCourseTermPair().containsKey(course2));
    }

    @Test
    void testEquals() {
        session = new Session(2013, SessionType.WINTER_SESSION, terms);

        assertTrue(session.equals(session));
        assertTrue(session.equals(new Session(2013, SessionType.WINTER_SESSION, terms)));
        assertTrue(session.equals(new Session(2013, SessionType.WINTER_SESSION, new HashMap<>())));

        assertFalse(session.equals(null));
        assertFalse(session.equals("Term 1"));
        assertFalse(session.equals(new Session(2012, SessionType.WINTER_SESSION, terms)));
        assertFalse(session.equals(new Session(2013, SessionType.SUMMER_SESSION, terms)));
    }
}
