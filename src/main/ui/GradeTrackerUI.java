package ui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.Student;
import persistance.FileHandler;
import ui.scene.DashboardScene;
import ui.scene.SessionScene;
import ui.scene.StudentInfoScene;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Represents and displays the app's GUI.
 */
public class GradeTrackerUI {
    public static final String APP_NAME = "UBC Grade Tracker";

    private Stage primaryStage;
    private Student student;
    private FileHandler fileHandler;

    public GradeTrackerUI(Stage primaryStage, DashboardScene dashboardScene,
                          StudentInfoScene studentInfoScene, SessionScene sessionScene) {
        this.primaryStage = primaryStage;

        fileHandler = new FileHandler();
        try {
            this.student = this.fileHandler.read();
            dashboardScene.initScene(student);
            init(dashboardScene.getScene());
        } catch (IOException | ClassNotFoundException e) {
            this.student = new Student();
            sessionScene.initScene(student);
            studentInfoScene.initScene(student, sessionScene.getScene());
            init(studentInfoScene.getScene());
        }
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static boolean isBefore(LocalDate date) {
        LocalDate today = LocalDate.now();
        return today.compareTo(date) <= 0;
    }

    public static void throwFillOutAlert() {
        String message = "Please fill out all required fields.";
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION, message, ButtonType.CANCEL
        );
        alert.showAndWait();
    }

    public void init(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_NAME);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(700);
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);
        primaryStage.show();
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
