package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Unit tests for the Student class.
 */
class StudentTest {
    private Student student;
    private Set<Session> sessions;

    Set<Course> term1Courses;
    Set<Course> term2Courses;

    Set<Course> summerCourses;

    @BeforeEach
    void runBefore() {
        term1Courses = new HashSet<>();
        term1Courses.add(new Course("MATH100", "L35"));
        term1Courses.add(new Course("BIOL111", "L35"));
        term1Courses.add(new Course("ASTR101", "L35"));
        term1Courses.add(new Course("ECON101", "L35"));
        term1Courses.add(new Course("CPSC110", "L35"));

        term2Courses = new HashSet<>();
        term2Courses.add(new Course("CPSC121", "L35"));
        term2Courses.add(new Course("CPSC210", "L35"));
        term2Courses.add(new Course("MATH101", "L35"));
        term2Courses.add(new Course("DSCI100", "L35"));

        Term winterTerm1 = new Term("Term 1", term1Courses);
        Term winterTerm2 = new Term("Term 2", term2Courses);

        Set<Term> winterTerms = new HashSet<>();
        winterTerms.add(winterTerm1);
        winterTerms.add(winterTerm2);

        summerCourses = new HashSet<>();
        summerCourses.add(new Course("CPSC310", "L35"));
        summerCourses.add(new Course("ASTR102", "L35"));
        summerCourses.add(new Course("ECON102", "L35"));

        Term summerTerm = new Term("Summer Term", summerCourses);

        Set<Term> summerTerms = new HashSet<>();
        summerTerms.add(summerTerm);

        sessions = new HashSet<>();
        sessions.add(new Session(2019, SessionType.SUMMER_SESSION, winterTerms));
        sessions.add(new Session(2018, SessionType.WINTER_SESSION, summerTerms));

        student = new Student("Scarlet Nguyen", "44304491", sessions);
    }

    @Test
    void testInit1() {
        student = new Student(
                "Scarlet Nguyen", "44304491", "d6x2b",
                "scarlet.nguyen01@gmail.com", "(604) 369-9123",
                3.8, sessions
        );

        assertEquals("Scarlet Nguyen", student.getName());
        assertEquals("44304491", student.getStudentId());
        assertEquals("d6x2b", student.getCsId());
        assertEquals("scarlet.nguyen01@gmail.com", student.getEmail());
        assertEquals("(604) 369-9123", student.getPhone());
        assertEquals(3.8, student.getGpa());
        assertEquals(sessions, student.getSessions());
    }

    @Test
    void testInit2() {
        assertEquals("Scarlet Nguyen", student.getName());
        assertEquals("44304491", student.getStudentId());
        assertTrue(student.getCsId().isEmpty());
        assertTrue(student.getEmail().isEmpty());
        assertTrue(student.getPhone().isEmpty());
        assertEquals(0.0, student.getGpa());
        assertEquals(sessions, student.getSessions());
    }

    @Test
    void testSetters() {
        student = new Student("Scarlet Nguyen", "443", new HashSet<>());

        student.setName("Scarlet");
        student.setStudentId("44304491");
        student.setCsId("d6x2b");
        student.setEmail("scarlet.nguyen01@gmail.com");
        student.setPhone("(604) 369-9123");
        student.setGpa(3.8);
        student.setSessions(sessions);

        assertEquals("Scarlet", student.getName());
        assertEquals("44304491", student.getStudentId());
        assertEquals("d6x2b", student.getCsId());
        assertEquals("scarlet.nguyen01@gmail.com", student.getEmail());
        assertEquals("(604) 369-9123", student.getPhone());
        assertEquals(3.8, student.getGpa());
        assertEquals(sessions, student.getSessions());
    }

    @Test
    void testGetAllCourses() {
        assertEquals(12, student.getAllCourses().size());
    }

    @Test
    void testEditCourse() {
        Course updatedCourse = new Course("CPSC121", "L26");

        student.editCourse(updatedCourse);

        assertTrue(student.getAllCourses().contains(updatedCourse));

        for (Course course: student.getAllCourses()) {
            if (updatedCourse.equals(course)) {
                assertEquals("L26", course.getSection());
            }
        }
    }

    @Test
    void testFindCourseByName() {
        assertEquals(new Course("CPSC121"), student.findCourseByName("CPSC121"));
        assertEquals(null, student.findCourseByName("CPSC400"));
    }

    @Test
    void testToString() {
        assertEquals("Scarlet Nguyen", student.toString());
    }
}