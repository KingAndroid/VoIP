
package voice_client;

import GUI.GUI;

import EventHandlers.CloseFrame;
import Componenets.User;
import EventHandlers.GlobalHotKeyListener;
import EventHandlers.MuteIncomingCheckboxHandler;
import EventHandlers.PushToTalkSelector;
import ObjectStream.ClientObjectStream;
import UDP.ConversationExecutorUDP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 *
 * @Author : Ofir Attia
 * @File : Driver.java
 * @Description : The following file is the driver that perform the user actions
 * on the gui. close conversations and start new ( switch rooms ). 1. On click
 * tree node , that user will be move to this room( disconnect from the old one
 * and connect to the new one ).
 *
 * @Date : 5/3/2015
 */
public class Driver implements Runnable {
    public static String roomHashTag = "#1";
    /**
     * host address
     */
    public String host = null;

    /**
     * port number
     */
    public int port = -1;
    /**
     * GUI to bind events .
     */
    private GUI g;

    /**
     * JTree to bind events and to update him from the server
     *
     * @see ClientObjectStream.java
     */
    public JTree jTree;
    /**
     * number of the current room.
     */
    private int currentRoom = -1;

    /**
     * socket object - hold the connection to the server.
     */
    public Socket conn = null;

    /**
     * Conversation object
     */
    public ConversationExecutorUDP ConversationObject;

    /**
     * custom tree node
     */
    public User me;

    /**
     * the window event handler
     */
    public CloseFrame FrameHandler;

    /**
     * ClientObjectStream control the updates from the server ( Online Users for
     * example )
     *
     * @see ClientObjectStream.
     */
    public ClientObjectStream ObjectReceiverClient;

    /**
     * Global Key Listener - Push-To-Talk
     */
    public GlobalHotKeyListener GlobalHotKey;
    /**
     * GUI checkbox to mute/unmute incoming data from server
     */
    private JCheckBox muteCheckBox;

    /**
     * GUI field to track the Push-To-Talk key
     */
    public JTextField hotKeyField;

    /**
     * GUI button to save the Push-To-Talk key
     */
    public JButton saveButton;

    /**
     * Eventhandler for the Push-To-Talk, by clicking on the key the class will
     * initiate talk() method and mute() method in the current conversation object
     */
    public PushToTalkSelector PTS;

    /**
     * Eventhandler for the mute checkbox - by check/uncheck
     * the class will initiate muteIncoming/unmuteIncoming 
     * methods in the current conversation object.
     */
    public MuteIncomingCheckboxHandler MIH;
    /**
     * GUI field to hold the username 
     */
    private JTextField userName;
    /**
     * GUI button to save the username.
     */
    private JButton saveUserName;
    /**
     * Connect Button \ Disconnect Button
     */
    private JButton connectButton;
    private JButton disconnectButton;

