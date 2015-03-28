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

    
}
