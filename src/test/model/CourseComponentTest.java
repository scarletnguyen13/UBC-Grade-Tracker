package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Unit tests for the CourseComponent class.
 */
public class CourseComponentTest {
    private CourseComponent courseComponent;

    @BeforeEach
    void runBefore() {
        courseComponent = new CourseComponent("Quizzes", 40);
    }

    @Test
    void testInit() {
        assertEquals("Quizzes", courseComponent.getName());
        assertEquals(40, courseComponent.getPercentage());
    }
}
