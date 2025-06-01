import dungeon.engine.GameEngine;
import dungeon.engine.entities.Entity;
import dungeon.engine.entities.Player;
import dungeon.engine.entities.*;
import dungeon.gui.Controller;


import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestController {
    private Controller controller;
    private GameEngine engine;
    /*
    @BeforeEach
    public void setup(){

        controller = new Controller();
        engine = new GameEngine(3);

        //controller.hpLabel = new Label();
        //controller.stepsLabel = new Label();
        //controller.scoreLabel = new Label();
        //controller.difficulty = new Label();
        //controller.logArea = new TextArea();
        //controller.grid = new GridPane();

        //controller.setDungeon(engine);
    }

    */

    @Test
    public void testLabelChange(){
        controller = new Controller();
        engine = new GameEngine(3);
        Entity[][] grid = engine.getGrid();
        controller.setDungeon(engine);
        Player player = new Player(); //?

        assertTrue(controller.hpLabel.getText().startsWith("HP: "));
        assertTrue(controller.stepsLabel.getText().startsWith("Steps: "));
        assertTrue(controller.scoreLabel.getText().startsWith("Score: "));
        assertTrue(controller.difficulty.getText().contains("Difficulty: "));

        //assertEquals("HP: 10", controller.hpLabel.getText());
        //assertEquals("Steps: 200", controller.stepsLabel.getText());
        //10,200,0,1

        String originalHP = controller.hpLabel.getText();
        String originalSteps = controller.stepsLabel.getText();
        String originalScore = controller.scoreLabel.getText();
        String originalDifficulty = controller.difficulty.getText();

        // clear row player will walk
        for (int i = 3; i < 20; i++){
            grid[i][3] = null;
            //set wall below test line
            grid[i][5] = new Wall();
        }

        // set gold Right of player
        grid[5][3] = new Gold();
        // set trap Right of gold
        grid[7][3] = new Trap();
        // set ladder Right of trap
        grid[9][3] = new Ladder();




    }


}
