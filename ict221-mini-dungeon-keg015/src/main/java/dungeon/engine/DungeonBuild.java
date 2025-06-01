package dungeon.engine;

import dungeon.engine.entities.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DungeonBuild {
    private Entity[][] grid = new Entity[21][21];
    private Player player;
    private int level;
    private int difficulty;
    private Random random = new Random();
    //ensures that no two entities can exist on the same location
    private Set<String> usedPositions = new HashSet<>();

    public DungeonBuild(Player player, int level,int difficulty){
        this.player = player;
        this.level = level;
        generateMap();
    }

    private void generateMap(){
        for (int i = 0; i < 21; i++) {
            Arrays.fill(grid[i],null);
        }
        // Add outer walls
        for (int i = 0; i < 21; i++) {
            grid[0][i] = new Wall();
            grid[20][i] = new Wall();
            grid[i][0] = new Wall();
            grid[i][20] = new Wall();
            usedPositions.add(getKey(i, 0));
            usedPositions.add(getKey(i, 20));
            usedPositions.add(getKey(0, i));
            usedPositions.add(getKey(20, i));

            // Add pillars
            for (int j = 0; j < 21; j++) {
                if ( i % 2 == 0 && j % 2 ==0){
                    grid[i][j] = new Wall();
                    usedPositions.add(getKey(i, j));
                }
            }

        }

        // Add player entry point
        int entryX = 1, entryY = 1;
        grid[entryX][entryY] = null;
        player.setPX(entryX);
        player.setPY(entryY);
        usedPositions.add(getKey(entryX, entryY));

        // Add Ladder
        placeRandomEntity(new Ladder());

        // Items with fixed counts
        placeRandomEntities(new Trap(), 5);
        placeRandomEntities(new Gold(), 5);
        placeRandomEntities(new MeleeMutant(), 3);
        placeRandomEntities(new HealthPotion(), 2);

        //for difficulty to be increased, the count must be increased

        placeRandomWalls(10+ (difficulty * 3));
        placeRandomEntities(new RangedMutant(), 3 + difficulty); // adjusted by difficulty d later
    }

    private void placeRandomEntities(Entity entity, int count) {
        for (int i = 0; i < count; i++) {
            placeRandomEntity(cloneEntity(entity));
        }
    }


    private void placeRandomEntity(Entity entity) {
        int x, y;
        int newX, newY;

        do {
            x = random.nextInt(9);
            y = random.nextInt(9);
            newX = x*2+1;
            newY = y*2+1;
        } while (isOccupied(newX,newY));

        grid[newX][newY] = entity;
        usedPositions.add(getKey(newX, newY));
        entity.setCoordinates(newX, newY);
    }


    private void placeRandomWalls(int count){
        int current = 0;
        while (current < count) {
            int x1 = random.nextInt(10) * 2;
            int y1 = (random.nextInt(10) * 2) + 1;
            int x = (random.nextInt(10) * 2) + 1;
            int y = random.nextInt(10) * 2;
            Wall wall = new Wall();
            System.out.println(x + " " + y);
            grid[x][y] = wall;
            usedPositions.add(getKey(x, y));
            grid[x1][y1] = wall;
            usedPositions.add(getKey(x1, y1));

            current++;

        }
    }


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
    private boolean isOccupied(int x, int y){
        return grid[x][y] != null;
    }

    private String getKey(int x, int y) {
        return x + "," + y;
    }

    public Entity getEntity(int x, int y) {
        return grid[x][y];
    }


    public String movePlayer(int dirX, int dirY) {
        int startX = player.getX();
        int startY = player.getY();
        int midX = startX + dirX / 2;
        int midY = startY + dirY / 2;
        int newX = startX + dirX;
        int newY = startY + dirY;

        if (!inBounds(newX, newY)) {
                return "    'this seems to be a solid rock, i wish i had a pokemon with rock smash'";
        }
        if (grid[midX][midY] instanceof Wall || grid[newX][newY] instanceof Wall) {
            return "    'WHO PUT THAT THERE?!? " +
                    "\nThe wall blocks your path";
        }

        Entity entity = grid[newX][newY];



        if (entity instanceof Wall) {
            return entity.onVisit(player);
        }

        player.move(dirX, dirY);
        String result = entity != null
                ? entity.onVisit(player)
                : "You moved one step." + String.format("current position %s %s \n ",(newX-1)/2 ,(newY-1)/2);
        if (!(entity instanceof Trap) && !(entity instanceof Ladder )) {
            grid[newX][newY] = null;
        }

        // Ranged mutant attack phase
        result += "\n" + rangedMutantAttack();

        return result;
    }

    private String rangedMutantAttack() {
        StringBuilder result = new StringBuilder();
        for (int dirX = -2; dirX <= 2; dirX++) {
            int x = player.getX()+ dirX;
            int y = player.getY();
            if (dirX != 0 && inBounds(x, y) && grid[x][y] instanceof RangedMutant) {
                result.append(((RangedMutant) grid[x][y]).attemptAttack(player)).append("\n");
            }
        }
        for (int dirY = -2; dirY <= 2; dirY++) {
            int x = player.getX();
            int y = player.getY() + dirY;
            if (dirY != 0 && inBounds(x, y) && grid[x][y] instanceof RangedMutant) {
                result.append(((RangedMutant) grid[x][y]).attemptAttack(player)).append("\n");
            }
        }
        return result.toString().strip();
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < 21 && y >= 0 && y < 21;
    }

    public Entity[][] getGrid() {
        return grid;
    }
    public Player getPlayer(){
        return player;
    }



}
