package dungeon.engine.entities;

public class Entry extends Entity{
    @Override
    public char getSymbol(){
        return 'E';// example
    }
    @Override
    public String onVisit (Player player){
        //player.variable to be changed;
        return "If you are reading this, something has gone wrong";
    }
}

