/*	Description: Class that controls the score system	
 * 	Date: January 17 2025
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ScoreClass {

	public final int HEART_WIDTH = (int) GamePanel.GAME_WIDTH / 15;
	public final int HEART_HEIGHT = (int) GamePanel.GAME_HEIGHT / 15;

	public int p1Score;
	public int p2Score;

	public int p1Health = 5;
	public int p2Health = 5;

	public double spaceBetweenHearts = (int) (GamePanel.GAME_WIDTH / 30);

	public Image heart;
	public Image grayHeart;


 	public static int highestScore;


	public ScoreClass() {
		p1Score = 0;
		p2Score = 0;

		heart = new ImageIcon("Heart.png").getImage();
		grayHeart = new ImageIcon("grayHeart.png").getImage();
	}

	
	//method for score recording
	public void recordScore(String player, int score) {
		try {
			if (player == "Player1") {
					highestScore = p1Score;
					GamePanel.fileWriter = new FileWriter(GamePanel.file);
					GamePanel.fileWriter.write(Integer.toString(highestScore));					
					GamePanel.fileWriter.close();
			}

			else if (player == "Player2") {
				highestScore = p2Score;
				GamePanel.fileWriter = new FileWriter(GamePanel.file);

				GamePanel.fileWriter.write(Integer.toString(highestScore));
				GamePanel.fileWriter.close();

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(Graphics g) {

		// Set the font size to 20 and text font is arial
		g.setFont(new Font("Arial", Font.BOLD, 65));
		g.setColor(Color.GREEN);

		g.drawString(String.valueOf(Math.max(0, p1Score)), GamePanel.GAME_WIDTH / 4,
				(int) (GamePanel.GAME_HEIGHT / 13.1));
		g.drawString(String.valueOf(Math.max(0, p2Score)), GamePanel.GAME_WIDTH / 2 + GamePanel.GAME_WIDTH / 4,
				(int) (GamePanel.GAME_HEIGHT / 13.1));

		// draws player one's health
		for (int i = 1; i <= 5; i++) {
			if (i <= p1Health) {
				g.drawImage(heart, (int) (spaceBetweenHearts * (i) - spaceBetweenHearts),
						 (GamePanel.GAME_HEIGHT / 90), HEART_WIDTH, HEART_HEIGHT, null);
			} else {
				g.drawImage(grayHeart, (int) (spaceBetweenHearts * (i) - spaceBetweenHearts),
						(GamePanel.GAME_HEIGHT / 90), HEART_WIDTH, HEART_HEIGHT, null);
			}
		}

		// draws player two's health
		for (int i = 1; i <= 5; i++) {
			if (i <= p2Health) {
				g.drawImage(heart, (int) ((GamePanel.GAME_WIDTH / 2) + spaceBetweenHearts * i),
						(GamePanel.GAME_HEIGHT / 90), HEART_WIDTH, HEART_HEIGHT, null);
			} else {
				g.drawImage(grayHeart, (int) ((GamePanel.GAME_WIDTH / 2) + spaceBetweenHearts * i),
						 (GamePanel.GAME_HEIGHT / 90), HEART_WIDTH, HEART_HEIGHT, null);
			}
		}

	}
}