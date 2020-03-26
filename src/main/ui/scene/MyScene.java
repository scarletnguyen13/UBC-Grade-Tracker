package ui.scene;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Student;

public abstract class MyScene {
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
