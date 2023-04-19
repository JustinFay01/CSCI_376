package edu.hope.cs.csci376.pcap;

public class ARP {

    int hardwareType = 0;
    int hardwareSize = 0;
    String protocolType = ""; //Kept in hex
    int protocolSize = 0;
    int MACAddrLength = 0;
    int IPAddrLength = 0;
    int operation = 0;

    byte[] senderMACAddress;
    byte[] senderIPAddress;
    byte[] targetMACAddress;
    byte[] targetIPAddress;

    public ARP(byte[] packet) {
        hardwareType = (packet[0] & 0xFF) * 256 + (packet[1] & 0xFF);
        setProtocolType(packet);
        hardwareSize = (packet[4] & 0xFF);
        protocolSize = (packet[5] & 0xFF);
        operation = (packet[6] & 0xFF) * 256 + (packet[7] & 0xFF);;

    }
    /*
     * ProtocolType is meant to be kept as a hex number, and for better
     * printing format, zeros are padded until there are four digits. 
     */
    public void setProtocolType(byte[] packet){
        String tmp = Integer.toHexString( (packet[2] & 0xFF) * 256 + (packet[3] & 0xFF));
        if(tmp.length() < 4){
            String padZero = "";
            for(int i = 0 ; i < 4-tmp.length(); i++){
                padZero += "0";
            }
            tmp = padZero + tmp;
        }
        protocolType = String.format("0x%s", tmp); //Needs to be changed to hex string
    }

    public void print() {
        System.out.println("--- ARP ---");
        System.out.println("   Hardware type: " + hardwareType + "\n" 
                            +"   Protocol type: " + protocolType + "\n" 
                            +"   Hardware Size: " + hardwareSize + "\n" 
                            +"   Protocol Size: " + protocolSize + "\n" 
                            +"   Operation: " + operation + " (request)\n" );
    }

}
