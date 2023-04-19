package edu.hope.cs.csci376.pcap;

public class ARP {

    int hardwareType = 0;
    int protocolType = 0;
    int MACAddrLength = 0;
    int IPAddrLength = 0;
    int operation = 0;
    byte[] senderMACAddress;
    byte[] senderIPAddress;
    byte[] targetMACAddress;
    byte[] targetIPAddress;
    
    public ARP(byte[] packet) {

        hardwareType = (packet[0]&0xFF)*256 + (packet[1]&0xFF); 
    }

    public void print() {
        System.out.println("--- ARP ---");
        System.out.println("   Hardware type: "+ hardwareType);
 
    }
    
}
