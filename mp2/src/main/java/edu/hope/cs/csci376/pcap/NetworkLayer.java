package edu.hope.cs.csci376.pcap;

public class NetworkLayer {

    public static final byte IPV4_PROTOCOL_TCP = (byte) 0x6;
    public static final byte IPV4_PROTOCOL_UDP = (byte) 0x11;
    public static final byte IPV4_PROTOCOL_ICMP = (byte) 0x3;
    
    byte protocol = (byte) 0x0;
    int length = 0;
    byte[] packet;

    public NetworkLayer(byte[] packet) {

        this.packet = packet;
        length = packet.length;

        protocol = packet[9];
       
    }

    public byte[] getPayload() {
        byte[] payload = new byte[length-20];
        for (int b=0; b<length-20; b++) payload[b] = packet[b+20];
        return payload;
    }

    public void print() {
        System.out.println("--- Network Layer ---");
        System.out.println("   Protocol = "+String.format("0x%02X", protocol));
    }
}
