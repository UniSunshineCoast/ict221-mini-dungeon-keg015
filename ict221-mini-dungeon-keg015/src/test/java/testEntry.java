import dungeon.engine.entities.Entry;
import dungeon.engine.entities.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class testEntry {


    @Test
    void testGetSymbol() {
        Entry test = new Entry();

        assertEquals('E', test.getSymbol());
    }
    @Test
    void testOnVisit(){
        Entry test = new Entry();
        Player fauxPlayer = new Player();
        String expectedMessage = "If you are reading this, something has gone wrong";
        assertEquals(expectedMessage, test.onVisit(fauxPlayer));
    }
}


