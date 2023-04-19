package edu.hope.cs.csci376.pcap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    private Pcap pcap;

    public ARP loadARPPackets(int packetIndex) {
        try {
            pcap = Pcap.fromFile("packets.pcap");
            DataLinkLayer link = new DataLinkLayer(pcap.packets().get(packetIndex)._raw_body());
            if (link.type == DataLinkLayer.TYPE_ARP) {
                return new ARP(link.getPayload());
            } else {
                throw new IOException("Not Arp Packet", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testArp() {
        // HAND INPUT PACKET INFO
        // Hardware Type = int
        // Protocol Type = String
        // Hardwarsize = int
        // Protocol Size = int
        // Operation = int
        // MAC Address = String
        // Sender IP Adress = String
        // Target MAC Address = String
        // Target IP address = String

        try {
            File file = new File("ArpAnswers");
            Scanner sc = new Scanner(file);
            int i = 0;
            int j = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains("STOP"))
                    return;
                if (line.contains("DONE"))
                    j = 0;
                else {
                    ARP arp = loadARPPackets(i);
                    switch (j++){
                        case 0:
                            assertEquals(line, String.valueOf(arp.getHardwareType()));
                            break;
                        case 1:
                            assertEquals(line, arp.getProtocolType()); //Already String
                            break;
                        case 2:
                            assertEquals(line, String.valueOf(arp.getHardwareSize()));
                            break;
                        case 3:
                            assertEquals(line, String.valueOf(arp.getProtocolSize()));
                            break;
                        case 4: // Skip Operation
                            assertEquals(line, arp.getSenderMACAddress());
                            break;
                        case 5:
                            assertEquals(line,arp.getSenderIPAddress());
                            break;
                        case 6:
                            assertEquals(line, arp.getTargetMACAddress());
                            break;
                        case 7:
                            assertEquals(line, arp.getTargetIPAddress());
                            break;
                    }
                    

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
