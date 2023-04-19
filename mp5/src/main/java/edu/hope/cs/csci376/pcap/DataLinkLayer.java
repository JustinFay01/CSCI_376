package edu.hope.cs.csci376.pcap;

public class DataLinkLayer {

    public static final short TYPE_ARP = 0x0806;
    public static final short TYPE_RARP = (short) 0x8035;
    public static final short TYPE_IPv4 = 0x0800;
    public static final short TYPE_IPv6 = (short) 0x86DD;
    public static final short TYPE_LLDP = (short) 0x88cc;
    public static final short TYPE_BSN = (short) 0x8942;

    short type = (short) 0x0;
    int length = 0;
    byte[] packet;

    public DataLinkLayer(byte[] packet) {

        this.packet = packet;
        length = packet.length;

    }

    public byte[] getPayload() {
        byte[] payload = new byte[length - 14];
        for (int b = 0; b < length - 14; b++)
            payload[b] = packet[b + 14];
        return payload;
    }

    public void print() {
        System.out.println("--- Ethernet ---");
        System.out.println("Destination: " + getDestination() + "\n"
                + "Source: " + getSource() + "\n"
                + "Type: " + getType());
    }

    /*
     * Find the destination address of a given ARP packet
     * The destination bits are located in the first 5 indices of the payload
     * It is & with 0xFF to gain the first hex values (Changes ffffff --> ff)
     */
    public String getDestination() {
        String destAddress = "";
        for (int i = 0; i <= 5; i++) {
            destAddress += Integer.toHexString(packet[i] & 0xFF);
            if (i != 5)
                destAddress += ":";
        }
        return destAddress;
    }

    /*
     * Find the source address of a given ARP packet
     * The source bits are located in the 6th through 11 indice
     * It is & with 0xFF to gain the first hex values (Changes ffffff --> ff)
     */
    public String getSource() {
        String srcAddress = "";
        for (int i = 6; i <= 11; i++) {
            srcAddress += Integer.toHexString(packet[i] & 0xFF);
            if (i != 11)
                srcAddress += ":";
        }
        return srcAddress;
    }

    public String getType() {
        int type = (packet[12] & 0xFF) * 256 + (packet[13] & 0xFF);
        switch (type) {
            case TYPE_ARP:
                return "0x0806";
            case TYPE_RARP:
                return "0x8035";
            case TYPE_IPv4:
                return "0x0800";
            case TYPE_IPv6:
                return "0x86DD";
            case TYPE_LLDP:
                return "0x88cc";
            case TYPE_BSN:
                return "0x8942";
            default:
                return "0x" + Integer.toHexString(type);
        }
    }
}
