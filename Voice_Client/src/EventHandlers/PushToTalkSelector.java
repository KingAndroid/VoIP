
package EventHandlers;

import Resources.Resources;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @Author : Ofir Attia
 * @File : PushToTalkSelector.java
 * @Description : The following file is a custom EventListener of the
 * hotKeyField in settings GUI. When the user click on key in this input he will
 * be able to save his Push-To-Talk button and the key code will be saved in
 * Config.ini file. In addition it will update GlobalHotKey with the new
 * Push-To-Talk button.
 * @Date : 9/3/2015
 */
public class PushToTalkSelector implements Runnable {

    private GlobalHotKeyListener HK;
    private JTextField hotKeyField;
    private JButton saveButton;
    private int hotKeyCode = 16;

    /**
     * constructor to init class fields
     * @param HK
     * @param hotKeyField
     * @param saveButton
     */
    public PushToTalkSelector(GlobalHotKeyListener HK, JTextField hotKeyField, JButton saveButton) {
        this.HK = HK;
        this.hotKeyField = hotKeyField;
        this.saveButton = saveButton;

    }

    /**
     * update the GlobalHotKey Object
     *
     * @param newHK
     */
    public void setHK(GlobalHotKeyListener newHK) {
        this.HK = newHK;
    }

    @Override
    public void run() {
        this.hotKeyField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                System.out.println("evt : " + e.getKeyChar());
                hotKeyField.setText(" ");
                hotKeyField.setText("Keyboard  :" + (hotKeyField.getText()) + "");
                hotKeyCode = e.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
        /*this.hotKeyField.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                hotKeyField.setText(" ");
                hotKeyField.setText("Mouse :" + (e.getButton()) + "");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });*/
        /**
         * when the user click on saveButton , I am updating the Push-To-Talk key in the Global Event Listener
         */
        this.saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Resources.getInstance().setHotKey(hotKeyCode);
                HK.setHOTKEYBUTTON(hotKeyCode);
            }

        });

    }

}
