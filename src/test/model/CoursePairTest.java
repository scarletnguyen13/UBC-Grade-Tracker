package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoursePairTest {
    private CoursePair coursePair;

    @Test
    void testInit() {
        Course course = new Course("CPSC 210", "201");
        CourseComponent component = new CourseComponent("Quizzes", 40);
        coursePair = new CoursePair(course, component);

        assertEquals(course, coursePair.course);
        assertEquals(component, coursePair.component);
        assertEquals(course.getName(), coursePair.toString());
    }

    @Test
    void testEmpty() {
        Course course = new Course("CPSC 210", "201");
        CourseComponent component = new CourseComponent("Quizzes", 40);
        coursePair = new CoursePair(course, component);
        assertFalse(coursePair.isEmpty());

        coursePair = new CoursePair(null, null);
        assertTrue(coursePair.isEmpty());

        coursePair = new CoursePair(course, null);
        assertFalse(coursePair.isEmpty());

        coursePair = new CoursePair(null, component);
        assertFalse(coursePair.isEmpty());
    }
}
