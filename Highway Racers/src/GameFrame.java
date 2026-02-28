/* GameFrame class establishes the frame (window) for the game
It is a child of JFrame because JFrame manages frames
Runs the constructor in GamePanel class

*/
import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame{

    GamePanel panel;

    public GameFrame(){
        panel = new GamePanel(); //run GamePanel constructor

        this.add(panel);

        this.setTitle("Highway Racers"); //set title for frame

        this.setBackground(Color.BLACK); //sets background to black

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //X button will stop program execution

        this.pack();//makes components fit in window - don't need to set JFrame size, as it will adjust accordingly

        this.setVisible(true); //makes window visible to user

        this.setResizable(false);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        this.setLocationRelativeTo(null);//set window in middle of screen
    }

}