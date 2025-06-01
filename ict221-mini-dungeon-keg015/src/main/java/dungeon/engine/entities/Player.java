package dungeon.engine.entities;

public class Player extends Entity {
   private int hp = 10;
   private int score = 0;
   private int steps = 200;

   public Player(){

   };

   public Player(int x, int y) {
      this.x = x;
      this.y = y;
   }

   /**
    *
    * @param xDir the change in the x direction, may be + or - 2
    * @param yDir the change in the y direction, may be + or - 2
    */
   public void move(int xDir, int yDir) {
      steps--;
      x += xDir;
      y += yDir;
   }

   /**
    *
    * @param amount takes the amount to heal the player by
    */
   public void increaseHP(int amount) {
      hp = Math.min(10, hp + amount);
   }

   /** damage the player;
    * if they walk into a trap or are hit by a mob
    * HP cannot fall below zero. if they hit zero they are dead
    * @param amount takes the amount of damage to be dealt to the player
    */
   public void decreaseHP(int amount) {
      hp = Math.max(0, hp - amount);
   }

   /**
    *
    * @param amount takes the amount to increase the player score by
    */
   public void increaseScore(int amount) {
      score += amount;
   }
   public void setPX(int newX){
      x = newX;
   }
   public void setPY(int newY){
      y = newY;
   }
   //get functions
   public int getHP() {
      return hp;
   }

   public int getScore() {
      return score;
   }

   public int getSteps() {
      return steps;
   }

   @Override
   public char getSymbol() {
      return 'P';
   }

   @Override
   public String onVisit(Player player) {
      return "HOW DID YOU GET HERE?";
   }
}
