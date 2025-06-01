import dungeon.engine.entities.Player;
import dungeon.engine.entities.Trap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testTrap {

    @Test
    void testGetSymbol() {
    Trap test = new Trap();

    assertEquals('T', test.getSymbol());
}
    @Test
    void testOnVisit(){
        Trap test = new Trap();
        Player fauxPlayer = new Player();
        String expectedMessage = "    'Oww that Hurt.' " +
                "\nYou fell into a trap." +
                "\n       Loose 2 HP";
        assertEquals(expectedMessage, test.onVisit(fauxPlayer));
    }
}
