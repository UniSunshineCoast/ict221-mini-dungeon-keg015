package dungeon.engine.entities;

public class HealthPotion extends Entity {
    @Override
    public char getSymbol(){
        return 'H';
    }
    @Override
    public String onVisit (Player player){
        player.increaseHP(5);
        return "    'I wonder what this tastes like.'" +
                "\nYou used a health potion." +
                "\n       Gain 5 HP";
    }
}
