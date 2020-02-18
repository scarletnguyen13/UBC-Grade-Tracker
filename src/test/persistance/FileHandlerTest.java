package persistance;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.engine.TestExecutionResult.successful;

public class FileHandlerTest {
    private static final String TEST_FILE = "./data/testStudent.txt";
    private FileHandler fileHandler;
    private Student student;

    @BeforeEach
    void runBefore() {
        this.fileHandler = new FileHandler(TEST_FILE);
        this.student = new Student("Scarlet Nguyen", "44304491", getMockSessions());
    }

    @Test
    void testInit1() {
        assertEquals(TEST_FILE, fileHandler.getPath());
    }

    @Test
    void testInit2() {
        this.fileHandler = new FileHandler();
        assertEquals("./data/student.txt", fileHandler.getPath());
    }

    @Test
    void testWriteAndReadSuccessfully() {
        try {
            fileHandler.write(this.student);
            Student newStudent = this.fileHandler.read();

            assertEquals("Scarlet Nguyen", newStudent.getName());
            assertEquals("44304491", newStudent.getStudentId());
            assertEquals(getMockSessions(), newStudent.getSessions());

        } catch (IOException | ClassNotFoundException e) {
            fail("IOException should not have been thrown");
            fail("ClassNotFoundException should not have been thrown");
        }
    }

    @Test
    void testWriteFailedIOException() {
        try {
            this.fileHandler = new FileHandler("./data/doesnotexist/testStudent.txt");
            fileHandler.write(this.student);
        } catch (IOException e) {
            successful();
        }
    }

    @Test
    void testReadFailedIOException() {
        try {
            this.fileHandler = new FileHandler("./data/doesnotexist/testStudent.txt");
            fileHandler.read();
        } catch (IOException e) {
            successful();
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException should not have been thrown");
        }
    }

    // EFFECTS: returns a mock session list used for testing
    private Set<Session> getMockSessions() {
        Set<Session> sessions = new HashSet<>();;
        Set<Course> summerCourses = new HashSet<>();;

        summerCourses.add(new Course("CPSC310", "L35"));
        summerCourses.add(new Course("ASTR102", "L35"));
        summerCourses.add(new Course("ECON102", "L35"));

        Set<Term> summerTerms = new HashSet<>();
        summerTerms.add(new Term("Summer Term", summerCourses));

        sessions.add(new Session(2018, SessionType.WINTER_SESSION, summerTerms));

        return sessions;
    }

}
