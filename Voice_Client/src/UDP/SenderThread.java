package UDP;

import static Quality.CompressionUtils.compress;
import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
    /**
     * server address
     */
    private  InetAddress server;
    /**
     * socket object
     */
    private  DatagramSocket socket;
    /**
     * flag to stop this thread
     */
    private boolean stopped = false;
    /**
     * port of the server
     */
    private  int port;
    /**
     * flag to control when to send the voice data to the server.
     */
    AtomicBoolean zPressed = new AtomicBoolean();

    /**
     * microphone access
     */
    public TargetDataLine microphone;
    
    /**
     * Construct the object with a host and port and create a socket.
     * @deprecated 
     * @param address
     * @param port
     * @throws SocketException
     */
    public SenderThread(InetAddress address, int port) throws SocketException {
        this.server = address;
        this.port = port;
        this.socket = new DatagramSocket();
        this.socket.connect(server, port);
    }
    
    /**
     * Stop this thread.
     */
    public void halt() {
        this.stopped = true;
    }

    /**
     * Init with connected socket
     * @param ds
     */
    public SenderThread(DatagramSocket ds){
        this.socket=ds;
    }

    /**
     * get the socket
     * @return socket
     */
    public DatagramSocket getSocket() {
        return this.socket;
    }

    @Override
    public synchronized void run() {
        try {
            byte[] soundDataCompressed;
            //AudioFormat af = new AudioFormat(8000.0f, 8, 1, true, false);
            AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 8, 1, 1, 8000.0F, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            
            microphone.open(af);
            microphone.start();
            int bytesRead = 0;
            byte[] soundData = new byte[200];
            
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
                    System.out.println("Reading....");
                    bytesRead = microphone.read(soundData, 0, soundData.length);
                    System.out.println("Bytes Read : "+bytesRead);

                    if (bytesRead >= 0) {
                        soundDataCompressed = compress(soundData);
                        DatagramPacket output
                                = new DatagramPacket(soundDataCompressed, soundDataCompressed.length);//, server, port

                        socket.send(output);
                    }
                    Thread.yield();
                     
                }
            } 
            catch (IOException ex) {
                System.err.println(ex);
            } catch (InterruptedException ex) {
            }

        } 
        catch (LineUnavailableException ex) {
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
