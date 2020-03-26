package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.scene.*;

public class Main extends Application {
    private GradeTrackerUI ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CourseInfoScene courseInfoScene = new CourseInfoScene(primaryStage);
        DashboardScene dashboardScene = new DashboardScene(primaryStage);
        StudentInfoScene studentInfoScene = new StudentInfoScene(primaryStage);
        SessionScene sessionScene = new SessionScene(primaryStage);
        TodoItemScene todoItemScene = new TodoItemScene(primaryStage);
        ui = new GradeTrackerUI(
                primaryStage, courseInfoScene, dashboardScene, studentInfoScene, sessionScene, todoItemScene
        );
    }

    @Override
    public void stop() {
        ui.saveData();
    }
}
