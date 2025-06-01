package dungeon.engine.entities;

public abstract class Entity {
    protected int x,y;

    public Entity() {
        this.x = 0;
        this.y = 0;
    }

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getCoordinates(){

        return new int[] {x,y};
    }
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract char getSymbol();
    public abstract String onVisit(Player player);

}
