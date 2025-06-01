package dungeon.gui;

import dungeon.engine.GameEngine;
import dungeon.engine.entities.*;


import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.layout.GridPane;


public class Controller {

    private GameEngine dungeon;

    @FXML
    public GridPane grid;
    @FXML
    public TextArea logArea;
    @FXML
    public Label hpLabel;
    @FXML
    public Label stepsLabel;
    @FXML
    public Label scoreLabel;
    @FXML
    public Label difficulty;

    @FXML
    private void moveUp()    { moveAndUpdate("u"); }
    @FXML
    private void moveDown()  { moveAndUpdate("d"); }
    @FXML
    private void moveLeft()  { moveAndUpdate("l"); }
    @FXML
    private void moveRight() { moveAndUpdate("r"); }



    public void setDungeon(GameEngine dungeon) {
        this.dungeon = dungeon;
        updateGrid();
        updateStats();
        logArea.setEditable(false);
    }

    private void log(String message) {
        logArea.appendText(message + "\n");

    }

    void moveWithKey(String direction){
        moveAndUpdate(direction);
    }

    private void moveAndUpdate(String direction) {
        String result = dungeon.move(direction);
        log(result);
        updateGrid();
        updateStats();
    }

    private void updateGrid() {
        grid.getChildren().clear();
        Entity[][] board = dungeon.getGrid();
        Player player = dungeon.getPlayer();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Label label = new Label();
                label.setMinSize(30, 30);
                label.setAlignment(Pos.CENTER);
                label.setStyle("-fx-border-color: black; -fx-font-size: 14px;");


                Entity entity = board[row][col];
                if (player.getX() == row && player.getY() == col) {
                    label.setText("P");
                    label.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
                } else if (entity != null) {
                    label.setText(Character.toString(entity.getSymbol()));
                    if(entity instanceof Wall){
                        label.setStyle("-fx-background-color: black;");
                    }
                }

                grid.add(label, col, row);
            }
        }
    }
    private void updateStats() {
        Player player = dungeon.getPlayer();
        hpLabel.setText("HP: " + player.getHP());
        stepsLabel.setText("Steps: " + player.getSteps());
        scoreLabel.setText("Score: " + player.getScore());
        difficulty.setText("Difficulty: "+ dungeon.getDifficulty());
    }

}
