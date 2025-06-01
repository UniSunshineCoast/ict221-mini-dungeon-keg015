package dungeon.engine.entities;

public class MeleeMutant extends Entity {
    @Override
    public char getSymbol(){
        return 'M';
    }
    @Override
    public String onVisit (Player player){
        player.decreaseHP(2);
        player.increaseScore(5);
        return "You stumble across a mutants, theres no time for questions. " +
                "\n      Lose 2 HP, Gain 5 XP";
    }
}
