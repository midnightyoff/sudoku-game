package sudokugame.controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sudokugame.model.Time;
import sudokugame.model.tile.Style;
import sudokugame.model.tile.Tile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;

public class GameController {
    @FXML
    private Label healthText, timeText, hintsText;
    @FXML
    private Pane mainPane;
    @FXML
    private GridPane mainGrid;
    private MainWindowController mainWindowController;
    private final Tile[][] tileArray = new Tile[9][9];
    private Tile lastSelected;
    private final Style style = new Style();
    private final Random random = new Random();
    private Time time;
    private int health, hints;
    private boolean isGameFinished;
    private String sudokuAnswer;

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        time.oneSecondPassed();
        timeText.setText(time.toString());
    }));

    @FXML
    public void initialize(int level, MainWindowController mainWindowController, String grid, String health, String hints, String currentTime, String sudokuAnswer){
        this.mainWindowController = mainWindowController;
        mainGrid.requestFocus();
        mainPane.setOnKeyPressed(this::handleKey);
        isGameFinished = false;
        initializeGrid();
        if (level != -1) initGame(grid, health, hints, currentTime, sudokuAnswer);
        else initGame(grid, health, hints, currentTime, sudokuAnswer);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
//    public void initNewGame(int level, String health, String hints){
//        time = new Time(0,0);
//        this.health = Integer.parseInt(health);
//        this.hints = Integer.parseInt(hints);
//        healthText.setText("Жизни: 3/3");
//        hintsText.setText("Подсказки: 3/3");
//        sudoku = sudokuGenerator.MakeSudoku(level);
//        this.sudokuAnswer = sudokuGenerator.sudokuAnswer;
//        setSudoku(sudoku);
//    }
    public void initGame(String grid, String health, String hints, String currentTime, String sudokuAnswer){
        time = new Time(currentTime);
        this.health = Integer.parseInt(health);
        this.hints = Integer.parseInt(hints);
        healthText.setText("Жизни: " + health + "/3");
        hintsText.setText("Подсказки: " + hints + "/3");
        this.sudokuAnswer = sudokuAnswer;
        setSudoku(grid);
    }
    public void initializeGrid(){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                Tile tile = new Tile();
                mainGrid.add(tile,i,j);
                tile.setAlignment(Pos.CENTER);
                tile.setPrefHeight(50);
                tile.setEditable(false);
                tile.setFont(Font.font("ROBOTO", FontWeight.BLACK, 20));
                tile.setStyle(style.tileStyle(i,j));
                tileArray[i][j] = tile;
                tile.setX(i);
                tile.setY(j);
                tile.setOnMouseClicked(this::tileSelected);
                tile.setOnKeyPressed(this::handleKey);
            }
        }
    }

    private void setSudoku(String sudoku){
        int c = 0;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (!String.valueOf(sudoku.charAt(c)).equals("0")) {
                    tileArray[j][i].setText(String.valueOf(sudoku.charAt(c)));
                    tileArray[j][i].setChangeable(false);
                }
                tileArray[j][i].setPosition(c);
                c++;
            }
        }
    }

    private void tileSelected(MouseEvent mouseEvent) {
        if (lastSelected != null) {
            setNormalStyle(lastSelected.getX(), lastSelected.getY());
        }
        lastSelected = (Tile) mouseEvent.getSource();
        setHighlightStyle(lastSelected.getX(), lastSelected.getY());
    }

    private void setNormalStyle(int x, int y){
        if (!tileArray[x][y].isWrong()) lastSelected.setStyle(style.tileStyle(x, y));
        for (int i = 0; i < 9; ++i) if (i != y && !tileArray[x][i].isWrong()) tileArray[x][i].setStyle(style.tileStyle(x,i));
        for (int i = 0; i < 9; ++i) if (i != x && !tileArray[i][y].isWrong()) tileArray[i][y].setStyle(style.tileStyle(i,y));
        int xFrom = (x / 3) * 3;
        int yFrom = (y / 3) * 3;
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j) {
                if (x != i+xFrom || j+yFrom != y) {
                    if (!tileArray[i+xFrom][j+yFrom].isWrong()) tileArray[i + xFrom][j + yFrom].setStyle(style.tileStyle(i + xFrom, j + yFrom));
                }
            }
    }
    private void setHighlightStyle(int x, int y){
        if (!tileArray[x][y].isWrong()) lastSelected.setStyle(style.getMainHighlightStyle(x, y));
        for (int i = 0; i < 9; ++i) if (i != y && !tileArray[x][i].isWrong()) tileArray[x][i].setStyle(style.getHighlightStyle(x,i));
        for (int i = 0; i < 9; ++i) if (i != x && !tileArray[i][y].isWrong()) tileArray[i][y].setStyle(style.getHighlightStyle(i,y));
        int xFrom = (x / 3) * 3;
        int yFrom = (y / 3) * 3;
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j) {
                if (x != i + xFrom || j + yFrom != y) {
                    if (!tileArray[i+xFrom][j+yFrom].isWrong()) tileArray[i + xFrom][j + yFrom].setStyle(style.getHighlightStyle(i + xFrom, j + yFrom));
                }
            }
    }
    public void handleKey(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_PRESSED){
            if (event.getCode() == KeyCode.P) pause();
            else if (lastSelected != null) {
                if (event.getText().matches("[1-9]")) {
                    int value = Integer.parseInt(event.getText());
                    if (!lastSelected.getText().equals(event.getText()) && lastSelected.isChangeable() && !isGameFinished) {
                        lastSelected.setText(Integer.toString(value));
                        checkNumber();
                    } else if (lastSelected.isChangeable() && !isGameFinished) {
                        lastSelected.clear();
                        checkNumber();
                    }
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    ((TextField) event.getSource()).setText("");
                    lastSelected.setWrong(false);
                } else if (event.getCode() == KeyCode.UP && lastSelected.getY() != 0) {
                    setNormalStyle(lastSelected.getX(), lastSelected.getY());
                    lastSelected = tileArray[lastSelected.getX()][lastSelected.getY() - 1];
                    setHighlightStyle(lastSelected.getX(), lastSelected.getY());
                } else if (event.getCode() == KeyCode.DOWN && lastSelected.getY() != 8) {
                    setNormalStyle(lastSelected.getX(), lastSelected.getY());
                    lastSelected = tileArray[lastSelected.getX()][lastSelected.getY() + 1];
                    setHighlightStyle(lastSelected.getX(), lastSelected.getY());
                } else if (event.getCode() == KeyCode.LEFT && lastSelected.getX() != 0) {
                    setNormalStyle(lastSelected.getX(), lastSelected.getY());
                    lastSelected = tileArray[lastSelected.getX() - 1][lastSelected.getY()];
                    setHighlightStyle(lastSelected.getX(), lastSelected.getY());
                } else if (event.getCode() == KeyCode.RIGHT && lastSelected.getX() != 8) {
                    setNormalStyle(lastSelected.getX(), lastSelected.getY());
                    lastSelected = tileArray[lastSelected.getX() + 1][lastSelected.getY()];
                    setHighlightStyle(lastSelected.getX(), lastSelected.getY());
                }
            }
        }
        event.consume();
    }
    @FXML
    public void backToMenu() {
        if (!isGameFinished()) saveGame();
        mainWindowController.switchPane(mainWindowController.getMenuPane());
        mainWindowController.checkFile();
    }
    @FXML
    public void pause() {
        if (!isGameFinished) {
            timeline.pause();
            mainPane.setEffect(new BoxBlur(10, 10, 10));
            initModalWindow(Type.PAUSE);
        }
    }

    private void initModalWindow(Type type){
        double x = mainPane.getScene().getWindow().getX();
        double y = mainPane.getScene().getWindow().getY();
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("view/modal.fxml")));
            Parent root = loader.load();
            ModalWindowController modal = loader.getController();
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), root);
            scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
            scaleTransition.setFromX(0);
            scaleTransition.setFromY(0);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            Stage stage = new Stage();
            modal.initialize(stage, mainPane, type, this);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            stage.setX(x+250);
            stage.setY(y+200);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    public void buttonClicked(ActionEvent event){
        if (lastSelected != null && !isGameFinished) {
            lastSelected.requestFocus();
            Button button = (Button) event.getSource();
            if (lastSelected.getText().equals(button.getText()) && lastSelected.isChangeable()) {
                lastSelected.clear();
            } else if (lastSelected.isChangeable()) {
                lastSelected.setText(button.getText());
            }
            checkNumber();
            button.requestFocus();
        }
    }
    @FXML
    public void hint(){
        if (lastSelected != null && hints != 0 && lastSelected.getText().equals("") && !isGameFinished) {
            lastSelected.setText(String.valueOf(sudokuAnswer.charAt(lastSelected.getPosition())));
            decreaseHints();
        } else if (hints != 0 && !isGameFinished) {
            while (true) {
                int x = random.nextInt(8);
                int y = random.nextInt(8);
                if (tileArray[y][x].isChangeable()) {
                    tileArray[y][x].setText(String.valueOf(sudokuAnswer.charAt(tileArray[y][x].getPosition())));
                    decreaseHints();
                    return;
                }
            }
        }
    }

    private void decreaseHints(){
        hints--;
        hintsText.setText("Подсказки: " + hints + "/3");
    }

    private void checkNumber(){
        if (lastSelected.getText().equals(String.valueOf(sudokuAnswer.charAt(lastSelected.getPosition()))) || lastSelected.getText().equals("")) {
            lastSelected.setWrong(false);
            lastSelected.setStyle(style.getHighlightStyle(lastSelected.getX(), lastSelected.getY()));
            checkSolution();
        }
        else {
            decreaseHealth();
            lastSelected.setWrong(true);
            lastSelected.setStyle(style.getWrongStyle(lastSelected.getX(), lastSelected.getY()));
        }
    }

    private void decreaseHealth(){
        health--;
        healthText.setText("Жизни: " + health + "/3");
        if (health == 0) {
            timeline.stop();
            initModalWindow(Type.LOSE);
            isGameFinished = true;
        }
    }

    public boolean isCompleted(){
        for(int i = 0; i < 9; ++i) for (int j = 0; j < 9; ++j) if (tileArray[i][j].getText().equals("")) return false;
        return true;
    }
    private void checkSolution(){
        if (isCompleted()){
            String solution = "";
            for (int j = 0; j < 9; j++){
                for (int i = 0; i < 9; i++){
                    solution += tileArray[i][j].getText();
                }
            }
            if (solution.equals(sudokuAnswer)) {
                timeline.stop();
                initModalWindow(Type.WIN);
                isGameFinished = true;
            }
        }
    }
    public void saveGame(){
        if (!isGameFinished) {
            String grid = "";
            for (int j = 0; j < 9; j++){
                for (int i = 0; i < 9; i++){
                        if (!Objects.equals(tileArray[i][j].getText(), "") && !tileArray[i][j].isWrong()) {
                            grid += tileArray[i][j].getText();
                        } else grid += "0";
                    }
                }
            try (FileWriter writer = new FileWriter("game.dat", false)) {
                writer.write(grid);
                writer.write("\n");
                writer.write(sudokuAnswer);
                writer.write("\n");
                writer.write(Integer.toString(health));
                writer.write("\n");
                writer.write(Integer.toString(hints));
                writer.write("\n");
                writer.write(time.getCurrentTime());
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public Time getTime(){
        return time;
    }

    public boolean isGameFinished(){
        return isGameFinished;
    }

    public void fileDelete() throws IOException {
        Files.deleteIfExists(Path.of("game.dat"));
    }
}
