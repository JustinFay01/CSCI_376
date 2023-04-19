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
     * Find the destination address of a given packet
     * The destination bits are located in the first 5 indices of the payload
     * It is & with 0xFF to gain the first hex values (Changes ffffff --> ff)
     */
    public String getDestination() {
        String destAddress = "";
        for (int i = 0; i <= 5; i++) {
            String tmp = Integer.toHexString(packet[i] & 0xFF); //Convert byte to hex string
            if(tmp.length() == 1) tmp = "0" + tmp;              //if the hex value is only one digit pad with 0
            destAddress += tmp;
            if (i != 5)     //if last value of loop dont add :
                destAddress += ":";
        }
        return destAddress;
    }

    /*
     * Find the source address of a given packet
     * The source bits are located in the 6th through 11 indice
     * It is & with 0xFF to gain the first hex values (Changes ffffff --> ff)
     */
    public String getSource() {
        String srcAddress = "";
        for (int i = 6; i <= 11; i++) {
            String tmp = Integer.toHexString(packet[i] & 0xFF); //Convert byte to hex string
            if(tmp.length() == 1) tmp = "0" + tmp;              //if the hex value is only one digit pad with 0
            srcAddress += tmp;
            if (i != 11)    //if last value of loop dont add :
                srcAddress += ":";
        }
        return srcAddress;
    }

    /*
     * Find the type of the given ARP packet 
     * The type is found in the 12th and 13th indice
     * The first value is multiplied by 256 to make room for the next bits 
     * Moreover, since a hex number is only 4 bits and we have two, we need an 8 bit number.
     * Thus, multiplying by 256 we expand the 4 bit number to an 8 bit one
     * It is & with 0xFF to gain the first hex values (Changes ffffff --> ff)
     */
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
