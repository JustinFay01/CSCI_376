package edu.hope.cs.csci376.pcap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class HTTP {

    int length = 0;
    byte[] packet;

    public HTTP(byte[] packet) {

        this.packet = packet;
        length = packet.length;

    }

    // Looking for 47 45 54
    // if string is empty and the first is new line print paylaod
    public void print() {
        System.out.println("--- HTTP ---");
        boolean gzip = false;
        String line = "";
        int encodeIndex = 0;
        for (int i = 0; i < packet.length-1; i++) {
            if ((char) packet[i] == '\r') {//Catch the end of a line
                System.out.print(line + "\\r\\n"); //Print out the line of string
                if(gzip && line.length() == 1){
                    System.out.println("Need to encode");
                    encodeIndex = i;
                    break;
                }
                line = ""; //reset the line                
            } else {
                line += String.valueOf((char) packet[i]); //Continue adding Characters to line until new line
                if(line.contains("Content-Encoding: gzip"))//Check for encoding
                    gzip = true;
                
            }  
        }
        System.out.println("Loop broken");
        byte[] toDecode = new byte[encodeIndex+1];

        for(int i = encodeIndex, j = 0; i < packet.length; i++, j++){
            toDecode[j] = packet[i];
            System.out.print((char) toDecode[j]);
        }

        if(gzip){
            GZIPInputStream gis;
            byte[] decompressed;
        try {
            gis = new GZIPInputStream(new ByteArrayInputStream(toDecode));
            decompressed = gis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }
}
