//---------------------------------------------------------------------
//
// Machine Problem 4: Use non-recursive DNS
//
// Mike Jipping, March 2023



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


        //while(moreServers)
        /*
         * response = new dnsResponse(dgs)
         * response.receieve
         * 
         * StringrNodeName = response.responseNAme
         * String rServerName = response.responseServerName
         * if(rServerName == null)
         * 
         * proessResponse
         * question
         * questionCount response[4] << 8 +response [5] 
         * answerCout  = ''6''7'
         * authorityCount = ''8''9'
         * 
         * If answer count is great than 0
         * get the name (GetDNSName)
         * get position 
         * get qtype
         * skip over tpye class and ttl and data length
         * if the answer is an A record record it ONLY WANT ARECORD 
         * if(qType == 0x0001)
         *      get the address using position
         * else if its a cname but its the only answer
         *      address = getDNSName(response, psition)
         *      return;
         * else if its a cname
         *  get the next answer
         */
    }
}
