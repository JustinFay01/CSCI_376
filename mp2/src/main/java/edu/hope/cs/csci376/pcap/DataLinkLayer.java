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

        type = (short)(packet[12]*256 + packet[13]);

    }

    public byte[] getPayload() {
        byte[] payload = new byte[length-14];
        for (int b=0; b<length-14; b++) payload[b] = packet[b+14];
        return payload;
    }

    public void print() {
        System.out.println("--- Ethernet ---");
        System.out.println("   Type = "+String.format("0x%04X", type));
    }
}
