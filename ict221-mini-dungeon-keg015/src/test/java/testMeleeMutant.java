import dungeon.engine.entities.MeleeMutant;
import dungeon.engine.entities.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testMeleeMutant{

    @Test
    void testGetSymbol () {
        MeleeMutant test = new MeleeMutant();

        assertEquals('E', test.getSymbol());
    }
    @Test
    void testOnVisit () {
        MeleeMutant test = new MeleeMutant();
        Player fauxPlayer = new Player();
        assertEquals(10,fauxPlayer.getHP());
        assertEquals(0,fauxPlayer.getScore());
        String expectedMessage = "You stumble across a mutants, theres no time for questions. " +
                "\n      Lose 2 HP, Gain 5 XP";
        assertEquals(expectedMessage, test.onVisit(fauxPlayer));
        assertEquals(8,fauxPlayer.getHP());
        assertEquals(5,fauxPlayer.getScore());

    }
}