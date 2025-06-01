import dungeon.gui.GameGUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGameGUI {

    @Test
    public void testDifficulty(){
        GameGUI.setDifficulty(5);
        assertEquals(5, GameGUI.getDifficulty());
    }

    //the following tests will not work because they rely on user interactions.
    @Test
    public void testDifficultySlider() throws Exception {
        GameGUI.openStart();
        //test that the slider exists
        //test for a non int number

    }

    @Test
    public void testOpenStartButtons() throws Exception{
        GameGUI.openStart();
        //test label is correct
        //test on click
    }

    @Test
    public void testGameStart_Controller() throws Exception{
        GameGUI.gameStart();

    }
}
