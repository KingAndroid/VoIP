package UDP;

import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
/**
 *
 * @Author : Ofir Attia
 * @File : SenderThread.java
 * @Description : this class control the data the send to the server.
 * @Date : 15/3/2015
 */
public class SenderThread extends Thread {

    private final InetAddress server;
    private final DatagramSocket socket;
    private boolean stopped = false;
    private final int port;
    /**
     * flag to control when to send the voice data to the server.
     */
    AtomicBoolean zPressed = new AtomicBoolean();
    public TargetDataLine microphone;

    public SenderThread(InetAddress address, int port)
            throws SocketException {
        this.server = address;
        this.port = port;
        this.socket = new DatagramSocket();
        this.socket.connect(server, port);
    }

    public void halt() {
        this.stopped = true;
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    @Override
    public synchronized void run() {
        try {
            AudioFormat af = new AudioFormat(8000.0f, 8, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(af);
            microphone.start();
            int bytesRead = 0;
            byte[] soundData = new byte[20];
            
            try {

                while (true) {
                    if (stopped) {
                        System.out.println(" ( SoundSender ) - OFF");
                        microphone.flush();
                        microphone.stop();
                        microphone.close();
                        Thread.sleep(1000);
                        return;
                    }
                    while (zPressed.get() == false) {
                        System.out.println("Mute...");
                        wait();
                    }
                    bytesRead = microphone.read(soundData, 0, soundData.length);
                

                    if (bytesRead >= 0) {
                        DatagramPacket output
                                = new DatagramPacket(soundData, soundData.length, server, port);

                        socket.send(output);
                    }
                    Thread.yield();

                }
            } 
            catch (IOException ex) {
                System.err.println(ex);
            } catch (InterruptedException ex) {
                //Logger.getLogger(SenderThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        } 
        catch (LineUnavailableException ex) {
            //Logger.getLogger(SenderThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * control when to send the voice data to the server.
     */
    public synchronized void talk() {
        System.out.println("SS talk");
        zPressed.set(true);
        notify();
    }

    /**
     * control when to finish send voice data to the server.
     */
    public void mute() {
        System.out.println("SS mute");
        zPressed.set(false);

    }

}
