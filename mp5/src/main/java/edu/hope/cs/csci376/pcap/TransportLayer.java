package edu.hope.cs.csci376.pcap;

public class TransportLayer {

    public static final short TRANSPORT_PORT_HTTP = (short) 0x50;

    int length = 0;
    byte[] packet;

    int sourcePort = 0;
    int destinationPort = 0;

    public TransportLayer(byte[] packet) {

        this.packet = packet;
        length = packet.length;

        sourcePort = (packet[0]&0xFF)*256 + (packet[1]&0xFF);
        destinationPort = (packet[2]&0xFF)*256 + (packet[3]&0xFF);
        
    }

    public byte[] getPayload() {
        byte[] payload = new byte[length-20];
        for (int b=0; b<length-20; b++) payload[b] = packet[b+20];
        return payload;
    }

    public void print() {
        System.out.println("--- Transport Layer ---");
        System.out.println("   Source port: "+ sourcePort) ;
        System.out.println("   Destination port: "+ destinationPort);
    }

}
