/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import Resources.Resources;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Ofir Attia
 */
public class PushToTalk implements Runnable {

    public void playSound() {
        try {
           
            AudioInputStream audioInputStream = Resources.getInstance().getSound();
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

        playSound();

    }

}
