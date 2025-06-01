package dungeon.gui;

import dungeon.engine.GameEngine;
import dungeon.engine.HighScore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * GUI for the Maze Runner Game.
 * NOTE: Do NOT run this class directly in IntelliJ - run 'RunGame' instead.
 */
public class GameGUI extends Application {
    static int gameDifficulty = 3;
    private static Stage gameStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        openStart();
    }

    /** Opens the game start menu, Allows player to choose the difficulty
     *
     */
    public static void openStart() throws Exception {//can this be moved to the gameEngine??
        Stage startWindow = new Stage();
        startWindow.initModality(Modality.APPLICATION_MODAL);
        startWindow.setTitle("Start Menu");

        //Difficulty slider
        Slider selectDifficulty = new Slider(1,10,3);
        selectDifficulty.setShowTickLabels(true);
        selectDifficulty.setShowTickMarks(true);
        selectDifficulty.setSnapToTicks(true);
        selectDifficulty.setMinorTickCount(0);
        selectDifficulty.setMajorTickUnit(1);

        Label chosenDifficulty = new Label("Difficulty: " + (int) selectDifficulty.getValue());

        selectDifficulty.valueProperty().addListener((observation, preset, chosen) -> {
                    chosenDifficulty.setText("Difficulty: " + chosen.intValue());
                });

        Label introduction = new Label("Press Start to Play");
        Button startButton = new Button("Start Game");
        Button cancelButton = new Button("Cancel");

        startButton.setOnAction(e -> startWindow.close());
        cancelButton.setOnAction(e-> System.exit(0));

        HBox holder = new HBox(10, startButton, cancelButton);
        holder.setStyle("-fx-alignment: center;");

        VBox layout = new VBox(10,selectDifficulty,chosenDifficulty, introduction, holder);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 300,200);

        startWindow.setScene(scene);
        startWindow.showAndWait();
        setDifficulty((int) selectDifficulty.getValue());
        gameStart();
    }

    public static void gameStart() throws Exception {
        if (gameStage != null) {
            gameStage.close();
        }
        gameStage = new Stage();
        FXMLLoader loader = new FXMLLoader(GameGUI.class.getResource("game_gui.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        GameEngine dungeon = new GameEngine(getDifficulty()); // default difficulty
        controller.setDungeon(dungeon);


        Scene scene = new Scene(root);
        // not working properly 15.9

        gameStage.setTitle("MiniDungeon Game");
        gameStage.setScene(scene);
        gameStage.show();


    }

    /** implemented when the player runs out of health or steps
     *
     */
    public static void GameOver(int type){

        if (gameStage != null){
            gameStage.close();
        }

        Stage gameOver = new Stage();
        gameOver.initModality(Modality.APPLICATION_MODAL);
        gameOver.setTitle("Failure Window");

        Label failMessage = new Label(type == 1 ? "You fainted": "You Died");

        Button tryAgain = new Button("Try Again");
        tryAgain.setOnAction(e-> {
            gameOver.close();
            try {
                openStart();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        
        Button giveUp = new Button("Give Up");
        giveUp.setOnAction(e-> System.exit(0));

        HBox holder = new HBox(10,tryAgain,giveUp);
        holder.setStyle("-fx-alignment: center;");

        VBox layout = new VBox(10, failMessage,holder);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout,200,300);
        gameOver.setScene(scene);
        gameOver.showAndWait();
    }

    /** implemented when the player has found the second ladder
     *
     */
    public static void GameComplete(double finalScore){
        if (gameStage != null) {
            gameStage.close();
        }

        Stage gameComplete = new Stage();
        gameComplete.initModality(Modality.APPLICATION_MODAL);
        gameComplete.setTitle("Win Window");

        Label winMessage = new Label("You escaped the dungeon! Game won.");
        Label gameScore = new Label("Your Score: "+ finalScore);
        Label prompt = new Label("Enter your name:");
        TextField nameField = new TextField();

        Button submitButton = new Button("Submit Score");
        Button playAgain = new Button("Play Again");
        playAgain.setOnAction(e-> {
            gameComplete.close();
            try {
                openStart();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }); //start game again

        Button endSession = new Button("Exit");
        endSession.setOnAction(e-> System.exit(0));

        VBox leaderboardBox = new VBox(5);
        leaderboardBox.setStyle("-fx-alignment: center;");

        submitButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                HighScore highScore = new HighScore();
                highScore.addScore(name, (int) finalScore);

                leaderboardBox.getChildren().clear();
                leaderboardBox.getChildren().add(new Label("Top Scores:"));
                for (HighScore.Score score : highScore.getScores()) {
                    leaderboardBox.getChildren().add(new Label(score.toString()));
                }
                // Disable name input after submission
                nameField.setDisable(true);
                submitButton.setDisable(true);
            }
        });

        endSession.setOnAction(e -> System.exit(0));

        HBox buttonRow = new HBox(10, playAgain, endSession);
        buttonRow.setStyle(" -fx-alignment: center;");

        VBox layout = new VBox(10, winMessage, gameScore, prompt, nameField, submitButton, leaderboardBox, buttonRow);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Scene scene = new Scene(layout,400,300);
        gameComplete.setScene(scene);
        gameComplete.showAndWait();
    }

    public static int getDifficulty(){
        return gameDifficulty;
    }
    public static void setDifficulty(int difficulty){
        gameDifficulty = difficulty;
    }


    /** In IntelliJ, do NOT run this method.  Run 'RunGame.main()' instead. */
    public static void main(String[] args) {
        launch(args);
    }
}
