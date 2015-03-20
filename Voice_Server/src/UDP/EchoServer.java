package UDP;

import java.net.*;
import java.io.*;

public class EchoServer 
{
	// UDP port to which service is bound
	public static final int SERVICE_PORT = 3001;
        final static String INET_ADDR = "224.0.0.3";
        final static int PORT = 8888;
	// Max size of packet, large enough for almost any client
	public static final int BUFSIZE = 4096;

	// Socket used for reading and writing UDP packets
	private DatagramSocket socket;
        private DatagramSocket serverSocket ;
        InetAddress addr;
	public EchoServer()
	{
		try
		{
			// Bind to the specified UDP port, to listen
			// for incoming data packets
			socket = new DatagramSocket( SERVICE_PORT );
                        serverSocket = new DatagramSocket();
                         addr = InetAddress.getByName(INET_ADDR);
			System.out.println ("Server active on port " + socket.getLocalPort() );
		}
		catch (Exception e)
		{
			System.err.println ("Unable to bind port");
		}
	}

	public void serviceClients()
	{
		// Create a buffer large enough for incoming packets
		byte[] buffer = new byte[BUFSIZE];
			
		for (;;)
		{
			try
			{
				// Create a DatagramPacket for reading UDP packets
				DatagramPacket packet = new DatagramPacket ( buffer, BUFSIZE );

				// Receive incoming packets
				socket.receive(packet);

				System.out.println ("Packet received from " + packet.getAddress() +
					":" + packet.getPort() + " of length " + packet.getLength() );
                                //sendToAll(packet.getData(),packet.getLength());
				// Echo the packet back - address and port 
				// are already set for us !
                                DatagramPacket msgPacket = new DatagramPacket(packet.getData(),
                                20, addr, PORT);
                                serverSocket.send(msgPacket);
                                System.out.println("packet sent ");
				//socket.send(packet);
                                
			}
			catch (IOException ioe)
			{
				System.err.println ("Error : " + ioe);
			} 
		}	
	}
        public  synchronized void sendToAll(byte[] byteArray, int q)
	{
            try {
                DatagramPacket packet = new DatagramPacket ( byteArray, q );
                socket.send(packet);
            } catch (IOException ex) {
                //Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
	}
	public static void main(String args[])
	{
		EchoServer server = new EchoServer();
		server.serviceClients();
	}
}