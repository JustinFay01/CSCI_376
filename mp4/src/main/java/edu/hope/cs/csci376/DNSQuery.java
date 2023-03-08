//-------------------------------------------------------------------
//
// Assemble a DNS query and send it.
//
// We build the query / DNS packet byte by byte, by writing bytes to
// a ByteArrayOutputStream object, then get the bytes from that.

package main.java.edu.hope.cs.csci376;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DNSQuery {

    String domainName = "";
    InetAddress serverAddress = null;

    // The constructor just collects the data necessary.
    public DNSQuery(String serverName, String domainName) {
        this.domainName = domainName;

		try {
			serverAddress = InetAddress.getByName(serverName);
		} catch (UnknownHostException uhe) {
			System.err.println("No such host: " + serverName);
			System.exit(0);
		}
    }
    
    // sendQuery builds the DNS packet, then sends it.
    // 
    // Note we return the socket we create and use because the same socket must
    // be used to receive the DNS response.
    public DatagramSocket sendQuery() {

        final int DNS_SERVER_PORT = 53;

        // We create a ByteArrayOutputStream object, but use a DataOutputStream
        // object to write to it (the I/O system in Java is very complex)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        DatagramSocket socket = null;
        
        try {
            // identification number
            dataOutputStream.writeShort(0xABCD); 

            // flags = 0x0100 (RD=1, recursion desired)
            short requestFlags = Short.parseShort("0000000100000000", 2); 
            ByteBuffer flagsByteBuffer = ByteBuffer.allocate(2).putShort(requestFlags);
            byte[] flagsByteArray = flagsByteBuffer.array();
            dataOutputStream.write(flagsByteArray);

            // Write the counts: 1 question, the rest = 0
            short questions = 1;
            short answers = 0;
            short authorities = 0;
            short additional = 0;
            dataOutputStream.writeShort(questions);
            dataOutputStream.writeShort(answers);
            dataOutputStream.writeShort(authorities);
            dataOutputStream.writeShort(additional);

            // Now we fill in the ONE name we want an answer for.  We have to fill the name in
			// in the following way:
			//   lettercount letters lettercount letters ... 0
			// no "." in the name and each section is preceded by the number of letters in the
			// section
            String[] domainParts = domainName.split("\\.");

            for (int i = 0; i < domainParts.length; i++) {
                byte[] domainBytes = domainParts[i].getBytes(StandardCharsets.UTF_8);
                dataOutputStream.writeByte(domainBytes.length);
                dataOutputStream.write(domainBytes);
            }
            dataOutputStream.writeByte(0);

            // Type 1 = A record (Host Request)
            dataOutputStream.writeShort(1);

            // Class 1 = IN record
            dataOutputStream.writeShort(1);

            // Now send the darn thing.
            byte[] queryBytes = byteArrayOutputStream.toByteArray();
            socket = new DatagramSocket();
            DatagramPacket dnsReqPacket = new DatagramPacket(queryBytes, queryBytes.length, serverAddress, DNS_SERVER_PORT);
            socket.send(dnsReqPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return socket;
    }
}
