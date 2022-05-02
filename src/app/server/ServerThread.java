package app.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServerThread extends Thread {
	private Hashtable<String, RoomThread> roomList;
	private ServerSocket server;
	
	public ServerThread() {
        try {
            this.roomList = new Hashtable<String, RoomThread>();
            this.server = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void run() {
    	System.out.println("Server Alive");
    	
        Socket socket;
        RoomThread rt;
    	
        // listen for a new connection
        while(true) {
            try {
                // accept a new connection
                socket = this.server.accept();
                
                if (rt.getPlayerCount() != 2) {
                	rt.setPlayer2(socket);
                }
                 // create a new WorkerThread
                rt = new RoomThread(socket, this);

                // start the new thread
                rt.start();

                // store the new thread to the hash table
                String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                
                System.out.println("Connection Established with " + clientId);
                
                this.clientList.put(clientId, wt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	
}
