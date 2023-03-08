//---------------------------------------------------------------------
//
// Machine Problem 4: Use non-recursive DNS
//
// Mike Jipping, March 2023

package main.java.edu.hope.cs.csci376;

import java.net.DatagramSocket;
import java.util.Scanner;

/**
 * This is the driver.  Read both server and domain name from user and
 * perform a DNS lookup.
 */
public class App {
    public static void main( String[] args )
    {
        // Get names from user
        Scanner in = new Scanner(System.in); 
        System.out.print( "Enter a server name: " );
        String serverName = in.nextLine(); 
        System.out.print( "Enter a domain name: " );
        String name = in.nextLine(); 

        // Assemble and send a DNS query
        DNSQuery query = new DNSQuery(serverName, name);
        DatagramSocket dgs = query.sendQuery();

        // Receive and print the DNS response
        DNSResponse response = new DNSResponse(dgs);
        response.receive();
        response.printResponse();
    }
}
