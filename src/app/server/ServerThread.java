package app.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class ServerThread extends Thread {
	private Set<RoomThread> roomList;
	private ServerSocket server;
	
	public ServerThread() {
        try {
            this.roomList = new HashSet<RoomThread>();
            this.server = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void run() {
    	System.out.println("Server Alive");
    	
        Socket socket;
        RoomThread rt = null;
    	
        // listen for a new connection
        while(true) {
            try {
                // accept a new connection
                socket = this.server.accept();
                
                if (rt != null && rt.getPlayerCount() != 2) {
                	rt.setPlayer2(new WorkerThread(socket, this));
                }
                else {                	
                	rt = new RoomThread(new WorkerThread(socket, this), this);
                	rt.start();
                	this.roomList.add(rt);
                	System.out.println("New Room Created");
                }

                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	
}
