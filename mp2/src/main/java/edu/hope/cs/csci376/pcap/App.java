package edu.hope.cs.csci376.pcap;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Scanner in = new Scanner(System.in); 
        System.out.print( "Enter PCAP file name: " );
        String pcapFilename = in.nextLine(); 

        try {
            Pcap pcap = Pcap.fromFile(pcapFilename);
            System.out.println("*** Read PCAP File ***");
            System.out.println("  Version: "+pcap.hdr().versionMajor()+"."+pcap.hdr().versionMinor
            ());
            System.out.println("  Has "+pcap.packets().size()+" packets");

            System.out.print( "Enter packet number: " );
            String packetNumStr = in.nextLine();     
            int packetNum = Integer.parseInt(packetNumStr);

            System.out.println("\nPacket #"+packetNum);
            System.out.println("   Size: "+ pcap.packets().get(packetNum).origLen());
            DataLinkLayer link = new DataLinkLayer(pcap.packets().get(packetNum)._raw_body());
            link.print();
            if (link.type == DataLinkLayer.TYPE_IPv4) {
                NetworkLayer network = new NetworkLayer(link.getPayload());
                network.print();
                if (network.protocol == NetworkLayer.IPV4_PROTOCOL_TCP) {
                    TransportLayer transport = new TransportLayer(network.getPayload());
                    transport.print();
                    if (transport.sourcePort == 80 || transport.destinationPort == 80) {
                        HTTP http = new HTTP(transport.getPayload());
                        http.print();
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
