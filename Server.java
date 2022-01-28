import java.net.ServerSocket;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class Server
{
    
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;


    //Constructor....
    public Server()
    {
        try {
            server=new ServerSocket(7777);
            System.out.println("Server is ready to accept the connection");
            System.out.println("Waiting....");
            socket=server.accept();

            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out=new PrintWriter(socket.getOutputStream());

        
      
            startReading();
            startWriting();
        } 
        catch (Exception e) 
        {
        
           e.printStackTrace();
        }
    }

        public void startReading()
        {
            // Thread - read krke deta rhega !
            Runnable r1 = ()->
            {
                System.out.println("Reader started.....");
                // For Exiting the Thread 
                try{
                while(true)
                { 
                    String msg= br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Client terminated the chat");
                        socket.close(); // For Ending Chat 
                        break;
                    }
                    System.out.println("Clientt : "+msg);
                } 
            } catch (Exception e)
            {
            
               e.printStackTrace();
           }


           };
            new Thread(r1).start();


        }
        public void startWriting()
        {
            //thread - user se lega and send krega client tak
            Runnable r2=()->
            { System.out.println("Writer started...");
           // Try catch for exiting from thread so thats why putin while inside try block!
            try {   
                //agr socket close nahi hai tabhi andar aaoge
            while(true && ! socket.isClosed())
                {
               
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content=br1.readLine();
            
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }

            }
        }
            catch (Exception e) {
               
                //e.printStackTrace();
                System.out.println("Connection Closed");
            }
            //Printingoutside while loop also
            System.out.println("Connection Closed");
        };

        new Thread(r2).start();

    }

   

    public static void main(String[] args) {
        System.out.println("THis is Server going to start Server");
        new Server();
    }
}