package edu.hope.cs.csci376.pcap;

public class ARP {

    int hardwareType = 0;
    int hardwareSize = 0;
    String protocolType = ""; // Kept in hex
    int protocolSize = 0;
    int MACAddrLength = 0;
    int IPAddrLength = 0;
    int operation = 0;

    String senderMACAddress = "";
    String senderIPAddress = "";
    String targetMACAddress = "";
    String targetIPAddress = "";

    byte[] packet;

    public ARP(byte[] packet) {
        this.packet = packet;
        
        hardwareType = (packet[0] & 0xFF) * 256 + (packet[1] & 0xFF);
        setProtocolType(packet);
        hardwareSize = (packet[4] & 0xFF);
        protocolSize = (packet[5] & 0xFF);
        operation = (packet[6] & 0xFF) * 256 + (packet[7] & 0xFF);

        //All start and end indices are determined by the ARP packet format
        //in the .pcap files, which can be viewed in wire shark.
        senderMACAddress = macConvert(8, 13);
        senderIPAddress = ipConvert(14, 17);
        targetMACAddress = macConvert(18, 23);
        targetIPAddress = ipConvert(24, 27);

    }

    public void print() {
        System.out.println("--- ARP ---");
        System.out.println("   Hardware type: " + hardwareType + "\n"
                + "   Protocol type: " + protocolType + "\n"
                + "   Hardware Size: " + hardwareSize + "\n"
                + "   Protocol Size: " + protocolSize + "\n"
                + "   Operation: " + operation + " (request)\n"
                + "   Sender MAC address: " + senderMACAddress + "\n"
                + "   Sender IP  address: " + senderIPAddress + "\n"
                + "   Target MAC address: " + targetMACAddress + "\n"
                + "   Target IP address: " + targetIPAddress + "\n");
    }

    /*
     * ProtocolType is meant to be kept as a hex number, and for better
     * printing format, zeros are padded until there are four digits.
     */
    public void setProtocolType(byte[] packet) {
        String tmp = Integer.toHexString((packet[2] & 0xFF) * 256 + (packet[3] & 0xFF));
        if (tmp.length() < 4) {
            String padZero = "";
            for (int i = 0; i < 4 - tmp.length(); i++) {
                padZero += "0";
            }
            tmp = padZero + tmp;
        }
        protocolType = String.format("0x%s", tmp); // Needs to be changed to hex string
    }

    /*
     * Serves as a helper function to take n length section of the byte array
     * and to convert it into the correct format hex value and in turn the correct
     * format mac address.
     */
    public String macConvert(int start, int end) {
        String val = "";
        for (int i = start; i <= end; i++) {
            String tmp = Integer.toHexString(packet[i] & 0xFF); // Convert byte to hex string
            if (tmp.length() == 1)
                tmp = "0" + tmp; // if the hex value is only one digit pad with 0
            val += tmp;
            if (i != end) // if last value of loop dont add :
                val += ":";
        }
        return val;
    }

    /*
     * Serves as a helper function to take n length section of the byte array
     * and to convert it into an unsigned integer and in turn the correct
     * format ip address.
     */
    public String ipConvert(int start, int end) {
        String val = "";
        for (int i = start; i <= end; i++) {
            val += Byte.toUnsignedInt(packet[i]); 
            if (i != end)
                val += ".";
        }
        return val;
    }

    /*
     * Getters for each field added for tests
     */
    public int getHardwareType() {
        return hardwareType;
    }

    public int getHardwareSize() {
        return hardwareSize;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public int getProtocolSize() {
        return protocolSize;
    }

    public int getMACAddrLength() {
        return MACAddrLength;
    }

    public int getIPAddrLength() {
        return IPAddrLength;
    }

    public int getOperation() {
        return operation;
    }

    public String getSenderMACAddress() {
        return senderMACAddress;
    }

    public String getSenderIPAddress() {
        return senderIPAddress;
    }

    public String getTargetMACAddress() {
        return targetMACAddress;
    }

    public String getTargetIPAddress() {
        return targetIPAddress;
    }

}
