module com.example.sudokugame {
    requires javafx.controls;
    requires javafx.fxml;


    opens sudokugame to javafx.fxml;
    exports sudokugame;
    exports sudokugame.tile;
    opens sudokugame.tile to javafx.fxml;
}