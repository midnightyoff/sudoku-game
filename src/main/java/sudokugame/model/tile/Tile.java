package sudokugame.model.tile;

import javafx.scene.control.TextField;

public class Tile extends TextField {
    private int x, y;
    private int position;
    private boolean isChangeable = true;
    private boolean isWrong = false;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isChangeable() {
        return isChangeable;
    }

    public void setChangeable(boolean changeable) {
        isChangeable = changeable;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isWrong() {
        return isWrong;
    }

    public void setWrong(boolean wrong) {
        isWrong = wrong;
    }

}
