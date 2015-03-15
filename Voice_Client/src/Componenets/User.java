/**
 *
 * @Author : Ofir Attia
 * @File : User.java
 * @Description : The following file is custom DefaultMutableTreeNode.
 * @Date : 5/3/2015
 */
package Componenets;

import javax.swing.tree.DefaultMutableTreeNode;


public class User extends DefaultMutableTreeNode {

    /**
     * username - node name
     */
    public String userName;

    /**
     * status of the node ( Online\Offline for example )
     */
    public String status;

    /**
     * Constructor
     * @param name
     */
    public User(String name) {
        userName = name;

    }

    /**
     * update the status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * retrieve the status
     * @return
     */
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return userName;
    }

}
