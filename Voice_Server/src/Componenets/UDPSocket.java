
package Componenets;

import java.net.InetAddress;

/**
 * @deprecated 
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
