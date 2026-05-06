/*	Description: Obstacle for the player
 * 	Date: January 17 2025
 */

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import java.util.*;

public class LeavesObstacle extends Rectangle {

	public Image leavesPileImage = new ImageIcon("LeavesPile.png").getImage(); // leaf image

	public Image singleLeafImage = new ImageIcon("SingleLeaf.png").getImage();

	// private static final int LEAVES_WIDTH = 95;
	private static final int LEAVES_WIDTH = (int) (GamePanel.GAME_WIDTH / 13.1578947368);

	private static final int LEAVES_HEIGHT = LEAVES_WIDTH;
	public static Rectangle leavesCollisionDetector = new Rectangle(0, 0, LEAVES_WIDTH, LEAVES_HEIGHT);
	public int leavesX;
	public int leavesY;
	public final int leafSpeed = Road.SPEED * 2;

	public static ArrayList<Image> p1Vision = new ArrayList<Image>();
	public static ArrayList<Image> p2Vision = new ArrayList<Image>();

	public LeavesObstacle(int x, int y) {
		leavesX = x;
		leavesY = y;

		Thread leavesThread = new Thread();
		leavesThread.start();

	}

	public void move() {
		// sets the xand y coordinates of the leaves
		leavesY += leafSpeed;
		leavesCollisionDetector.x = leavesX;
		leavesCollisionDetector.y = leavesY;
	}

	public void draw(Graphics g) {

		g.drawImage(leavesPileImage, leavesX, leavesY, LEAVES_WIDTH, LEAVES_HEIGHT, null);

		// source used: https://stackoverflow.com/questions/8639567/java-rotating-images
		if (GamePanel.p1VisionBlock) {
			for (int i = 0; i < 50; i++) {

				g.drawImage(singleLeafImage,
						GamePanel.rando.nextInt(0,
								(int) (GamePanel.GAME_WIDTH / 2 - GamePanel.GAME_WIDTH / 48 - LEAVES_WIDTH)),
						(int) GamePanel.rando.nextInt(0, GamePanel.GAME_HEIGHT - LEAVES_HEIGHT), LEAVES_WIDTH,
						LEAVES_HEIGHT, null);

			}
		}

		if (GamePanel.p2VisionBlock) {
			for (int i = 0; i < 50; i++) {

				g.drawImage(singleLeafImage,
						GamePanel.rando.nextInt((GamePanel.GAME_WIDTH / 2 + GamePanel.GAME_WIDTH / 48),
								GamePanel.GAME_WIDTH - LEAVES_WIDTH),
						GamePanel.rando.nextInt(0, GamePanel.GAME_HEIGHT - LEAVES_HEIGHT), LEAVES_WIDTH, LEAVES_HEIGHT,
						null);

			}
		}
	}
}
