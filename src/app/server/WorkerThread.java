package app.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import app.game.Message;

public class WorkerThread {
	private Socket socket;
    private ObjectOutputStream ous;
    private ObjectInputStream ois;
    private ServerThread serverThread;
    private String username;
    private boolean gameOver;
    
    public WorkerThread(Socket socket, ServerThread serverThread) {
        try {
            this.socket = socket;
            this.ous = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());
            this.serverThread = serverThread;
            this.setGameOver(false);
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
    	
    	while(!this.isGameOver()) {
    		try {
				Message message = (Message) this.ois.readObject();
				//
				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    	}
    }
    
    public void send(Message message) {
        try {
            this.ous.writeObject(message);
            this.ous.flush();
        } catch (IOException e) {
            //
        }
    }
    
    public String getUsername() {
    	return this.username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public Socket getSocket() {
    	return this.socket;
    }

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}
