package sudokugame.tile;

public class Style {
    private final String[][] tileStyle = new String[9][9];
    public Style(){
        initStyle();
    }
    private void initStyle(){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                tileStyle[i][j] = "-fx-background-color: #00000025; -fx-display-caret: false; ";
                if (j == 0) {
                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 2 0 0 2; -fx-border-color: black";
                    else if (i == 8) tileStyle[i][j] += "-fx-border-width: 2 2 0 0; -fx-border-color: black";
                    else tileStyle[i][j] += "-fx-border-width: 2 0 0 0; -fx-border-color: black";
                }
                if (j == 1) {
                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 0 0 0 2; -fx-border-color: black";
                    if (i == 8) tileStyle[i][j] += "-fx-border-width: 0 2 0 0; -fx-border-color: black";
                }
                if (j == 2) {

                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 0 0 2 2; -fx-border-color: black";
                    else if (i == 8) tileStyle[i][j] += "-fx-border-width: 0 2 2 0; -fx-border-color: black";
                    else tileStyle[i][j] += "-fx-border-width: 0 0 2 0; -fx-border-color: black";
                }
                if (j == 3){
                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 0 0 0 2; -fx-border-color: black";
                    if (i == 8) tileStyle[i][j] += "-fx-border-width: 0 2 0 0; -fx-border-color: black";
                }
                if (j == 4){
                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 0 0 0 2; -fx-border-color: black";
                    if (i == 8) tileStyle[i][j] += "-fx-border-width: 0 2 0 0; -fx-border-color: black";
                }
                if (j == 5){
                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 0 0 2 2; -fx-border-color: black";
                    else if (i == 8) tileStyle[i][j] += "-fx-border-width: 0 2 2 0; -fx-border-color: black";
                    else tileStyle[i][j] += "-fx-border-width: 0 0 2 0; -fx-border-color: black";
                }
                if (j == 6){
                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 0 0 0 2; -fx-border-color: black";
                    if (i == 8) tileStyle[i][j] += "-fx-border-width: 0 2 0 0; -fx-border-color: black";
                }
                if (j == 7){
                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 0 0 0 2; -fx-border-color: black";
                    if (i == 8) tileStyle[i][j] += "-fx-border-width: 0 2 0 0; -fx-border-color: black";
                }
                if (j == 8){
                    if (i == 0 || i == 3 || i == 6) tileStyle[i][j] += "-fx-border-width: 0 0 2 2; -fx-border-color: black";
                    else if (i == 8) tileStyle[i][j] += "-fx-border-width: 0 2 2 0; -fx-border-color: black";
                    else tileStyle[i][j] += "-fx-border-width: 0 0 2 0; -fx-border-color: black";
                }
            }
        }
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                if (tileStyle[i][j] == null) tileStyle[i][j] = "-fx-display-caret: false";
            }
        }
    }
    public String tileStyle(int i,int j){
        return tileStyle[i][j];
    }

    public String getMainHighlightStyle(int i, int j){
        return  tileStyle[i][j] + "; -fx-background-color: #00000125 ; -fx-background-insets: 1";
    }
    public String getHighlightStyle(int i, int j){
        return  tileStyle[i][j] + "; -fx-background-color: #00000100; -fx-background-insets: 1";
    }
    public String getWrongStyle(int i, int j){
        return tileStyle[i][j] + "; -fx-background-color: #DB5656; -fx-background-insets: 0.5";
    }
}
