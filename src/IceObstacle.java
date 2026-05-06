/* Description: Powerups for the player
 * Date: January 17 2025
 * 
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class IceObstacle extends Rectangle {

	public Image iceImage; // shield image
	 // hitbox for shield

	//private static final int ICE_WIDTH = 95;
	private static final int ICE_WIDTH = (int)(GamePanel.GAME_WIDTH/13.1578947368);	//private static final int ICE_WIDTH = 95;

	private static final int ICE_HEIGHT = ICE_WIDTH;
	
	public static Rectangle iceCollisionDetector= new Rectangle(0, 0, ICE_WIDTH, ICE_HEIGHT);

	private int iceFallingSpeed = Road.SPEED;

	public int iceX;
	public int iceY;

	public IceObstacle(int x, int y) {
		
			iceImage = new ImageIcon("Ice.png").getImage();
			iceCollisionDetector = new Rectangle(iceX = x, iceY = y, ICE_WIDTH, ICE_HEIGHT);


	}

	public void move() {
		//sets both the ice image and iceCollisionDetector to the same y
		iceY = (y+=iceFallingSpeed);  
		iceCollisionDetector.y = iceY; 
	}

	public void draw(Graphics g) {
		
		g.drawImage(iceImage, iceX, iceY, ICE_WIDTH, ICE_HEIGHT, null);
	}
}
