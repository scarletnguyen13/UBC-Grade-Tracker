package ui.scene;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Student;

public abstract class MyScene {
    protected static final String NAME = "Name";
    protected static final String STUDENT_ID = "Student ID";
    protected static final String CS_ID = "CS ID";
    protected static final String EMAIL = "Email";
    protected static final String PHONE = "Phone";
    protected static final String GPA = "GPA";
    protected static final String SUBMIT = "Submit";
    protected static final VBox EMPTY_BOX = new VBox();

    protected Stage primaryStage;
    protected Scene scene;
    protected Student student;

    public MyScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene getScene() {
        return this.scene;
    }

    protected HBox createCancelButtonContainer() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            DashboardScene scene = new DashboardScene(primaryStage);
            scene.initScene(student);
            primaryStage.setScene(scene.getScene());
        });
        HBox hbox = new HBox(20, cancelButton);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }
}
