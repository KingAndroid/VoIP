package UDP;

import static Quality.CompressionUtils.compress;
import static Quality.CompressionUtils.decompress;
import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @Author : Ofir Attia
 * @File : ReceiverThread.java
 * @Description : This class control the data the sent from the server and write
 * it to the speakers.
 * @Date : 5/3/2015
 */
class ReceiverThread extends Thread {

    /**
     * socket
     */
    DatagramSocket socket;
    /**
     * flag to stop this thread.
     */
    private boolean stopped = false;
    /**
     * speakers\headphones access
     */
    public SourceDataLine inSpeaker;
    /**
     * flag to know if data read
     */
    private int bytesRead = 0;
    /**
     * port of the server
     */
    private int port;
    /**
     * host address
     */
    private String ia;
    /**
     * host address converted into InetAddress object
     */
    private InetAddress address;
    /**
     * can be used to stop the thread
     *
     * @deprecated
     */
    public boolean keep = true;
    /**
     * flag to control the data that received to be written to the speakers
     */
    AtomicBoolean muteIncoming = new AtomicBoolean();

    /**
     * Init the receiver thread with the socket ( passed from
     * ConversationExecutorUDP object )
     *
     * @param ds
     * @throws SocketException
     */
    public ReceiverThread(DatagramSocket ds) throws SocketException {
        this.socket = ds;
    }

    /**
     * Init with host and port
     *
     * @deprecated
     * @param ia
     * @param port
     * @throws SocketException
     */
    ReceiverThread(String ia, int port) throws SocketException {
        try {
            this.ia = ia;
            this.port = port;
            address = InetAddress.getByName(ia);
        } catch (UnknownHostException ex) {
        }

    }

    /**
     * stop the infinite loop.
     */
    public void halt() {
        this.stopped = true;
    }

    @Override
    public void run() {

        try {

            
            bytesRead = 0;
            //byte[] inSound = new byte[25];
            byte[] buffer = new byte[200];
            //AudioFormat af = new AudioFormat(8000.0f, 8, 1, true, false);
            AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 8, 1, 1, 8000.0F, false);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
            inSpeaker = (SourceDataLine) AudioSystem.getLine(info);
            inSpeaker.open(af);
            inSpeaker.start();
            while (true) {
                if (stopped) {
                    System.out.println("( SoundReciver ) - OFF");
                    inSpeaker.flush();
                    inSpeaker.close();
                    Thread.sleep(1000);
                    return;
                }
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                try {
                    System.out.println("wait for data from server");
                    socket.receive(dp);
                    if (dp.getLength() >= 0 && muteIncoming.get() == false) {
                        System.out.println("Received data" + dp.getAddress() + " " + dp.getPort() + " size packet: " + dp.getLength());
                         
                        byte[] soundData = (dp.getData());
                        inSpeaker.write(soundData, 0,soundData.length);

                    }

                    Thread.yield();
                } catch (IOException ex) {
                    System.err.println(ex);
                }

            }
        } catch (Exception e) {

        }
    }
    /**
     * "mute" the incoming data, block the access to speaker
     */
    public void mute() {
        muteIncoming.set(true);

    }

    /**
     * "unmute"  the incoming data. un block the access to speaker
     */
    public void unMute() {
        muteIncoming.set(false);

    }

}
