package ui.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.time.LocalDate;
import java.util.Calendar;

import static ui.GradeTrackerUI.throwFillOutAlert;

public class TodoItemScene extends MyScene {
    private TextField descriptionInput;
    private DatePicker datePicker;
    private ComboBox courseComboBox;
    private ComboBox componentComboBox;
    private TextField markInput;
    private TextField outOfInput;
    private CheckBox isCompleted;

    public TodoItemScene(Stage primaryStage) {
        super(primaryStage);
    }

    public void initScene(Student student, TodoItem item) {
        this.student = student;
        VBox vbox = new VBox(20);
        ListView list = new ListView();
        VBox.setVgrow(list, Priority.ALWAYS);

        initAddTodoItem(item);

        Label descriptionLabel = new Label("(*) Task Description: ");
        HBox courseContainer = new HBox(50, courseComboBox, componentComboBox);

        Label deadlineLabel = new Label("(*) Deadline: ");
        Label courseLabel = new Label("(*) Course & Component: ");
        Label gradeLabel = new Label("Mark: ");
        HBox gradeContainer = new HBox(20, markInput, new Label("out of"), outOfInput);

        HBox buttonContainer = createCancelButtonContainer();
        buttonContainer.getChildren().add(createAddTodoItemButton(item));

        Button removeTodoItemButton = createRemoveTodoItemButton(item);

        vbox.getChildren().addAll(
                descriptionLabel, descriptionInput, deadlineLabel, datePicker, courseLabel,
                courseContainer, gradeLabel, gradeContainer, isCompleted, buttonContainer,
                item.getCoursePair().isEmpty() ? EMPTY_BOX : removeTodoItemButton
        );
        vbox.setPadding(new Insets(20));
        this.scene = new Scene(vbox);

    }

    private void initAddTodoItem(TodoItem item) {
        descriptionInput = new TextField(item.getDescription());
        datePicker = new DatePicker(item.getDeadline());
        initCourseComboBox(item.getCoursePair().course);
        initComponentComboBox(item.getCoursePair());
        markInput = new TextField(item.getGrade().mark.toString());
        outOfInput = new TextField(item.getGrade().outOf.toString());
        isCompleted = new CheckBox("Is Completed?");
        isCompleted.setSelected(item.isCompleted());
    }

    private void initCourseComboBox(Course course) {
        courseComboBox = new ComboBox<Course>();
        ObservableList<Course> courses = FXCollections.observableArrayList(this.student.getAllCourses());
        courseComboBox.setItems(courses);
        courseComboBox.setPromptText("Course");
        courseComboBox.setValue(course);
        if (course != null) {
            courseComboBox.setDisable(true);
            courseComboBox.setOpacity(1);
        }
        courseComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            ObservableList<CourseComponent> list =
                    FXCollections.observableArrayList(((Course) newValue).getComponents());
            componentComboBox.setItems(list);
        });
    }

    private void initComponentComboBox(CoursePair coursePair) {
        componentComboBox = new ComboBox<CourseComponent>();
        componentComboBox.setPromptText("Component");
        if (coursePair.course != null && coursePair.component != null) {
            ObservableList<CourseComponent> components =
                    FXCollections.observableArrayList(coursePair.course.getComponents());
            componentComboBox.setItems(components);
            componentComboBox.setValue(coursePair.component);
            componentComboBox.setDisable(true);
            componentComboBox.setOpacity(1);
        }
    }

    private Button createAddTodoItemButton(TodoItem item) {
        Button submitButton = new Button(SUBMIT);
        submitButton.setOnAction(e -> {
            String description = descriptionInput.getText();
            LocalDate deadline = datePicker.getValue();
            deadline.atTime(Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND);
            Course course = (Course) courseComboBox.getValue();
            Grade grade = getGradeFromInput();
            boolean completed = isCompleted.isSelected();
            addTodoItemAction(item, course, grade, description, deadline, completed);
        });
        return submitButton;
    }

    private void addTodoItemAction(
            TodoItem item, Course course, Grade grade, String description, LocalDate deadline, boolean completed
    ) {
        if (checkAddTodoCondition()) {
            CourseComponent component = course.findComponentByName(componentComboBox.getValue().toString());
            CoursePair coursePair = new CoursePair(course, component);

            double currentMark = component.getTotalMarkGained() + grade.mark;
            double currentMax = component.getMaxMark() + grade.outOf;

            if (this.student.getTodoList().contains(item)) {
                currentMark -= item.getGrade().mark;
                currentMax -= item.getGrade().outOf;
                item.setProps(description, coursePair, deadline, grade, completed);
            } else {
                this.student.addTodoItem(new TodoItem(description, coursePair, deadline, grade, completed));
            }
            component.setTotalMarkGained(currentMark);
            component.setMaxMark(currentMax);

            DashboardScene scene = new DashboardScene(primaryStage);
            scene.initScene(student);
            primaryStage.setScene(scene.getScene());
        } else {
            throwFillOutAlert();
        }
    }

    private Button createRemoveTodoItemButton(TodoItem item) {
        Button removeTodoButton = new Button("Remove Todo");
        removeTodoButton.setStyle("-fx-background-color: #cf0e00; -fx-text-fill: #fff");
        removeTodoButton.setOnAction(e -> {
            String message = "Are you sure?";
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
            );
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                this.student.removeTodoItem(item);
                DashboardScene scene = new DashboardScene(primaryStage);
                scene.initScene(student);
                primaryStage.setScene(scene.getScene());
            }
        });
        return removeTodoButton;
    }

    private boolean checkAddTodoCondition() {
        return !descriptionInput.getText().isEmpty() && !datePicker.getValue().equals(null)
                && courseComboBox.getValue() != null && componentComboBox.getValue() != null;
    }

    private Grade getGradeFromInput() {
        Grade grade;
        try {
            grade = new Grade(Double.parseDouble(markInput.getText()), Double.parseDouble(outOfInput.getText()));
        } catch (Exception err) {
            grade = new Grade(0.0, 0.0);
        }
        return grade;
    }
}
