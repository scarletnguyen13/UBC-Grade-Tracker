package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;
import persistance.FileHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents and displays the app's GUI.
 */
public class GradeTrackerUI {
    private static final String APP_NAME = "UBC Grade Tracker";
    private static final String SUMMER = "Summer";
    private static final String WINTER = "Winter";
    private static final String TERM_1 = "Term 1";
    private static final String TERM_2 = "Term 2";
    private static final String SUMMER_TERM = "Summer Term";

    private static final String NAME = "Name";
    private static final String STUDENT_ID = "Student ID";
    private static final String CS_ID = "CS ID";
    private static final String EMAIL = "Email";
    private static final String PHONE = "Phone";
    private static final String GPA = "GPA";

    private static final String SUBMIT = "Submit";
    private static final VBox EMPTY_BOX = new VBox();

    private static final String[] SEASONS = {
            "Winter", "Winter", "Spring", "Spring", "Summer", "Summer",
            "Summer", "Summer", "Fall", "Fall", "Winter", "Winter"
    };

    private Stage primaryStage;
    private Student student;
    private FileHandler fileHandler;

    // INFO SCENE
    private TextField nameTextField;
    private TextField studentIdTextField;
    private TextField csIdTextField;
    private TextField emailTextField;
    private TextField phoneTextField;
    private TextField gpaTextField;

    // ADD SESSION SCENE
    private ComboBox yearComboBox;
    private ComboBox sessionComboBox;
    private ComboBox termComboBox;
    private VBox courseTextFieldContainer;

    // ADD TODOITEM SCENE
    private TextField descriptionInput;
    private DatePicker datePicker;
    private ComboBox courseComboBox;
    private ComboBox componentComboBox;
    private TextField markInput;
    private TextField outOfInput;
    private CheckBox isCompleted;

    // COURSE INFO SCENE
    private TextField sectionInput;
    private TextField instructorNameInput;
    private TextField instructorEmailInput;
    private TextField finalGradeInput;
    private VBox courseComponentContainer;

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

