package edu.hope.cs.csci376.pcap;

public class TCPPacket extends TransportLayer {

    int checksum = 0;
    long sequenceNumber = 0;
    long ackNumber = 0;
    int headerLength = 0;
    String flags = "";
    int windowSize = 0;
    int urgentPointer = 0;

    public TCPPacket(byte[] packet) {
        super(packet);

        sequenceNumber = (packet[4]&0xFF)*256*256*256 + (packet[5]&0xFF)*256*256 +
                         (packet[6]&0xFF)*256 + (packet[7]&0xFF);
        if (sequenceNumber < 0) sequenceNumber += Math.pow(2, 32);
        ackNumber = (packet[8]&0xFF)*256*256*256 + (packet[9]&0xFF)*256*256 +
                    (packet[10]&0xFF)*256 + (packet[11]&0xFF);
        if (ackNumber < 0) ackNumber += Math.pow(2, 32);
        headerLength = ((packet[12]&0xFF) >> 4);
        flags = String.format("%8s", Integer.toBinaryString(packet[13])).replace(' ', '0');
        windowSize = (packet[14]&0xFF)*256 + (packet[15]&0xFF);
        urgentPointer = (packet[18]&0xFF)*256 + (packet[19]&0xFF);
    }

    public void print() {
        System.out.println("--- Transport Layer: TCP Packet ---");
        System.out.println("   Source port: "+ sourcePort) ;
        System.out.println("   Destination port: "+ destinationPort);
        System.out.println("   Sequence number: "+ sequenceNumber);
        System.out.println("   Acknowledgement number: "+ ackNumber);
        System.out.println("   Header length: "+ headerLength*4 + " bytes");
        System.out.println("   Flags: "+String.format("0x%2x", packet[13]));

			// Print flags
			System.out.printf("      %1s... .... %s\n", 
                                flags.substring(0, 1),
					            flags.substring(0,1).equals("0")
                                    ?"Congestion Window Reduced: Not set"
							        :"Congestion Window Reduced: Set");
			System.out.printf("      .%1s.. .... %s\n", 
                                flags.substring(1, 2),
					            flags.substring(1,2).equals("0")
                                    ?"ECN-Echo: Not set"
							        :"ECN-Echo: Set");
            System.out.printf("      ..%1s. .... %s\n", 
                                    flags.substring(2, 3),
                                    flags.substring(2,3).equals("0")
                                        ?"Urgent: Not set"
                                        :"Urgent: Set");
            System.out.printf("      ...%1s .... %s\n", 
                                    flags.substring(3, 4),
                                    flags.substring(3,4).equals("0")
                                        ?"Acknowledgement: Not set"
                                        :"Acknowledgement: Set");
            System.out.printf("      .... %1s... %s\n", 
                                    flags.substring(4, 5),
                                    flags.substring(4,5).equals("0")
                                        ?"Push: Not set"
                                        :"Push: Set");
            System.out.printf("      .... .%1s.. %s\n", 
                                    flags.substring(5, 6),
                                    flags.substring(5,6).equals("0")
                                        ?"Reset: Not set"
                                        :"Reset: Set");
            System.out.printf("      .... ..%1s. %s\n", 
                                    flags.substring(6, 7),
                                    flags.substring(6,7).equals("0")
                                        ?"SYN: Not set"
                                        :"SYN: Set");
            System.out.printf("      .... ...%1s %s\n", 
                                    flags.substring(7, 8),
                                    flags.substring(7,8).equals("0")
                                        ?"FIN: Not set"
                                        :"FIN: Set");
        System.out.println("   Window size: "+ windowSize);
        System.out.println("   Checksum: "+String.format("0x%2x%2x", packet[16],packet[17]));
        System.out.println("   Urgent pointer: "+ urgentPointer);
    }
}
