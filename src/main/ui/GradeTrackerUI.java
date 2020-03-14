package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;
import persistance.FileHandler;

import java.io.IOException;
import java.util.*;

public class GradeTrackerUI {
    private Stage primaryStage;
    private Student student;
    private FileHandler fileHandler;

    public GradeTrackerUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
        fileHandler = new FileHandler();
        try {
            this.student = this.fileHandler.read();
            init(createDashboardScene());
        } catch (IOException | ClassNotFoundException e) {
            this.student = new Student();
            init(createInfoScene(createSessionScene()));
        }
    }

    private void init(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setTitle("UBC Grade Tracker");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.show();
    }

    private Scene createInfoScene(Scene nextScene) {
        VBox vbox = new VBox();
        ListView list = new ListView();
        VBox.setVgrow(list, Priority.ALWAYS);

        TextField nameTextField = new TextField();
        nameTextField.setText(this.student.getName());

        TextField studentIdTextField = new TextField();
        studentIdTextField.setText(this.student.getStudentId());

        TextField csIdTextField = new TextField();
        csIdTextField.setText(this.student.getCsId());

        TextField emailTextField = new TextField();
        emailTextField.setText(this.student.getEmail());

        TextField phoneTextField = new TextField();
        phoneTextField.setText(this.student.getPhone());

        TextField gpaTextField = new TextField();
        gpaTextField.setText(this.student.getGpa());

        VBox nameBox = createInputField("Name");
        nameBox.getChildren().add(nameTextField);

        VBox studentIdBox = createInputField("Student ID");
        studentIdBox.getChildren().add(studentIdTextField);

        VBox csIdBox = createInputField("CS ID");
        csIdBox.getChildren().add(csIdTextField);

        VBox emailBox = createInputField("Email");
        emailBox.getChildren().add(emailTextField);

        VBox phoneBox = createInputField("Phone");
        phoneBox.getChildren().add(phoneTextField);

        VBox gpaBox = createInputField("GPA");
        gpaBox.getChildren().add(gpaTextField);

        Button toSessionSceneButton = new Button("Done");
        toSessionSceneButton.setOnAction(e -> {
            this.student.setName(nameTextField.getText());
            this.student.setStudentId(studentIdTextField.getText());
            this.student.setCsId(csIdTextField.getText());
            this.student.setEmail(emailTextField.getText());
            this.student.setPhone(phoneTextField.getText());
            this.student.setGpa(gpaTextField.getText());

            this.primaryStage.setScene(nextScene == null ? createDashboardScene() : nextScene);
        });

        vbox.getChildren().addAll(nameBox, studentIdBox, csIdBox, emailBox, phoneBox, gpaBox, toSessionSceneButton);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        return new Scene(vbox);
    }

    private Scene createSessionScene() {
        VBox vbox = new VBox();
        ListView list = new ListView();
        VBox.setVgrow(list, Priority.ALWAYS);

        final ComboBox yearComboBox = createYearComboBox();
        final ComboBox sessionComboBox = createGeneralComboBox(new String[]{"Winter", "Summer"});
        final ComboBox termComboBox = createGeneralComboBox(new String[]{"Term 1", "Term 2"});

        HBox optionContainer = new HBox(yearComboBox, sessionComboBox, termComboBox);
        optionContainer.setSpacing(29);

        Label addCourseLabel = new Label("Courses: ");
        VBox coursesContainer = createTextFieldsContainer();

        Button addCourseButton = new Button("Add more Course");
        addCourseButton.setOnAction(e -> {
            coursesContainer.getChildren().add(new TextField());
        });

        Button toDashboardButton = new Button("Go to Dashboard");

        HBox buttonContainer = new HBox(addCourseButton, toDashboardButton);
        buttonContainer.setSpacing(30);

        vbox.getChildren().addAll(
                optionContainer,
                addCourseLabel,
                coursesContainer,
                buttonContainer
        );

        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        toDashboardButton.setOnAction(e -> {
            Term term = new Term(termComboBox.getValue().toString());
            SessionType type = sessionComboBox.getValue().toString().equals("Summer") ? SessionType.SUMMER_SESSION
                    : SessionType.WINTER_SESSION;

            Session currentSession = new Session(Integer.parseInt(yearComboBox.getValue().toString()), type);

            Set<Course> courses = new HashSet<>();
            for (Node node : coursesContainer.getChildren()) {
                String courseName = ((TextField)node).getText();
                if (!courseName.isEmpty()) {
                    courses.add(new Course(courseName, initEmptyList(), term, currentSession));
                }
            }

            term.setCourses(courses);
            currentSession.addTerm(term);

            this.student.addSession(currentSession);

            this.primaryStage.setScene(createDashboardScene());
        });

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return new Scene(scrollPane);
    }

    private VBox createTextFieldsContainer() {
        VBox coursesContainer = new VBox();

        for (int i = 0; i < 5; i++) {
            coursesContainer.getChildren().add(new TextField());
        }

        coursesContainer.setSpacing(10);

        return coursesContainer;
    }

    private ComboBox createGeneralComboBox(String[] options) {
        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(options[0], options[1]);
        comboBox.setValue(options[0]);
        return comboBox;
    }

    private ComboBox createYearComboBox() {
        final ComboBox yearComboBox = new ComboBox();

        List years = new ArrayList();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = currentYear - 1; i <= currentYear + 1; i++) {
            years.add(i);
        }
        yearComboBox.getItems().addAll(years);
        yearComboBox.setValue(currentYear);

        return yearComboBox;
    }

    private Scene createDashboardScene() {
        Label nameLabel = new Label("Name: " + this.student.getName());
        Label studentIdLabel = new Label("Student ID: " + this.student.getStudentId());
        Label csIdLabel = new Label("CS ID: " + this.student.getCsId());
        Label emailLabel = new Label("Email: " + this.student.getEmail());
        Label phoneLabel = new Label("Phone: " + this.student.getPhone());
        Label gpaLabel = new Label("GPA: " + this.student.getGpa());

        Button editInfoButton = new Button("Edit Info");
        editInfoButton.setOnAction(e -> {
            this.primaryStage.setScene(createInfoScene(null));
        });

        Button addSessionButton = new Button("Add Session");
        addSessionButton.setOnAction(e -> {
            this.primaryStage.setScene(createSessionScene());
        });

        HBox hbox = new HBox(editInfoButton, addSessionButton);
        hbox.setSpacing(30);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(
                nameLabel, studentIdLabel, csIdLabel, emailLabel, phoneLabel, gpaLabel, hbox
        );

        Label coursesLabel = new Label("Courses: ");

        TableView tableView = new TableView();

        TableColumn<String, Course> column1 = new TableColumn<>("Course");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<String, Session> column2 = new TableColumn<>("Session");
        column2.setCellValueFactory(new PropertyValueFactory<>("session"));

        TableColumn<String, Term> column3 = new TableColumn<>("Term");
        column3.setCellValueFactory(new PropertyValueFactory<>("term"));


        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);

        column1.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        column2.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        column3.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));

        for (Course course : this.student.getAllCourses()) {
            tableView.getItems().add(course);
        }

        tableView.setEditable(true);
        tableView.setPrefWidth(150);

        tableView.getSortOrder().add(column2);
        tableView.getSortOrder().add(column3);
        tableView.sort();

        tableView.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty())) {
                    Course course = row.getItem();
                    this.primaryStage.setScene(createCourseInfoScene(course));
                }
            });
            return row;
        });

        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        SplitPane pane = new SplitPane();
        pane.getItems().addAll(vbox, tableView);

        return new Scene(pane);
    }

    private Scene createCourseInfoScene(Course course) {
        Label courseLabel = new Label(course.getName());
        courseLabel.setFont(new Font(30));

        Label sectionLabel = new Label("Section:");
        TextField sectionInput = new TextField(course.getSection());

        Label instructorNameLabel = new Label("Instructor Name:");
        TextField instructorNameInput = new TextField(course.getInstructor().getName());

        Label instructorEmailLabel = new Label("Instructor Email:");
        TextField instructorEmailInput = new TextField(course.getInstructor().getEmail());

        Label gradeBreakdownLabel = new Label("Grade Breakdown:");
        VBox courseComponentContainer = new VBox();
        courseComponentContainer.setSpacing(10);

        for (CourseComponent cp : course.getComponents()) {
            TextField componentInput = new TextField(cp.getName());
            TextField percentageInput = new TextField(cp.getPercentage() + "");
            percentageInput.setPrefWidth(50);
            HBox hbox = new HBox(componentInput, percentageInput, new Label("%"));
            hbox.setSpacing(10);
            courseComponentContainer.getChildren().add(hbox);
        }

        Button toDashboardButton = new Button("Go to Dashboard");
        toDashboardButton.setOnAction(e -> {
            course.setSection(sectionInput.getText());
            course.setInstructor(
                    new Instructor(
                            instructorNameInput.getText(),
                            instructorEmailInput.getText()
                    )
            );

            Set<CourseComponent> components = new HashSet<>();
            for (Node node1 : courseComponentContainer.getChildren()) {
                HBox container = (HBox) node1;
                String component = "";
                int percentage = 0;
                for (Node node2 : container.getChildren()) {
                    if (node2 instanceof TextField) {
                        String text = ((TextField)node2).getText();
                        try {
                            percentage = Integer.parseInt(text);
                        } catch (Exception error) {
                            component = text;
                        }
                    }
                }

                if (!component.isEmpty() && percentage != 0) {
                    components.add(new CourseComponent(component, percentage));
                    course.setComponents(components);
                }
            }
            this.primaryStage.setScene(createDashboardScene());
        });

        Button addComponentButton = new Button("Add more Component");
        addComponentButton.setOnAction(e -> {
            TextField componentInput = new TextField();
            TextField percentageInput = new TextField("0");
            percentageInput.setPrefWidth(50);
            HBox hbox = new HBox(componentInput, percentageInput, new Label("%"));
            hbox.setSpacing(10);
            courseComponentContainer.getChildren().add(hbox);
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(
                courseLabel,
                sectionLabel, sectionInput,
                instructorNameLabel, instructorNameInput,
                instructorEmailLabel, instructorEmailInput,
                gradeBreakdownLabel,
                courseComponentContainer, addComponentButton,
                toDashboardButton
        );

        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return new Scene(scrollPane);
    }

    private VBox createInputField(String labelString) {
        Label label = new Label(labelString + ":");
        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        return vbox;
    }

    public void saveData() {
        try {
            this.fileHandler.write(this.student);
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    private static Set<CourseComponent> initEmptyList() {
        Set<CourseComponent> set = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            set.add(new CourseComponent("", 0));
        }
        return set;
    }
}
