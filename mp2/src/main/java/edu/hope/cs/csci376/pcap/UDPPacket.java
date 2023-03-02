package edu.hope.cs.csci376.pcap;

public class UDPPacket extends TransportLayer{

    public byte[] packet;

    public UDPPacket(byte[] packet) {
        super(packet);
        this.packet = packet;
    }

    // Final print method of all Header information
    public void print() {
        System.out.println("--- Transport Layer: UDP Packet ---");
        super.print(); // Gives Source & Destination port
        System.out.println("   Length: " + getLength() + 
                           "\n   Checksum: " + getCheckSum());

    }
    //UDP Packet length including headers
    public long getLength(){
        return (packet[4] & 0xFF) * 256 + (packet[5] & 0xFF); 
    }

    //UDP 16 bit check sum
    public long getCheckSum(){
        return (packet[6] & 0xFF) * 256 + (packet[7] & 0xFF); 
    }
    
}
