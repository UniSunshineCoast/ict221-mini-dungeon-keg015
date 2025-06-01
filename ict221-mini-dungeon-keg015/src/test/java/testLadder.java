
import dungeon.engine.entities.Ladder;
import dungeon.engine.entities.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testLadder {
    @Test
    void testGetSymbol() {
        Ladder test = new Ladder();

        assertEquals('L', test.getSymbol());
    }
    @Test
    void testOnVisit(){
        Ladder test = new Ladder();
        Player fauxPlayer = new Player();
        String expectedMessage = "You Have found a Ladder. " +
                "\n   'this is a bad time to be afraid of heights'";
        assertEquals(expectedMessage, test.onVisit(fauxPlayer));
    }
}
