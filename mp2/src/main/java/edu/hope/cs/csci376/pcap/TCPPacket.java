package edu.hope.cs.csci376.pcap;

public class TCPPacket extends TransportLayer {

    public byte[] packet;

    public TCPPacket(byte[] packet) {
        super(packet);
        this.packet = packet;
    }

    public void print() {
        super.print(); // Gives Source & Destination port
        System.out.println("   Sequence number: " + getSequenceNumber() +
                "\n   Acknowledgement number: " + getAcknowledgemntNubmber() +
                "\n   Header Length: " + getHeaderLength() + " bytes" +
                "\n   Flags: ???");
        printFlags();
        System.out.println("   Window Size: " + getWindowSize() +
                "\n   Checksum: " + getCheckSum() + 
                "\n   Urgent Pointer: " + getUrgentPointer());

    }

    // Sequence number 4 bytes is a java integer and will print negative sometimes
    public int getSequenceNumber() {
        int seq = (packet[4] & 0xFF) * 256 * 256 * 256 + (packet[5] & 0xFF) * 256 * 256 + (packet[6]) * 256
                + (packet[7] & 0xFF);
        return seq = (seq < 0 ? seq + (int) Math.pow(2, 32) : seq);
    }

    // Acknowledgement number 4 bytes is a java integer and will print negative
    // sometimes
    public int getAcknowledgemntNubmber() {
        int ack = (packet[8] & 0xFF) * 256 * 256 * 256 + (packet[9] & 0xFF) * 256 * 256 + (packet[10]) * 256
                + (packet[11] & 0xFF);
        return ack = (ack < 0 ? ack + (int) Math.pow(2, 32) : ack);
    }

    // Header Length: 4 bits, leftmost of byte 0x2E (leftmost of octet 12 in the TCP
    // packet)
    // Must divide by 4 for the data offset
    public int getHeaderLength() {
        return ((packet[12] & 0xFF) / 4);
    }

    //Take flags from specific byte
    public void printFlags() {
        String flags = String.format("%8s", Integer.toBinaryString(packet[13])).replace(' ', '0');
        System.out.printf("    %1s... .... %s\n", // Window Reduced
                flags.substring(0, 1), flags.substring(0, 1).equals("0")
                        ? "Congestion Window Reduced: Not set"
                        : "congestion Window Reduced: Set");
        System.out.printf("    .%1s.. .... %s\n", // ECN-Echo
                flags.substring(1, 2), flags.substring(1, 2).equals("0")
                        ? "ECN-Echo: Not set"
                        : "ECN-Echo: set");
        System.out.printf("    ..%1s. .... %s\n", // Urgent
                flags.substring(2, 3), flags.substring(2, 3).equals("0")
                        ? "Urgent: Not set"
                        : "Urgent: set");
        System.out.printf("    ...%1s .... %s\n", // Ackowledgment
                flags.substring(3, 4), flags.substring(3, 4).equals("0")
                        ? "Acknowledgement: Not set"
                        : "Acknowledgement: set");
        System.out.printf("    .... %1s... %s\n", // Push
                flags.substring(4, 5), flags.substring(4, 5).equals("0")
                        ? "Push: Not Set"
                        : "Push: set");
        System.out.printf("    .... .%1s.. %s\n", // Reset
                flags.substring(5, 6), flags.substring(5, 6).equals("0")
                        ? "Reset: Not set"
                        : "Reset: set");
        System.out.printf("    .... ..%1s. %s\n", // SYN
                flags.substring(6, 7), flags.substring(6, 7).equals("0")
                        ? "SYN: Not Set"
                        : "SYN: Set");
        System.out.printf("    .... ...%1s %s\n", // FIN
                flags.substring(7, 8), flags.substring(7, 8).equals("0")
                        ? "FIN: Not set"
                        : "FIN: set");
    }

    public int getWindowSize(){
        return (packet[14]&0xFF) * 256 + (packet[15]&0xff);
    }

    public String getCheckSum(){
        return "0x" + Integer.toHexString((packet[16]&0xFF) * 256 + (packet[17]&0xff));
    }

    public int getUrgentPointer(){
        return (packet[18]&0xFF) * 256 + (packet[19]&0xff);
    }


}
