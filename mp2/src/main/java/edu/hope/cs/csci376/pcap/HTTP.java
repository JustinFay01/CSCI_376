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

    // Time O(n^2 + m) n length of packet (including payload) and m length of
    // payload\
    // Space O(m)
    public void print() {
        System.out.println("--- HTTP ---");
        boolean gzip = false;
        String line = "";
        int encodeIndex = 0;

        for (int i = 0; i < packet.length; i++) {
            if ((char) packet[i] == '\r') {// Catch the end of a line
                System.out.print(line + "\\r\\n"); // Print out the line of string
                if (gzip && line.length() == 1) {// We have reached the compressed payload
                    encodeIndex = i + 2;// skip passed \r and \n
                    break;
                }
                line = ""; // reset the line
            } else {
                line += String.valueOf((char) packet[i]); // Continue adding Characters to line until new line
                String encode = "Content-Encoding: gzip";
                if (line.length() >= encode.length()) {
                    if (line.contains(encode))// Check for encoding O(n) time
                        gzip = true;
                }

            }
        }

        if (gzip) {
            byte[] toDecode = new byte[packet.length - encodeIndex];
            System.out.println();
            for (int j = 0; encodeIndex < packet.length; encodeIndex++, j++)
                toDecode[j] = packet[encodeIndex]; // copy elements of payload into decode

            // Decompression
            GZIPInputStream gis;
            byte[] decompressed;
            try {
                gis = new GZIPInputStream(new ByteArrayInputStream(toDecode));
                decompressed = gis.readAllBytes();

                for (int i = 0; i < decompressed.length; i++) {
                    if ((char) decompressed[i] == '\n') // If we have a new line character
                        System.out.print("\\n\n"); // print the actual newline character then the new line
                    else
                        System.out.print((char) decompressed[i]);// else just print the character
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
