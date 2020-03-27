package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;

public class TodoItem implements Serializable, Comparable<TodoItem> {
    private String description;
    private CoursePair coursePair;
    private LocalDate deadline;
    private Grade grade;
    private boolean isCompleted;

    public TodoItem(
            String description, CoursePair coursePair,
            LocalDate deadline, Grade grade, boolean isCompleted
    ) {
        this.description = description;
        this.coursePair = coursePair;
        this.deadline = deadline;
        this.grade = grade;
        this.isCompleted = isCompleted;
    }

    public TodoItem(String description, CoursePair coursePair, Grade grade) {
        this(description, coursePair, LocalDate.now(), grade, false);
    }

    public TodoItem() {
        this("", new CoursePair(null, null), new Grade(0.0, 0.0));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public CoursePair getCoursePair() {
        return coursePair;
    }

    public void setCoursePair(CoursePair coursePair) {
        this.coursePair = coursePair;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    // MODIFIES: this
    // EFFECTS: set multiple fields at the same time
    public void setProps(
            String description, CoursePair coursePair, LocalDate deadline, Grade grade, boolean isCompleted
    ) {
        this.description = description;
        this.coursePair = coursePair;
        this.deadline = deadline;
        this.grade = grade;
        this.isCompleted = isCompleted;
    }

    @Override
    public int compareTo(TodoItem other) {
        return Comparator
                .comparing(TodoItem::getDeadline, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(TodoItem::getDescription)
                .compare(this, other);
    }
}
