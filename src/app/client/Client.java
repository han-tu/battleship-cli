package app.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.game.Message;

public class Client {
	
	private static boolean status;
	
	public static void main(String[] args) {
		System.out.println("Client Alive");
        try {
            Socket socket = new Socket("127.0.0.1", 9000);
            status = true;
            ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
            WorkerThread wt = new WorkerThread(new ObjectInputStream(socket.getInputStream()));
            wt.start();
 
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
 
            System.out.println("Enter your username: \n>> ");
            String username = reader.readLine();
            
            Message firstMessage = new Message();
            firstMessage.setText(username);
            ous.writeObject(firstMessage);
            ous.flush();
            
            menu();
            while(status) {
                
                String req = reader.readLine();
                
                if(req.equals("help")) {
                	menu();
                	continue;
                }
                
                Message message = new Message();
                message.setSender(username);
                message.setRequest(req);
 
                ous.writeObject(message);
                ous.flush();
            }
 
//            unreacable statements
            ous.close();
            socket.close();
 
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public static void menu() {
		System.out.println("---Request List Keyword (With CLI Command)---");
        System.out.println("> Send Message -> send-message <message text>");
        System.out.println("> See My Board -> see -mb");
        System.out.println("> See Opponent Board -> see -ob");
        System.out.println("> Fire Missile -> fire <tile position>");
        System.out.println("> Help Menu -> help");
        System.out.println("Enter your request: \n");
	}

}
