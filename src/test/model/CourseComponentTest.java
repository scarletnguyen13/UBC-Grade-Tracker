package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        courseComponent.setName("Homework");
        assertEquals("Homework", courseComponent.getName());
    }

    @Test
    void testCompareTo() {
        CourseComponent component1 = new CourseComponent("Homework", 20);
        CourseComponent component2 = new CourseComponent("Quizzes", 30);
        CourseComponent component3 = new CourseComponent("Exams", 50);

        CourseComponent[] list1 = {component3, component1, component2};
        CourseComponent[] list2 = {component1, component2, component3};

        // assertion pass
        Arrays.sort(list2);
        assertArrayEquals(list1, list2);
    }

    @Test
    void testToString() {
        assertEquals("Quizzes", courseComponent.toString());
    }
}