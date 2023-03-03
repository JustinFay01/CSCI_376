package edu.hope.cs.csci376.pcap;

public class UDPPacket {

    int length = 0;
    byte[] packet;

    int sourcePort = 0;
    int destinationPort = 0;

    public UDPPacket(byte[] packet) {
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

    // Final print method of all Header information
    public void print() {
        System.out.println("--- Transport Layer: UDP Packet ---");
        System.out.println("   Source port: "+ sourcePort) ;
        System.out.println("   Destination port: "+ destinationPort); // Gives Source & Destination port
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
