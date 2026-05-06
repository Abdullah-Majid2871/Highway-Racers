/* Description: Controls how the second menu screen will work
 * Date : January 17 2025

 */

import java.awt.*;
import java.awt.event.*;

public class PlayOptions {

	public static int buttonWidth = 300;
	public static int buttonHeight = 90;

	static int left = (int) (GamePanel.GAME_WIDTH / 12.5);
	static int middle = (int) (GamePanel.GAME_WIDTH / 2.63157894737);
	static int right = (int) (GamePanel.GAME_WIDTH / 1.47058823529);

	static int topMapButtonsY = (int) (GamePanel.GAME_HEIGHT / 5);
	static int diffcultyMapButtonsY = (int) (GamePanel.GAME_HEIGHT / 1.66666666667);

	// creates boxes for the game weather
	private static final Rectangle DEFAULT_BUTTON = new Rectangle(left, topMapButtonsY, buttonWidth, buttonHeight);
	private static final Rectangle FALL_BUTTON = new Rectangle(middle, topMapButtonsY, buttonWidth, buttonHeight);
	private static final Rectangle WINTER_BUTTON = new Rectangle(right, topMapButtonsY, buttonWidth, buttonHeight);

	// creates boxes for the game speed
	private static final Rectangle EASY_BUTTON = new Rectangle(left, diffcultyMapButtonsY, buttonWidth, buttonHeight);
	private static final Rectangle NORMAL_BUTTON = new Rectangle(middle, diffcultyMapButtonsY, buttonWidth, buttonHeight);
	private static final Rectangle HARD_BUTTON = new Rectangle(right, diffcultyMapButtonsY, buttonWidth, buttonHeight);
	
	static Rectangle[] buttons = { EASY_BUTTON, NORMAL_BUTTON, HARD_BUTTON };


	static Rectangle buttonPicked;

	public static final Rectangle ENTER_BUTTON = new Rectangle(GamePanel.GAME_WIDTH / 2 - 150,
			GamePanel.GAME_HEIGHT - GamePanel.GAME_HEIGHT / 6, buttonWidth, buttonHeight);

	// sets the font for the header
	static Font headerFont = new Font("SansSerif", Font.BOLD, 75);
	 static Font buttonFont = new Font("SansSerif", Font.BOLD, 30); // sets font

	// creates the layout
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.BLACK); // sets the draw color

		g.setFont(headerFont);
		// WEATHER
		g.drawString("Pick Map", left, (int) (GamePanel.GAME_HEIGHT / 7.5));

		// adds boxes to contain text
		g2d.fill(DEFAULT_BUTTON);
		g2d.fill(FALL_BUTTON);
		g2d.fill(WINTER_BUTTON);

		g.setColor(Color.WHITE);
		g.setFont(buttonFont); // uses font
		// places the text inside the boxes
		g.drawString("Default", DEFAULT_BUTTON.x + 15, DEFAULT_BUTTON.y + 55);
		g.drawString("Fall", FALL_BUTTON.x + 15, FALL_BUTTON.y + 55);
		g.drawString("Winter", WINTER_BUTTON.x + 15, WINTER_BUTTON.y + 55);

		g2d.setColor(Color.BLACK); // sets the draw color

		// ACTION SPEED
		g.setFont(headerFont);
		g2d.drawString("Pick Difficulty", left, (int) (GamePanel.GAME_HEIGHT / 1.875));


		//sets the color of the difficulty that the user has picked
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] == buttonPicked) {
				g2d.setColor(Color.BLUE);
				g2d.fill(buttons[i]);
			} else if (buttons[i] != buttonPicked) {
				g2d.setColor(Color.BLACK);
				g2d.fill(buttons[i]);
			}

		}

		g2d.setColor(Color.BLACK);

		g2d.fill(ENTER_BUTTON);

		g.setColor(Color.WHITE);

		g.setFont(buttonFont);
		g.drawString("Easy", EASY_BUTTON.x + 15, EASY_BUTTON.y + 55);
		g.drawString("Normal", NORMAL_BUTTON.x + 15, NORMAL_BUTTON.y + 55);
		g.drawString("Hard", HARD_BUTTON.x + 15, HARD_BUTTON.y + 55);

		g.drawString("ENTER", ENTER_BUTTON.x + 95, ENTER_BUTTON.y + 55);

	}

	// detects mouse clicked
	public static void mouseClicked(MouseEvent e) {

		// controls to set the map selection of the game
		if (DEFAULT_BUTTON.contains(e.getPoint())) {
			Road.GameMap = Road.GAMEMAP.DEFAULT;
			createNewRoad();
		}
		if (FALL_BUTTON.contains(e.getPoint())) {
			Road.GameMap = Road.GAMEMAP.AUTUMN;
			createNewRoad();
		}
		if (WINTER_BUTTON.contains(e.getPoint())) {
			Road.GameMap = Road.GAMEMAP.WINTER;
			createNewRoad();
		}

		// Controls to set the difficulty of the game
		if (EASY_BUTTON.contains(e.getPoint())) {
			GamePanel.GameDifficulty = GamePanel.GAMEDIFFICULTY.EASY;
			buttonPicked = EASY_BUTTON;

		}
		if (NORMAL_BUTTON.contains(e.getPoint())) {
			GamePanel.GameDifficulty = GamePanel.GAMEDIFFICULTY.NORMAL;
			buttonPicked = NORMAL_BUTTON;

		}
		if (HARD_BUTTON.contains(e.getPoint())) {
			GamePanel.GameDifficulty = GamePanel.GAMEDIFFICULTY.HARD;
			buttonPicked = HARD_BUTTON;
		}

		// Enter button, Button to start playing the game
		if (ENTER_BUTTON.contains(e.getPoint())) {
			GamePanel.GameState = GamePanel.GAMESTATE.GAME;
		}

	}

	// creates new road when the user picks the map they want
	 static void createNewRoad() {
		GamePanel.road1 = new Road(0, 0);
		GamePanel.road2 = new Road(0, GamePanel.road1.y - GamePanel.GAME_HEIGHT);
	}



}