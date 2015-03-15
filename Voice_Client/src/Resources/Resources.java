
package Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Ofir Attia
 * @file Resources.java
 * @description : this class is a Singleton Pattern, used to get&set media
 *                e.g ini files, sound files
 * @date: 15/03/2015
 */
public class Resources {
   private static Resources instance = null;
   private static final String workingdirectory = System.getProperty("user.dir");
   protected Resources() {
      // Exists only to defeat instantiation.
   }
   public static Resources getInstance() {
      if(instance == null) {
         instance = new Resources();
      }
      return instance;
   }
   public String getHotKeyConfiguration(){
       try {
           FileInputStream fileInputStream = null;
           // load the hotkey configuration into the gui
           Properties p = new Properties();
           fileInputStream = new FileInputStream(workingdirectory+"\\resources\\Config.ini");
           p.load(fileInputStream);
           String s = (p.getProperty("Hotkey"));
           return s;
       } catch (FileNotFoundException ex) {
           
       } catch (IOException ex) {
           
       }
       return null;
   }
   public void setHotKey(int  hotKeyCode){
                Properties p = new Properties();
                p.setProperty("Hotkey", hotKeyCode + "");
                FileOutputStream output;
                try {
                    output = new FileOutputStream(workingdirectory+"\\resources\\Config.ini");
                    p.store(output, null);
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                }
   }
   public void setGlobalConfiguration(String newName){
                Properties p = new Properties();
                p.setProperty("Username", newName + "");
                FileOutputStream output;
                try {
                    output = new FileOutputStream(workingdirectory+"\\resources\\Global.ini");
                    p.store(output, null);
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                }
   }
   public String getGlobalConfiguration(){
       FileInputStream fileInputStream = null;
        try {
            Properties p = new Properties();
            fileInputStream = new FileInputStream(workingdirectory+"\\resources\\Global.ini");
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
   public AudioInputStream getSound(){
       AudioInputStream audioInputStream = null;
       try {
           File soundFile = new File(workingdirectory+"\\resources\\beep.wav");
           audioInputStream = AudioSystem.getAudioInputStream(soundFile);
           return audioInputStream;
       } catch (UnsupportedAudioFileException ex) {
       } catch (IOException ex) {
       } 
       
       return null;
   }
}
