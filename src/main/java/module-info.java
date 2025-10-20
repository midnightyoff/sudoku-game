module com.example.sudokugame {
    requires javafx.controls;
    requires javafx.fxml;


    opens sudokugame to javafx.fxml;
    exports sudokugame;
    exports sudokugame.model.tile;
    opens sudokugame.model.tile to javafx.fxml;
    exports sudokugame.model;
    opens sudokugame.model to javafx.fxml;
    exports sudokugame.controller;
    opens sudokugame.controller to javafx.fxml;
}