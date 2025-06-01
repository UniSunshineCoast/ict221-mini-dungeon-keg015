package dungeon.engine.entities;

import java.util.Random;

public class RangedMutant extends Entity {
    private static final Random random = new Random();

    @Override
    public char getSymbol(){
        return 'R';
    }
    @Override
    public String onVisit (Player player){
        player.increaseScore(5);
        return "Your blood-lust is quenched, the mutant is dead" +
                "\n       Gain 5XP";
    }
    public String attemptAttack(Player player){
        if (random.nextBoolean()){
            player.decreaseHP(2);
            return "The mutant attacks from a distance. \nYour reflexes need improvement." +
                    "\n       Loose 2 HP";
        }else {
            return "The mutant attacks from a distance. \nYou dodge the bolt ";
        }
    }
}
