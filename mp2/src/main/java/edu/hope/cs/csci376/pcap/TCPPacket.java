package edu.hope.cs.csci376.pcap;

public class TCPPacket extends TransportLayer {

    public byte[] packet;

    public TCPPacket(byte[] packet) {
        super(packet);
        this.packet = packet;
    }

    public void print() {
        System.out.println("--- Transport Layer: TCP Packet ---");
        super.print(); // Gives Source & Destination port
        System.out.println("   Sequence number: " + getSequenceNumber() +
                "\n   Acknowledgement number: " + getAcknowledgemntNubmber() +
                "\n   Header length: " + getHeaderLength() + " bytes" +
                "\n   Flags: " + String.format("0x%02X",(short) packet[13]));
        printFlags();
        System.out.println("   Window size: " + getWindowSize() +
                "\n   Checksum: " + getCheckSum() +
                "\n   Urgent pointer: " + getUrgentPointer());

    }

    // Sequence number 4 bytes is a java integer and will print negative sometimes
    public long getSequenceNumber() {
        long seq = (packet[4] & 0xFF) * 256 * 256 * 256 + (packet[5] & 0xFF) * 256 * 256 + (packet[6] & 0xFF) * 256
                + (packet[7] & 0xFF);
        return seq = (seq < 0 ? seq += Math.pow(2, 32) : seq);
    }

    // Acknowledgement number 4 bytes is a java integer and will print negative
    // sometimes
    public long getAcknowledgemntNubmber() {
        long ack = (packet[8] & 0xFF) * 256 * 256 * 256 + (packet[9] & 0xFF) * 256 * 256 + (packet[10]) * 256
                + (packet[11] & 0xFF);
        return ack = ack < 0 ? ack += Math.pow(2, 32) : ack;
    }

    // Header Length: 4 bits, leftmost of byte 0x2E (leftmost of octet 12 in the TCP
    // packet)
    // Must Multiple by 4 for the data offset
    public int getHeaderLength() {
        return ((packet[12] & 0xFF) >> 4) * 4;
    }

    public String getFormat(int i) {
        if (i == 0)return "      %1s... .... %s\n";
        else if (i == 1)return "      .%1s.. .... %s\n";
        else if (i == 2)return "      ..%1s. .... %s\n";
        else if (i == 3)return "      ...%1s .... %s\n";
        else if (i == 4)return "      .... %1s... %s\n";
        else if (i == 5)return "      .... .%1s.. %s\n";
        else if (i == 6)return "      .... ..%1s. %s\n";
        else if (i == 7)return "      .... ...%1s %s\n";
        return "Error, to many flags!";
    }

    // Take flags from specific byte
    public void printFlags() {
        String flags = String.format("%8s", Integer.toBinaryString(packet[13])).replace(' ', '0');
        String[] flagOptions = { "Congestion Window Reduced", "ECN-Echo", "Urgent", "Acknowledgement", "Push", "Reset",
                "SYN", "FIN" };

        for (int i = 0; i < flags.length(); i++) {
            System.out.printf(getFormat(i), flags.substring(i, i + 1), flags.substring(i, i + 1).equals("0")
                    ? flagOptions[i] + ": Not set"
                    : flagOptions[i] + ": Set");
        }
    }

    public long getWindowSize() {
        return (packet[14] & 0xFF) * 256 + (packet[15] & 0xFF);
    }

    public String getCheckSum() {
        return "0x" + Integer.toHexString((packet[16] & 0xFF) * 256 + (packet[17] & 0xFF));
    }

    public long getUrgentPointer() {
        return (packet[18] & 0xFF) * 256 + (packet[19] & 0xFF);
    }

}
