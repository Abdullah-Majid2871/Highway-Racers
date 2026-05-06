/* Description: Controls ho the menu works
 * Date: January 7 2025
 */

import java.awt.*;
import java.awt.event.*;

public class Menu {

	static int buttonWidth = 400;

	static int buttonX = GamePanel.GAME_WIDTH / 2 - buttonWidth / 2;

	private static Rectangle playButton = new Rectangle(buttonX, (int) (GamePanel.GAME_HEIGHT / 3), buttonWidth, 90);
	private static Rectangle quitButton = new Rectangle(buttonX,
			(int) (GamePanel.GAME_HEIGHT / 3 + GamePanel.GAME_HEIGHT / 4), buttonWidth, 90);

	// sets the font for the header
	Font headerFont = new Font("SansSerif", Font.BOLD, 75);

	// creates new font
	Font buttonFont = new Font("SansSerif", Font.BOLD, 30); // sets font

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.setFont(headerFont);

		g.setColor(Color.BLACK);

		// draws header
		g.drawString("WELCOME TO HIGHWAY RACERS", (int) (GamePanel.GAME_WIDTH / 25),
				(int) (GamePanel.GAME_HEIGHT / 7.8125));

		g.setFont(buttonFont); // uses font

		g2d.fill(playButton);
		g2d.fill(quitButton);

		g.setColor(Color.WHITE);

		// places the text inside the boxes
		g.drawString("Play", playButton.x + 165, playButton.y + 55);
		g.drawString("Quit", quitButton.x + 165, quitButton.y + 55);

		// draws the text for how to play
		g.drawString("Left Player Controls: Use 'w', 'a','s','d' to control the car", 60, GamePanel.GAME_HEIGHT - 100);
		g.drawString("Right Player Controls: Use arrows keys to control the car", 60, GamePanel.GAME_HEIGHT - 50);

		g.drawString("Score to beat: " + ScoreClass.highestScore, GamePanel.GAME_WIDTH / 5, GamePanel.GAME_HEIGHT / 3);
	}

	public static void mouseClicked(MouseEvent e) {

		if (playButton.contains(e.getPoint())) {
			GamePanel.GameState = GamePanel.GAMESTATE.GAME_PLAY_OPTIONS;
		}

		if (quitButton.contains(e.getPoint())) {
			System.exit(0); // quits program
		}

	}

}
