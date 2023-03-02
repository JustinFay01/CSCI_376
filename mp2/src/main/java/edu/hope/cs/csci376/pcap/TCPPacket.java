package edu.hope.cs.csci376.pcap;

public class TCPPacket extends TransportLayer {

    public byte[] packet;

    public TCPPacket(byte[] packet) {
        super(packet);
        this.packet = packet;
    }

    // Final print method of all Header information
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

    /*
     * Each hex number is 8 bites long, since the sequence and acknowledegment
     * numbers are both 32 bits long, we multiply by 256 for the location of that
     * specific
     * hex number.For example, since packet[4] includes the first bytes that have
     * sequence
     * number informationWe need to make room for the rest of the hex numbers. Since
     * we know we will
     * need three more hex numbers we must mutiply by 256 three times. Essentially
     * extending the 8
     * bits we are looking at currently 24 bits to the left. Thus packet[5] is the
     * next number meaning we only have an addition 16
     * bits afterwards, thus, we only shift 16 over. The anding with 0xFF (255)
     * extracts the first 8 bits we are looking for.
     * Sequence number 4 bytes is a java integer and will print negative sometimes
     */
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
        if (i == 0)      return "      %1s... .... %s\n";
        else if (i == 1) return "      .%1s.. .... %s\n";
        else if (i == 2) return "      ..%1s. .... %s\n";
        else if (i == 3) return "      ...%1s .... %s\n";
        else if (i == 4) return "      .... %1s... %s\n";
        else if (i == 5) return "      .... .%1s.. %s\n";
        else if (i == 6) return "      .... ..%1s. %s\n";
        else if (i == 7) return "      .... ...%1s %s\n";
        return "Error, to many flags!";
    }

    /*
     * Locating flags within TCP packets
     * Since there are 8 flags and each are only one bit, we need only one hex
     * number. However, it is still represented as a hex number. Therefore, we must
     * turn the hex number into binary and then asses each bit individually. The
     * flag options array serves as an sorted array of flags found in a tcp packet
     * according to bit location (ascending order).
     */
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

    // Retreive 16 bit window size
    public long getWindowSize() {
        return (packet[14] & 0xFF) * 256 + (packet[15] & 0xFF);
    }

    // Retreive 16 bit check sum in hex format
    public String getCheckSum() {
        return "0x" + Integer.toHexString((packet[16] & 0xFF) * 256 + (packet[17] & 0xFF));
    }

    // Retreive 16 bit urgent pointer
    // Only on when TCP packet urgent pointer flag is also indicated as on
    public long getUrgentPointer() {
        return (packet[18] & 0xFF) * 256 + (packet[19] & 0xFF);
    }

}
