import dungeon.engine.entities.Gold;
import dungeon.engine.entities.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testGold {



        @Test
        void testGetSymbol() {
            Gold test = new Gold();

            assertEquals('G', test.getSymbol());
        }

        @Test
        void testOnVisit() {
            Gold test = new Gold();
            Player fauxPlayer = new Player();
            String expectedMessage = "    'OOo piece of candy.'" +
                                    "\n It turns out to be gold" +
                                   "\n       Gain 2 Score";
            assertEquals(expectedMessage, test.onVisit(fauxPlayer));
        }
}

