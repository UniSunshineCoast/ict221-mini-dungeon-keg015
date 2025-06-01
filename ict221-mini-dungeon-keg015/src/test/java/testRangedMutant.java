import dungeon.engine.entities.Player;
import dungeon.engine.entities.RangedMutant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testRangedMutant {
    @Test
    void testGetSymbol() {
        RangedMutant test = new RangedMutant();

        assertEquals('R', test.getSymbol());
    }
    @Test
    void testOnVisit(){
        //Random random = new Random();
        RangedMutant test = new RangedMutant();
        Player fauxPlayer = new Player();
        String expectedMessage = "Your blood-lust is quenched, the mutant is dead" +
        "\n       Gain 5XP";
        assertEquals(expectedMessage, test.onVisit(fauxPlayer));
    }
}
