package app.server;

import java.net.Socket;

public class RoomThread extends Thread {
	private WorkerThread player1;
	private WorkerThread player2;
	private ServerThread serverThread;
	private int playerCount;
	private boolean gameOver;
	private String winner;
	
	public RoomThread(WorkerThread player1, ServerThread serverThread) {
		this.player1 = player1;
		this.serverThread = serverThread;
	}
	
	public boolean isGameOver() {
		return this.gameOver;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void setPlayer2(WorkerThread player2) {
		this.player2 = player2;
	}
}
