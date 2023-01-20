//-----------------------------------------------------------
//
//   Find an actor's bio on IMDB
//
//   We so this by finding a random actor using the ActorSite 
//   class then finding that actor's bio through links on imdb.com.
//   Eventually, we have to scrape a page to get the bio.
//   
//   Mike Jipping, January 2017
//
//-----------------------------------------------------------

import java.net.*;
import java.io.*;
import java.util.*;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ActorInfo {

   static final int DEFAULT_PORT = 443;
   static final int TIMEOUT = 5 * 1000; // 5 seconds

   protected DataInputStream reply = null;
   protected PrintStream send = null;   
   protected SSLSocket sock = null;  

   //***********************************************************
   //*** The constructors create the socket and set up the input
   //*** and output data streams on that socket.

   public ActorInfo ()
      throws UnknownHostException, IOException
   {
	  SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
	  sock = (SSLSocket) factory.createSocket("www.imdb.com", DEFAULT_PORT);
       if (sock != null) {
    	  reply = new DataInputStream(sock.getInputStream());
    	  send = new PrintStream(sock.getOutputStream());
    	  sock.setSoTimeout(TIMEOUT);
      }
 	  sock.startHandshake();
   }    

   //*** We get an actor's bio by using 2 imdb pages.  The first 
   //*** is a general actor's page, then we get the bio page from that
   //*** first page.
   //***
   //*** NOTE: we use a "keep-alive" in the HTTP header to keep the
   //*** socket open so we don't have to initialize it everything again.
   public String getBio (String actor)
   {
      int pos1, pos2;
      String HTMLline, cmd, subject, buffer, tweet, tweets;
      StringTokenizer st;
      Date hostDate;
      boolean eof, more, list;
      
      // Get the first actor page.  Send the HTTP GET.
      actor = actor.replaceAll("\\s", "+");

      cmd = "GET /search/name/?name="+actor+" HTTP/1.1";
      send.println(cmd);
      cmd = "Host: www.imdb.com";
      send.println(cmd);
      cmd = "Connection: keep-alive";
      send.println(cmd);
      send.println("");
      
      // Collect the Web page content.  We only collect the content
      // after a certain patten is matched to save some space.
      eof = false;
      list = false;
      buffer = "";
      while (! eof) {
         try {
            HTMLline = reply.readLine();
            if (HTMLline != null) {
               if (list) {
	               if (HTMLline.contains("</body>")) {
	            	   eof = true;
	              } else
	            	   buffer += HTMLline;
               } else if (HTMLline.contains("Death")) {
            	   list = true;
                  buffer = HTMLline;
               }
            } else {
            	eof = true;
            }	    
         } catch (Exception e) {
            e.printStackTrace();
            eof = true;
         }
      }

      // Now we process that first page.  All we are after is the
      // "actor number" used to the reference the actor's data.  We 
      // do some replacing in the URL used to access that actor's data 
      // and get the next page.
      pos1 = buffer.indexOf("href=");
      pos2 = buffer.indexOf("\"", pos1+6);
      cmd = buffer.substring(pos1+6,pos2);
      cmd = "GET "+cmd+"/bio?ref_=nm_ov_bio_sm HTTP/1.1";
      
      // Send the next GET.  This is the last Web page access, so we
      // don't care about any stinkin' "keep-alive" headers.
      send.println(cmd);
      cmd = "Host: www.imdb.com";
      send.println(cmd);
      send.println("");
      
      // Collect the Web page data.  Again, we wait until we receive a
      // specific pattern to actually collect the data.
      eof = false;
      list = false;
      buffer = "";
      while (! eof) {
         try {
            HTMLline = reply.readLine();
            //System.out.println(HTMLline);      
            if (HTMLline != null) {
                if (list) {
 	               if (HTMLline.contains("trivia")) {
 	            	   eof = true;
 	              } else
 	            	   buffer += HTMLline;
                } else if (HTMLline.contains("\"mini_bio")) {
             	   list = true;
                  buffer = HTMLline;
                }
            } else {
            	eof = true;
            }	    
         } catch (Exception e) {
            e.printStackTrace();
            eof = true;
         }
      }
      
      // Close the socket.
      try {
		sock.close();
	  } catch (IOException e) {
		e.printStackTrace();
	  }

      // Now, process the string data to find the bio text.
      pos1 = buffer.indexOf("<p>");
      pos2 = buffer.indexOf("</p>", pos1);
      
      String bio = buffer.substring(pos1+3, pos2);
      bio = bio.trim();
      bio = bio.replaceAll("<br />", "\n");
      bio = bio.replaceAll("<a[^>]*>(.*?)</a>", "$1");

      // The bio data is in one long string.  Here we add the necessary
      // line feed characters to cut the line length to 80 characters.
      StringBuilder sb = new StringBuilder(bio);
      int i = 0;
      while (i + 80 < sb.length() && (i = sb.lastIndexOf(" ", i + 80)) != -1) {
          sb.replace(i, i + 1, "\n");
      }

      bio = sb.toString();
      return bio;
   }
   
   public void serveUpActor(String actorInfo) {
	   DataInputStream input;
	   PrintStream output=null;
	    
	   try {
		   ServerSocket serverSocket = new ServerSocket(8100);
	       Socket clientSocket = serverSocket.accept();
	       input = new DataInputStream(clientSocket.getInputStream());
	       output = new PrintStream(clientSocket.getOutputStream());
	       
	       String cmd = input.readLine();
	       output.println("HTTP/1.1 200 OK\r\n\r\n");
	       output.println(actorInfo);
	       output.println("\r\n");
	       
	       clientSocket.close();
		   serverSocket.close();
	   } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }

   }

   //***********************************************************
   //*** The main program retrieves a random actor and sends that 
   //*** actor to the ActorInfo class.

   public static void main (String[] args) 
   {
      String actor;
      String actorInfo=null;
      ActorSite actors;
      Socket remotesock=null;
      
      ActorInfo info=null;
      
      try {
         actors = new ActorSite();
         actor = actors.random();

         info = new ActorInfo();
         actorInfo = info.getBio(actor);
         
         System.out.println("Biography for "+actor);
         System.out.println("----------------------------------------------");
         System.out.println(actorInfo);

         info.serveUpActor(actorInfo);
         
      } catch (Exception ee) {
    	 ee.printStackTrace(); 
      }
   }
}
