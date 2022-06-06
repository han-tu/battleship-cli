package app.server;

import java.net.Socket;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import app.exceptions.ShipNotValidException;
import app.game.Board;
import app.game.Message;
import app.game.Ship;

public class RoomThread extends Thread {
	private Hashtable<String, WorkerThread> players;
	private Hashtable<String, Board> boards;
	private Hashtable<String, Set<Ship>> ships;
	private int playerCount;
	private boolean gameOver;
	private String winner;
	private Socket socket;
	
	public RoomThread(Socket socket) {
		this.players = new Hashtable<String, WorkerThread>();
		this.boards = new Hashtable<String, Board>();
		this.ships = new Hashtable<String, Set<Ship>>();
		
		this.socket = socket;
	}
	
	public void run() {
		System.out.println("Buat Room");
		WorkerThread newWt = new WorkerThread(this.socket, this);
		newWt.start();
		while (true) {
			
		}
	}
	
	public void attack(String attacker, String pos) {
		
		Board enemyBoard = null ;
		Set<Ship> enemyShips = null;
		String enemy = null;
		
		// iterate through all players
    	Enumeration<String> usernames = this.players.keys();
        while (usernames.hasMoreElements()) {
            String username = usernames.nextElement();

            WorkerThread wt = this.players.get(username);

            // get board
            if(!wt.getUsername().equals(attacker)) {
            	enemy = wt.getUsername();
            	enemyBoard = this.getBoard(wt.getUsername());
            	enemyShips = this.getShips(wt.getUsername());
            	break;
            }
        }

        Iterator<Ship> shipIterator = enemyShips.iterator();
        while (shipIterator.hasNext()) {
        	Ship ship = shipIterator.next();
        	
        	if (ship.isHitShip(pos)) {
        		ship.setShipCondition(pos, "X");
        		enemyBoard.setBoardConditionAt(pos, "H");
        		sendMessage(createMessage("Your ship at " + pos + " has been hitted", "Server", enemy));
        		sendMessage(createMessage("Your missile hits the enemy ship", "Server", attacker));
        		return;
        	}
        }
        
        enemyBoard.setBoardConditionAt(pos, "M");
        sendMessage(createMessage("Enemy missile missed", "Server", enemy));
        sendMessage(createMessage("Your missile missed", "Server", attacker));
        
	}
	
	public void addShip(String owner, int size, String start, String end) {
		try {
			Ship newShip = new Ship(size, start, end);
			this.ships.get(owner).add(newShip);
		} catch (ShipNotValidException e) {
			sendMessage(createMessage(e.getMessage(), "Server", owner));
		}
	}
	
	public void sendMessage(Message message) {
        
		// iterate through all players
    	Enumeration<String> usernames = this.players.keys();
        while (usernames.hasMoreElements()) {
            String username = usernames.nextElement();

            WorkerThread wt = this.players.get(username);

            // send the message to specified username
            if(wt.getUsername().equals(message.getReceiver())) {
            	wt.send(message);
            	break;
            }
        }
    }
	
	public String getOpponentName(String myUsername) {
		// iterate through all players
    	Enumeration<String> usernames = this.players.keys();
        while (usernames.hasMoreElements()) {
            String username = usernames.nextElement();

            WorkerThread wt = this.players.get(username);

            // send the message to specified username
            if(!wt.getUsername().equals(myUsername)) {
            	return wt.getUsername();
            }
        }
        return null;
	}
	
	public void createPlayer(WorkerThread player) {
		players.put(player.getUsername(), player);
		boards.put(player.getUsername(), new Board());
		ships.put(player.getUsername(), new HashSet<Ship>());
		this.playerCount += 1;
		System.out.println("Player with username: " + player.getUsername() + " has been joined");
	}
	
	public Set<Ship> getShips(String username) {
		return this.ships.get(username);
	}
	
	public Board getBoard(String username) {
		return this.boards.get(username);
	}
	
	public boolean isGameOver() {
		return this.gameOver;
	}

	public int getPlayerCount() {
		return playerCount;
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
	
	public Message createMessage(String message, String sender, String receiver) {
		Message newMessage = new Message();
		newMessage.setText(message);
		newMessage.setSender(sender);
		newMessage.setReceiver(receiver);
		return newMessage;
	}
}
