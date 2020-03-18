package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Session class.
 */
public class SessionTest {
    private Session session;
    private Set<Term> terms;

    @BeforeEach
    void runBefore() {
        terms = new HashSet<>();
        terms.add(new Term("Term 1"));
        terms.add(new Term("Term 2"));
    }

    @Test
    void testInit1() {
        session = new Session(2013, SessionType.WINTER_SESSION, terms);

        assertEquals(2013, session.getYear());
        assertEquals(SessionType.WINTER_SESSION, session.getType());
        assertEquals(terms, session.getTerms());
    }

    @Test
    void testInit2() {
        session = new Session(2019, SessionType.SUMMER_SESSION);

        assertEquals(2019, session.getYear());
        assertEquals(SessionType.SUMMER_SESSION, session.getType());
        assertEquals(0, session.getTerms().size());
    }

    @Test
    void testSetters() {
        session = new Session(2019, SessionType.SUMMER_SESSION);

        session.setYear(2014);
        session.setType(SessionType.WINTER_SESSION);
        session.setTerms(terms);

        assertEquals(2014, session.getYear());
        assertEquals(SessionType.WINTER_SESSION, session.getType());
        assertEquals(terms, session.getTerms());
    }

    @Test
    void testAddTerm() {
        session = new Session(2019, SessionType.SUMMER_SESSION);
        assertEquals(0, session.getTerms().size());

        Term term1 = new Term("Term 1");
        session.addTerm(term1);

        assertEquals(1, session.getTerms().size());
        assertTrue(session.getTerms().contains(term1));

        // Add again, check for duplication
        session.addTerm(term1);
        assertEquals(1, session.getTerms().size());

        Term term1Duplicated = new Term("Term 1");
        session.addTerm(term1Duplicated);
        assertEquals(1, session.getTerms().size());

        Term term2 = new Term("Term 2");
        session.addTerm(term2);
        assertEquals(2, session.getTerms().size());
    }

    @Test
    void testRemoveTerm() {
        session = new Session(2019, SessionType.SUMMER_SESSION);
        assertEquals(0, session.getTerms().size());

        Term term1 = new Term("Term 1");
        Term term2 = new Term("Term 2");

        session.addTerm(term1);
        session.addTerm(term2);

        assertEquals(2, session.getTerms().size());

        session.removeTerm(term2);
        assertEquals(1, session.getTerms().size());
        assertTrue(session.getTerms().contains(term1));
        assertFalse(session.getTerms().contains(term2));
    }

    @Test
    void testEquals() {
        session = new Session(2013, SessionType.WINTER_SESSION, terms);

        assertTrue(session.equals(session));
        assertTrue(session.equals(new Session(2013, SessionType.WINTER_SESSION, terms)));

        assertFalse(session.equals(null));
        assertFalse(session.equals(new Term("Term 1")));
        assertFalse(session.equals(new Session(2012, SessionType.WINTER_SESSION, terms)));
        assertFalse(session.equals(new Session(2013, SessionType.SUMMER_SESSION, terms)));
        assertFalse(session.equals(new Session(2013, SessionType.WINTER_SESSION, new HashSet<>())));

        Set<Session> testSessions = new HashSet<>();
        testSessions.add(new Session(2019, SessionType.SUMMER_SESSION, terms));
        testSessions.add(new Session(2019, SessionType.SUMMER_SESSION, terms));

        assertEquals(1, testSessions.size());
    }
}
