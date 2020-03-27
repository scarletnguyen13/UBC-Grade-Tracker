package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.scene.DashboardScene;
import ui.scene.SessionScene;
import ui.scene.StudentInfoScene;

public class Main extends Application {
    private GradeTrackerUI ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DashboardScene dashboardScene = new DashboardScene(primaryStage);
        StudentInfoScene studentInfoScene = new StudentInfoScene(primaryStage);
        SessionScene sessionScene = new SessionScene(primaryStage);
        ui = new GradeTrackerUI(primaryStage, dashboardScene, studentInfoScene, sessionScene);
    }

    @Override
    public void stop() {
        ui.saveData();
    }
}
