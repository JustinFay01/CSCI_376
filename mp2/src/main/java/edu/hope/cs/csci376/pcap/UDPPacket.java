package edu.hope.cs.csci376.pcap;

public class UDPPacket extends TransportLayer{

    public byte[] packet;

    public UDPPacket(byte[] packet) {
        super(packet);
        this.packet = packet;
    }

    public void print() {
        super.print();
        System.out.println(packet.length);
        System.out.println("   Length: " + getLength());
    }

    public int getLength(){
        int len = (packet[34] & 0xFF) * 256 + (packet[35] & 0xFF); 
        return len;
    }

    public int getCheckSum(){
        return 0;
    }
    
}
