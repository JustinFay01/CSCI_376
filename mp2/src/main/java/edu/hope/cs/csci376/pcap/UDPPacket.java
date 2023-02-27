package edu.hope.cs.csci376.pcap;

public class UDPPacket extends TransportLayer{

    public byte[] packet;

    public UDPPacket(byte[] packet) {
        super(packet);
        this.packet = packet;
    }

    public void print() {
        super.print();
        System.out.println("   Length: " + getLength() + 
                           "\n   Checksum: " + getCheckSum());

    }
    //UDP Packet length 5th and 6th hex number
    public int getLength(){
        return (packet[4] & 0xFF) * 256 + (packet[5] & 0xFF); 
    }

    //UDP Packet length 7th and 8th hex number
    public int getCheckSum(){
        return (packet[6] & 0xFF) * 256 + (packet[7] & 0xFF); 
    }
    
}
