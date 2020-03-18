package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Instructor class.
 */
public class InstructorTest {
    private Instructor instructor;

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
    void testSetters() {
        instructor = new Instructor("Dirk", "dirkvandepol@gmail.com");

        instructor.setName("Scarlet");
        instructor.setEmail("scarlet.nguyen01@gmail.com");

        assertEquals("Scarlet", instructor.getName());
        assertEquals("scarlet.nguyen01@gmail.com", instructor.getEmail());
    }

    @Test
    void testEquals() {
        instructor = new Instructor("Dirk", "dirkvandepol@gmail.com");
        Instructor newInstructor = new Instructor("Dirk", "dirkvandepol@gmail.com");

        assertTrue(instructor.equals(instructor));
        assertTrue(instructor.equals(new Instructor("Dirk", "dirkvandepol@gmail.com")));
        assertTrue(instructor.hashCode() == newInstructor.hashCode());

        assertFalse(instructor.equals(new Course("CPSC 121", "201")));
        assertFalse(instructor.equals(new Instructor("Dirk")));
        assertFalse(instructor.equals(new Instructor("", "dirkvandepol@gmail.com")));
        assertFalse(instructor.equals(null));
        assertFalse(instructor.equals(new Instructor("Dirk", "dirkvandepol@gmail.ca")));
    }
}
