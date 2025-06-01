package dungeon.engine.entities;

public class Gold extends Entity {
    @Override
    public char getSymbol(){
        return 'G';
    }
    @Override
    public String onVisit (Player player){
        player.increaseScore(2);
        return "    'OOo piece of candy.'" +
                "\n It turns out to be gold" +
                "\n       Gain 2 Score";
    }
}
