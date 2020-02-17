package model;

import java.io.Serializable;

/**
 * Represents a course's components such as homework, quizzes, etc.
 */
public class CourseComponent implements Serializable {
    private String name;
    private int percentage;

    public CourseComponent(String name, int percentage) {
        this.name = name;
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return name + " " + percentage + "%";
    }
}
