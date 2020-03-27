package ui.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Course;
import model.Session;
import model.Student;
import model.TodoItem;

public class DashboardScene extends MyScene {
    public DashboardScene(Stage primaryStage) {
        super(primaryStage);
    }

    public void initScene(Student student) {
        this.student = student;
        SplitPane pane = new SplitPane();
        pane.getItems().addAll(createInfoDisplay(), createCourseTable());
        this.scene = new Scene(pane);
    }

    private VBox createInfoDisplay() {
        Button editInfoButton = new Button("Edit Info");
        editInfoButton.setOnAction(e -> {
            StudentInfoScene studentInfoScene = new StudentInfoScene(primaryStage);
            studentInfoScene.initScene(student, null);
            this.primaryStage.setScene(studentInfoScene.getScene());
        });

        HBox buttons = new HBox(40,
                editInfoButton,
                createAddSessionButton(),
                this.student.getAllCourses().size() > 0 ? createAddTodoButton() : EMPTY_BOX
        );
        buttons.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(
                createLabelContainer(), buttons, EMPTY_BOX, createTodoTable()
        );
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        return vbox;
    }

    private Button createAddTodoButton() {
        Button addTodoButton = new Button("Add Todo");
        addTodoButton.setOnAction(e -> {
            TodoItemScene scene = new TodoItemScene(primaryStage);
            scene.initScene(student, new TodoItem());
            this.primaryStage.setScene(scene.getScene());
        });

        return addTodoButton;
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
                    CourseInfoScene scene = new CourseInfoScene(primaryStage);
                    scene.initScene(student, row.getItem());
                    this.primaryStage.setScene(scene.getScene());
                }
            });
            return row;
        });

        VBox vbox = new VBox(createSearchContainer(filteredData), tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        return vbox;
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

        tableView.setPlaceholder(new Label("No courses available"));
    }

    private Button createAddSessionButton() {
        Button addSessionButton = new Button("Add Session/Courses");
        addSessionButton.setOnAction(e -> {
            SessionScene scene = new SessionScene(primaryStage);
            scene.initScene(student);
            this.primaryStage.setScene(scene.getScene());
        });

        return addSessionButton;
    }

    private VBox createLabelContainer() {
        Label nameLabel = new Label(this.student.getName());
        nameLabel.setTextFill(Color.web("#595959"));
        nameLabel.setFont(new Font("Courier New", 30));
        Label studentIdLabel = initLabel(STUDENT_ID, this.student.getStudentId());
        Label csIdLabel = initLabel(CS_ID, this.student.getCsId());
        Label emailLabel = initLabel(EMAIL, this.student.getEmail());
        Label phoneLabel = initLabel(PHONE, this.student.getPhone());
        Label gpaLabel = initLabel(GPA, this.student.getGpa());

        VBox vbox = new VBox(10, nameLabel, studentIdLabel, csIdLabel, emailLabel, phoneLabel, gpaLabel);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.5, 0.0, 0.0);"
                + "-fx-background-color: white;");

        return vbox;
    }

    private Label initLabel(String text, String info) {
        Label label = new Label("• " + text + ": " + info);
        label.setTextFill(Color.web("#4a4a4a"));
        label.setFont(new Font("Courier New", 14));
        return label;
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

    private VBox createTodoTable() {
        TableView tableView = new TableView();

        ObservableList<TodoItem> data = FXCollections.observableArrayList(this.student.getTodoList());
        FilteredList<TodoItem> filteredData = new FilteredList(data, c -> true);
        SortedList<TodoItem> sortableData = new SortedList<>(filteredData);
        tableView.setItems(sortableData);
        sortableData.comparatorProperty().bind(tableView.comparatorProperty());

        initTodoTableProps(tableView);

        tableView.setRowFactory(tv -> {
            TableRow<TodoItem> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TodoItem item = row.getItem();
                    TodoItemScene scene = new TodoItemScene(primaryStage);
                    scene.initScene(student, item);
                    this.primaryStage.setScene(scene.getScene());
                }
            });
            return row;
        });

        VBox vbox = new VBox(10, createTodoChoiceBox(filteredData), tableView);
        vbox.setAlignment(Pos.CENTER_RIGHT);
        return vbox;
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

    private ChoiceBox<String> createTodoChoiceBox(FilteredList<TodoItem> flItem) {
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("All", "Todo", "Completed");
        choiceBox.setValue("All");

        choiceBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue.equals("All")) {
                flItem.setPredicate(c -> true);
            } else if (newValue.equals("Todo")) {
                flItem.setPredicate(c -> !c.isCompleted());
            } else {
                flItem.setPredicate(c -> c.isCompleted());
            }
        });

        return choiceBox;
    }
}
