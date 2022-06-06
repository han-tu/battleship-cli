package app.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import app.game.Message;

public class WorkerThread extends Thread {
	private ObjectInputStream ois;
	 
    public WorkerThread(ObjectInputStream ois) {
        this.ois = ois;
    }
 
    public void run() {
        while(true) {
            try {
                Message message = (Message) ois.readObject();
 
                if (message.getRequest().equals("Message")) {
                    System.out.println("You have new message from \"" + message.getSender() + "\"");
                    System.out.println(message.getSender() + " : " + message.getText());
                    System.out.println("---End of Message---");
                }
                else if (message.getSender().equals("Server")) {
                	System.out.println("You have new message from \"" + "Server" + "\"");
                	System.out.println(message.getText());
                	System.out.println("---End of Message---");
                }
                
            } catch (IOException e) {
                System.out.println("Connection Lost: Server Disconnected");
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
 
        }
    }
}
