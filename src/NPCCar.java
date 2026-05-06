/* Description: controls the behaviour of the npccars on the road
 * Date: Janurary 7 2025

 */

import java.awt.*;
import javax.swing.ImageIcon;

public class NPCCar extends Rectangle {

    public static final int CAR_DIMENSIONS = 30; // size of the car

    public Image npcCar1;
    public Image npcCar2;
    public Image npcCar3;

    private final int NPC_WIDTH = PlayerCar.CAR_WIDTH;
    private final int NPC_HEIGHT = PlayerCar.CAR_HEIGHT;


     static int l1 = (int)(GamePanel.GAME_WIDTH/14.7058823529);    //Ratio: default road width / default lane 1 location
     static int l2 = (int)(GamePanel.GAME_WIDTH/5.31914893617);
     static int l3 = (int)(GamePanel.GAME_WIDTH/3.37837837838);
     static int l4 = (int)(GamePanel.GAME_WIDTH/2.45098039216);
     static int l5 = (int)(GamePanel.GAME_WIDTH/1.92307692308);
     static int l6 = (int)(GamePanel.GAME_WIDTH/1.58227848101);
     static int l7 = (int)(GamePanel.GAME_WIDTH/1.33689839572);
     static int l8 = (int)(GamePanel.GAME_WIDTH/1.16279069767);



    public static int[] lanes = { l1, l2, l3, l4, l5, l6, l7, l8 }; // x coordinate of each of each of the

    public Image carPicked;

    // constructor creates ball at given location with given dimensions
    public NPCCar(int x, int y) {
        super(x, y, CAR_DIMENSIONS, CAR_DIMENSIONS);
        npcCar1 = new ImageIcon("nonPlayerCar1.png").getImage();
        npcCar2 = new ImageIcon("nonPlayerCar2.png").getImage();
        npcCar3 = new ImageIcon("nonPlayerCar3.png").getImage();
        pickRandCar();
        
        
    }

    // updates the current location of the npc car
    public void move() {

    	y+= 7;
    }

    public void pickRandCar() { // method to randomly pick an npc car's image
        int randNum = GamePanel.rando.nextInt(1, 4);

        if (randNum == 1) {
            carPicked = npcCar1;
        } 
        else if (randNum == 2) {
            carPicked = npcCar2;
        } 
        else if (randNum == 3) {
            carPicked = npcCar3;
        } 
       
    }

    public static double pickRandLaneLeft() {
        return lanes[GamePanel.rando.nextInt(4)]; // returns which lane the car should be in (for the left side of the highway)
    }

    public static double pickRandLaneRight() {
        return lanes[GamePanel.rando.nextInt(4,8)]; // returns which lane the car should be in (for the right side of the highway)
    }

    
    // draws the current location of the npccar to the screen
    public void draw(Graphics g) {
        g.drawImage(carPicked, x, y, NPC_WIDTH, NPC_HEIGHT, null);

    }

}