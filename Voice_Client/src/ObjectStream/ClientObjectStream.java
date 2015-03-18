
package ObjectStream;

import Resources.Resources;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
/**
 *
 * @Author : Ofir Attia
 * @File : ClientObjectStream.java
 * @Description : The following file connect to the server and receive objects
 * from the server. for example, the users JTree of the server update in the
 * clients.
 * @Date : 5/3/2015
 */
public class ClientObjectStream implements Runnable {

    /**
     * jTree object - refresh with the jTree of the server
     */
    public JTree jTree;

    /**
     * socket of the server
     */
    public Socket con = null;

    /**
     * host of the server
     */
    public String host;
    int port;

    /**
     * input stream of the server
     */
    public ObjectInputStream fromServer = null;

    /**
     * output stream of the server
     */
    private ObjectOutputStream toServer;
    /**
     * username GUI object
     */
    private final JTextField userName;
    /**
     * save button next to userName
     */
    private final JButton saveUserName;

    /**
     * hold the username @ string
     */
    public String userNameString;

    /**
     * hold the current room number
     */
    public int currentRoom;

    /**
     * Constructor
     * @param host
     * @param port
     * @param jTree
     * @param userName
     * @param saveUserName
     */
    public ClientObjectStream(String host, int port, JTree jTree, JTextField userName, JButton saveUserName) {

        this.host = host;
        this.port = port;
        this.jTree = jTree;
        this.userName = userName;
        this.saveUserName = saveUserName;
        initRooms();
        initSaveUserName();

    }

    /**
     * send data to the server
     * @param s
     */
    public void sendData(String s) {
        try {
            toServer.writeObject((s));
            toServer.flush();
            toServer.reset();
        } catch (IOException ex) {
        }
    }

    @Override
    public void run() {

        try {
            System.out.println("Connecting to " + host + " port: " + port);
            con = new Socket(host, port);
            toServer = new ObjectOutputStream(this.con.getOutputStream());
            userNameString = userName.getText();
            joinRoom(1);
            currentRoom = 1;
            fromServer = new ObjectInputStream(con.getInputStream());
            while (true) {
                Object o = null;
                try {
                    o = fromServer.readObject();
                    if (o == null) {
                        continue;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    continue;
                }

                if (o instanceof JTree) {

                    updateTree(o);
                }
                if (o instanceof String) {
                    System.out.println("String :" + ((String) o).toString());
                }
            }
        } catch (IOException ex) {
        }
    }

    /**
     * update username and send the request to the server ( to update everyone )
     * @param oldName
     * @param newName
     * @param currentRoom
     */
    public void updateUser(String oldName, String newName, int currentRoom) {
        this.sendData("UPDATE#" + oldName + "#" + newName + "#" + currentRoom);
        this.userNameString = newName;
        Resources.getInstance().setGlobalConfiguration(newName);
    }

    /**
     * send request to the server to join to room 
     * @param portN
     */
    public void joinRoom(int portN) {
        currentRoom = portN;
        this.sendData("JOIN#" + this.userName.getText() + "#" + portN);
    }

    /**
     * send request to server to leave the current room
     * @param currentRoom
     */
    public void leaveRoom(int currentRoom) {
        this.sendData("LEAVE#" + this.userName.getText() + "#" + currentRoom);
    }

    /**
     *
     * @return username string
     */
    public String getUserName() {
        return this.userNameString;
    }

    /**
     * send request to server to leave the room
     */
    public void exit() {
        this.leaveRoom(currentRoom);
    }

    /**
     * Assign ActionListener to saveUsername button
     * @see updateUser
     */
    public void initSaveUserName() {
        this.saveUserName.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser(getUserName(), userName.getText(), currentRoom);
            }
        });
    }
    /**
     * refresh the jTree GUI object
     */
    private synchronized void refreshTree() {
        DefaultTreeModel model = (DefaultTreeModel) this.jTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        model.nodeChanged(root);
        for (int i = 0; i < jTree.getRowCount(); i++) {
            jTree.expandRow(i);
        }

    }

    /**
     * Create a Tree model of rooms to the user
     */
    public void initRooms() {
        DefaultTreeModel model = (DefaultTreeModel) (this.jTree).getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        for (int i = 1; i < 6; i++) {
            DefaultMutableTreeNode Room = new DefaultMutableTreeNode("Room" + i);
            root.add(Room);
            refreshTree();

        }
    }

    /**
     * update the current tree of the user with new one ( Received from server )
     * @param o
     */
    public synchronized void updateTree(Object o) {
        DefaultTreeModel modelServer = (DefaultTreeModel) ((JTree) o).getModel();
        this.jTree.setModel(modelServer);
        refreshTree();
    }
}
