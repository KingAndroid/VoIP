package UDP;

import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
/**
 *
 * @Author : Ofir Attia
 * @File : ReceiverThread.java
 * @Description : This class control the data the sent from the server and write it to the speakers.
 * @Date : 5/3/2015
 */
class ReceiverThread extends Thread {

    DatagramSocket socket;
    private boolean stopped = false;
    public SourceDataLine inSpeaker;
    private int bytesRead = 0;
    private int port;
    private String ia;
    private InetAddress address;
    public boolean keep = true;
    AtomicBoolean muteIncoming = new AtomicBoolean();

    public ReceiverThread(DatagramSocket ds) throws SocketException {
        this.socket = ds;
    }
    
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
            try (MulticastSocket clientSocket = new MulticastSocket(this.port + 4000)) {
                //Joint the Multicast group.
                clientSocket.joinGroup(address);
                bytesRead = 0;
                byte[] inSound = new byte[20];
                byte[] buffer = new byte[20];
                AudioFormat af = new AudioFormat(8000.0f, 8, 1, true, false);
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
                        clientSocket.receive(dp);
                        if (dp.getLength() >= 0 && muteIncoming.get() == false) {
                            inSpeaker.write(dp.getData(), 0, dp.getLength());
                        }
                        inSpeaker.flush();
                        Thread.yield();
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }

                }

            } catch (IOException | InterruptedException ex) {
            }
        } catch (LineUnavailableException ex) {}

    }

    public void mute() {
        muteIncoming.set(true);

    }

    /**
     * "unmute" in the incoming data. un block the access to speaker
     */
    public void unMute() {
        muteIncoming.set(false);

    }

}
