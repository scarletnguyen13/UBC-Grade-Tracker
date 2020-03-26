package ui.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;
import ui.GradeTrackerUI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static ui.GradeTrackerUI.EMPTY_BOX;
import static ui.GradeTrackerUI.SUBMIT;

public class CourseInfoScene extends MyScene {
    private Label sectionLabel;
    private Label instructorNameLabel;
    private Label instructorEmailLabel;
    private Label finalGradeLabel;
    private Label gradeBreakdownLabel;

    private TextField sectionInput;
    private TextField instructorNameInput;
    private TextField instructorEmailInput;
    private TextField finalGradeInput;
    private VBox courseComponentContainer;

    public CourseInfoScene(Stage primaryStage) {
        super(primaryStage);
    }

    public void initScene(Student student, Course course) {
        this.student = student;
        initLabels();
        Label courseLabel = new Label(course.getName());
        courseLabel.setFont(new Font(30));
        initCourseInfo(course);
        HBox buttonContainer = createCancelButtonContainer();
        buttonContainer.getChildren().add(createCourseInfoSubmitButton(course));
        Button addComponentButton = createCourseInfoAddButton();
        Button removeCourseButton = createRemoveCourseButton(course);
        HBox hbox = new HBox(createPieChart(course), new VBox(20, courseComponentContainer, addComponentButton));
        VBox vbox = new VBox(20,
                courseLabel, sectionLabel, sectionInput, instructorNameLabel, instructorNameInput, instructorEmailLabel,
                instructorEmailInput, finalGradeLabel, finalGradeInput, gradeBreakdownLabel, hbox, buttonContainer,
                EMPTY_BOX, removeCourseButton
        );
        vbox.setPadding(new Insets(20));
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        this.scene = new Scene(scrollPane);
    }

    private void initLabels() {
        sectionLabel = new Label("Section:");
        instructorNameLabel = new Label("Instructor Name:");
        instructorEmailLabel = new Label("Instructor Email:");
        finalGradeLabel = new Label("Final Grade (out of 100):");
        gradeBreakdownLabel = new Label("Grade Breakdown:");
    }

    private void initCourseInfo(Course course) {
        sectionInput = new TextField(course.getSection());
        instructorNameInput = new TextField(course.getInstructor().getName());
        instructorEmailInput = new TextField(course.getInstructor().getEmail());
        finalGradeInput = new TextField(course.getFinalGrade().mark.toString());

        courseComponentContainer = new VBox();
        courseComponentContainer.setSpacing(10);

        for (CourseComponent component : course.getComponents()) {
            TextField componentInput = new TextField(component.getName());
            TextField percentageInput = new TextField(component.getPercentage() + "");
            percentageInput.setPrefWidth(50);
            HBox hbox = new HBox(componentInput, percentageInput, new Label("%"));
            hbox.setSpacing(10);
            courseComponentContainer.getChildren().add(hbox);
        }
    }

    private Button createCourseInfoAddButton() {
        Button addComponentButton = new Button("Add more Component");
        addComponentButton.setOnAction(e -> {
            TextField componentInput = new TextField();
            TextField percentageInput = new TextField("0");
            percentageInput.setPrefWidth(50);
            HBox hbox = new HBox(componentInput, percentageInput, new Label("%"));
            hbox.setSpacing(10);
            courseComponentContainer.getChildren().add(hbox);
        });
        return addComponentButton;
    }

    private Button createCourseInfoSubmitButton(Course course) {
        Button toDashboardButton = new Button(SUBMIT);
        toDashboardButton.setOnAction(e -> {
            course.setSection(sectionInput.getText());
            course.setInstructor(
                    new Instructor(
                            instructorNameInput.getText(),
                            instructorEmailInput.getText()
                    )
            );
            course.setFinalGrade(new Grade(Double.parseDouble(finalGradeInput.getText()), 100.0));
            iterateCourseComponents(course);

            DashboardScene scene = new DashboardScene(primaryStage);
            scene.initScene(student);
            this.primaryStage.setScene(scene.getScene());
        });
        return toDashboardButton;
    }

    private void iterateCourseComponents(Course course) {
        Set<CourseComponent> components = new HashSet<>();
        for (Node node1 : courseComponentContainer.getChildren()) {
            HBox container = (HBox) node1;
            String component = "";
            int percentage = 0;
            for (Node node2 : container.getChildren()) {
                if (node2 instanceof TextField) {
                    String text = ((TextField) node2).getText();
                    try {
                        percentage = Integer.parseInt(text);
                    } catch (Exception error) {
                        component = text;
                    }
                }
            }
            if (!component.isEmpty() && percentage != 0) {
                components.add(new CourseComponent(component, percentage));
            }
        }
        transferComponentGrade(course, components);
        course.setComponents(components);
    }

    private void transferComponentGrade(Course course, Set<CourseComponent> components) {
        for (CourseComponent component : components) {
            CourseComponent oldComponent = course.findComponentByName(component.getName());
            if (oldComponent != null) {
                component.setTotalMarkGained(oldComponent.getTotalMarkGained());
                component.setMaxMark(oldComponent.getMaxMark());
            }
        }
    }

    private Button createRemoveCourseButton(Course course) {
        Button removeCourseButton = new Button("Remove Course");
        removeCourseButton.setStyle("-fx-background-color: #cf0e00; -fx-text-fill: #fff");
        removeCourseButton.setOnAction(e -> {
            String message = "Are you sure?";
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
            );
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                Session s = course.getSession();
                s.removeCourse(course);

                DashboardScene scene = new DashboardScene(primaryStage);
                scene.initScene(student);
                this.primaryStage.setScene(scene.getScene());
            }
        });
        return removeCourseButton;
    }

    private VBox createPieChart(Course course) {
        AnchorPane anchor = new AnchorPane();

        ArrayList<HBox> bars = new ArrayList<>();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (CourseComponent component : course.getComponents()) {
            if (!component.getName().isEmpty() && component.getPercentage() != 0) {
                pieChartData.add(new PieChart.Data(component.getName(), component.getPercentage()));
                bars.add(createProgressContainer(component));
            }
        }

        Label estimatedGradeLabel = new Label("Estimated Grade: "
                + GradeTrackerUI.round(course.getEstimatedGrade(), 1) + "%");
        estimatedGradeLabel.setFont(new Font(20));

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Visualization");
        chart.setLabelLineLength(10);
        chart.setLegendSide(Side.BOTTOM);

        anchor.getChildren().add(pieChartData.size() > 0 ? chart : EMPTY_BOX);
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(bars);
        vbox.setAlignment(Pos.CENTER);

        return new VBox(20, anchor, vbox, estimatedGradeLabel);
    }

    private HBox createProgressContainer(CourseComponent component) {
        Double mark = 0.0;
        if (component.getMaxMark() > 0.0) {
            mark = component.getTotalMarkGained() / component.getMaxMark();
        }
        ProgressBar progressBar = new ProgressBar(mark);
        progressBar.setPrefWidth(200);
        progressBar.setMaxWidth(200);
        HBox hbox = new HBox(
                20,
                new Label(component.getName() + ": "),
                progressBar,
                new Label(GradeTrackerUI.round(mark * component.getPercentage(), 1)
                        + " / " + component.getPercentage() + "%")
        );
        hbox.setAlignment(Pos.TOP_LEFT);
        return hbox;
    }
}
