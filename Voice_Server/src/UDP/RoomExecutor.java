
package UDP;



import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author Ofir Attia
 */
public class RoomExecutor implements Runnable {
    public  static Collection<Socket>[] sockets = new ArrayList[6];
    int port;
    JTextArea jText;
    JTree jTree;
    int roomNumber;
    DefaultMutableTreeNode Room;
    public RoomExecutor(int num,int port,JTextArea jText,JTree jTree){
        sockets[num]=new ArrayList();
        this.port=port;
        this.jText=jText;
        this.jTree=jTree;
        this.roomNumber = num;
        
    }
    @Override
    public void run() {
        try {
            
            
            System.out.println("Room: Created "+this.roomNumber +" and port  listen: "+this.port);
            
               
                Thread echoThread = new Thread(new RoomListener(port,this.jText,this.jTree,this.roomNumber,Room));
                echoThread.start();
                
                //sendToAll();
           
        } catch (IOException ex) {
            //Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
            //Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }    }
    
    
}
