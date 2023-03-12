//-------------------------------------------------------------------
//
// DNSReponse receives a response from a DNS query and prints the 
// response.
//
// NOTE: there's a bug in the additional response section and it 
// prints crap after the first entry.
//
// NOTE: We need the socket used to send the DNS query so we receive
// the response correctly.

package edu.hope.cs.csci376;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DNSResponse {

    DatagramSocket dgSocket = null;
    byte[] response = null;

    // Constructor records the socket used.
    public DNSResponse(DatagramSocket dgSocket) {
        this.dgSocket = dgSocket;
    }

    // Receive the packet on the socket given in the constructor.
    public void receive() {
        DatagramPacket rec;

        try {
            // Now we wait for the response.
            rec = new DatagramPacket(new byte[65530], 65530);
            rec.setLength(65530);
            dgSocket.receive(rec);
            response = rec.getData();
        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        }

    }

    ////////////////////////////////////////    MY   ///////////////////////////////////////////////////
    ////////////////////////////////////////  CODE  ///////////////////////////////////////////////////
    // Print the DNSname from the byte array in that wacky format
    public static int processDNSName(byte bytes[], int start) {

        int pos = start;
        while (bytes[pos] != 0) {
            // if (pos != start)
            // System.out.print(".");
            int length = bytes[pos];
            int pos2 = (length >> 6) & 0x03; // Takes the length we had, looks at the
            // first 2 bits, masks the rest.
            if (pos2 != 0) {
                pos2 = ((length & 0x3f) << 8) + (bytes[pos + 1] & 0xff);
                processDNSName(bytes, pos2);
                pos++;
                break;
            } else {
                // for (int i = 1; i <= length; i++) {
                // System.out.print((char) bytes[pos + i]);
                // }
                pos += length + 1;
            }
        }
        pos++;
        return pos;
    }

    // Returns the DNSName
    public static String getDNSName(byte bytes[], int start, String dnsName) {

        int pos = start;
        while (bytes[pos] != 0) {
            if (pos != start)
                dnsName += ".";
            // System.out.print(".");
            int length = bytes[pos];
            int pos2 = (length >> 6) & 0x03; // Takes the length we had, looks at the
            // first 2 bits, masks the rest.
            if (pos2 != 0) {
                pos2 = ((length & 0x3f) << 8) + (bytes[pos + 1] & 0xff);
                dnsName += getDNSName(bytes, pos2, dnsName); // Continue concatinatnig DNS name through recursive calls
                pos++;
                break;
            } else {
                for (int i = 1; i <= length; i++) {
                    dnsName += (char) bytes[pos + i];
                    // System.out.print((char) bytes[pos + i]);
                }
                pos += length + 1;
            }
        }
        pos++;
        return dnsName;
    }

    //////////////////////////////////////// JIPPING ///////////////////////////////////////////////////
    ////////////////////////////////////////  CODE  ///////////////////////////////////////////////////
    // Print the packet in the "response" byte array.
    public void printResponse() {

        // And now we interpret the response!
        System.out.println("Transaction ID: 0x" + bytesToHex(response, 0, 1));

        System.out.println("Flags: 0x" + bytesToHex(response, 2, 3));

        String flags = byteToBinary(response[2]) + byteToBinary(response[3]);
        String message;
        if (flags.charAt(0) == '1') {
            message = "Message is a response";
        } else {
            message = "Message is a query";
        }
        System.out.print("   ");
        printBits(flags, 16, 0, 0, message);

        String opcode = flags.substring(1, 5);
        if (opcode.equals("0000")) {
            message = "Standard query (0)";
        } else if (opcode.equals("0001")) {
            message = "Inverse query (1)";
        } else {
            message = "Other opcode";
        }
        System.out.print("   ");
        printBits(flags, 16, 1, 4, message);

        if (flags.charAt(5) == '1') {
            message = "Server is an authority for the domain";
        } else {
            message = "Server is not an authority for the domain";
        }
        System.out.print("   ");
        printBits(flags, 16, 5, 5, message);

        if (flags.charAt(6) == '1') {
            message = "Message is truncated";
        } else {
            message = "Message is not truncated";
        }
        System.out.print("   ");
        printBits(flags, 16, 6, 6, message);

        if (flags.charAt(7) == '1') {
            message = "Do query recursively";
        } else {
            message = "Do not query recursively";
        }
        System.out.print("   ");
        printBits(flags, 16, 7, 7, message);

        if (flags.charAt(8) == '1') {
            message = "Server can do recursive queries";
        } else {
            message = "Server cannot do recursive queries";
        }
        System.out.print("   ");
        printBits(flags, 16, 8, 8, message);

        System.out.print("   ");
        printBits(flags, 16, 9, 11, "Reserved");

        String responseCode = flags.substring(12);
        if (responseCode.equals("0000")) {
            message = "No error (0)";
        } else if (responseCode.equals("0001")) {
            message = "Format error (1) - The name server was unable to interpret the query";
        } else if (responseCode.equals("0010")) {
            message = "Server failure (2) - The name server was unable to process this query due to a problem with the name server";
        } else if (responseCode.equals("0011")) {
            message = "Name Error (3) - The domain name referenced in the query does not exist";
        } else if (responseCode.equals("0100")) {
            message = "Not Implemented (4) - The name server does not support the requested kind of query";
        } else if (responseCode.equals("0101")) {
            message = "Refused (5) - The name server refuses to perform the specified operation for policy reasons";
        } else {
            message = "Unknown opcode";
        }
        System.out.print("   ");
        printBits(flags, 16, 12, 16, message);

        int questionCount = (response[4] << 8) + response[5];
        System.out.println("Number of Questions: " + questionCount);
        int answerCount = (response[6] << 8) + response[7];
        System.out.println("Number of Answers: " + answerCount);
        int serverCount = (response[8] << 8) + response[9];
        System.out.println("Number of Authoritative Nameservers: " + serverCount);
        int additionalResponseCount = (response[10] & 0xFF << 8) | response[11];
        System.out.println("Number of Additional Responses: " + additionalResponseCount);

        int position = 12;

        if (questionCount > 0)
            System.out.println("\n*** Questions ***");
        while (questionCount > 0) {
            System.out.print("   ");
            position = printDNSName(response, position);
            System.out.print(": type ");
            int qtype = (response[position] << 8) + response[position + 1];
            switch (qtype) {
                case 0:
                    System.out.print("I");
                    break;
                case 1:
                    System.out.print("A");
                    break;
                case 2:
                    System.out.print("M");
                    break;
                case 3:
                    System.out.print("MX");
                    break;
                case 4:
                    System.out.print("*");
                    break;
            }
            System.out.print(", class ");

            position += 2;
            int qclass = (response[position] << 8) + response[position + 1];
            if (qclass == 1)
                System.out.print("IN");
            else
                System.out.print("other");
            System.out.println();
            questionCount--;
            position += 2;
        }

        if (answerCount > 0)
            System.out.println("\n*** Answers ***");
        while (answerCount > 0) {
            position = printAnswer(response, position);

            System.out.println();
            answerCount--;
        }

        if (serverCount > 0)
            System.out.println("\n*** Authoritative Nameservers ***");
        while (serverCount > 0) {
            System.out.print("   ");
            position = printDNSName(response, position);
            System.out.print(": type ");
            int qtype = (response[position] << 8) + response[position + 1];
            switch (qtype) {
                case 1:
                    System.out.print("A");
                    break;
                case 2:
                    System.out.print("NS");
                    break;
                case 5:
                    System.out.print("CNAME");
                    break;
                case 6:
                    System.out.print("SOA");
                    break;
                case 12:
                    System.out.print("PTR");
                    break;
                case 15:
                    System.out.print("MX");
                    break;
                case 16:
                    System.out.print("TXT");
                    break;
                default:
                    System.out.print("ERR");
                    break;
            }
            System.out.print(", class ");

            position += 2;
            int qclass = (response[position] << 8) + response[position + 1];
            if (qclass == 1)
                System.out.print("IN");
            else
                System.out.print("other");
            System.out.println();
            position += 2;
            System.out.println("   Time to live: " + ((response[position] << 24) +
                    (response[position + 1] << 16) + (response[position + 2] << 8) +
                    response[position + 3]));
            position += 4;
            System.out.println("   Length of Data: " + ((response[position] << 8) + response[position + 1]));
            position += 2;
            System.out.print("   Name server: ");
            position = printDNSName(response, position);
            System.out.println();

            System.out.println();
            serverCount--;
        }

        if (additionalResponseCount > 0)
            System.out.println("\n*** Additional Responses ***");
        while (additionalResponseCount > 0) {
            position = printAnswer(response, position);

            System.out.println();
            additionalResponseCount--;
        }

        if (dgSocket != null)
            try {
                dgSocket.close();
            } catch (Exception e) {
                // don't do anything with it.
            }
    }

    // Turn the byte parameter into an 8-bit wide binary in a string.
    private static String byteToBinary(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    // Turn the byte array given, from start to end, into hexadecimal string.
    public static String bytesToHex(byte[] bytes, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            sb.append(String.format("%02x", bytes[i]));
        }
        return sb.toString();
    }

    // Print the bytes give into a "dotted" representation with the string message
    public static void printBits(String bytes, int width, int start, int end, String message) {
        for (int i = 0; i < width; i++) {
            if (i > 0 && i % 4 == 0)
                System.out.print(" ");
            if (i < start)
                System.out.print(".");
            else if (i > end)
                System.out.print(".");
            else {
                System.out.print(bytes.charAt(i));
            }
        }
        System.out.println(" = " + message);
    }

    // Print the DNSname from the byte array in that wacky format
    public static int printDNSName(byte bytes[], int start) {

        int pos = start;
        while (bytes[pos] != 0) {
            if (pos != start)
                System.out.print(".");
            int length = bytes[pos];
            int pos2 = (length >> 6) & 0x03; // Takes the length we had, looks at the
            // first 2 bits, masks the rest.
            if (pos2 != 0) {
                pos2 = ((length & 0x3f) << 8) + (bytes[pos + 1] & 0xff);
                printDNSName(bytes, pos2);
                pos++;
                break;
            } else {
                for (int i = 1; i <= length; i++) {
                    System.out.print((char) bytes[pos + i]);
                }
                pos += length + 1;
            }
        }
        pos++;
        return pos;
    }

    // Print the answer section of the DNS reponse.
    public static int printAnswer(byte response[], int start) {
        int position = start;

        System.out.print("   ");
        position = printDNSName(response, position);
        System.out.print(": type ");
        int qtype = (response[position] << 8) + response[position + 1];
        switch (qtype) {
            case 1:
                System.out.print("A");
                break;
            case 2:
                System.out.print("NS");
                break;
            case 5:
                System.out.print("CNAME");
                break;
            case 6:
                System.out.print("SOA");
                break;
            case 12:
                System.out.print("PTR");
                break;
            case 15:
                System.out.print("MX");
                break;
            case 16:
                System.out.print("TXT");
                break;
            default:
                System.out.print("ERR");
                break;
        }
        System.out.print(", class ");

        position += 2;
        int qclass = (response[position] << 8) + response[position + 1];
        if (qclass == 1)
            System.out.print("IN");
        else
            System.out.print("other");
        System.out.println();
        position += 2;
        System.out.println("   Time to live: " + ((response[position] << 24) +
                (response[position + 1] << 16) + (response[position + 2] << 8) +
                response[position + 3]));
        position += 4;
        System.out.println("   Length of Data: " + ((response[position] << 8) + response[position + 1]));
        position += 2;
        switch (qtype) {
            case 1:
                System.out.print("   Address: ");
                System.out
                        .print("" + (response[position] < 0 ? 256 + response[position++] : response[position++]) + ".");
                System.out
                        .print("" + (response[position] < 0 ? 256 + response[position++] : response[position++]) + ".");
                System.out
                        .print("" + (response[position] < 0 ? 256 + response[position++] : response[position++]) + ".");
                System.out.println("" + (response[position] < 0 ? 256 + response[position++] : response[position++]));
                break;
            case 2:
                System.out.print("   Name server: ");
                position = printDNSName(response, position);
                break;
            case 5:
                System.out.print("   Alias for: ");
                position = printDNSName(response, position);
                System.out.println();
                break;
            case 6:
                System.out.print("   Source of authority for: ");
                position = printDNSName(response, position);
                System.out.print("\n      Person responsible: ");
                position = printDNSName(response, position);
                System.out.print("\n      Serial number: ");
                System.out.print("" + ((response[position] << 24) +
                        (response[position + 1] << 16) + (response[position + 2] << 8) +
                        response[position + 3]));
                position += 4;
                System.out.print("\n      Retry interval: ");
                System.out.print("" + ((response[position] << 24) +
                        (response[position + 1] << 16) + (response[position + 2] << 8) +
                        response[position + 3]));
                position += 4;
                System.out.print("\n      Expiration: ");
                System.out.print("" + ((response[position] << 24) +
                        (response[position + 1] << 16) + (response[position + 2] << 8) +
                        response[position + 3]));
                position += 4;
                System.out.print("\n      Minimum caching TTL: ");
                System.out.print("" + ((response[position] << 24) +
                        (response[position + 1] << 16) + (response[position + 2] << 8) +
                        response[position + 3]));
                position += 4;
                break;
            case 12:
                System.out.print("   Pointer to: ");
                position = printDNSName(response, position);
                break;
            case 15:
                System.out.print("   Mail exchange for: ");
                int priority = (response[position] << 8) + response[position + 1];
                position += 2;
                position = printDNSName(response, position);
                System.out.println("(priority = " + priority + ")");
                break;
            case 16:
                System.out.print("   Text: ");
                char c = ' ';
                while (c != 0) {
                    c = (char) response[position++];
                    if (c != 0)
                        System.out.print(c);
                    position++;
                }
                System.out.println();
                break;
        }

        return position;

    }

}
