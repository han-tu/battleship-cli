package app.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import app.game.Message;

public class WorkerThread extends Thread {
	private Socket socket;
	private ObjectOutputStream ous;
	private ObjectInputStream ois;
	private RoomThread rt;
	private String username;
	private boolean ready;
	private boolean gameOver;

	public WorkerThread(Socket socket, RoomThread roomThread) {
		try {
			this.socket = socket;
			this.ous = new ObjectOutputStream(this.socket.getOutputStream());
			this.ois = new ObjectInputStream(this.socket.getInputStream());
			this.rt = roomThread;
			this.setGameOver(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			Message firstMessage = (Message) this.ois.readObject();
			this.setUsername(firstMessage.getText());
			rt.createPlayer(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (!this.isGameOver()) {
			try {
				Message message = (Message) this.ois.readObject();

				// Handling Room yang belum penuh

				if (rt.getPlayerCount() == 2) {

					String reqType = message.getRequest().split(" ")[0];
					if (reqType.equals("send-message")) {
						Message newMessage = rt.createMessage(message.getRequest().split(" ")[1], this.username,
								rt.getOpponentName(this.username));
						newMessage.setRequest("Message");
						rt.sendMessage(newMessage);
					} else if (reqType.equals("see")) {
						if (message.getRequest().split(" ")[1].equals("-mb")) {
							String board = rt.getBoard(this.username).toString();
							Message newMessage = rt.createMessage(board, "Server", this.username);
							newMessage.setRequest("");
							rt.sendMessage(newMessage);
						} else if (message.getRequest().split(" ")[1].equals("-ob")) {
							String board = rt.getBoard(rt.getOpponentName(this.username)).toString();
							Message newMessage = rt.createMessage(board, "Server", this.username);
							newMessage.setRequest("");
							rt.sendMessage(newMessage);
						}
					} else if (reqType.equals("fire")) {
						String tile = message.getRequest().split(" ")[1];
						rt.attack(this.username, tile);
					}
				} else {
					Message newMessage = rt.createMessage("Waiting for opponent", "Server", this.username);
					newMessage.setRequest("");
					rt.sendMessage(newMessage);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Player \"" + this.username + "\" has been disconnected");
				break;
			}
    		
    	}
    }
    
    public String parseMessageText(String req) {
    	int i = 0;
    	String message = "";
    	for (String token : req.split(" ")) {
    		if (i != 0) {
    			message += token + " ";
    		}
    		i++;
    	}
    	return message;
    }
    
    public void send(Message message) {
        try {
            this.ous.writeObject(message);
            this.ous.flush();
        } catch (IOException e) {
            // Code Here
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

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
