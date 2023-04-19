//--------------------------------------------------------------------
//
//  HTTP Class for CSCI 376, Machine Problem 2
//
//  Goal is to print out the HTTP payload, including any compressed pages
//
//  Mike Jipping February 2023

package edu.hope.cs.csci376.pcap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class HTTP {

    // These are set up to recognize like endings
    final byte CARRIAGE_RETURN = 0x0d;
    final byte LINE_FEED = 0x0a;

    int length = 0;
    byte[] packet;
    
    // Constructor: receive the HTTP payload from the Transport
    // layer.
    public HTTP(byte[] packet) {

        this.packet = packet;
        length = packet.length;       

    }

    // And we have the print method.  
    // When we process the header, we record a couple of things:
    // We set up the "bodybytes" array with the value from the Content-Length
    // and we note if we have compressed data from the server with the
    // Content-Encoding header.
    public void print() {
        boolean body = false;
        boolean gzip = false;
        String line = "";
        byte[] bodybytes = null;
        int contentLength = 0;
        int byteCount = 0;

        System.out.println("--- HTTP ---");

        // We will process character by character, collecting into a 
        // "line" string.  We will analyze the line when we hit the
        // LINE_FEED character.
        for (int c=0; c<length; c++){
            if (packet[c] == CARRIAGE_RETURN) {
                line += "\\r";
                if (line.length() == 2) body = true;  // blank line switches to body

            } else if (packet[c] == LINE_FEED) {
                line += "\\n";
                if (!gzip && line.contains("Content-Encoding: gzip")) {
                    gzip = true;
                }
                if (line.contains("Content-Length:")) {
                    String[] pieces = line.split(" ");
                    pieces[1] = pieces[1].substring(0, pieces[1].length()-4); 
                    contentLength = Integer.parseInt(pieces[1]);
                    bodybytes = new byte[contentLength];
                }
                System.out.println(line);
                line = "";

            } else {
                if (body) {
                    bodybytes[byteCount++] = packet[c];  // Collect body bytes
                } else {
                    line += (char)packet[c];   // Header line
                }
            }
        }

        // If we have compressed data, we can easily decompress it with 
        // GZIPInputStream.
        if (gzip) {
            GZIPInputStream gis;
            byte[] b;
            try {
                gis = new GZIPInputStream(new ByteArrayInputStream(bodybytes));
                b = gis.readAllBytes();
                bodybytes = b;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // If we have a body to the data, we will print it out, including special
        // notations for carriage return and line feed.
        if (bodybytes != null) {
            for (int c=0; c<bodybytes.length; c++){
                if (bodybytes[c] == LINE_FEED) {
                    System.out.println("\\n");
                } else if (bodybytes[c] == CARRIAGE_RETURN) {
                    System.out.print("\\r");
                } else {
                    System.out.print((char)bodybytes[c]);
                }
            }
        }
    }

}
