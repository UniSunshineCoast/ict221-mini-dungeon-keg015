package dungeon.engine.entities;

public class Ladder extends Entity {
    @Override
    public char getSymbol(){
        return 'L';
    }
    @Override
    public String onVisit (Player player){
        return "You Have found a Ladder. " +
                "\n   'this is a bad time to be afraid of heights'";
    }
}
