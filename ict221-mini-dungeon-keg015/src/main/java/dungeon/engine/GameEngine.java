package dungeon.engine;

import dungeon.engine.entities.*;
import dungeon.gui.GameGUI;


import java.util.*;

public class GameEngine {
    private static final int GRID_SIZE = 21;

    private int spawnX = 3;
    private int spawnY = 3;

    private Entity[][] grid = new Entity[GRID_SIZE][GRID_SIZE];
    private Player player;
    private int level = 1;
    private int difficulty;
    private Random random = new Random();
    private Set<String> usedPositions = new HashSet<>();


    public GameEngine(int difficulty) {
        this.difficulty = difficulty;
        this.player = new Player();
        generateMap(spawnX, spawnY);
    }

    /** Build and populate the map
     *
     * @param playerStartX the current X location of the player
     * @param playerStartY the current Y location of the player
     *
     *
     */
    private void generateMap(int playerStartX, int playerStartY) {
        usedPositions.clear();
        player.setCoordinates(playerStartX,playerStartY);
        usedPositions.add(getKey(playerStartX,playerStartY));

        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(grid[i], null);
        }

        for (int i = 0; i < GRID_SIZE; i++) {
            grid[0][i] = new Wall();
            grid[GRID_SIZE - 1][i] = new Wall();
            grid[i][0] = new Wall();
            grid[i][GRID_SIZE - 1] = new Wall();
            usedPositions.add(getKey(i, 0));
            usedPositions.add(getKey(i, GRID_SIZE - 1));
            usedPositions.add(getKey(0, i));
            usedPositions.add(getKey(GRID_SIZE - 1, i));

            for (int j = 0; j < GRID_SIZE; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    grid[i][j] = new Wall();
                    usedPositions.add(getKey(i, j));
                }
            }
        }
        //get the ladders spawn, to be used for the players spawn on level 2
        Ladder ladder = new Ladder();
        placeRandomEntity(ladder);
        spawnX = ladder.getX();
        spawnY = ladder.getY();

        //spawn all the other entities
        placeRandomEntities(new Trap(), 5);
        placeRandomEntities(new Gold(), 5);
        placeRandomEntities(new MeleeMutant(), 3);
        placeRandomEntities(new HealthPotion(), 2);
        placeRandomEntities(new RangedMutant(), difficulty);
        placeRandomEntities(new Wall(), difficulty);
        placeRandomWalls(10 + (difficulty * 4));
    }

    /** Spawn a specified number of entities on the grid.
     * Uses placeRandomEntities to place them on the grid.
     *
     * @param entity entity type
     * @param count number of entities to spawn
     */
    private void placeRandomEntities(Entity entity, int count) {
        for (int i = 0; i < count; i++) {
            placeRandomEntity(cloneEntity(entity));
        }
    }
    //5.6

    /** get an unused location that is not the player and spawns entities individually
     *
     * @param entity type of entity to spawn
     */
    private void placeRandomEntity(Entity entity) {
        int x, y, newX, newY;
        do {
            x = random.nextInt(10);
            y = random.nextInt(10);
            newX = x * 2 + 1;
            newY = y * 2 + 1;
        } while (isOccupied(newX, newY) || (newX == player.getX() && newY == player.getY()));
        // if occupied OR equal to the players location, pick another spot

        entity.setCoordinates(newX, newY);
        grid[newX][newY] = entity;
        usedPositions.add(getKey(newX, newY));
    }

    /** places the internal walls randomly
     *
     * @param count number of walls to be placed in the maze
     */
    private void placeRandomWalls(int count) {
        int current = 0;
        while (current < count) {
            int x1 = random.nextInt(10) * 2;
            int y1 = (random.nextInt(10) * 2) + 1;
            int x2 = (random.nextInt(10) * 2) + 1;
            int y2 = random.nextInt(10) * 2;

            Wall wall = new Wall();
            if (!isOccupied(x1, y1)) {
                grid[x1][y1] = wall;
                usedPositions.add(getKey(x1, y1));
                current++;
            }
            if (!isOccupied(x2, y2) && current < count) {
                grid[x2][y2] = wall;
                usedPositions.add(getKey(x2, y2));
                current++;
            }
        }
    }

    /** Check if a space is occupied
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return boolean
     */
    private boolean isOccupied(int x, int y) {
        return grid[x][y] != null;
    }

    /** Creates a clone of an entity to be spawned in the grid
     *
     * @param entity type of entity to be cloned
     * @return returns a new entity
     */
    private Entity cloneEntity(Entity entity) {
        if (entity instanceof Wall) return new Wall();
        if (entity instanceof Trap) return new Trap();
        if (entity instanceof Gold) return new Gold();
        if (entity instanceof MeleeMutant) return new MeleeMutant();
        if (entity instanceof RangedMutant) return new RangedMutant();
        if (entity instanceof HealthPotion) return new HealthPotion();
        if (entity instanceof Ladder) return new Ladder();
        return null;
    }

    private String getKey(int x, int y) {
        return x + "," + y;
    }

    /** Moves an entity (Currently only player)
     *
     * @param direction direction to move
     * @return the move offset to be changed of an entities current location
     */
    public String move(String direction) {
        if(player.getHP()==0){
            GameGUI.GameOver(2);
        }if (player.getSteps()==0){
            GameGUI.GameOver(1);
        }
        int dx = 0, dy = 0;
        switch (direction) {
            case "u" -> dx = -2;
            case "d" -> dx = 2;
            case "l" -> dy = -2;
            case "r" -> dy = 2;
            default -> {
                return "Invalid direction!";
            }
        }
        return moveByOffset(dx, dy);
    }

    /** Executes the move
     *
     * @param dx displacement in the x direction
     * @param dy displacement in the y direction
     * @return outcome if the move is valid or invalid.
     */
    public String moveByOffset(int dx, int dy){
        int startX = player.getX();
        int startY = player.getY();
        int midX = startX + dx / 2;
        int midY = startY + dy / 2;
        int newX = startX + dx;
        int newY = startY + dy;

        if (!inBounds(newX, newY)) {
            return "You cannot move outside the dungeon!";
        }
        if (grid[midX][midY] instanceof Wall || grid[newX][newY] instanceof Wall) {
            return "The wall blocks your path.";
        }

        Entity entity = grid[newX][newY];
        player.move(dx, dy);
        StringBuilder result = new StringBuilder();

        if (entity != null) {
            result.append(entity.onVisit(player)).append("\n");
        } else {
            result.append("You moved one step.").append("\n");
        }

        if (!(entity instanceof Trap) && !(entity instanceof Ladder)) {
            grid[newX][newY] = null;
        }

        result.append(rangedMutantAttack()).append("\n");

        result.append(handleLevelAdvance());

        if (player.getHP() <= 0) {
            result.append("You have died. Game over.\nFinal score: -1");
            //GameGUI.GameOver(2);
        }if (player.getSteps() <= 0){
            result.append("\"You have passed out from exhaustion. Game over.\\nFinal score: -1\"");
            //GameGUI.GameOver(1);
        }
        return result.toString().strip();
    }

    /** Advances the player to the next stage
     *
     * @return either advances to the next level or completes the game
     */
    private String handleLevelAdvance(){
        Entity current = grid[player.getX()][player.getY()];
        if (current instanceof Ladder) {
            if (level == 1) {
                level = 2;
                difficulty += 2;
                generateMap(spawnX, spawnY);  // Rebuild grid for level 2
                return "You find your way to Level 2!";
            } else {
                double finalScore = player.getScore() * (1+(0.1* difficulty));
                GameGUI.GameComplete(finalScore);
                return "You escaped the dungeon! Game won."; //never reached due to line above closing window

            }
        }
        return "";
    }

    /** Ranged mutant attack if player is in range of 2 (due to wall size:4)
     *
     * @return the outcome of the ranged mutants attack
     */
    private String rangedMutantAttack() {
        StringBuilder result = new StringBuilder();
        for (int dx = -4; dx <= 4; dx++) {
            int x = player.getX() + dx;
            int y = player.getY();
            if (dx != 0 && inBounds(x, y) && grid[x][y] instanceof RangedMutant) {
                result.append(((RangedMutant) grid[x][y]).attemptAttack(player)).append("\n");
            }
        }
        for (int dy = -4; dy <= 4; dy++) {
            int x = player.getX();
            int y = player.getY() + dy;
            if (dy != 0 && inBounds(x, y) && grid[x][y] instanceof RangedMutant) {
                result.append(((RangedMutant) grid[x][y]).attemptAttack(player)).append("\n");
            }
        }
        return result.toString().strip();
    }



    public void printGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (player.getX() == i && player.getY() == j) {
                    System.out.print("P ");  // Player
                } else if (grid[i][j] != null) {
                    System.out.print(grid[i][j].getSymbol() + " ");  // Entity
                } else {
                    System.out.print(". ");  // Empty space
                }
            }
            System.out.println();
        }
    }


    private boolean inBounds(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }



    public int setDifficulty(int newDifficulty){
        difficulty = newDifficulty;
        return difficulty;
    }

    public Entity[][] getGrid() {
        return grid;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSpawnX(){
        return spawnX;
    }
    public int getSpawnY(){
        return spawnY;
    }

    public int getLevel() {
        return level;
    }
    public int getDifficulty(){
        return difficulty;
    }

    /** Allows for the game to be run in the console
     *
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to MiniDungeon Console Edition!");
        System.out.print("Enter difficulty (1-10): ");
        int difficulty = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        GameEngine game = new GameEngine(difficulty);

        while (true) {
            game.printGrid();
            System.out.println("HP: " + game.getPlayer().getHP() + " | Steps: " + game.getPlayer().getSteps() + " | Score: " + game.getPlayer().getScore());
            System.out.print("Move (u/d/l/r): ");
            String move = scanner.nextLine().trim().toLowerCase();

            if (move.equals("quit")) {
                System.out.println("Exiting game...");
                break;
            }

            String result = game.move(move);
            System.out.println(result);

            if (game.getPlayer().getHP() <= 0 || game.getPlayer().getSteps() <= 0) {
                System.out.println("Game Over.");
                break;
            }

            Entity current = game.getGrid()[game.getPlayer().getX()][game.getPlayer().getY()];
            if (current instanceof Ladder && game.getLevel() == 2) {
                System.out.println("You escaped the dungeon! Game won.");
                break;
            }
        }

        System.out.println("Final Score: " + game.getPlayer().getScore() * (1 + (0.1 * game.getDifficulty())));
    }
}
