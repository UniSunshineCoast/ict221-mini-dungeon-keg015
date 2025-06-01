import dungeon.engine.entities.HealthPotion;
import dungeon.engine.entities.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testHealthPotion {
    @Test
    void testGetSymbol() {
        HealthPotion test = new HealthPotion();

        assertEquals('H', test.getSymbol());
    }
    @Test
    void testOnVisit(){
        HealthPotion test = new HealthPotion();
        Player fauxPlayer = new Player();
        String expectedMessage = "    'I wonder what this tastes like.'" +
                                "\nYou used a health potion." +
                                "\n       Gain 5 HP";
        assertEquals(expectedMessage, test.onVisit(fauxPlayer));
    }
}
