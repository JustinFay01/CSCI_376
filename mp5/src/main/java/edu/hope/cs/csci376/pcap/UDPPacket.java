package edu.hope.cs.csci376.pcap;

public class UDPPacket extends TransportLayer {

    int length = 0;
    int checksum = 0;

    public UDPPacket(byte[] packet) {
        super(packet);

        length = (packet[4]&0xFF)*256 + (packet[5]&0xFF);
        checksum = (packet[6]&0xFF)*256 + (packet[7]&0xFF);
    }

    public void print() {
        System.out.println("--- Transport Layer: UDP Packet ---");
        System.out.println("   Source port: "+ sourcePort) ;
        System.out.println("   Destination port: "+ destinationPort);
        System.out.println("   Length: "+ length) ;
        System.out.println("   Checksum: "+ checksum);

    }
    
}
