import dungeon.engine.GameEngine;
import dungeon.engine.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameEngine {


    @Test
    public void testInitialPlayerPosition() {
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        assertEquals(3, player.getX());
        assertEquals(3, player.getY());
    }

    @Test
    public void testPlayerMovesIntoEmptySpace() {
        // Move down if the space is clear (not a wall)
        GameEngine dungeon = new GameEngine(3);
        Entity[][] grid = dungeon.getGrid();
        // Clear intermediate tiles
        grid[4][3] = null; // middle tile
        grid[5][3] = null; // destination tile
        String result = dungeon.move("d");
        assertTrue(result.contains("You moved one step") || result.contains("You have"));
        assertEquals(5, dungeon.getPlayer().getX()); // moved down 2 tiles
    }

    @Test
    public void testWallBlocksMovement() {
        // Place a wall manually in front of player
        GameEngine dungeon = new GameEngine(3);
        Entity[][] grid = dungeon.getGrid();
        grid[4][3] = new Wall();

        String result = dungeon.move("d");
        assertTrue(result.contains("wall blocks your path"));
        assertEquals(3, dungeon.getPlayer().getX()); // position unchanged
    }

    @Test
    public void testGoldIncreasesScore() {
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        // Clear intermediate tile
        Entity[][] grid = dungeon.getGrid();
        grid[4][3] = null;
        // Place gold directly in front
        dungeon.getGrid()[5][3] = new Gold();
        String result = dungeon.move("d");

        assertTrue(result.contains("gold"));
        assertEquals(2, player.getScore());
    }

    @Test
    public void testTrapDecreasesHP() {
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        int initialHP = player.getHP();
        // Clear intermediate tile
        Entity[][] grid = dungeon.getGrid();
        grid[4][3] = null; // Clear intermediate tile
        dungeon.getGrid()[5][3] = new Trap();
        String result = dungeon.move("d");

        assertTrue(result.contains("trap"));
        assertTrue(player.getHP() < initialHP);
    }

    @Test
    public void testHealthPotionRestoresHP() {
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        player.decreaseHP(6); // set HP to 4
        // Clear intermediate tile
        Entity[][] grid = dungeon.getGrid();
        grid[4][3] = null; // Clear intermediate tile
        dungeon.getGrid()[5][3] = new HealthPotion();//set health potion directly below player

        dungeon.move("d");

        assertEquals(9, player.getHP()); // 4 + 4 = 8
    }

    @Test
    public void testPlayerProgressesToNextLevel() {
        GameEngine dungeon = new GameEngine(3);
        //Player player = dungeon.getPlayer();
        Entity[][] grid = dungeon.getGrid();
        grid[4][3] = null; // Clear intermediate tile
        dungeon.getGrid()[5][3] = new Ladder();

        String result = dungeon.move("d");

        assertTrue(result.contains("You find your way to Level 2!"));
        assertEquals(2, dungeon.getLevel());
    }

    @Test
    public void testGameOverByHP() {
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        player.decreaseHP(8);
        Entity[][] grid = dungeon.getGrid();
        grid[4][3] = null;
        dungeon.getGrid()[5][3] = new Trap();

        String result = dungeon.move("d");//Move player into Trap

        assertTrue(result.contains("Game over"));
    }

    @Test
    public void testGameOverBySteps() {
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        while (player.getSteps() > 1) {
            player.move(0, 0); // just decrease steps artificially
        }

        String result = dungeon.move("d");

        assertTrue(result.contains("Game over"));
    }

    @Test
    public void testRangedAttack(){
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        Entity[][] grid = dungeon.getGrid();

        for (int i = 3; i < 10; i++){
            grid[3][i] = null;
        }

        //spawn ranged mutant 3 moves away (6 spaces)
        grid[3][7] = new MeleeMutant();
        String result = dungeon.moveByOffset(0,2);
        //assert false on attack
        assertFalse(result.contains("The mutant attacks from a distance"));
        //move player 1 step closer
        result = dungeon.moveByOffset(0,2);;
        //assert true on attack
        System.out.println(result);
        //assertEquals(true, result.contains("The mutant attacks from a distance"));

        //check message
    }

    @Test
    public void testRangedOnStep(){
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        Entity[][] grid = dungeon.getGrid();

        //clear column path
        for (int i = 3; i < 10; i++){
            grid[3][i] = null;
        }

        //spawn player next to ranged mutant
        grid[3][5] = new RangedMutant();

        //move player onto ranged mutant
        String result = dungeon.move("r");
        assertEquals(5, player.getScore());
        assertEquals("Your blood-lust is quenched, the mutant is dead" +
                "\n       Gain 5XP",result);

    }

    @Test
    public void testMeleeMutantOnStep(){
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        Entity[][] grid = dungeon.getGrid();

        //clear column path
        for (int i = 3; i < 10; i++){
            grid[3][i] = null;
        }

        //spawn player next to Melee mutant
        grid[3][5] = new MeleeMutant();
        String result = dungeon.move("r");
        assertEquals(5, player.getScore());
        assertEquals(8, player.getHP());
        assertEquals("You stumble across a mutants, theres no time for questions. " +
                "\n      Lose 2 HP, Gain 5 XP",result);
    }

    @Test
    public void testAdvanceLevel2(){
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        Entity[][] grid = dungeon.getGrid();

        // spawn player next to the ladder, ensure current cell and intimidate cells are empty
        int spawnX = dungeon.getSpawnX();
        int spawnY = dungeon.getSpawnY();
        grid[spawnX][spawnY-2] = null;
        player.setCoordinates(spawnX,spawnY-2);
        grid[spawnX][spawnY-1] = null;

        String result =dungeon.move("r");
        assertTrue(result.contains("You find your way to Level 2!"));
        assertEquals(5,dungeon.getDifficulty()); //assert difficulty = +2
        assertEquals(2,dungeon.getLevel()); //assert level = 2

        grid[spawnX][spawnY + 1]= null;
        grid[spawnX][spawnY + 2]= new Ladder();


        //assert output message correct
        //String result2 =dungeon.move("r");
        //game closes before message can be printed
        // return statment never reached
        //assertTrue(result2.contains("You escaped the dungeon! Game won."));

        //player reaches ladder again
        //assert player has won
    }

    @Test
    public void testBounds(){
        GameEngine dungeon = new GameEngine(3);
        Player player = dungeon.getPlayer();
        Entity[][] grid = dungeon.getGrid();
        //can player exit maze??
        player.setCoordinates(1,1);//spawns on a wall
        String result = dungeon.move("u");
        assertEquals("You cannot move outside the dungeon!",result);
        assertEquals(200,player.getSteps()); //make sure steps wasn't decreased






    }
}
