package dungeon.engine.entities;

public class Wall extends Entity {
    @Override
    public char getSymbol() {
        return '#';
    }
    @Override
    public String onVisit(Player player) {
        return "You smash your face against a wall, those things tend to be solid.";


    }
}
