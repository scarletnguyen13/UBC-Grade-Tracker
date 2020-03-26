package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Course class.
 */
public class CourseTest {

    private Course course;
    private Set<CourseComponent> courseComponents;
    private String term;
    private Session session;

    @BeforeEach
    void runBefore() {
        term = "Term 1";
        HashMap<Course, String> courses = new HashMap<>();
        courses.put(course, term);

        session = new Session(2013, SessionType.WINTER_SESSION, courses);

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
        assertEquals(0.0, course.getFinalGrade().mark);
        assertEquals(100.0, course.getFinalGrade().outOf);

        course.setTerm(term);
        course.setSession(session);
        course.setFinalGrade(new Grade(100.0, 100.0));
        assertEquals(term, course.getTerm());
        assertEquals(session, course.getSession());
        assertEquals(100.0, course.getFinalGrade().mark);
        assertEquals(100.0, course.getFinalGrade().outOf);
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
        course = new Course("CPSC 110", "210");

        assertEquals("CPSC 110", course.getName());
        assertEquals("210", course.getSection());
        assertTrue(course.getComponents().isEmpty());
    }

    @Test
    void testInit4() {
        course = new Course("CPSC 210", courseComponents, term, session);

        assertEquals("CPSC 210", course.getName());
        assertTrue(course.getSection().isEmpty());
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

    @Test
    void testFindComponentByName() {
        course = new Course("CPSC 210", courseComponents, term, session);

        CourseComponent foundComponent = course.findComponentByName("Homework");
        assertEquals("Homework", foundComponent.getName());
        assertEquals(20, foundComponent.getPercentage());

        CourseComponent notFoundComponent = course.findComponentByName("homework");
        assertEquals(null, notFoundComponent);
    }

    @Test
    void testEstimatedGrade() {
        CourseComponent component1 = new CourseComponent("Homework", 20);
        component1.setTotalMarkGained(25);
        component1.setMaxMark(40);

        CourseComponent component2 = new CourseComponent("Quizzes", 30);
        component2.setTotalMarkGained(30);
        component2.setMaxMark(50);

        CourseComponent component3 = new CourseComponent("Exams", 50);
        component3.setTotalMarkGained(80);
        component3.setMaxMark(100);

        courseComponents = new HashSet<>();
        courseComponents.add(component1);
        courseComponents.add(component2);
        courseComponents.add(component3);

        course = new Course("CPSC 210", courseComponents, term, session);
        double expectedResult = (25.0 / 40.0 * 20.0) + (30.0 / 50.0 * 30.0) + (80.0 / 100.0 * 50.0);
        assertEquals(expectedResult, course.getEstimatedGrade());

        component1.setPercentage(0);
        component2.setPercentage(0);
        component3.setPercentage(0);
        assertEquals(0.0, course.getEstimatedGrade());
    }
}
