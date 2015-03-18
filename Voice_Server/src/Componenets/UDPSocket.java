/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Componenets;

import java.net.InetAddress;

/**
 *
 * @author Ofir Attia
 */
public class UDPSocket {
    public InetAddress ADRS;
    public int    PORT;
    public UDPSocket(InetAddress ADRS,int PORT){
        this.ADRS=ADRS;
        this.PORT=PORT;
    }
    @Override
    public String toString(){
        return this.ADRS.toString()+" : "+this.PORT;
    }
}
