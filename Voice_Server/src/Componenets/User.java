/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Componenets;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Ofir Attia
 */
public class User extends DefaultMutableTreeNode {
    public String userName;
    public String status;

    public User(String name) {
        userName = name;

    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return userName;
    }

}
