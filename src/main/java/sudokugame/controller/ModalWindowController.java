package sudokugame.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ModalWindowController {
    private Type type;
    private GameController gameController;
    @FXML
    private Label timeText, windowName, textField;
    private double windowX, windowY;
    private Stage stage;
    private Pane mainPane;

    @FXML
    public void initialize(Stage stage ,Pane mainPane, Type type, GameController gameController){
        this.gameController = gameController;
        this.type = type;
        this.stage = stage;
        this.mainPane = mainPane;
        if (type == Type.PAUSE) {
            windowName.setText("Пауза");
            textField.setText("Время:");
            timeText.setText(gameController.getTime().toString());
        } else if (type == Type.WIN) {
            windowName.setText("Победа");
            windowName.setLayoutX(130);
            textField.setText("Победа за время:");
            timeText.setText(gameController.getTime().toString());
            textField.setLayoutX(72);
        } else if (type == Type.LOSE) {
            windowName.setText("Проигрыш");
            windowName.setLayoutX(113);
            textField.setText("Кончились все жизни!");
            textField.setLayoutX(52);
            timeText.setText("");
        }
    }
    @FXML
    public void getCoordinates(MouseEvent mouseEvent){
        windowX = mouseEvent.getSceneX();
        windowY = mouseEvent.getSceneY();
    }
    @FXML
    public void dragWindow(MouseEvent mouseEvent){
        stage.setX(mouseEvent.getScreenX()- windowX);
        stage.setY(mouseEvent.getScreenY()- windowY);
    }
    @FXML
    public void btnAction() throws IOException {
        if (type == Type.PAUSE) {
            mainPane.setEffect(null);
            gameController.timeline.play();
            stage.close();
        }
        if (type == Type.LOSE){
            gameController.fileDelete();
            gameController.backToMenu();
            stage.close();
        }
        if (type == Type.WIN){
            gameController.fileDelete();
            stage.close();
        }
    }
}
enum Type
{
    PAUSE,
    WIN,
    LOSE
}
