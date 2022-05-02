package app.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import app.message.Message;

public class WorkerThread {
	private Socket socket;
    private ObjectOutputStream ous;
    private ObjectInputStream ois;
    private RoomThread roomThread;
    private String username;
    
    public WorkerThread(Socket socket, RoomThread roomThread) {
        try {
            this.socket = socket;
            this.ous = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());
            this.roomThread = roomThread;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
    	try {
			Message firstMessage = (Message) this.ois.readObject();
			this.setUsername(firstMessage.getText());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	while (!)
    }
    
    
}
