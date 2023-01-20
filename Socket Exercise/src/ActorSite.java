//-------------------------------------------------------------------
//
//  Class to choose an actor at random from a Web site with a list of actors
//  Current, we use movies.about.com; it has a list of moderate size.
//
//  Mike Jipping, January 2017

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.text.html.HTML;

public class ActorSite {

   static final int DEFAULT_PORT = 443;
   static final int TIMEOUT = 10 * 1000; // 5 seconds

   protected DataInputStream reply = null;
   protected PrintStream send = null;
   protected SSLSocket sock = null;
   
   Random rand;

   // Constructor creates the socket, initializes the data streams
   // and sets the timeout.
   
   public ActorSite ()
      throws UnknownHostException, IOException
   {
	  SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
	  sock = (SSLSocket) factory.createSocket("www.ranker.com", DEFAULT_PORT);
      if (sock != null) {
	      reply = new DataInputStream(sock.getInputStream());
	      send = new PrintStream(sock.getOutputStream());
	      sock.setSoTimeout(TIMEOUT);
      }
	   sock.startHandshake();
      
      rand = new Random();
   }    
   
   // random() access the Web site and chooses a random actor.  
   // It scrapes the page to set up a list of actors, then chooses
   // a string from a random position in the array.

   String random()
   {
      int pos1, pos2;
      String HTMLline, actor, cmd, buffer;
      boolean eof, more, actorlist;
      ArrayList<String> actors = new ArrayList<String>();
      
      // Start the HTTP protocol with a GET
      cmd = "GET /list/the-best-actors-working-today/bustermcdermott HTTP/1.1";
      send.println(cmd);
      send.println("Host: www.ranker.com");
      send.println("");
      
      // Here we collect the Web page that is returned.  We actually don't
      // collect the HTML until after we get to a specific pattern in the
      // page (to save some semblance of space).
      eof = false;
      actorlist = true;
	   actor = "Mike Jipping";
	   buffer = "";
      HTMLline = "";
      while (! eof) {
         try {
            HTMLline = reply.readLine();
            if (HTMLline != null) {     // will be null when we are out of data
            	if (actorlist) {            		
         		   buffer += HTMLline;
         		   if (HTMLline.contains("rightRail")) {
                      eof = true;
                   }
            	} else {
            		if (HTMLline.contains("listItem_hasProps")){
            			actorlist = true;
            		}
            	}
           } else {
           	eof = true;
           }	    
         } catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
            eof = true;
         } catch (Exception e) {
            e.printStackTrace();
            eof = true;
         }
      }
      
      // Now we have the Web page contents in the "buffer" string.
      // We rip it into pieces using pattern matching and create the
      // the list of actors.
      more = true;
      while (more) {
    	  if (buffer.contains("listItem_hasProps")) {
    		  pos1 = buffer.indexOf("listItem_hasProps");
    		  pos1 = buffer.indexOf("title=\"", pos1);
    		  pos2 = buffer.indexOf("\"", pos1+7);
    		  actor = buffer.substring(pos1+7, pos2);
    		  buffer = buffer.substring(pos2+1);

    		  actors.add(actor);
    	  } else 
    		  more = false;
      }
      
      // Now find a random spot in the actor list. 
      // Stupid glitch: if the actor is listed last name first, flip
      // the names.
      actor = actors.get(rand.nextInt(actors.size()-1));
      if (actor.contains(",")) {
    	  String names[] = actor.split(",");
    	  actor = names[1].trim() + " " + names[0].trim();
      }
      
      return actor;
   }

   // When we delete the object instance, close the socket.
   protected void finalize() 
      throws Throwable 
   {
      sock.close();
   }

   public static void main (String[] args) 
   {
      String actor;
      String actorInfo=null;
      ActorSite actors;
      
      ActorInfo info=null;
      
      try {
         actors = new ActorSite();
         actor = actors.random();
         System.out.println("Got random: "+actor);
      } catch (Exception ee) {
         ee.printStackTrace(); 
      }
   }
}
