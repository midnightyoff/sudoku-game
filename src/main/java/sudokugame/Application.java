package sudokugame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("view/menu.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("Sudoku");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        ((MainWindowController)loader.getController()).initialize(stage);
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}