    /**
     * Init class fields, create 5 rooms to let the user to choose between. Init
     * jTree Mouse Listener - to choose between the rooms.
     *
     * @param host
     * @param port
     * @param g
     * @param jTree
     */
    public Driver(String host, int port, GUI g, JTree jTree) {
        this.host = host;
        this.port = port;
        this.g = g;
        this.jTree = jTree;
        this.currentRoom = 1;

        jTree.addMouseListener(new MouseAdapter() {
            public synchronized void mouseClicked(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me) && me.getClickCount() == 2) {

                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                    // check if the user clicking on Room node and not on user, because the listener is on each node in the tree
                    String[] partsCheck = selectedNode.toString().split("m");
                    if(!partsCheck[0].equals("Roo")){System.out.println("no room");return;}
                    // when the user switch rooms we will close the current conversation.
                    //***ConversationObject.stop(); 
                    // send leave message to the server that the user left the room.
                    ObjectReceiverClient.leaveRoom(currentRoom);
                    // Example Room3 will be splitted to Room and 3
                    String[] parts = selectedNode.toString().split("m"); 
                    // Convert it to integer (3)
                    int portN = Integer.parseInt(parts[1]);
                    // Print it for debugging
                    System.out.println("port :" + portN);
                    System.out.println("Joining Room : " + portN);
                    // send message to the server to join the room
                    ObjectReceiverClient.joinRoom(portN);
                    // update the current class attribute in the current room.
                    currentRoom = Integer.parseInt(parts[1]);
                    roomHashTag = "#"+currentRoom;
                    // create new conversation - initial SenderThread and ReceiverThread
                    //***ConversationObject = new ConversationExecutorUDP(host, port + portN);
                    // update the key listener with the new conversation
                    //GlobalHotKey.setCE(ConversationObject); 
                    // update the window listener with the new conversation
                    //FrameHandler.setCE(ConversationObject);  
                    // update the mutecheckbox with the new conversation
                    //MIH.setCO(ConversationObject);
                    // update the push to talk listener with the new conversation
                    //PTS.setHK(GlobalHotKey);
                    // set the new conversation in thread
                    //Thread chat = new Thread(ConversationObject);
                    // start the new conversation.
                    //chat.start();                                                   
                    System.out.println("New Conversation has started");

                }
            }
        });

    }

    @Override
    public void run() {
        // create conversation
        ConversationObject = new ConversationExecutorUDP(this.host, this.port + 1);
        Thread chat = new Thread(ConversationObject);
        // start the conversation
        chat.start();
        // set window event handler and initiate it with the conversation object 
        FrameHandler = new CloseFrame(ConversationObject);
        // add the window listener to the gui.
        g.addWindowListener(FrameHandler);
        // create global hot key listener - use JNI libarary
        GlobalHotKey = new GlobalHotKeyListener(jTree, ConversationObject);
        // set push to talk selector - update on-the-fly the global hot key listener
        PTS = new PushToTalkSelector(GlobalHotKey, this.hotKeyField, this.saveButton);
        Thread pts = new Thread(PTS);
        // start the push-to-talk handler
        pts.start();

        // define global key listener
        initListener(GlobalHotKey);

        // set mute checkbox listener to block incoming data from server
        MIH = new MuteIncomingCheckboxHandler(ConversationObject, this.muteCheckBox);
        Thread mih = new Thread(MIH);
        // start the checkbox listener
        mih.start();

        // set object receiver from the server - update the gui of the current client.
        ObjectReceiverClient = new ClientObjectStream(host, port, jTree, userName, saveUserName);
        // when the user close the window we will send exit message to the server to remove the user from the gui and update everyone.
        FrameHandler.initClientObjectStream(ObjectReceiverClient); 
        // wait for messages from the server.
        Thread notif = new Thread(ObjectReceiverClient);
        // start the object receiver 
        notif.start();
        
        // set action listener for connect\disconnect button
        this.disconnectButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
               
                    ObjectReceiverClient.disconnect();    
                    ConversationObject.stop();
                    
                    
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
                }
                    connectButton.setEnabled(true);
                    disconnectButton.setEnabled(false);
                    
               
            }
        
        });
        

    }

    /**
     * receive GlobalHotKeyListener object and register the event listener.
     *
     * @param HK
     */
    public void initListener(GlobalHotKeyListener HK) {
        try {
            GlobalScreen.registerNativeHook();

        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.getInstance().addNativeKeyListener(HK);

    }

    /**
     * Init the Driver class with the GUI objects
     * @param connectButton
     * @usage : pass those objects to the event handlers.
     * @param muteCheckBox
     * @param hotKeyField
     * @param saveButton
     * @param userName
     * @param saveUserName
     */
    public void init(JCheckBox muteCheckBox, JTextField hotKeyField, JButton saveButton, JTextField userName, JButton saveUserName,JButton disconnectButton,JButton connectButton) {
        this.muteCheckBox = muteCheckBox;
        this.hotKeyField = hotKeyField;
        this.saveButton = saveButton;
        this.userName = userName;
        this.saveUserName = saveUserName;
        this.disconnectButton = disconnectButton;
        this.connectButton = connectButton;
    }
}
