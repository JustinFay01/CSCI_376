package edu.hope.cs.csci376.pcap;

public class HTTP {

    int length = 0;
    byte[] packet;
    
    public HTTP(byte[] packet) {

        this.packet = packet;
        length = packet.length;       

    }

    public void print() {
        System.out.println("--- HTTP ---");
    }

}
