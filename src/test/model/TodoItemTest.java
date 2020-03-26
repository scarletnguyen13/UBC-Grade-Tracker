package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TodoItemTest {

    private TodoItem item;

    @Test
    void testInit1() {
        String description = "Homework #3";
        Course course = new Course("CPSC 210", "201");
        CourseComponent component = new CourseComponent("Quizzes", 40);
        CoursePair coursePair = new CoursePair(course, component);
        LocalDate deadline = LocalDate.now();
        Grade grade = new Grade(80.0, 100.0);

        item = new TodoItem(description, coursePair, deadline, grade, false);

        assertEquals(description, item.getDescription());
        assertEquals(coursePair, item.getCoursePair());
        assertEquals(deadline, item.getDeadline());
        assertEquals(grade, item.getGrade());
        assertFalse(item.isCompleted());

        description = "Quiz #4";
        coursePair = new CoursePair(null, null);
        deadline = LocalDate.now().minusDays(20L);
        grade = new Grade(90.0, 100.0);

        item.setDescription(description);
        item.setCoursePair(coursePair);
        item.setDeadline(deadline);
        item.setGrade(grade);
        item.setCompleted(true);

        assertEquals(description, item.getDescription());
        assertEquals(coursePair, item.getCoursePair());
        assertEquals(deadline, item.getDeadline());
        assertEquals(grade, item.getGrade());
        assertTrue(item.isCompleted());
    }

    @Test
    void testInit2() {
        CourseComponent component = new CourseComponent("Homework", 20);
        component.setTotalMarkGained(80);
        component.setMaxMark(100);
        CoursePair coursePair = new CoursePair(
                new Course("CPSC 110", "L21"),
                component
        );
        Grade grade = new Grade(40.0, 60.0);

        item = new TodoItem("Homework #2", coursePair, grade);

        assertEquals("Homework #2", item.getDescription());
        assertEquals(coursePair, item.getCoursePair());
        assertEquals(LocalDate.now(), item.getDeadline());
        assertEquals(grade, item.getGrade());
        assertFalse(item.isCompleted());
    }

    @Test
    void testInit3() {
        item = new TodoItem();
        assertEquals("", item.getDescription());
        assertEquals(LocalDate.now(), item.getDeadline());
        assertFalse(item.isCompleted());
    }

    @Test
    void testSetProps() {
        item = new TodoItem();

        String description = "Homework #3";
        Course course = new Course("CPSC 210", "201");
        CourseComponent component = new CourseComponent("Quizzes", 40);
        CoursePair coursePair = new CoursePair(course, component);
        LocalDate deadline = LocalDate.now();
        Grade grade = new Grade(80.0, 100.0);

        item.setProps(description, coursePair, deadline, grade, true);

        assertEquals(description, item.getDescription());
        assertEquals(coursePair, item.getCoursePair());
        assertEquals(deadline, item.getDeadline());
        assertEquals(grade, item.getGrade());
        assertTrue(item.isCompleted());
    }
}
