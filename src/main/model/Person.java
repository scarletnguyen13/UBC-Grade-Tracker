package model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Person implements Serializable {
    protected String name;
    protected String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person that = (Person) o;
        return name.equals(that.name)
                && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
