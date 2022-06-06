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
        		enemyBoard.setBoardConditionAt(pos, "S");
        		sendMessage(createMessage("Your ship at " + pos + " has been hitted", "Server", enemy, ""));
        		sendMessage(createMessage("Your missile hits the enemy ship", "Server", attacker, ""));
        		return;
        	}
        }
        
        enemyBoard.setBoardConditionAt(pos, "M");
        sendMessage(createMessage("Enemy missile missed", "Server", enemy, ""));
        sendMessage(createMessage("Your missile missed", "Server", attacker, ""));
        
	}
	
	public void addShip(String owner, int size, String start, String end) {
		try {
			Ship newShip = new Ship(size, start, end);
			if (!this.isShipMissplaced(owner, newShip, start, end) && 
					this.isShipAvailable(owner, size) &&
					!this.isShipCrashed(owner, start, end)) {
				this.ships.get(owner).add(newShip);
			}
			Board myBoard = this.getBoard(owner);
			updateBoard(myBoard, newShip);
		} catch (ShipNotValidException e) {
			sendMessage(createMessage(e.getMessage(), "Server", owner, ""));
		}
	}
	
	public void updateBoard(Board board, Ship ship) {
		
		Enumeration<String> tiles = ship.getShipCondition().keys();
		while (tiles.hasMoreElements()) {
			String tile = tiles.nextElement();
			board.setBoardConditionAt(tile, "O");
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
	
	public Message createMessage(String message, String sender, String receiver, String req) {
		Message newMessage = new Message();
		newMessage.setText(message);
		newMessage.setSender(sender);
		newMessage.setReceiver(receiver);
		newMessage.setRequest(req);
		return newMessage;
	}
	
	public boolean isShipCrashed(String username, String start, String end) {
		
		Set<Ship> ships = this.ships.get(username);
		Iterator<Ship> shipIterator = ships.iterator();
		while (shipIterator.hasNext()) {
			Ship ship = shipIterator.next();
			
			if (ship.isCrashed(start, end)) {
				return true;
			}
			
		}
		return false;
	}
	
	public boolean isShipPlaced(String username) {
		int s = 0, ms = 0, mb = 0, b = 0;

		Set<Ship> ships = this.ships.get(username);
		Iterator<Ship> shipIterator = ships.iterator();
		while (shipIterator.hasNext()) {
			Ship ship = shipIterator.next();
			if (ship.getSize() == 5) {
				b += 1;
			} else if (ship.getSize() == 4) {
				mb += 1;
			} else if (ship.getSize() == 3) {
				ms += 1;
			} else if (ship.getSize() == 2) {
				s += 1;
			}
		}

		if (b == 1 && mb == 1 && ms == 2 && s == 1) {
			return true;
		} 
		return false;

	}
	
	public boolean isShipAvailable(String username, int size) throws ShipNotValidException{
		int s = 0, ms = 0, mb = 0, b = 0;

		Set<Ship> ships = this.ships.get(username);
		Iterator<Ship> shipIterator = ships.iterator();
		while (shipIterator.hasNext()) {
			Ship ship = shipIterator.next();
			if (ship.getSize() == 5) {
				b += 1;
			} else if (ship.getSize() == 4) {
				mb += 1;
			} else if (ship.getSize() == 3) {
				ms += 1;
			} else if (ship.getSize() == 2) {
				s += 1;
			}
		}
		
		if (size == 5 && b < 1) {
			return true;
		} else if (size == 4 && mb < 1) {
			return true;
		} else if (size == 3 && ms <2) {
			return true;
		} else if (size == 2 && s <1) {
			return true;
		} 
		throw new ShipNotValidException("This ship type has reached maximum amount allowed");
	}

	public boolean isShipMissplaced(String owner, Ship newShip, String start, String end) throws ShipNotValidException {
		if (newShip.isOutOfBoard(start, end)) {
			throw new ShipNotValidException("Ship is out of board");
		}
		return false;
	}
}
