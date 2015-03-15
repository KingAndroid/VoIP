/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;


import Componenets.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import static UDP.EchoServer.BUFSIZE;
import static UDP.EchoServer.INET_ADDR;
import static UDP.EchoServer.SERVICE_PORT;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Ofir Attia
 */
public class RoomListener implements Runnable {
        
        Socket connection = null;
        final static String INET_ADDR = "224.0.0.3";
        public static final int BUFSIZE = 20;
        private DatagramSocket socket;
        private DatagramSocket serverSocket ;
        InetAddress addr;
	DataInputStream dataIn = null;
	DataOutputStream dataOut = null;
        JTextArea jText ;
        public JTree jTree ;
        int roomNumber;
	DefaultMutableTreeNode Room;
        public int port=0;
        private final List<User> userList = new ArrayList();
	public RoomListener(int port,JTextArea jText,JTree jTree,int roomNumber,DefaultMutableTreeNode Room) throws Exception
	{
                try
		{
                        this.port=port;
			// Bind to the specified UDP port, to listen
			// for incoming data packets
			socket = new DatagramSocket( this.port );
                        serverSocket = new DatagramSocket();
                         addr = InetAddress.getByName(INET_ADDR);
			System.out.println ("Server active on port " + socket.getLocalPort() );
                        System.out.println ("Server broadcast on port "+this.port+4000);
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
                                20, addr, this.port+4000);
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
        @Override
	public synchronized void run()
	{
           
            serviceClients();
                    
	}
       
       
	
         private static String getTreeText(TreeModel model, Object object, String indent) {
            String myRow = indent + object + "\n";
            for (int i = 0; i < model.getChildCount(object); i++) {
                myRow += getTreeText(model, model.getChild(object, i), indent + "  ");
            }
            return myRow;
        }
	public synchronized void removeUser(Socket connection){
            
            int i=0;
            String tempKey = getUserKey(connection);
            for(User user : userList){
                
                if(user.toString().equals(tempKey)){
                    
                    userList.remove(i);
                    Room.remove(user);
                    System.out.println("Removing user "+user+" from Room "+this.roomNumber);
                    refreshTree();
                    return;
                }
                i+=1;
            }
            
        }
        private synchronized void refreshTree(){
                    DefaultTreeModel model = (DefaultTreeModel)this.jTree.getModel();
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
                    model.nodeChanged(root);
                    model.reload(root);
       
        }
        private synchronized void addUser(Socket connection){
                User u1 = new User(getUserKey(connection));
                System.err.println(u1);
                userList.add(u1);
                Room.add(u1);
                refreshTree();
            
            
        }
	private synchronized String getUserKey(Socket connection){
            return "U:"+connection.getInetAddress()+":"+connection.getPort();
        }
        
}
