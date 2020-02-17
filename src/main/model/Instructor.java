package model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a course's instructor/professor.
 */
public class Instructor implements Serializable {
    private String name;
    private String email;

    public Instructor(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Instructor(String name) {
        this(name, "");
    }

    public Instructor() {
        this("", "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // EFFECTS: returns true if all the fields are empty, false otherwise
    public boolean isEmpty() {
        return this.name.isEmpty() && this.email.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Instructor that = (Instructor) o;
        return name.equals(that.name)
                && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
