/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectStream;

import static UDP.RoomExecutor.sockets;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import javax.swing.JTree;

import java.io.ObjectOutputStream;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 *
 * @author Ofir Attia
 */
public class ObjectStreamServer implements Runnable{
    
    JTree jTree;
    int port;
    public static ArrayList<DefaultMutableTreeNode> rooms = new ArrayList<DefaultMutableTreeNode>();
    public static ArrayList<ObjectOutputStream> objectStreams = new ArrayList<ObjectOutputStream>();
    public ObjectStreamServer(JTree jTree,int port){
        this.jTree=jTree;
        this.port=port;
        sockets[0]=new ArrayList();
        DefaultTreeModel model = (DefaultTreeModel)(this.jTree).getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
            
            for(int i=1;i<6;i++){
                DefaultMutableTreeNode Room = new DefaultMutableTreeNode("Room"+i);
                rooms.add(Room);
                root.add(Room);
                
                refreshTree();
                
            }
    }
    private synchronized void refreshTree(){
                    DefaultTreeModel model = (DefaultTreeModel)this.jTree.getModel();
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
                    model.nodeChanged(root);
                    model.reload(root);
       
        }
    @Override
    public void run() {
        try {
            
            ServerSocket serverSocket = new ServerSocket(port);
            
            while(true){
               
                Thread echoThread = new Thread(new ClientHandler(serverSocket.accept(),this.jTree));
                echoThread.start();
                System.out.println("user connected");
                //sendToAll();
            }
        } catch (IOException ex) {
            System.out.println("Error I/O: "+ex.getMessage());
            //Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
            //Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }    }
}
