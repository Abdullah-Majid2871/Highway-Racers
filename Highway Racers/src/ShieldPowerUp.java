/*Description: Shield powerup for the player
 * 
 */

import java.awt.*;

import javax.swing.*;

import java.util.*;

public class ShieldPowerUp extends Rectangle {

	public Image shield; // shield image
	public static Rectangle shieldDetector = null; // hitbox for shield


	private static final int SHIELD_WIDTH =(int)(GamePanel.GAME_WIDTH/13.1578947368);
	private static final int SHIELD_HEIGHT = SHIELD_WIDTH;

	public int shieldX;
	public int shieldY;
	

	public ShieldPowerUp(int x, int y) {
		shield = new ImageIcon("Shield.png").getImage();
		shieldDetector = new Rectangle(shieldX=x, shieldY=y, SHIELD_WIDTH, SHIELD_HEIGHT);
	}

	public void move() {
		
		//sets the shieldcollisiondetector and the shield image to the same x and y cooridnates
		shieldY = y+= 6;
		shieldDetector.x = shieldX;
		shieldDetector.y = shieldY;



	}

	public void draw(Graphics g) {
		g.drawImage(shield, shieldX, shieldY, SHIELD_WIDTH, SHIELD_HEIGHT, null);
	}

}
