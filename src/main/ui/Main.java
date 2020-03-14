package ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private GradeTrackerUI ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ui = new GradeTrackerUI(primaryStage);
    }

    @Override
    public void stop() {
        ui.saveData();
    }
}
