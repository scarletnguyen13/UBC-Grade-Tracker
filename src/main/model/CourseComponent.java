package model;

import java.io.Serializable;
import java.util.Comparator;

/*
 * Represents a course's components such as homework, quizzes, etc.
 */
public class CourseComponent implements Serializable, Comparable<CourseComponent> {
    private String name;
    private int percentage;
    private double totalMarkGained;
    private double maxMark;

    public CourseComponent(String name, int percentage) {
        this.name = name;
        this.percentage = percentage;
        this.totalMarkGained = 0.0;
        this.maxMark = 0.0;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(double maxMark) {
        this.maxMark = maxMark;
    }

    public double getTotalMarkGained() {
        return totalMarkGained;
    }

    public void setTotalMarkGained(double totalMarkGained) {
        this.totalMarkGained = totalMarkGained;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(CourseComponent other) {
        return Comparator
                .comparing(CourseComponent::getName)
                .compare(this, other);
    }
}