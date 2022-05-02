package app.server;

import java.net.Socket;

public class RoomThread extends Thread {
	private Socket toPlayer1;
	private Socket toPlayer2;
	
	private int playerCount;
	private boolean gameOver;
	private String winner;
	
	
}
