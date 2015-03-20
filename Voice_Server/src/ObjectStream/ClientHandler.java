
package ObjectStream;

import Componenets.User;
import static ObjectStream.ObjectStreamServer.objectStreams;
import static ObjectStream.ObjectStreamServer.rooms;
import static UDP.RoomExecutor.sockets;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
/**
 *
 * @author Ofir Attia
 */
class ClientHandler implements Runnable {
    public Socket con;
    JTree jTree;
    int roomNumber;
    DefaultMutableTreeNode Room;
    private final List<User> userList = new ArrayList();
    public DataInputStream dataIn=null;
    public DataOutputStream dataOut=null;
    public BufferedReader in=null;
    private ObjectInputStream fromClient;
    private ObjectOutputStream toClient;
    public ClientHandler(Socket accept, JTree jTree) {
        
        try {
            this.con=accept;
            this.jTree=jTree;
            sockets[0].add(accept);
            objectStreams.add(new ObjectOutputStream(this.con.getOutputStream()));
            System.out.println("User connected to Object stream server");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
           
            
       
    }

    

    
    public synchronized void broad(){
        
        for(ObjectOutputStream s : objectStreams){
            try {
                
                //ObjectOutputStream tempOut = new ObjectOutputStream(s.getOutputStream());
                
                s.writeObject(new JTree(jTree.getModel()));
                s.flush();
                s.reset();
                
                
            } catch (IOException ex) {
                
               // Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        
    }
    
        private static String getTreeText(TreeModel model, Object object, String indent) {
            String myRow = indent + object + "\n";
            for (int i = 0; i < model.getChildCount(object); i++) {
                myRow += getTreeText(model, model.getChild(object, i), indent + "  ");
            }
            return myRow;
        }
	public synchronized void removeUser(String User,int room){
            
            int i=0;
            
            for(User user : userList){
                
                if(user.toString().equals(User)){
                    
                    userList.remove(i);
                    
                    Room = rooms.get(room-1);
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
      
                    System.out.println("broadcasting: ");
                    TreeModel m = this.jTree.getModel();
                        System.out.println(getTreeText(m,m.getRoot(),""));
                    
       
        }
        private synchronized void addUser(String User,int room){
                User u1 = new User(User);
                System.err.println(u1);
                userList.add(u1);
                Room = rooms.get(room-1);
                Room.add(u1);
                refreshTree();
            
            
        }
	

    @Override
    public void run() {
        try {
            //dataIn = new DataInputStream(this.con.getInputStream());
            //dataOut = new DataOutputStream(this.con.getOutputStream());
            
             //broad();
            String  bytesRead="";
            byte[] buffer;
            
           //in = new BufferedReader(new InputStreamReader(dataIn));
            Object o=null;
           fromClient = new ObjectInputStream(this.con.getInputStream());
           while (true) {
                    try{
                        System.out.println("trying to read...");
                        o = fromClient.readObject();
                        
                        if(o==null)continue;
                    }catch(IOException e){
                        System.out.println("exception "+e.getMessage());
                        return;
                    } catch (ClassNotFoundException ex) {
                    //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                    String os = (String)o;
                    if(os==null)continue;
                    System.out.println("DATA: "+os);
                    
                    String[] command = os.split("#");
                    System.out.println("command: "+command[0].toString());
                    switch(command[0]){
                        case "JOIN":
                            System.out.println("User Join "+command[1]+" To ROOM: "+command[2]);
                            addUser(command[1],Integer.parseInt(command[2]));
                            broad();
                            break;
                        case "LEAVE":
                            System.out.println("User Left "+command[1]+" ROOM: "+command[2]);
                            removeUser(command[1],Integer.parseInt(command[2]));
                            broad();
                            break;
                        case "UPDATE":
                            System.out.println("User update "+command[1]+" to: "+command[2]+" in room "+command[3]);
                            updateUser(command[1],command[2],Integer.parseInt(command[3]));
                            //Thread.sleep(1000);
                            broad();
                            break;
                    }
                    o=null;
                    Thread.sleep(200);
                            
             }
          
            
        } 
        catch (IOException | InterruptedException ex) {}
    }

    private void updateUser(String oldName, String newName, int room) {
        removeUser(oldName,room);
        addUser(newName,room);
    }
    /**
     *
     * @param room
     * @return the root of jTree
     */
    public DefaultMutableTreeNode getRootOfTree(int room){
                DefaultTreeModel model = (DefaultTreeModel)(this.jTree).getModel();
                DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
                DefaultMutableTreeNode s = (DefaultMutableTreeNode) root.getChildAt(room-1);
                return s;
    }
}
