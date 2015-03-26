
package UserActions;

import Resources.Resources;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Ofir Attia
 * @File PushToTalk.java
 * @desc The following file is an object that I am passing into a thread when I want to play some sound
 *       for example : when the user push-to-talk I play a beep sound.
 * @Date 18/03/2015
 */
public class PushToTalk implements Runnable {
    /**
     * play a beep sound by using Resources ( Singleton ) class to get the push-to-talk sound
     */
    private void playSound() {
        try {
           
            AudioInputStream audioInputStream = Resources.getInstance().getSound();
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            
        }
    }

    @Override
    public void run() {

        playSound();

    }

}
