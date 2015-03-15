
package UDP;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
/**
 *
 * @Author : Ofir Attia
 * @File : ConversationExecutor.java
 * @Description : The following file control two objects, SoundSender(AKA SS)
 * and the another is SoundReceiver(AKA SR) The class create the Socket object
 * and pass it to SS and SR. Options: 1. stop() method change the "keep" flags
 * in SS & SR Objects to close the connection with the server and in addition to
 * close the Speaker and Microphone. 2. mute() & talk() execute by hotkey press
 * & release.
 * @Date : 5/3/2015
 */
public class ConversationExecutorUDP implements Runnable {

    /**
     * Thread to hold the ReceiverThread object
     */
    private Thread inThread;
    /**
     * Thread to hold the SendThread object
     */
    private Thread outThread;
    /**
     * ReceiverThread - wait for data from the server and flush it to the speakers of the user.
     */
    private ReceiverThread SR;
    /**
     * SenderThread - wait for user to Push-To-Talk and send the voice data to the server.
     */
    private SenderThread SS;
    /** 
     * hold the internet address to listen to messages from the server. ( Broadcasting server ).
     */
    final static String INET_ADDR = "224.0.0.3";
    public final static int DEFAULT_PORT = 8;

    /**
     * host of the server to connect to.
     */
    public String host;

    /**
     * port that the server is listen to.
     */
    public int port;
   
    /**
     * get the name of the internet address
     * @see INET_ADDR
     */
    private InetAddress ia = null;

    /**
     * constructor initialize the host and port fields
     *
     * @param host
     * @param port
     */
    public ConversationExecutorUDP(String host, int port) {
        try {
            this.host = host;
            this.port = port;
            ia = InetAddress.getByName(host);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConversationExecutorUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * control the SenderThread that take data from microphone and send it to the server.
     */
    public void talk() {
        SS.talk();
        muteInComing();
    }

    /**
     * control the SenderThread and stop send data to the server.
     */
    public void mute() {
        SS.mute();
        unmuteInComing();
    }

    /**
     * control the ReceiverThread and mute incoming sound from server
     */
    public void muteInComing() {
        System.out.println("muteing");
        SR.mute();
    }

    /**
     * control the ReceiverThread and unmute incoming sound from server
     */
    public void unmuteInComing() {
        SR.unMute();
    }

    /**
     * stop the conversation with the server.
     * close the microphone,speakers of the user
     * and stop the threads ( SenderThread & ReceiverThread : halt() method)
     */
    public synchronized void stop() {
        
        System.out.println("Stoping Conversation");
        notify();
        if (SS.microphone != null) {
            SS.microphone.flush();
            SS.microphone.stop();
            SS.microphone.close();
        }
        SR.inSpeaker.flush();
        SR.inSpeaker.close();
        System.out.println("( SoundReciver ) - OFF");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {}
        SS.halt();
        SR.halt();

    }

    @Override
    public void run() {
        try {
            
            SR = new ReceiverThread(INET_ADDR, port);
            SS = new SenderThread(ia, port);
            inThread = new Thread(SR);
            outThread = new Thread(SS);
            inThread.start();
            outThread.start();
            // wait for the threads to finish to be sure everyting is closed.
            outThread.join();
            inThread.join();

            System.out.println("*****CLOSE*****");
            Thread.sleep(1000);
            
        } catch (Exception ex) {}
    }

}
