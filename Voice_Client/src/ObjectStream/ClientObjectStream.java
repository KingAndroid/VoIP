/**
 *
 * @Author : Ofir Attia
 * @File : ClientObjectStream.java
 * @Description : The following file connect to the server and receive objects
 * from the server. for example, the users JTree of the server update in the
 * clients.
 * @Date : 5/3/2015
 */
package ObjectStream;

import Resources.Resources;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class ClientObjectStream implements Runnable {

    public JTree jTree;
    public Socket con = null;
    public String host;
    int port;
    public ObjectInputStream fromServer = null;
    public ObjectOutputStream inServer = null;
    private ObjectOutputStream toServer;
    private final JTextField userName;
    private final JButton saveUserName;
    public String userNameString;
    public int currentRoom;

    /**
     *
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

    public void updateUser(String oldName, String newName, int currentRoom) {
        this.sendData("UPDATE#" + oldName + "#" + newName + "#" + currentRoom);
        this.userNameString = newName;
        Resources.getInstance().setGlobalConfiguration(newName);
    }

    public void joinRoom(int portN) {
        currentRoom = portN;
        this.sendData("JOIN#" + this.userName.getText() + "#" + portN);
    }

    public void leaveRoom(int currentRoom) {
        this.sendData("LEAVE#" + this.userName.getText() + "#" + currentRoom);
    }

    public String getUserName() {
        return this.userNameString;
    }

    public void exit() {
        this.leaveRoom(currentRoom);
    }

    public void initSaveUserName() {
        this.saveUserName.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser(getUserName(), userName.getText(), currentRoom);
            }
        });
    }

    private synchronized void refreshTree() {
        DefaultTreeModel model = (DefaultTreeModel) this.jTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        model.nodeChanged(root);
        for (int i = 0; i < jTree.getRowCount(); i++) {
            jTree.expandRow(i);
        }

    }

    public void initRooms() {
        DefaultTreeModel model = (DefaultTreeModel) (this.jTree).getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        for (int i = 1; i < 6; i++) {
            DefaultMutableTreeNode Room = new DefaultMutableTreeNode("Room" + i);
            root.add(Room);
            refreshTree();

        }
    }

    public synchronized void updateTree(Object o) {
        DefaultTreeModel modelServer = (DefaultTreeModel) ((JTree) o).getModel();
        this.jTree.setModel(modelServer);
        refreshTree();
    }
}
