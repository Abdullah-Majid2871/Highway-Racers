/* Date: January 17 2025
 * Description: Sets up and helps with the playing of the sound
 */
import java.io.*;
import javax.sound.sampled.*;
public class SoundClass {

    private File crashSoundFile;
    private File skidSoundEffect;
    private File bgMusic;

    private Clip crash, skid, bgClip;


    private AudioInputStream crashInputStream;
    private AudioInputStream skidInputStream;
    private AudioInputStream bgInputStream;

    //constructor for Audio class
    public SoundClass() {
        try {
            // sets up the audio files
            crashSoundFile = new File("CarCrash.wav"); // Obtains the soudn effect for goal scored
            skidSoundEffect = new File("CarSkidding.wav");
            bgMusic = new File("bgMusic.wav");

            crashInputStream = AudioSystem.getAudioInputStream(crashSoundFile);
            skidInputStream = AudioSystem.getAudioInputStream(skidSoundEffect);
            bgInputStream = AudioSystem.getAudioInputStream(bgMusic);

            crash = AudioSystem.getClip();
            crash.open(crashInputStream);

            skid = AudioSystem.getClip();
            skid.open(skidInputStream);

            bgClip = AudioSystem.getClip();
            bgClip.open(bgInputStream);

            // Catches exceptions
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Audio file format is not supported: " + e.getMessage()); //prints to the standard error.
        } catch (IOException e) {
            System.err.println("Error playing audio file: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line for playing back is unavailable: " + e.getMessage());
        }
    }

    //Plays goal scored effect
    public void playCrash() {
        crash.setFramePosition(0); // rewinds from start
        crash.start();
    }

    //Plays goal scored effect
    public void playSkid() {
        skid.setFramePosition(0); // rewinds from start
        skid.start();
    }

    public void playbgMusic() {
        bgClip.setFramePosition(0);
        bgClip.loop(bgClip.LOOP_CONTINUOUSLY);
        bgClip.start();
    }
}
