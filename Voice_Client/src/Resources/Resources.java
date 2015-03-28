package Resources;

import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author Ofir Attia
 * @file Resources.java
 * @description : this class is a Singleton Pattern, used to get&set media e.g
 * ini files, sound files
 * @date: 15/03/2015
 */
public class Resources {

    private static Resources instance = null;
    private static final String workingdirectory = System.getProperty("user.dir");

    protected Resources() {
        // Exists only to defeat instantiation.
    }

    /**
     *
     * @return instance of Resources
     */
    public static Resources getInstance() {
        if (instance == null) {
            instance = new Resources();
        }
        return instance;
    }

    /**
     *
     * @return push-to-talk button
     */
    public String getHotKeyConfiguration() {
        try {
            FileInputStream fileInputStream = null;
            // load the hotkey configuration into the gui
            Properties p = new Properties();
            fileInputStream = new FileInputStream(workingdirectory + "\\resources\\Config.ini");
            p.load(fileInputStream);
            String s = (p.getProperty("Hotkey"));
            return s;
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }
        return null;
    }
    /**
     * 
     * @return iconJFrame URL 
     */
    public String getIconJFrame(){
        return workingdirectory + "\\resources\\iconJFrame40.png";
        
    }
    /**
     * open UserManual.pdf
     */
    public void openUserManual() {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(workingdirectory + "\\resources\\UserManual.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }

    /**
     * save the push-to-talk button
     *
     * @param hotKeyCode
     */
    public void setHotKey(int hotKeyCode) {
        Properties p = new Properties();
        p.setProperty("Hotkey", hotKeyCode + "");
        FileOutputStream output;
        try {
            output = new FileOutputStream(workingdirectory + "\\resources\\Config.ini");
            p.store(output, null);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     * save the username
     *
     * @param newName
     */
    public void setGlobalConfiguration(String newName) {
        Properties p = new Properties();
        p.setProperty("Username", newName + "");
        FileOutputStream output;
        try {
            output = new FileOutputStream(workingdirectory + "\\resources\\Global.ini");
            p.store(output, null);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     *
     * @return username
     */
    public String getGlobalConfiguration() {
        FileInputStream fileInputStream = null;
        try {
            Properties p = new Properties();
            fileInputStream = new FileInputStream(workingdirectory + "\\resources\\Global.ini");
            try {
                p.load(fileInputStream);
            } catch (IOException ex) {
                //Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            String s = (p.getProperty("Username"));

            return s;
        } catch (FileNotFoundException ex) {
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
            }
        }
        return null;
    }

    /**
     *
     * @return the audio stream object with the beep sound ( when user
     * push-to-talk )
     */
    public AudioInputStream getSound() {
        AudioInputStream audioInputStream = null;
        try {
            File soundFile = new File(workingdirectory + "\\resources\\beep.wav");
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            return audioInputStream;
        } catch (UnsupportedAudioFileException ex) {
        } catch (IOException ex) {
        }

        return null;
    }
}
