
import dungeon.engine.entities.Player;
import dungeon.engine.entities.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testWall {
    @Test
    void testGetSymbol() {
        Wall test = new Wall();

        assertEquals('#', test.getSymbol());
    }
    @Test
    void testOnVisit(){
        Wall test = new Wall();
        Player fauxPlayer = new Player();
        String expectedMessage = "You smash your face against a wall, those things tend to be solid.";
        assertEquals(expectedMessage, test.onVisit(fauxPlayer));
    }
}
