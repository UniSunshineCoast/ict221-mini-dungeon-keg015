package dungeon.engine.entities;

public class Trap extends Entity {
    @Override
    public char getSymbol(){
        return 'T';// example
    }
    @Override
    public String onVisit (Player player) {
        player.decreaseHP(2);
        return "    'Oww that Hurt.' " +
                "\nYou fell into a trap." +
                "\n       Loose 2 HP";
    }
}
