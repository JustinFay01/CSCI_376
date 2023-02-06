package edu.hope.cs.csci376.pcap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class HTTP {

    int length = 0;
    byte[] packet;

    public HTTP(byte[] packet) {

        this.packet = packet;
        length = packet.length;

    }

    public void print() {
        System.out.println("--- HTTP ---");
        boolean gzip = false;
        String line = "";
        int encodeIndex = 0;

        for (int i = 0; i < packet.length; i++) {
            if ((char) packet[i] == '\r') {// Catch the end of a line
                System.out.print(line + "\\r\\n"); // Print out the line of string
                if (gzip && line.length() == 1) {//We have reached the compressed payload
                    encodeIndex = i;
                    break;
                }
                line = ""; // reset the line
            } else {
                line += String.valueOf((char) packet[i]); // Continue adding Characters to line until new line
                if (line.contains("Content-Encoding: gzip"))// Check for encoding
                    gzip = true;

            }
        }

        if (gzip) {
            byte[] toDecode = new byte[packet.length-encodeIndex];
            System.out.println();
            for (int i = encodeIndex, j = 0; i < packet.length; i++, j++) {
                toDecode[j] = packet[i];
                System.out.print(toDecode[j] + " ");
            }
            System.out.println();
            // InputStream by = new ByteArrayInputStream(toDecode);
            
            // try{
            //     while(by.available() > 0){
            //         System.out.print(by.read() + " ");
            //     }
            //     System.out.println();
            //     gis = new GZIPInputStream(by);
            // } catch (Exception e){
            //     e.printStackTrace();
            // }
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
