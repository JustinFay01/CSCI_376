package edu.hope.cs.csci376.pcap;

public class TCPPacket extends TransportLayer {

    public int destinationPort;
    public int sourcePort;
    public byte[] packet;

    public TCPPacket(byte[] packet) {
        super(packet);
        this.packet = packet;
    }

    public void print() {
        super.print();
        System.out.println("   Sequence number: " + getSequenceNumber() +
                           "\n   Acknowledgement number: " + getAcknowledgemntNubmber());
    }

    //Sequence number 4 bytes is a java integer and will print negative sometimes
    public int getSequenceNumber() {
        int seq = (packet[4] & 0xFF) * 256 * 256 * 256 + (packet[5] & 0xFF) * 256 * 256 + (packet[6]) * 256
                + (packet[7] & 0xFF);
        return seq = (int) (seq < 0 ? Math.pow(2, 32) : seq);
    }

    public int getAcknowledgemntNubmber() {
        int ack = (packet[8] & 0xFF) * 256 * 256 * 256 + (packet[9] & 0xFF) * 256 * 256 + (packet[10]) * 256
                + (packet[11] & 0xFF);
        return ack = (int) (ack < 0 ? Math.pow(2, 32) : ack);
    }

    //Header Length: 4 bits, leftmost of byte 0x2E (leftmost of octet 12 in the TCP packet)
    
}
