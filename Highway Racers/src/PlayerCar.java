/* Description: Controls the behaviours of the car that the player is controlling
 * Date: January 9 2025
 */

import java.awt.*;

import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.TimerTask;
import java.util.Timer;

import javax.swing.*;

public class PlayerCar extends Rectangle {

    public int yVelocity;
    public int xVelocity;
    public int SPEED = 5; // base movement speed of car
   public static final int CAR_WIDTH = (int)(GamePanel.GAME_WIDTH/15.625); // width of car
   public static final int CAR_HEIGHT = (int)(CAR_WIDTH*1.77777777778); // height of car

    //public static final int CAR_WIDTH = (int)(GamePanel.GAME_WIDTH/13.8888888889 ); // width of car
    //public static final int CAR_HEIGHT = (int)(GamePanel.GAME_HEIGHT/4.6875 );;

    private double currentTime;
    private Timer decelTimer = new Timer();
    private Timer accelTimer = new Timer();

   
    
    // rectangles to handle hit detection
    public static RoundRectangle2D p1Hbox = new RoundRectangle2D.Double(0, 0, PlayerCar.CAR_WIDTH - 50, PlayerCar.CAR_HEIGHT, 50, 50);
    public static RoundRectangle2D p2Hbox = new RoundRectangle2D.Double(0, 0, PlayerCar.CAR_WIDTH - 50, PlayerCar.CAR_HEIGHT, 50, 50);

    // image of the player's car
    public Image mainCarImage;

    // constructor creates car at given location with given dimensions
    public PlayerCar(int x, int y) {
        super(x, y, CAR_WIDTH, CAR_HEIGHT);
        mainCarImage = new ImageIcon("PlayerCar.png").getImage();

    }