    private static Set<CourseComponent> initSampleComponentList() {
        Set<CourseComponent> components = new TreeSet<>();
        components.add(new CourseComponent("Homework", 20));
        components.add(new CourseComponent("Quizzes", 30));
        components.add(new CourseComponent("Exam", 50));
        return components;
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private static boolean isBefore(LocalDate date) {
        LocalDate today = LocalDate.now();
        return today.compareTo(date) <= 0;
    }

    private void init(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_NAME);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(700);
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);
        primaryStage.show();
    }

    private Scene createInfoScene(Scene nextScene) {
        VBox vbox = new VBox();
        ListView list = new ListView();
        VBox.setVgrow(list, Priority.ALWAYS);

        this.initInfoTextFields();

        VBox nameBox = createInputField("(*) " + NAME);
        nameBox.getChildren().add(nameTextField);

        VBox studentIdBox = createInputField("(*) " + STUDENT_ID);
        studentIdBox.getChildren().add(studentIdTextField);

        VBox csIdBox = createInputField(CS_ID);
        csIdBox.getChildren().add(csIdTextField);

        VBox emailBox = createInputField(EMAIL);
        emailBox.getChildren().add(emailTextField);

        VBox phoneBox = createInputField(PHONE);
        phoneBox.getChildren().add(phoneTextField);

        VBox gpaBox = createInputField(GPA);
        gpaBox.getChildren().add(gpaTextField);

        Button toSessionSceneButton = createInfoSubmitButton(nextScene);

        vbox.getChildren().addAll(nameBox, studentIdBox, csIdBox, emailBox, phoneBox, gpaBox, toSessionSceneButton);
        vbox.setSpacing(40);
        vbox.setPadding(new Insets(20));

        return new Scene(vbox);
    }

    private Scene createSessionScene() {
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

        return new Scene(scrollPane);
    }

    private Scene createDashboardScene() {
        SplitPane pane = new SplitPane();
        pane.getItems().addAll(createInfoDisplay(), createCourseTable());

        return new Scene(pane);
    }

    private Scene createCourseInfoScene(Course course) {
        Label courseLabel = new Label(course.getName());
        courseLabel.setFont(new Font(30));
        Label sectionLabel = new Label("Section:");
        Label instructorNameLabel = new Label("Instructor Name:");
        Label instructorEmailLabel = new Label("Instructor Email:");
        Label finalGradeLabel = new Label("Final Grade (out of 100):");
        Label gradeBreakdownLabel = new Label("Grade Breakdown:");
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
        return new Scene(scrollPane);
    }

    private Scene createAddTodoScene(TodoItem item) {
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
        return new Scene(vbox);
    }

    private HBox createCancelButtonContainer() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            primaryStage.setScene(createDashboardScene());
        });
        HBox hbox = new HBox(20, cancelButton);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
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
            primaryStage.setScene(createDashboardScene());
        } else {
            throwFillOutAlert();
        }
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

    private void initInfoTextFields() {
        nameTextField = new TextField();
        nameTextField.setText(this.student.getName());

        studentIdTextField = new TextField();
        studentIdTextField.setText(this.student.getStudentId());

        csIdTextField = new TextField();
        csIdTextField.setText(this.student.getCsId());

        emailTextField = new TextField();
        emailTextField.setText(this.student.getEmail());

        phoneTextField = new TextField();
        phoneTextField.setText(this.student.getPhone());

        gpaTextField = new TextField();
        gpaTextField.setText(this.student.getGpa());
    }

    private Button createInfoSubmitButton(Scene nextScene) {
        Button button = new Button(SUBMIT);
        button.setOnAction(e -> {
            if (!nameTextField.getText().isEmpty() && !studentIdTextField.getText().isEmpty()) {
                this.student.setName(nameTextField.getText());
                this.student.setStudentId(studentIdTextField.getText());
                this.student.setCsId(csIdTextField.getText());
                this.student.setEmail(emailTextField.getText());
                this.student.setPhone(phoneTextField.getText());
                this.student.setGpa(gpaTextField.getText());

                this.primaryStage.setScene(nextScene == null ? createDashboardScene() : nextScene);
            } else {
                throwFillOutAlert();
            }
        });

        return button;
    }

    private void throwFillOutAlert() {
        String message = "Please fill out all required fields.";
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION, message, ButtonType.CANCEL
        );
        alert.showAndWait();
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

            this.primaryStage.setScene(createDashboardScene());
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

    private VBox createTextFieldsContainer() {
        VBox coursesContainer = new VBox();
        for (int i = 0; i < 5; i++) {
            coursesContainer.getChildren().add(new TextField());
        }
        coursesContainer.setSpacing(10);
        return coursesContainer;
    }

    private ComboBox createGeneralComboBox(String[] options, String defaultValue) {
        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(options);
        comboBox.setValue(defaultValue);
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

        String season = SEASONS[Calendar.getInstance().get(Calendar.MONTH)];
        if (season.equals("Spring")) {
            currentYear--;
        }

        yearComboBox.setValue(currentYear);

        return yearComboBox;
    }

    private TableView createTodoTable() {
        TableView tableView = new TableView();
        ObservableList<TodoItem> data = FXCollections.observableArrayList(this.student.getTodoList());
        SortedList<TodoItem> sortableData = new SortedList<>(data);
        tableView.setItems(sortableData);
        sortableData.comparatorProperty().bind(tableView.comparatorProperty());

        initTodoTableProps(tableView);

        tableView.setRowFactory(tv -> {
            TableRow<TodoItem> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TodoItem item = row.getItem();
                    this.primaryStage.setScene(createAddTodoScene(item));
                }
            });
            return row;
        });

        return tableView;
    }

    private void initTodoTableProps(TableView tableView) {
        TableColumn<TodoItem, TodoItem> column1 = new TableColumn<>("Task");
        column1.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<TodoItem, TodoItem> column2 = new TableColumn<>("Deadline");
        column2.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        TableColumn<TodoItem, TodoItem> column3 = new TableColumn<>("Course");
        column3.setCellValueFactory(new PropertyValueFactory<>("coursePair"));

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);

        column1.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        column2.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        column3.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));

        tableView.setEditable(true);
        tableView.setPrefWidth(150);

        tableView.getSortOrder().add(column2);
        tableView.getSortOrder().add(column1);
        tableView.getSortOrder().add(column3);
        tableView.sort();

        tableView.setPlaceholder(new Label("Todo list is empty"));
    }

    private VBox createCourseTable() {
        TableView tableView = new TableView();
        ObservableList<Course> data = FXCollections.observableArrayList(this.student.getAllCourses());
        FilteredList<Course> filteredData = new FilteredList(data, c -> true); //Pass the data to a filtered list
        SortedList<Course> sortableData = new SortedList<>(filteredData);
        tableView.setItems(sortableData);
        sortableData.comparatorProperty().bind(tableView.comparatorProperty());

        this.initCourseTableProps(tableView);

        tableView.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Course course = row.getItem();
                    this.primaryStage.setScene(createCourseInfoScene(course));
                }
            });
            return row;
        });

        VBox vbox = new VBox(createSearchContainer(filteredData), tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        return vbox;
    }

    private HBox createSearchContainer(FilteredList<Course> filteredData) {
        ChoiceBox<String> choiceBox = createSearchChoiceBox();
        TextField textField = createSearchTextField(choiceBox, filteredData);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                textField.setText("");
                filteredData.setPredicate(null);
            }
        });
        HBox hbox = new HBox(5, choiceBox, textField);
        HBox.setHgrow(textField, Priority.ALWAYS);
        return hbox;
    }

    private ChoiceBox<String> createSearchChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Course", "Session");
        choiceBox.setValue("Course");

        return choiceBox;
    }

    private TextField createSearchTextField(ChoiceBox<String> choiceBox, FilteredList<Course> flCourse) {
        TextField textField = new TextField();
        textField.setPromptText("Search...");

        textField.setOnKeyReleased(keyEvent -> {
            switch (choiceBox.getValue()) {
                case "Course":
                    flCourse.setPredicate(c -> c.getName().toLowerCase()
                            .contains(textField.getText().toLowerCase().trim()));
                    break;
                case "Session":
                    flCourse.setPredicate(c -> c.getSession().toString().toLowerCase()
                            .contains(textField.getText().toLowerCase().trim()));
                    break;
            }
        });

        return textField;
    }

    private void initCourseTableProps(TableView tableView) {
        TableColumn<String, Course> column1 = new TableColumn<>("Course");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<String, Session> column2 = new TableColumn<>("Session");
        column2.setCellValueFactory(new PropertyValueFactory<>("session"));

        TableColumn<String, String> column3 = new TableColumn<>("Term");
        column3.setCellValueFactory(new PropertyValueFactory<>("term"));


        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);

        column1.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        column2.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        column3.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));

        tableView.setEditable(true);
        tableView.setPrefWidth(150);

        tableView.getSortOrder().add(column2);
        tableView.getSortOrder().add(column3);
        tableView.getSortOrder().add(column1);
        tableView.sort();
    }

    private Button createAddSessionButton() {
        Button addSessionButton = new Button("Add Session/Courses");
        addSessionButton.setOnAction(e -> {
            this.primaryStage.setScene(createSessionScene());
        });

        return addSessionButton;
    }

    private Button createAddTodoButton() {
        Button addTodoButton = new Button("Add Todo");
        addTodoButton.setOnAction(e -> {
            this.primaryStage.setScene(createAddTodoScene(new TodoItem()));
        });

        return addTodoButton;
    }

    private VBox createInfoDisplay() {
        Button editInfoButton = new Button("Edit Info");
        editInfoButton.setOnAction(e -> {
            this.primaryStage.setScene(createInfoScene(null));
        });

        HBox buttons = new HBox(50,
                createAddSessionButton(),
                this.student.getAllCourses().size() > 0 ? createAddTodoButton() : EMPTY_BOX
        );
        VBox labels = createLabelContainer2();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(labels, editInfoButton, EMPTY_BOX, buttons, createTodoTable());

        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        return vbox;
    }

    private VBox createLabelContainer2() {
        VBox labels = new VBox();

        Label nameLabel = new Label(NAME + ": " + this.student.getName());
        Label studentIdLabel = new Label(STUDENT_ID + ": " + this.student.getStudentId());
        Label csIdLabel = new Label(CS_ID + ": " + this.student.getCsId());
        Label emailLabel = new Label(EMAIL + ": " + this.student.getEmail());
        Label phoneLabel = new Label(PHONE + ": " + this.student.getPhone());
        Label gpaLabel = new Label(GPA + ": " + this.student.getGpa());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(
                nameLabel, studentIdLabel, csIdLabel, emailLabel, phoneLabel, gpaLabel
        );

        vbox.setSpacing(20);
        return vbox;
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
                this.primaryStage.setScene(createDashboardScene());
            }
        });
        return removeCourseButton;
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
                this.primaryStage.setScene(createDashboardScene());
            }
        });
        return removeTodoButton;
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
            this.primaryStage.setScene(createDashboardScene());
        });
        return toDashboardButton;
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
                + round(course.getEstimatedGrade(), 1) + "%");
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
                new Label(round(mark * component.getPercentage(), 1)
                        + " / " + component.getPercentage() + "%")
        );
        hbox.setAlignment(Pos.TOP_LEFT);
        return hbox;
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

    private VBox createInputField(String labelString) {
        Label label = new Label(labelString + ":");
        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        return vbox;
    }

    public void saveData() {
        if (!this.student.getName().isEmpty() && !this.student.getStudentId().isEmpty()) {
            try {
                this.fileHandler.write(this.student);
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            }
        }
    }
}
