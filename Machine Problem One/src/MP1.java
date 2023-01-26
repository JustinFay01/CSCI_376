//----------------------------------------------------------------------
//
//  CSCI 376 MP1 Template
//
//  Send data to an IFTTT webhook, which sends it back to us.
//
//  Mike Jipping, January 2023

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.Enumeration;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MP1 {

    DataInputStream reply = null;
    PrintStream send = null;
    SSLSocket sock = null;

    // The Receiver class implements a thread that waits to
    // receive an http request from IFTTT
    private class Receiver implements Runnable {

        static final int DEFAULT_PORT = 80;

        protected DataInputStream input = null;
        protected PrintStream output = null;
        protected ServerSocket netsock = null;
        Socket remotesock;

        public void run() {
            try {
                // Put the code to receive and process the
                // Web request from IFTTT HERE!
                

                System.out.println("running...");
                netsock = new ServerSocket(DEFAULT_PORT, 443);
                remotesock = netsock.accept();

                input = new DataInputStream(remotesock.getInputStream());
                output = new PrintStream(remotesock.getOutputStream());

                System.out.println("\nGot a connection from " +
                        remotesock.getInetAddress().getHostName());
                netsock.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Constructor.
    // Here we start the receiver, then create an SSL socket and
    // set up the I/O variables.
    public MP1() throws UnknownHostException, IOException {
        final int DEFAULT_PORT = 443;
        final int TIMEOUT = 5 * 1000; // 5 seconds

        // Spawn a listener thread
        Thread thread = new Thread(new Receiver());
        thread.start();

        // Now access the webhook at IFTTT and set up the
        // stream variables
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        sock = (SSLSocket) factory.createSocket("maker.ifttt.com", DEFAULT_PORT);
        if (sock != null) {
            reply = new DataInputStream(sock.getInputStream());
            send = new PrintStream(sock.getOutputStream());
            sock.setSoTimeout(TIMEOUT);
        }
        sock.startHandshake();

    }

    // This method checks all the interfaces the executing computer has and
    // enumerates these, giving a choice to the user as to which one to use.
    // It returns a string with that IP address.
    public String getMyIPAddress() {
        InetAddress[] addresses = new InetAddress[10];
        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
            return null;
        }

        System.out.println("**** Choose an IP address and give its number ***");
        int count = 0;
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                addresses[count] = i;
                count++;
                System.out.println("(" + count + ") " + i.getHostAddress());
            }
        }
        System.out.print("Choice? ");
        Scanner s = new Scanner(System.in);
        int choice = s.nextInt();
        s.close();

        return addresses[choice - 1].getHostAddress();
    }

    // The sendHook method constructs and access the URL for the webhook.
    public void sendHook() {
        String cmd;

        String address = getMyIPAddress();

        // Send the Webhook data
        cmd = "GET /trigger/mp1/with/key/czMmaZy40u35Uim0pfJYYO?value1=" + address + " HTTP/1.1";
        send.println(cmd);

        cmd = "Host: maker.ifttt.com";
        send.println(cmd);

        cmd = "Connection: keep-alive";
        send.println(cmd);

        //Content Type
        //Value
        //Length
        //Content Length
        //Json structure

        //Body Content Length
        //Json Version

        //{ \"value1"}

        send.println("");

        // Read and report the response
        try {
            StringBuffer inputLine = new StringBuffer();
            String tmp;
            while ((tmp = reply.readLine()) != null) {
                inputLine.append(tmp);
                System.out.println(tmp);
            }
            reply.close();
        } catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Close the socket
    public void close() {
        try {
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // The main method...easy.
    public static void main(String[] args) {
        try {
            MP1 mp1 = new MP1();
            mp1.sendHook();
            mp1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}