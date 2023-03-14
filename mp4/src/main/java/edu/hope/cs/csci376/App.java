//---------------------------------------------------------------------
//
// Machine Problem 4: Use non-recursive DNS
//
// Mike Jipping, March 2023

package edu.hope.cs.csci376;

import java.net.DatagramSocket;
import java.util.Scanner;

/**
 * This is the driver. Read both server and domain name from user and
 * perform a DNS lookup.
 */
public class App {
    public static void main(String[] args) {
        // Get names from user
        Scanner in = new Scanner(System.in);
        // System.out.print("Enter a server name: ");
        // String serverName = in.nextLine();
        System.out.print("Enter a domain name: ");
        String name = in.nextLine();
        in.close();
        // response.printResponse();
        String domains[] = name.split("\\.");
        StringBuilder parsedName = new StringBuilder(); // String builder so we can insert at front of string
        System.out.println();
        String serName = "";

        for (int i = domains.length - 1; i > -1; i--) {
            parsedName.insert(0, (i == domains.length - 1 ? domains[i] : domains[i] + "."));
            if (i == domains.length - 1) { // Set original Server
                serName = "a.root-servers.net";
            } 

            DNSQuery query = new DNSQuery(serName, parsedName.toString()); // Create Query based on
                                                                           // original root or found domain
            DatagramSocket dgs = query.sendQuery(); // Send the query

            // Receive and print the DNS response
            DNSResponse response = new DNSResponse(dgs); // set up socket
            response.receive(); // reieve resposne

            if (!name.equals(parsedName.toString())) { // Check if we've reached the full domain name
                System.out.println("Querying: " + serName + "\n" +
                        "\"" + parsedName.toString() + "\" domain found: " + response.getResponse());
                serName = response.getResponse(); //Set new domain name to query next iteration     
            } else { // We've Reached our destination
                String result = ": ";
                String finalDomain = response.getResponse();
                if(!response.getResponseFlag()){
                    result += "Alias for ";
                } 
                System.out.println("Querying: " + serName + "\n" +
                    "Reached ---> " + parsedName.toString() + result + finalDomain + "\n" +
                    "Done!");
    
            }
        }
    }
}
