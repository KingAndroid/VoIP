/**
 *
 * @Author : Ofir Attia
 * @File : CloseFrame.java
 * @Description : The following file is a custom EventListener of the window.
 * When the user close the window , we will close the conversation
 * (ConversationExecutor) In addition, there is option to update the CE when the
 * user switch rooms.
 * @Date : 5/3/2015
 */
package EventHandlers;

import java.awt.*;
import java.awt.event.*;

import ObjectStream.ClientObjectStream;
import UDP.ConversationExecutorUDP;

public class CloseFrame extends Frame implements WindowListener {

    Label label;
    ConversationExecutorUDP x = null;
    private ClientObjectStream COS;

    CloseFrame(String title) {
        setTitle(title);
        label = new Label("Close the frame");
        addWindowListener(this);
    }

    /**
     * init the event listener with ConversationExecutor.
     *
     * @param s
     */
    public CloseFrame(ConversationExecutorUDP s) {
        this.x = s;
    }

    public void initClientObjectStream(ClientObjectStream COS) {
        this.COS = COS;
    }

    void launchFrame() {
        setSize(300, 300);
        setVisible(true);
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("EXIT");
        COS.exit();
        x.stop();
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    /**
     * Update the current ConversationExecutor with new one.
     *
     * @param x
     */
    public void setCE(ConversationExecutorUDP x) {
        this.x = x;
    }

}
