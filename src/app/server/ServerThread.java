package app.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
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
                
                // Disini kita bikin player nya
                if (rt != null && rt.getPlayerCount() != 2) {
                	rt.createPlayer(new WorkerThread(socket, rt));
                }
                // DIsini kita bikin room nya
                else {                	
                	rt = new RoomThread(socket);
                	rt.start();
//                	rt.createPlayer(new WorkerThread(socket, rt));
                	this.roomList.add(rt);
                	System.out.println("New Room Created");
                }

                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	
}
