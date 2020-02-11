package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Unit tests for the Instructor class.
 */
public class InstructorTest {
    private Instructor instructor;

    @BeforeEach
    void runBefore() {

    }

    @Test
    void testInit1() {
        instructor = new Instructor("Dirk", "dirkvandepol@gmail.com");

        assertEquals("Dirk", instructor.getName());
        assertEquals("dirkvandepol@gmail.com", instructor.getEmail());
    }

    @Test
    void testInit2() {
        instructor = new Instructor("Dirk");

        assertEquals("Dirk", instructor.getName());
        assertTrue(instructor.getEmail().isEmpty());
    }

    @Test
    void testInit3() {
        instructor = new Instructor();

        assertTrue(instructor.getName().isEmpty());
        assertTrue(instructor.getEmail().isEmpty());
    }

    @Test
    void testIsEmpty() {
        instructor = new Instructor();
        assertTrue(instructor.isEmpty());

        instructor = new Instructor("Dirk");
        assertFalse(instructor.isEmpty());
    }

    @Test
    void testEquals() {
        instructor = new Instructor("Dirk");
        assertTrue(instructor.equals(new Instructor("Dirk")));

        instructor = new Instructor("Dirk", "dirkvandepol@gmail.com");
        assertFalse(instructor.equals(new Instructor("Dirk")));
        assertFalse(instructor.equals(new Instructor("Dirk", "dirkvandepol@gmail.ca")));

    }
}
