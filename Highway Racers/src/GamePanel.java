/* Description: Class that initiates the commands
 * Date: Janurary 17 2025

 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	// dimensions of window
	public static final int GAME_WIDTH = (int) screenSize.getWidth();
	public static final int GAME_HEIGHT = (int) screenSize.getHeight();

	// variables used to make the road move
	public static Road road1;
	public static Road road2;
	public Image roadImage;

	public Thread gameThread;
	public Image image;
	public Graphics graphics;

	public Menu mainMenu = new Menu();
	public PlayOptions gameOptions = new PlayOptions();

	public static Random rando = new Random(); // variable for random number genration

	// variables used for the npcs
	private static final int NPC_WIDTH = PlayerCar.CAR_WIDTH - 10;
	private static final int NPC_HEIGHT = PlayerCar.CAR_HEIGHT;
	public static RoundRectangle2D npcHitbox = new RoundRectangle2D.Double(0, 0, NPC_WIDTH, NPC_HEIGHT, 50, 50);
	public ArrayList<RoundRectangle2D> hitBoxesLeftSide = new ArrayList<RoundRectangle2D>();
	public ArrayList<RoundRectangle2D> hitBoxesRightSide = new ArrayList<RoundRectangle2D>();
	public ArrayList<NPCCar> npcCarsLeftSide = new ArrayList<NPCCar>();
	public ArrayList<NPCCar> npcCarsRightSide = new ArrayList<NPCCar>();

	// Variables that create the player's car
	public static PlayerCar playerOne;
	public static PlayerCar playerTwo;

	// variables associated with the shield powerup
	public ShieldPowerUp shieldPowerUp = null;
	private static final int PLAYER_SHIELD_HEIGHT = PlayerCar.CAR_HEIGHT * 3;
	private static final int PLAYER_SHIELD_WIDTH = PLAYER_SHIELD_HEIGHT;
	private Ellipse2D.Double p1ShieldWhileInv = new Ellipse2D.Double(0, 0, PLAYER_SHIELD_WIDTH, PLAYER_SHIELD_HEIGHT);
	private Ellipse2D.Double p2ShieldWhileInv = new Ellipse2D.Double(0, 0, PLAYER_SHIELD_WIDTH, PLAYER_SHIELD_HEIGHT);
	public static boolean playerOneInvincibility = false;
	public static boolean playerTwoInvincibility = false;

	// variables associated with the ice obstacle
	public static IceObstacle ice = null;
	public static boolean p1Slip = false;
	public static boolean p2Slip = false;
	public static int p1SlipVal;
	public static int p2SlipVal;
	public static boolean p1TempKeyDisable = false;
	public static boolean p2TempKeyDisable = false;

	// variables associated with player score
	public ScoreClass scoreClass;

	// variables associated with the leaves obstacle
	public LeavesObstacle leaves;
	public static boolean p1VisionBlock = false;
	public static boolean p2VisionBlock = false;

	// variables used for time tracking
	public long startTimeLeftNPC = 0;
	public long startTimeRightNPC = 0;
	public long startTimeShieldPowerup = 0;
	public long startTimeObstacle = 0;
	public int carSpawnIntervals;

	public SoundClass soundClass;

	// Variable for gameover
	public boolean gameOver = false;

	// variable to go back to main menu after game is over
	public Rectangle BACK_TO_MAIN;

	// Variables associated with file
	public static File file;
	public Scanner fileReader;
	public static FileWriter fileWriter;
	public String rawData;
	public int convertedData;

	public GamePanel() {
		soundClass = new SoundClass();
		soundClass.playbgMusic();
		roadImage = new ImageIcon("RoadDesign.png").getImage();

		// location to the left side of screen
		playerOne = new PlayerCar(GAME_WIDTH / 3, GAME_HEIGHT / 2); // create a player controlled car, sets start
		// location to right side of screen
		playerTwo = new PlayerCar((GAME_WIDTH * 2) / 3, GAME_HEIGHT / 2);

		// Road
		road1 = new Road(0, 0);
		road2 = new Road(0, road1.y - GAME_HEIGHT);
	
		// detects if the file exists already
		try {
			file = new File("HighScores.txt");
			fileReader = new Scanner(file);
			fileWriter = new FileWriter("HighScores.txt", true);

			if (file.createNewFile()) {

			} else if (file.exists()) {
				if (fileReader.hasNextLine()) {
					rawData = fileReader.nextLine();
					convertedData = Integer.valueOf(rawData);
					ScoreClass.highestScore = convertedData;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		this.addMouseListener(new MouseAdapter() { // handles user interaction with the menu and options screen
			public void mousePressed(MouseEvent e) {

				if (GameState == GAMESTATE.MENU) {
					gameOver = false;
					road1 = new Road(0, 0);
					road2 = new Road(0, road1.y - GAME_HEIGHT);
					scoreClass = new ScoreClass();

					Menu.mouseClicked(e);
				}

				if (GameState == GAMESTATE.GAME_PLAY_OPTIONS) {
					PlayOptions.mouseClicked(e);
				}

				if (gameOver) {
					if (BACK_TO_MAIN.contains(e.getPoint())) {
						GameState = GAMESTATE.MENU;
					}

				}
			}
		});

		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input

		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		gameThread = new Thread(this);
		gameThread.start();
	}

	// creates an enum for the game running condition
	public static enum GAMESTATE {
		MENU, GAME, GAME_PLAY_OPTIONS
	}

	// sets the menu variable to be true so game starts at main menu
	public static GAMESTATE GameState = GAMESTATE.MENU;

	public enum GAMEDIFFICULTY {
		EASY, NORMAL, HARD
	}

	public static GAMEDIFFICULTY GameDifficulty = GAMEDIFFICULTY.NORMAL;

	public void paint(Graphics g) {
		g.drawImage(roadImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, this);
		draw(g); // draws all game elements on top of the background
	}

	// updates positions as things move
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		road2.draw(g);
		road1.draw(g);

		if (GameState == GAMESTATE.GAME) {
			playerOne.draw(g);
			playerTwo.draw(g);

			if (Road.GameMap == Road.GAMEMAP.WINTER && ice != null) {
				ice.draw(g);
			}

			// draws the npc cars to the screen and their hitboxes
			for (NPCCar npcCar : npcCarsLeftSide) {
				npcCar.draw(g);
			}

			for (NPCCar npcCar : npcCarsRightSide) {
				npcCar.draw(g);
			}

			scoreClass.draw(g);

			g.setColor(Color.BLACK);

			if (shieldPowerUp != null) {
				shieldPowerUp.draw(g);
			}

			// draws the invinsibility when the player is invincible
			if (playerOneInvincibility) {
				g2d.draw(p1ShieldWhileInv);

			}

			if (playerTwoInvincibility) {
				g2d.draw(p2ShieldWhileInv);
			}
			if (Road.GameMap == Road.GAMEMAP.AUTUMN && leaves != null) {
				leaves.draw(g);
			}

			// displays the main menu
			if (gameOver) {
				BACK_TO_MAIN = new Rectangle(PlayOptions.middle, GamePanel.GAME_HEIGHT - GamePanel.GAME_HEIGHT / 6,
						PlayOptions.buttonWidth + 30, PlayOptions.buttonHeight);
				g2d.fill(BACK_TO_MAIN);
				g2d.setFont(PlayOptions.buttonFont);
				g2d.setColor(Color.WHITE);
				g2d.drawString("BACK TO MAIN MENU", BACK_TO_MAIN.x + 15, BACK_TO_MAIN.y + 55);
				g2d.setColor(Color.BLACK);

				g2d.setFont(PlayOptions.headerFont);

				if (scoreClass.p1Health > scoreClass.p2Health) {
					g2d.drawString("Player 1 wins", GAME_WIDTH / 8, GAME_HEIGHT / 2);
				} else {
					g2d.drawString("Player 2 wins", GAME_WIDTH / 2 + GAME_WIDTH / 20, GAME_HEIGHT / 2);
				}
			}
		}

		if (GameState == GAMESTATE.GAME_PLAY_OPTIONS) {
			gameOptions.render(g);
		}

		// displays the main menu
		if (GameState == GAMESTATE.MENU) {
			mainMenu.render(g);
		}

	}

	// Updates positions
	public void move() {
		if (GameState == GAMESTATE.GAME) {
			road1.move();
			road2.move();
			playerOne.move();
			playerTwo.move();

			for (NPCCar npcCar : npcCarsLeftSide) {
				npcCar.move();
			}

			for (NPCCar npcCar : npcCarsRightSide) {
				npcCar.move();
			}

			if (shieldPowerUp != null) {
				shieldPowerUp.move();
			}

			if (Road.GameMap == Road.GAMEMAP.WINTER) {
				if (ice != null) {
					ice.move();
				}
				if (p1Slip) {
					playerOne.x += p1SlipVal;
				}
				if (p2Slip) {
					playerTwo.x += p2SlipVal;
				}
			}

			if (Road.GameMap == Road.GAMEMAP.AUTUMN && leaves != null) {
				leaves.move();
			}
			if (playerOne.x <= GAME_WIDTH / 30) {
				playerOne.x = GAME_WIDTH / 30;
			}
			if (playerTwo.getMaxX() >= GAME_WIDTH - GAME_WIDTH / 30) {
				playerTwo.x = GAME_WIDTH - GAME_WIDTH / 30 - PlayerCar.CAR_WIDTH;
			}

		}
	}

	public void setPlayerHitBoxFrames() {
		PlayerCar.p1Hbox.setFrame(playerOne.x + 10, playerOne.y, playerOne.getWidth() - 20, playerOne.getHeight());
		PlayerCar.p2Hbox.setFrame(playerTwo.x + 10, playerTwo.y, playerTwo.getWidth() - 20, playerTwo.getHeight());
	}

	// handles all collision detection and responds accordingly
	public void checkCollision() {

		if (GameState == GAMESTATE.GAME) {

			// sets the difficulty for the game. Difficulty is based on user input at the
			// previous screen
			if (GameDifficulty == GAMEDIFFICULTY.EASY) {
				carSpawnIntervals = 1000;
			}
			if (GameDifficulty == GAMEDIFFICULTY.NORMAL) {
				carSpawnIntervals = 750;
			}
			if (GameDifficulty == GAMEDIFFICULTY.HARD) {
				carSpawnIntervals = 500;
			}

			if (scoreClass.p1Health > 0) {
				// calculates how much time has passed and generates an npc car
				if (System.currentTimeMillis() - startTimeLeftNPC > carSpawnIntervals) {
					npcCarsLeftSide.add(new NPCCar((int) NPCCar.pickRandLaneLeft(), -150));
					hitBoxesLeftSide.add(null);
					startTimeLeftNPC = System.currentTimeMillis();
					scoreClass.p1Score++;
				}
			}

			if (scoreClass.p2Health > 0) {
				if (System.currentTimeMillis() - startTimeRightNPC > carSpawnIntervals) {
					npcCarsRightSide.add(new NPCCar((int) NPCCar.pickRandLaneRight(), -150));
					hitBoxesRightSide.add(null);
					startTimeRightNPC = System.currentTimeMillis();
					scoreClass.p2Score++;
				}
			}

			// what happens when the game is over
			if (scoreClass.p1Health <= 0 && scoreClass.p2Health <= 0) {
				gameOver = true;
				npcCarsRightSide.clear();
				hitBoxesRightSide.clear();
				npcCarsLeftSide.clear();
				hitBoxesLeftSide.clear();

				// if the player beats the highest score ever, records their score into a file
				if (scoreClass.p1Score > ScoreClass.highestScore) {
					scoreClass.recordScore("Player1", scoreClass.p1Score);
				} else if (scoreClass.p2Score > ScoreClass.highestScore) {
					ScoreClass.highestScore = scoreClass.p2Score;
					scoreClass.recordScore("Player2", scoreClass.p2Score);
				}
			}

			if (scoreClass.p1Score < 0) {
				scoreClass.p1Score = 0;
			}

			if (scoreClass.p2Score < 0) {
				scoreClass.p2Score = 0;
			}

			// shield power up
			if (System.currentTimeMillis() - startTimeShieldPowerup > 25000) {
				shieldPowerUp = new ShieldPowerUp(NPCCar.lanes[rando.nextInt(8)], 0);
				startTimeShieldPowerup = System.currentTimeMillis();
			}

			// sets the player hit box frames
			setPlayerHitBoxFrames();
			// Assigns hit boxes to the cars
			for (int i = 0; i < npcCarsLeftSide.size(); i++) { // hitboxes for left side
				hitBoxesLeftSide.set(i, npcHitbox = new RoundRectangle2D.Double(npcCarsLeftSide.get(i).x + 10,
						npcCarsLeftSide.get(i).y, NPC_WIDTH, NPC_HEIGHT, 50, 50));
			}
			for (int i = 0; i < npcCarsRightSide.size(); i++) { // hitboxes for left side
				hitBoxesRightSide.set(i, new RoundRectangle2D.Double(npcCarsRightSide.get(i).x + 10,
						npcCarsRightSide.get(i).y, NPC_WIDTH, NPC_HEIGHT, 50, 50));
			}

			// deletes the npc cars from existence after they move off-screen
			for (int i = 0; i < npcCarsLeftSide.size(); i++) {
				if (npcCarsLeftSide.get(i).y >= GAME_HEIGHT) {
					npcCarsLeftSide.remove(i);
					hitBoxesLeftSide.remove(i);
				}
			}
			for (int i = 0; i < npcCarsRightSide.size(); i++) {
				if (npcCarsRightSide.get(i).y >= GAME_HEIGHT) {
					npcCarsRightSide.remove(i);
					hitBoxesRightSide.remove(i);
				}
			}

			// if player is NOT invincible,checks collision with the cars
			if (!playerOneInvincibility) {
				for (int i = 0; i < hitBoxesLeftSide.size(); i++) {
					if (PlayerCar.p1Hbox.intersects(hitBoxesLeftSide.get(i).getX(), hitBoxesLeftSide.get(i).getY(),
							hitBoxesLeftSide.get(i).getWidth(), hitBoxesLeftSide.get(i).getHeight())) {

						npcCarsLeftSide.get(i).x += playerOne.xVelocity * 15;
						npcCarsLeftSide.get(i).y -= ((playerOne.yVelocity) + 45) * 3;

						// prevents the cars from going over to the right side
						if (npcCarsLeftSide.get(i).x + PlayerCar.CAR_WIDTH >= (GAME_WIDTH / 2) - (GAME_WIDTH / 48)) {
							npcCarsLeftSide.get(i).x = (GAME_WIDTH / 2)
									- (int) ((GAME_WIDTH / 48) + hitBoxesLeftSide.get(i).getWidth());
						}
						scoreClass.p1Health--;
						soundClass.playCrash();
					}
				}
			}
			if (!playerTwoInvincibility) {
				for (int i = 0; i < hitBoxesRightSide.size(); i++) {
					if (PlayerCar.p2Hbox.intersects(hitBoxesRightSide.get(i).getX(), hitBoxesRightSide.get(i).getY(),
							hitBoxesRightSide.get(i).getWidth(), hitBoxesRightSide.get(i).getHeight())) {

						npcCarsRightSide.get(i).x += playerTwo.xVelocity * 15;
						// prevents the cars from going over to the left side
						if (npcCarsRightSide.get(i).x <= (int) (GAME_WIDTH / 2) + (int) (GAME_WIDTH / 48)) {
							npcCarsRightSide.get(i).x = (int) (GAME_WIDTH / 2) + (int) (GAME_WIDTH / 48);
						}
						npcCarsRightSide.get(i).y -= ((playerTwo.yVelocity) + 45) * 3;

						scoreClass.p2Health--;
						soundClass.playCrash();

					}
				}
			}

			// If player IS invincible, checks if their surrounding shield has touched an
			// npc car, then removes the npc car
			if (playerOneInvincibility) {

				// sets the location of the shield that playerone has
				p1ShieldWhileInv.x = playerOne.x + ((double) PlayerCar.CAR_WIDTH / 2)
						- ((double) PLAYER_SHIELD_WIDTH / 2);
				p1ShieldWhileInv.y = playerOne.y + ((double) PlayerCar.CAR_HEIGHT / 2)
						- ((double) PLAYER_SHIELD_HEIGHT / 2);

				for (int i = 0; i < hitBoxesLeftSide.size(); i++) {
					if (p1ShieldWhileInv.intersects(hitBoxesLeftSide.get(i).getX(), hitBoxesLeftSide.get(i).getY(),
							hitBoxesLeftSide.get(i).getWidth(), hitBoxesLeftSide.get(i).getHeight())) {

						npcCarsLeftSide.remove(i);
						hitBoxesLeftSide.remove(i);
					}
				}
			} else {
				p1ShieldWhileInv.y = -GAME_HEIGHT;
			}

			if (playerTwoInvincibility) {
				// sets the location of the shield that player 2 has
				p2ShieldWhileInv.x = playerTwo.x + ((double) PlayerCar.CAR_WIDTH / 2)
						- ((double) PLAYER_SHIELD_WIDTH / 2);
				p2ShieldWhileInv.y = playerTwo.y + ((double) PlayerCar.CAR_HEIGHT / 2)
						- ((double) PLAYER_SHIELD_HEIGHT / 2);

				for (int i = 0; i < hitBoxesRightSide.size(); i++) {
					if (p2ShieldWhileInv.intersects(hitBoxesRightSide.get(i).getX(), hitBoxesRightSide.get(i).getY(),
							hitBoxesRightSide.get(i).getWidth(), hitBoxesRightSide.get(i).getHeight())) {
						npcCarsRightSide.remove(i);
						hitBoxesRightSide.remove(i);
					}
				}
			} else {
				p2ShieldWhileInv.y = -GAME_HEIGHT;
			}

			// force player to remain on screen
			if (playerOne.y <= 0) {
				playerOne.y = 0;
			}
			if (playerTwo.y <= 0) {
				playerTwo.y = 0;
			}

			// keeps the player from going beneath the screen
			if (playerOne.y >= GAME_HEIGHT - PlayerCar.CAR_HEIGHT) {
				playerOne.y = GAME_HEIGHT - PlayerCar.CAR_HEIGHT;
			}
			if (playerTwo.y >= GAME_HEIGHT - PlayerCar.CAR_HEIGHT) {
				playerTwo.y = GAME_HEIGHT - PlayerCar.CAR_HEIGHT;
			}

			// keeps player within the screen laterally
			if (playerOne.x <= 0) {
				playerOne.x = 0;
				setPlayerHitBoxFrames();
			}
			if (playerTwo.x <= 0) {
				playerTwo.x = 0;
				setPlayerHitBoxFrames();
			}

			// force player to remain within frame horizontally
			if (playerOne.x + PlayerCar.CAR_WIDTH >= GAME_WIDTH) {
				playerOne.x = GAME_WIDTH - PlayerCar.CAR_WIDTH;
				setPlayerHitBoxFrames();
			}
			if (playerTwo.x + PlayerCar.CAR_WIDTH >= GAME_WIDTH) {
				playerTwo.x = GAME_WIDTH - PlayerCar.CAR_WIDTH;
				setPlayerHitBoxFrames();
			}

			// detects the white border in the middle
			if (playerOne.x + PlayerCar.CAR_WIDTH >= (GAME_WIDTH / 2) - 10) {
				playerOne.x = ((GAME_WIDTH / 2) - 10) - PlayerCar.CAR_WIDTH;
				setPlayerHitBoxFrames();
			}
			if (playerTwo.x <= (GAME_WIDTH / 2) + 10) {
				playerTwo.x = (GAME_WIDTH / 2) + 10;
				setPlayerHitBoxFrames();
			}

			// Continuously drawing one image above the other to create the illusion of a
			// moving road
			if (road1.y > 0) {
				road2.y = road1.y - GAME_HEIGHT;
			}

			if (road2.y > 0) {
				road1.y = road2.y - GAME_HEIGHT;
			}

			// if shield power up is in circulation
			if (shieldPowerUp != null) {
				// detects when the shield has been moved off the screen and makes it null
				if (shieldPowerUp.y >= GAME_HEIGHT) {
					shieldPowerUp = null;
				}

				// detects if the players collided with the power up and gives then
				// invincibility
				// for 7 seconds
				if (PlayerCar.p1Hbox.intersects(ShieldPowerUp.shieldDetector)) {
					playerOneInvincibility = true;
					shieldPowerUp = null;

					Timer timer = new Timer();
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							playerOneInvincibility = false; // removes their superpower after 7 seconds
						}
					};
					timer.schedule(task, 7000);

				}
				if (PlayerCar.p2Hbox.intersects(ShieldPowerUp.shieldDetector)) {
					playerTwoInvincibility = true;
					shieldPowerUp = null;

					Timer timer = new Timer();
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							playerTwoInvincibility = false;
						}
					};
					timer.schedule(task, 7000);
				}

			}

			// if the game map is winter, differnt layouts will happen
			if (Road.GameMap == Road.GAMEMAP.WINTER) {
				// Ice Obstacle
				if (System.currentTimeMillis() - startTimeObstacle > 10000) {
					ice = new IceObstacle(NPCCar.lanes[rando.nextInt(8)], 0);
					startTimeObstacle = System.currentTimeMillis();
				}

				if (ice != null) {

					// detects when the ice has moved off the screen and makes it null
					if (ice.y >= GAME_HEIGHT) {
						ice = null;
					}
					// detects if the players touch the ice
					if (PlayerCar.p1Hbox.intersects(IceObstacle.iceCollisionDetector)) {
						ice = null;
						p1Slip = true;
						p1TempKeyDisable = true;
						soundClass.playSkid();

						// creates an instance of randomness to the direction of the slip
						if (rando.nextInt(1, 3) == 1) {
							p1SlipVal = 2;
						} else {
							p1SlipVal = -2;
						}
						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								p1Slip = false;
								p1TempKeyDisable = false;
								p1SlipVal = 0;
							}
						};
						timer.schedule(task, 1000);
					}
					if (PlayerCar.p2Hbox.intersects(IceObstacle.iceCollisionDetector)) {
						ice = null;
						p2Slip = true;
						p2TempKeyDisable = true;
						soundClass.playSkid();

						// creates an instance of randomness to the direction of the slip
						if (rando.nextInt(1, 3) == 1) {
							p2SlipVal = 2;
						} else {
							p2SlipVal = -2;
						}

						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								p2Slip = false;
								p2TempKeyDisable = false;
								p2SlipVal = 0;
							}
						};
						timer.schedule(task, 1000);
					}

				}
			}

			// Special conditions for the AUTMN map
			if (Road.GameMap == Road.GAMEMAP.AUTUMN) {

				if (System.currentTimeMillis() - startTimeObstacle > 1000) {
					leaves = new LeavesObstacle(NPCCar.lanes[rando.nextInt(8)], 0);
					startTimeObstacle = System.currentTimeMillis();
				}

				if (leaves != null) {
					// detects if the players touch the leaves and blocks their vision
					if (PlayerCar.p1Hbox.intersects(LeavesObstacle.leavesCollisionDetector)) {
						p1VisionBlock = true;

						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								p1VisionBlock = false;

							}
						};
						timer.schedule(task, 2000);
					}
					if (PlayerCar.p2Hbox.intersects(LeavesObstacle.leavesCollisionDetector)) {
						p2VisionBlock = true;

						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								p2VisionBlock = false;
							}
						};
						timer.schedule(task, 2000);
					}
					if (leaves.y >= GAME_HEIGHT) {
						leaves = null;
					}
				}
			}
		}
	}

	// run() method is what makes the game continue running without end. It calls
	// other methods to move objects, check for collision, and update the screen
	//Source: The man, the myth, the legend, Mr.anthony 
	public void run() {
		// the CPU runs the game code too quickly - we need to slow it down! The
		// following lines of code "force" the computer to get stuck in a loop for short
		// intervals between calling other methods to update the screen.

		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			// only move objects around and update screen if enough time has passed
			if (delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}

	// if a key is pressed, we'll send it over to the PlayerBall class for
	// processing
	public void keyPressed(KeyEvent e) {
		// following if statement is to differentiate inputs for the car on the left vs
		// the car on the right
		if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_S
				|| e.getKeyCode() == KeyEvent.VK_D) {
			playerOne.keyPressed(e);
		} else {
			playerTwo.keyPressed(e);
		}

	}

	// if a key is released, we'll send it over to the other classs for processing
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_S
				|| e.getKeyCode() == KeyEvent.VK_D) {
			playerOne.keyReleased(e);
		} else {
			playerTwo.keyReleased(e);
		}
	}

	// left empty because we don't need it; must be here because it is required to
	// be overrided by the KeyListener interface
	public void keyTyped(KeyEvent e) {

	}
}
