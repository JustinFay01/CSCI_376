package edu.hope.cs.csci376.pcap;

public class NetworkLayer {

    public static final byte IPV4_PROTOCOL_TCP = (byte) 0x6;
    public static final byte IPV4_PROTOCOL_UDP = (byte) 0x11;
    public static final byte IPV4_PROTOCOL_ICMP = (byte) 0x3;
    
    byte protocol = (byte) 0x0;
    int length = 0;
    byte[] packet;
    int version = 0;
    int headerLength = 0;
    int differentiatedServices = 0;
    int ecn = 0;
    int packetLength = 0;
    int identification = 0;
    String flags = "";
    int fragmentOffset = 0;
    int timeToLive = 0;
    int headerChecksum = 0;

    public NetworkLayer(byte[] packet) {

        this.packet = packet;
        length = packet.length;

        version = (packet[0]&0xFF) >> 4;
        headerLength = (packet[0]&0xF);
        differentiatedServices = (packet[1]&0xFF) >> 2;
        ecn = (packet[1]&0x3);
        packetLength = (packet[2]&0xFF)*256 + (packet[3]&0xFF);
        identification = (packet[4]&0xFF)*256 + (packet[5]&0xFF);
        flags = String.format("%3s", Integer.toBinaryString((packet[6]&0xD0)>>5)).replace(' ', '0');
        fragmentOffset = (packet[6]&0x1F)*256 + (packet[7]&0xFF);
        timeToLive = (packet[8]&0xFF);
        protocol = packet[9];
        headerChecksum = (packet[10]&0xFF)*256 + (packet[11]&0xFF);
    }

    public byte[] getPayload() {
        byte[] payload = new byte[length-20];
        for (int b=0; b<length-20; b++) payload[b] = packet[b+20];
        return payload;
    }

    public void print() {
        System.out.println("--- Network Layer ---");
        System.out.println("   Version: "+version);
        System.out.println("   Header length: "+ headerLength*4 + " bytes");
        System.out.println("   Differentiated services: "+String.format("0x%02X", differentiatedServices));
        System.out.println("   ECN: "+String.format("0x%02X", ecn));
        System.out.println("   Packet length: "+ packetLength);
        System.out.println("   Identification: "+String.format("0x%04X", identification));
        			// Print flags
			System.out.printf("      %1s.. %s\n", 
                flags.substring(0, 1),
                flags.substring(0,1).equals("0")
                    ?"Reserved: Not set"
                    :"Reserved: Set");
            System.out.printf("      .%1s. %s\n", 
                flags.substring(1, 2),
                flags.substring(1,2).equals("0")
                    ?"Don't fragment: Not set"
                    :"Don't fragment: Set");
            System.out.printf("      ..%1s %s\n", 
                flags.substring(2, 3),
                flags.substring(2,3).equals("0")
                    ?"More fragments: Not set"
                    :"More fragments: Set");
                    
        System.out.println("   Fragment offset: "+ fragmentOffset);
        System.out.println("   Time to live: "+ timeToLive);
        System.out.println("   Protocol = "+String.format("0x%02X", protocol));
        System.out.println("   Header checksum: "+String.format("0x%04X", headerChecksum));
        System.out.printf("   Source IP address: %d.%d.%d.%d\n", 
                               (packet[12]&0xFF), (packet[13]&0xFF), (packet[14]&0xFF), (packet[15]&0xFF) );
        System.out.printf("   Destination IP address: %d.%d.%d.%d\n", 
                               (packet[16]&0xFF), (packet[17]&0xFF), (packet[18]&0xFF), (packet[19]&0xFF) );
        if (headerLength > 5) {
            System.out.printf("   Options type: %d\n", (packet[20]&0xFF));
            System.out.printf("   Options length: %d\n", (packet[21]&0xFF));
            System.out.printf("   Options data: %d\n", (packet[22]&0xFF));
        }

    }
}
