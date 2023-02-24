package edu.hope.cs.csci376.pcap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class HTTP {

    int length = 0;
    byte[] packet;

    final byte CARRIAGE_RETURN = 0x0d;
    final byte LINE_FEED = 0x0a;

    public HTTP(byte[] packet) {

        this.packet = packet;
        length = packet.length;

    }

    // Time O(n^2 + m) n length of packet (including payload)
    // and m length of payload
    // Space O(m)
    public void print() {
        System.out.println("--- HTTP ---");
        boolean gzip = false;
        String line = "";
        int encodeIndex = 0;

        for (int i = 0; i < packet.length; i++) {
            if ((char) packet[i] == CARRIAGE_RETURN) {// Catch the end of a line
                System.out.print(line + "\\r\\n"); // Print out the line of string
                if (line.length() == 1) {// We have reached the compressed payload
                    encodeIndex = i + 2;// skip passed \r and \n
                    break;
                }
                line = ""; // reset the line
            } else {
                line += (char) packet[i]; // Continue adding Characters to line until new line
                String encode = "Content-Encoding: gzip";
                if (!gzip && line.length() >= encode.length()) {//prevent string matching every loop
                    if (line.contains(encode))// Check for encoding O(n) time
                        gzip = true;
                }
            }
        }
        byte[] toDecode = new byte[packet.length - encodeIndex];
        for (int j = 0; encodeIndex < packet.length; encodeIndex++, j++)
            toDecode[j] = packet[encodeIndex]; // copy elements of payload into toDecode

        if (gzip) {// Decompression Required
            try {
                byte[] decompressed = decompress(toDecode);
                printPayload(decompressed);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else printPayload(toDecode);
    }
    // Decompression
    // Time Unknown
    // Space Unknown
    public byte[] decompress(byte[] toDecode) throws IOException {
        GZIPInputStream gis;
        byte[] decompressed;

        gis = new GZIPInputStream(new ByteArrayInputStream(toDecode));
        decompressed = gis.readAllBytes();
        return decompressed;
    }

    //Time O(n)
    //Space O(1)
    public void printPayload(byte[] decompressed) {
        for (int i = 0; i < decompressed.length; i++) {
            if ((char) decompressed[i] == LINE_FEED) // If we have a new line character
                System.out.print("\\n\n"); // print the actual newline character then the new line
            else
                System.out.print((char) decompressed[i]);// else just print the character
        }

    }
}
