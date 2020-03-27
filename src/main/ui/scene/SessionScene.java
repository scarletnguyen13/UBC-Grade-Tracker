package ui.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.util.*;

public class SessionScene extends MyScene {
    private static final String[] SEASONS = {
            "Winter", "Winter", "Spring", "Spring", "Summer", "Summer",
            "Summer", "Summer", "Fall", "Fall", "Winter", "Winter"
    };

    private static final String SUMMER = "Summer";
    private static final String WINTER = "Winter";
    private static final String TERM_1 = "Term 1";
    private static final String TERM_2 = "Term 2";
    private static final String SUMMER_TERM = "Summer Term";

    private ComboBox yearComboBox;
    private ComboBox sessionComboBox;
    private ComboBox termComboBox;
    private VBox courseTextFieldContainer;

    public SessionScene(Stage primaryStage) {
        super(primaryStage);
    }

    private static Set<CourseComponent> initSampleComponentList() {
        Set<CourseComponent> components = new TreeSet<>();
        components.add(new CourseComponent("Homework", 20));
        components.add(new CourseComponent("Quizzes", 30));
        components.add(new CourseComponent("Exam", 50));
        return components;
    }

    public void initScene(Student student) {
        this.student = student;
        VBox vbox = new VBox();
        ListView list = new ListView();
        VBox.setVgrow(list, Priority.ALWAYS);

        Label addCourseLabel = new Label("Courses: ");
        addCourseLabel.setFont(new Font(25));
        courseTextFieldContainer = createTextFieldsContainer();

        Button addCourseButton = createAddCourseButton();
        HBox buttonContainer = createCancelButtonContainer();
        buttonContainer.getChildren().add(createSessionSubmitButton());

        vbox.getChildren().addAll(
                this.createComboBoxContainer(),
                addCourseLabel,
                this.courseTextFieldContainer,
                addCourseButton,
                buttonContainer
        );

        vbox.setSpacing(30);
        vbox.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        this.scene = new Scene(scrollPane);
    }

    private VBox createTextFieldsContainer() {
        VBox coursesContainer = new VBox();
        for (int i = 0; i < 5; i++) {
            coursesContainer.getChildren().add(new TextField());
        }
        coursesContainer.setSpacing(10);
        return coursesContainer;
    }

    private Button createAddCourseButton() {
        Button addCourseButton = new Button("Add more Course");
        addCourseButton.setOnAction(e -> {
            courseTextFieldContainer.getChildren().add(new TextField());
        });
        return addCourseButton;
    }

    private Button createSessionSubmitButton() {
        Button submitButton = new Button(SUBMIT);

        submitButton.setOnAction(e -> {
            String term = termComboBox.getValue().toString();
            SessionType type = sessionComboBox.getValue().toString().equals(SUMMER) ? SessionType.SUMMER_SESSION
                    : SessionType.WINTER_SESSION;

            Session currentSession = student.findSession(Integer.parseInt(yearComboBox.getValue().toString()), type);
            this.student.addSession(currentSession);

            for (Node node : courseTextFieldContainer.getChildren()) {
                String courseName = ((TextField) node).getText();
                if (!courseName.isEmpty()) {
                    currentSession.addPair(
                            new Course(courseName, initSampleComponentList(), term, currentSession),
                            term
                    );
                }
            }

            DashboardScene scene = new DashboardScene(primaryStage);
            scene.initScene(student);
            this.primaryStage.setScene(scene.getScene());
        });
        return submitButton;
    }

    private HBox createComboBoxContainer() {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String season = SEASONS[month];
        String defaultType = season.equals(SUMMER) ? SUMMER : WINTER;
        String defaultTerm = month < 6 ? TERM_2 : TERM_1;

        yearComboBox = createYearComboBox();
        sessionComboBox = createGeneralComboBox(new String[]{WINTER, SUMMER}, defaultType);
        termComboBox = createGeneralComboBox(new String[]{TERM_1, TERM_2}, defaultTerm);

        sessionComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            ObservableList<String> list = FXCollections.observableArrayList();
            if (newValue.equals(SUMMER)) {
                list.add(SUMMER_TERM);
                termComboBox.setValue(SUMMER_TERM);
            } else {
                list.addAll(TERM_1, TERM_2);
                termComboBox.setValue(TERM_1);
            }
            termComboBox.setItems(list);
        });

        HBox container = new HBox(yearComboBox, sessionComboBox, termComboBox);
        container.setSpacing(29);

        return container;
    }

    private ComboBox createYearComboBox() {
        final ComboBox yearComboBox = new ComboBox();

        List years = new ArrayList();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = currentYear - 1; i <= currentYear + 1; i++) {
            years.add(i);
        }
        yearComboBox.getItems().addAll(years);

        String season = SEASONS[Calendar.getInstance().get(Calendar.MONTH)];
        if (season.equals("Spring")) {
            currentYear--;
        }

        yearComboBox.setValue(currentYear);

        return yearComboBox;
    }

    private ComboBox createGeneralComboBox(String[] options, String defaultValue) {
        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(options);
        comboBox.setValue(defaultValue);
        return comboBox;
    }
}
