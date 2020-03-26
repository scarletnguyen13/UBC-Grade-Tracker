package model;

/**
 * Represents a course's instructor/professor.
 */
public class Instructor extends Person {
    private String name;
    private String email;

    public Instructor(String name, String email) {
        super(name, email);
    }

    public Instructor(String name) {
        this(name, "");
    }

    public Instructor() {
        this("", "");
    }
}
