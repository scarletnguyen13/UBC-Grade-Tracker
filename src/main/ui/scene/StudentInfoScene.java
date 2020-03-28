package ui.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Student;
import ui.GradeTrackerUI;

public class StudentInfoScene extends MyScene {
    private TextField nameTextField;
    private TextField studentIdTextField;
    private TextField csIdTextField;
    private TextField emailTextField;
    private TextField phoneTextField;
    private TextField gpaTextField;

    public StudentInfoScene(Stage primaryStage) {
        super(primaryStage);
    }

    public void initScene(Student student, Scene nextScene) {
        this.student = student;
        VBox vbox = new VBox(40);
        vbox.setPadding(new Insets(20));
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
        HBox buttonContainer = createCancelButtonContainer();
        buttonContainer.getChildren().add(toSessionSceneButton);

        vbox.getChildren().addAll(nameBox, studentIdBox, csIdBox, emailBox, phoneBox, gpaBox, buttonContainer);

        this.scene = new Scene(vbox);
    }

    private void initInfoTextFields() {
        nameTextField = new TextField();
        nameTextField.setText(this.student.getName());

        studentIdTextField = new TextField();
        studentIdTextField.setText(student.getStudentId());
        initAsDecimalTextField(studentIdTextField);

        csIdTextField = new TextField();
        csIdTextField.setText(this.student.getCsId());

        emailTextField = new TextField();
        emailTextField.setText(this.student.getEmail());

        phoneTextField = new TextField();
        phoneTextField.setText(this.student.getPhone());

        gpaTextField = new TextField();
        gpaTextField.setText(this.student.getGpa());
        initAsDecimalTextField(gpaTextField);
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

                DashboardScene dashboardScene = new DashboardScene(primaryStage);
                dashboardScene.initScene(student);
                this.primaryStage.setScene(nextScene == null ? dashboardScene.getScene() : nextScene);
            } else {
                GradeTrackerUI.throwFillOutAlert();
            }
        });

        return button;
    }

    private VBox createInputField(String labelString) {
        Label label = new Label(labelString + ":");
        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        return vbox;
    }
}
