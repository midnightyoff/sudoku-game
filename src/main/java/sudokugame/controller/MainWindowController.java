package sudokugame.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sudokugame.model.SudokuGenerator;

import java.io.*;
import java.util.Objects;

public class MainWindowController {
    private Stage stage;
    @FXML
    private Pane titlePane, mainPane, menuPane;
    private double windowX, windowY;
    @FXML
    private ImageView btnMinimize;
    private GameController gameController;
    @FXML
    private Button buttonNewGame, buttonEasyLevel, buttonMediumLevel, buttonHardLevel, btnLoad;
    @FXML
    private Label rulesText;
    private final SudokuGenerator sudokuGenerator = new SudokuGenerator();
    @FXML
    public void initialize(Stage stage){
        this.stage = stage;
        titlePane.setOnMousePressed(mouseEvent -> {
            windowX = mouseEvent.getSceneX();
            windowY = mouseEvent.getSceneY();
        });
        titlePane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX()- windowX);
            stage.setY(mouseEvent.getScreenY()- windowY);
        });
        btnMinimize.setOnMouseClicked(mouseEvent -> {
            stage.setIconified(true);
        });
        checkFile();
    }
    @FXML
    public void startNewGame() {
        buttonNewGame.setDisable(true);
        buttonEasyLevel.setDisable(false);
        buttonEasyLevel.setOpacity(1);
        buttonMediumLevel.setDisable(false);
        buttonMediumLevel.setOpacity(1);
        buttonHardLevel.setDisable(false);
        buttonHardLevel.setOpacity(1);
    }
    @FXML
    public void startEasyGame(ActionEvent event) throws IOException {
        int level = SudokuGenerator.EASY;
        startGame(event, level, sudokuGenerator.MakeSudoku(level), "3","3","00:00", sudokuGenerator.sudokuAnswer);
    }
    @FXML
    public void startMediumGame(ActionEvent event) throws IOException {
        int level = SudokuGenerator.MEDIUM;
        startGame(event, level, sudokuGenerator.MakeSudoku(level), "3","3","00:00", sudokuGenerator.sudokuAnswer);
    }
    @FXML
    public void startHardGame(ActionEvent event) throws IOException {
        int level = SudokuGenerator.HARD;
        startGame(event, level, sudokuGenerator.MakeSudoku(level), "3","3","00:00", sudokuGenerator.sudokuAnswer);
    }

    public void startGame(ActionEvent event, int level, String grid, String health, String hints, String time, String sudokuAnswer) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("view/game.fxml")));
        Parent root = loader.load();
        gameController = loader.getController();
        gameController.initialize(level, this, grid, health, hints, time, sudokuAnswer);
        switchPane((Pane)root);
    }

    public void checkFile() {
        File file = new File("game.dat");
        if (!file.exists()) btnLoad.setDisable(true);
        else btnLoad.setDisable(false);
    }

    @FXML
    public void loadGame() throws IOException {
        File file = new File("game.dat");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String grid = bufferedReader.readLine();
        String sudokuAnswer = bufferedReader.readLine();
        String health = bufferedReader.readLine();
        String hints = bufferedReader.readLine();
        String time = bufferedReader.readLine();
        startGame(new ActionEvent(), -1, grid, health, hints, time, sudokuAnswer);
        bufferedReader.close();
    }
    @FXML
    public void closeGame() {
        if (gameController !=null && !gameController.isGameFinished()) gameController.saveGame();
        stage.close();
    }

    @FXML
    public void showRules() {
        if (rulesText.getText().equals("")) {
            rulesText.setText("Цель судоку – заполнить сетку 9×9 цифрами, чтобы в каждом столбце, строке и сетке 3×3 были цифры от 1 до 9.\n" +
                    "В начале игры некоторые ячейки сетки 9×9 будут заполнены.\n" +
                    "Ваша задача – вписать недостающие цифры и заполнить всю сетку при помощи логики.\n" +
                    "Не забудьте, ход будет неверным, если:\n" +
                    "Любая строка содержит дублирующиеся цифры от 1 до 9\n" +
                    "Любой столбец содержит дублирующиеся цифры от 1 до 9\n" +
                    "Любая сетка 3×3 содержит дублирующиеся цифры от 1 до 9");
        } else rulesText.setText("");
    }

    public void switchPane(Pane root){
        mainPane.getChildren().clear();
        mainPane.getChildren().add(root);
    }
    public Pane getMenuPane(){
        return menuPane;
    }

}