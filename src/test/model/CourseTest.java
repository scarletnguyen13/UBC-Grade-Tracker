package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Unit tests for the Course class.
 */
public class CourseTest {

    private Course course;
    private Set<CourseComponent> courseComponents;

    @BeforeEach
    void runBefore() {
        courseComponents = new HashSet<>();
        courseComponents.add(new CourseComponent("Homework", 20));
        courseComponents.add(new CourseComponent("Quizzes", 30));
        courseComponents.add(new CourseComponent("Exams", 50));
    }

    @Test
    void testInit1() {
        course = new Course("CPSC121", "L56",
                new Instructor("Dirk"), courseComponents);

        assertEquals("CPSC121", course.getName());
        assertEquals("L56", course.getSection());
        assertEquals(new Instructor("Dirk"), course.getInstructor());
        assertEquals(courseComponents, course.getComponents());
    }

    @Test
    void testInit2() {
        course = new Course("CPSC210", "L13");

        assertEquals("CPSC210", course.getName());
        assertEquals("L13", course.getSection());
        assertEquals(new Instructor(), course.getInstructor());
        assertEquals(new HashSet<>(), course.getComponents());
    }

    @Test
    void testInit3() {
        course = new Course("CPSC110");

        assertEquals("CPSC110", course.getName());
        assertTrue(course.getSection().isEmpty());
        assertTrue(course.getInstructor().isEmpty());
        assertTrue(course.getComponents().isEmpty());
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
