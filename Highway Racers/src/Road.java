
/* Description: Controls the behaviour and layout of the road
 * Date: Janurary 17 2025
 */
import java.awt.*;

import javax.swing.ImageIcon;

public class Road extends Rectangle {

	public int yVelocity;
	public int xVelocity;
	public static final int SPEED = 6; // movement speed of ball

	public Image road;

	public static enum GAMEMAP {
		DEFAULT, AUTUMN, WINTER
	};

	// sets the menu variable to be true so game starts at main menu
	public static GAMEMAP GameMap = GAMEMAP.DEFAULT;

	// constructor creates road image at given location with given dimensions
	public Road(int x, int y) {
		super(x, y, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT);

		if (GameMap == GAMEMAP.DEFAULT)
			road = new ImageIcon("RoadDesign.png").getImage();

		if (GameMap == GAMEMAP.AUTUMN)
			road = new ImageIcon("RoadAutmn.png").getImage();

		if (GameMap == GAMEMAP.WINTER)
			road = new ImageIcon("RoadWinter.png").getImage();

	}

	// updates the current location of the background image
	// method called to move the image of the map
	public void move() {
		y = y + 15;
	}

	// called frequently from the GamePanel class
	// draws the current location of the road to the screen
	public void draw(Graphics g) {
		g.drawImage(road, x, y, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT, null);
	}
}
