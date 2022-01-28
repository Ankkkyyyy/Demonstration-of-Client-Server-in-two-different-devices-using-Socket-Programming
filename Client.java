import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;



public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    
    // After Testing it in cmd now i will be converting the Client Side into the GUI 
    // So Declaring Components here....
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Arial",Font.PLAIN,20);
    



  //-------------------------------------------- Constructor-------------------------------------- 
    public Client()
    {
        try{
            System.out.println("Sending request to server ");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection done");
            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            
            createGUI(); // Created this
            handleEvnts();//then after gui this

            startReading();
            // startWriting();

        }catch(Exception e){

        }
    }
    
    private void handleEvnts()
    {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Key Released"+e.getKeyCode());
                if(e.getKeyChar()==10)
                {
                   // System.out.println("You have passed enter button !");
                   String contentToSend = messageInput.getText();
                   // The time when we will send mssge so i am showing that also in my textfield !
                   messageArea.append("Me:"+contentToSend+"\n");
                   out.println(contentToSend);
                   out.flush();
                   messageInput.setText("");
                   messageInput.requestFocus();
                }

            }

        });

    }

    private void createGUI()
    {
         // GUI Code...
         this.setTitle("Client Messenger !");
         this.setSize(600,550);
         this.setLocationRelativeTo(null);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setVisible(true);

         // Component Code here ..
         heading.setFont(font);
         
         messageArea.setFont(font);
         messageInput.setFont(font);
         heading.setIcon(new ImageIcon("Icon.jpeg"));
         heading.setHorizontalTextPosition(SwingConstants.CENTER);// For Bringing text at center
         heading.setVerticalTextPosition(SwingConstants.BOTTOM); // & then down the imageicon !
         // Setting the heading in the center !
         heading.setHorizontalAlignment(SwingConstants.CENTER);
         heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));//TOp //Left//bottom//Right Border spacing for header
        
         messageArea.setEditable(false);
         messageInput.setHorizontalAlignment(SwingConstants.CENTER);
         //Frame's Layout ko set karta hua Ankit !
         this.setLayout(new BorderLayout());

         //Adding Component to Frame !

         this.add(heading,BorderLayout.NORTH);
         JScrollPane jScrollPane = new JScrollPane(messageArea);
         this.add(jScrollPane,BorderLayout.CENTER);
         this.add(messageInput,BorderLayout.SOUTH);
  

         this.setVisible(true);





    }



    // -----------------------------------------------Start Reading Method---------------------------------------
    public void startReading()
    {
        // Thread - read krke deta rhega !
        Runnable r1 = ()->
        {
          
         
        System.out.println("Reader started.....");
        //For Exiting from the thread putin while loop in try block!
         try{  
         while(true)
            { 
                String msg= br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"Server terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
               // System.out.println("Server : "+msg);
               messageArea.append("Server : "+msg+"\n");
            } 
            System.out.println("Connection Closed");
          }
            catch (Exception e)
             {
              
                //e.printStackTrace();
                System.out.println("Connection Closed");

            }


        };
        new Thread(r1).start();


    }
    //-----------------------------------------------Start Writing Method------------------------------------------------------
    public void startWriting()
    {
        //thread - user se lega and send krega client tak
        Runnable r2=()->
         { System.out.println("Writer started...");
        // For exiting from thread as we want to stop the talk implementing while inside try
         try {   
        while(true && !socket.isClosed())
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
          catch (Exception e)
           {
               
                e.printStackTrace();
            }

    };

    new Thread(r2).start();

 }
    public static void main(String[] args) {
        System.out.println("THis is Client");
        new Client();
    }
    
}
