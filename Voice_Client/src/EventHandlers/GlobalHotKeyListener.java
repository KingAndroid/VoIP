
package EventHandlers;

import Resources.Resources;
import UserActions.PushToTalk;
import javax.swing.JTree;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import UDP.ConversationExecutorUDP;
/**
 *
 * @Author : Ofir Attia
 * @File : GlobalHotKeyListener.java
 * @Description : The following file is JNI EventListener, the file wait for
 * events from OperationSystem with no need that the window will be focused. On
 * key press we will execute ConversationExecutor(that we init in the
 * constructor ) talk() and On release we will execute mute() of CE. In addition
 * we can update the ConversationExecutor when the user switch rooms.
 *
 * @Date : 5/3/2015 Updates : 1. Changed default hotkey code to variable 2.
 * Initial hotkey button from Config.ini file 3. Added setHOTKEYBUTTON(int
 * keycode) - user able to change the hotkey
 *
 */
public class GlobalHotKeyListener implements NativeKeyListener {

    ConversationExecutorUDP x;
    JTree jTree;
    boolean pressed = false;
    public int HOTKEYBUTTON = 16;

    /**
     * Constructor
     *
     * @param jTree
     * @param x
     */
    public GlobalHotKeyListener(JTree jTree, ConversationExecutorUDP x) {
        this.jTree = jTree;
        this.x = x;
        this.HOTKEYBUTTON = Integer.parseInt(Resources.getInstance().getHotKeyConfiguration());

        

    }

    /**
     * update hot key button
     *
     * @param n
     */
    public void setHOTKEYBUTTON(int n) {
        this.HOTKEYBUTTON = n;
    }

    /**
     *
     * @param e
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        //System.out.println("Key Pressed: " + e.getKeyCode());
        if (e.getKeyCode() == NativeKeyEvent.VK_ESCAPE) {
            GlobalScreen.unregisterNativeHook();
        }

        if (e.getKeyCode() == HOTKEYBUTTON && pressed == false) {
            System.out.println("T clicked");
            x.talk();
            pressed = true;
            Thread beep = new Thread(new PushToTalk());
            beep.start();
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
       // System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (e.getKeyCode() == HOTKEYBUTTON && pressed == true) {
            System.out.println("T released");
            x.mute();
            pressed = false;
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    /**
     *
     * @param newX
     */
    public void setCE(ConversationExecutorUDP newX) {
        this.x = newX;
    }

}