    // called from GamePanel when any keyboard input is detected
    /* updates the direction of the car based on user input if the keyboard input
     * isn't any of the options (d, a, w, s, or arrow keys), then nothing happens
     */
    public void keyPressed(KeyEvent e) {
    	
    	if(!GamePanel.p1TempKeyDisable) {
    		if (e.getKeyChar() == 'd') {
                setXDirection(SPEED);
                SPEED += 2;
                accelerateX(GamePanel.playerOne);
                move();
            }

            if (e.getKeyChar() == 'a') {
                setXDirection(SPEED * -1);
                SPEED += 2;

                move();
            }

            if (e.getKeyChar() == 'w') {
                setYDirection(SPEED * -1);
                SPEED += 2;

                move();
            }

            if (e.getKeyChar() == 's') {
                setYDirection(SPEED);
                SPEED += 2;

                move();
            }
    	}
    	
        
    	if(!GamePanel.p2TempKeyDisable) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            setXDirection(SPEED);
            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            setXDirection(SPEED * -1);
            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(SPEED * -1);
            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(SPEED);
            move();
        }
    	}
    }

    // called from GamePanel when any key is released (no longer being pressed down)
    // Makes the car stop moving in that direction
    public void keyReleased(KeyEvent e) {

        if (e.getKeyChar() == 'd') {
            setXDirection(0);
            SPEED = 5;
            move();
        }

        if (e.getKeyChar() == 'a') {
            setXDirection(0);
            SPEED = 5;

            move();
        }

        if (e.getKeyChar() == 'w') {
            setYDirection(0);
            SPEED = 5;
            decelerateY(GamePanel.playerOne);
            move();
        }

        if (e.getKeyChar() == 's') {
            setYDirection(0);
            SPEED = 5;

            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            setXDirection(0);
            SPEED = 5;

            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            setXDirection(0);
            SPEED = 5;

            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(0);
            SPEED = 5;

            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(0);
            SPEED = 5;

            move();
        }
    }

    // called whenever the movement of the ball changes in the y-direction (up/down)
    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    // called whenever the movement of the ball changes in the x-direction
    // (left/right)
    public void setXDirection(int xDirection) {
        xVelocity = xDirection;
    }

    public void decelerateX(PlayerCar car) { // method to smoothly decelerate the car https://www.youtube.com/watch?v=QEF62Fm81h4 (
        if (decelTimer != null) {
            decelTimer.cancel();
        //    System.out.println("previous timer being canceled");
        }
       // System.out.println(car.xVelocity);
        decelTimer = new Timer();
        TimerTask decelerate = new TimerTask() { // Using a timertask for this because running this in the main thread would cause the game to freeze
            @Override
            public void run() {
        //        System.out.println("Timer task is running");
                if (car.xVelocity < 0) {
                    car.xVelocity++;
                    car.setXDirection(xVelocity);
              //      System.out.println(car.xVelocity);
                } else if (car.xVelocity > 0) {
                    car.xVelocity--;
                    car.setXDirection(xVelocity);
           //         System.out.println(car.xVelocity);
                } else {
                    decelTimer.cancel(); // stop the execution of the Timer if the speed has already been reduced to 0
                //    System.out.println("Timer has been canceled");
                }
            }
        };
    //    System.out.println("Scheduling timerTask");
        decelTimer.scheduleAtFixedRate(decelerate, 0, 250); // the timerTask will execute periodically every 0.4 seconds
    }

    public void decelerateY(PlayerCar car) { // method to smoothly decelerate the car https://www.youtube.com/watch?v=QEF62Fm81h4 (
        if (decelTimer != null) {
            decelTimer.cancel();
   //         System.out.println("previous timer being canceled");
        }
 //       System.out.println(car.yVelocity);
        decelTimer = new Timer();
        TimerTask decelerate = new TimerTask() { // Using a timertask for this because running this in the main thread would cause the game to freeze
            @Override
            public void run() {
    //            System.out.println("Timer task is running");
                if (car.yVelocity < 0) {
                    car.yVelocity++;
                    car.setYDirection(yVelocity);
     //               System.out.println(car.yVelocity);
                } else if (car.yVelocity > 0) {
                    car.yVelocity--;
                    car.setYDirection(yVelocity);
    //                System.out.println(car.yVelocity);
                } else {
                    decelTimer.cancel(); // stop the execution of the Timer if the speed has already been reduced to 0
     //               System.out.println("Timer has been canceled");
                }
            }
        };
 //       System.out.println("Scheduling timerTask");
        decelTimer.scheduleAtFixedRate(decelerate, 0, 250); // the timerTask will execute periodically every 0.4 seconds
    }

    public void accelerateX(PlayerCar car) { // method to smoothly decelerate the car https://www.youtube.com/watch?v=QEF62Fm81h4 (
        if (accelTimer != null) {
            accelTimer.cancel();
 //           System.out.println("previous acceleration timer being canceled");
        }
  //      System.out.println(car.xVelocity);
        accelTimer = new Timer();
        TimerTask accelerate = new TimerTask() { // Using a timertask for this because running this in the main thread would cause the game to freeze
            @Override
            public void run() {
                System.out.println("Timer task is running");
                if (car.xVelocity < 0) {
                    car.xVelocity--;
                    car.setXDirection(xVelocity);
              //      System.out.println(car.xVelocity);
                } else if (car.xVelocity > 0) {
                    car.xVelocity++;
                    car.setXDirection(xVelocity);
           //         System.out.println(car.xVelocity);
                } else {
                    accelTimer.cancel(); // stop the execution of the Timer if the speed has already been reduced to 0
           //         System.out.println("Timer has been canceled");
                }
            }
        };
    }

    public void accelerateY(PlayerCar car) { // method to smoothly decelerate the car https://www.youtube.com/watch?v=QEF62Fm81h4 (
        if (accelTimer != null) {
            accelTimer.cancel();
     //       System.out.println("previous acceleration timer being canceled");
        }
     //   System.out.println(car.yVelocity);
        accelTimer = new Timer();
        TimerTask accelerate = new TimerTask() { // Using a timertask for this because running this in the main thread would cause the game to freeze
            @Override
            public void run() {
          //      System.out.println("Timer task is running");
                if (car.yVelocity < 0) {
                    car.yVelocity--;
                    car.setYDirection(yVelocity);
       //             System.out.println(car.yVelocity);
                } else if (car.yVelocity > 0) {
                    car.yVelocity++;
                    car.setYDirection(yVelocity);
             //       System.out.println(car.yVelocity);
                } else {
                    accelTimer.cancel(); // stop the execution of the Timer if the speed has already been reduced to 0
             //       System.out.println("Timer has been canceled");
                }
            }
        };
     //   System.out.println("Scheduling timerTask");
        accelTimer.scheduleAtFixedRate(accelerate, 0, 250); // the timerTask will execute periodically every 0.4 seconds
    }

    // called frequently from both PlayerBall class and GamePanel class
    // updates the current location of the car
    public void move() {
        y += yVelocity;
        x += xVelocity;
        
       
    }

    // called frequently from the GamePanel class
    // draws the current location of the car to the screen
    public void draw(Graphics g) { // Rounded rectangles can't be drawn with Graphics objects - only a Graphics 2D object.
        Graphics2D g2d = (Graphics2D) g; // Created a graphics 2d object by typecasting the graphics object.
        // https://stackoverflow.com/questions/179415/java2d-is-it-always-safe-to-cast-graphics-into-graphics2d
       
        g.drawImage(mainCarImage, x, y, CAR_WIDTH, CAR_HEIGHT, null);


    }
}