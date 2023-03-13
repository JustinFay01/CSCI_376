//---------------------------------------------------------------------
//
// Machine Problem 4: Use non-recursive DNS
//
// Mike Jipping, March 2023

package edu.hope.cs.csci376;

import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This is the driver. Read both server and domain name from user and
 * perform a DNS lookup.
 */
public class App {
    public static void main(String[] args) {
        // Get names from user
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a server name: ");
        String serverName = in.nextLine();
        System.out.print("Enter a domain name: ");
        String name = in.nextLine();
        in.close();
        // response.printResponse();
        String domains[] = name.split("\\.");
        StringBuilder parsedName = new StringBuilder();
        System.out.println();
        for (int i = domains.length - 1; i > -1; i--) {
            parsedName.insert(0,(i == domains.length - 1 ? domains[i] : domains[i] + "."));
            // Assemble and send a DNS query
            DNSQuery query = new DNSQuery(serverName, parsedName.toString());
            DatagramSocket dgs = query.sendQuery();
            // Receive and print the DNS response
            DNSResponse response = new DNSResponse(dgs);
            response.receive();

            if (i != 0) {
                System.out.println("Found " + parsedName.toString() + " served by " + response.getResponse());
            } else {
                System.out.println("Found " + parsedName.toString() + " address is " + response.getResponse());
            }
        }
    }

    // FOUND (Third part of TDL), served by
    // FOUND (Second part), served by
    // Found (First part), address is
}
