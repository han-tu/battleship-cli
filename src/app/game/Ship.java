package app.game;

import java.util.Enumeration;
import java.util.Hashtable;
import app.exceptions.ShipNotValidException;

public class Ship {
	private int size;
	private String shipPositionType;
	private Hashtable <String, String> shipCondition;
	private boolean totallyDestroyed;
	
	public Ship(int size, String start, String end) throws ShipNotValidException {
		this.size = size;
		this.totallyDestroyed = false;
		
		if (size < 2 || size > 5)
			throw new ShipNotValidException("Ship size doesn't valid");
		
		if (isValid(start, end)) {
			this.shipCondition = new Hashtable<String, String>();
			arrangeShipToBoard(start);
		}
	}
	
	public void checkCondition() {
		Enumeration<String> tiles = this.shipCondition.keys();
		int destroyed = 0;
		while (tiles.hasMoreElements()) {
			String tile = tiles.nextElement();
			if (shipCondition.get(tile).equals("X")) {
				destroyed++;
			}
		}
		if (destroyed == this.size) {
			this.setTotallyDestroyed(true);
		}
	}
	
	private void arrangeShipToBoard(String start) {
		if (this.shipPositionType.equals("horizontal")) {
			for (int i = 0 ; i < this.size ; i++) {
				char posY = (char) (getVertical(start));
				shipCondition.put(posY + Integer.toString(getHorizontal(start) + i), "O");
			}
		}
		else if (this.shipPositionType.equals("vertical")) {
			for (int i = 0 ; i < this.size ; i++) {
				char posY = (char) (getVertical(start) + i);
				shipCondition.put(posY + Integer.toString(getHorizontal(start)), "O");
			}
		}
	}
	
	private boolean isValid(String start, String end) throws ShipNotValidException {
		int start_horizontal = getHorizontal(start);
		int start_vertical = getVertical(start);
		int end_horizontal = getHorizontal(end);
		int end_vertical = getVertical(end);
		
		if (start_horizontal == end_horizontal && Math.abs(end_vertical-start_vertical) + 1 == this.size) {
			this.shipPositionType = "vertical";
			return true;
		}
		
		if (start_vertical == end_vertical && Math.abs(end_horizontal-start_horizontal) + 1 == this.size) {			
			this.shipPositionType = "horizontal";
			return true;
		}
		
		throw new ShipNotValidException("Ship position is not valid");
		
	}
	
	public boolean isHitShip(String checkPosition) {
		// iterate through all players
    	Enumeration<String> positions = this.shipCondition.keys();
        while (positions.hasMoreElements()) {
            String position = positions.nextElement();

            if (position.equals(checkPosition)) {
            	return true;
            }
        }
        return false;
	}
	
	private int getHorizontal(String pos) {
		return Integer.parseInt(pos.substring(1));
	}
	
	private int getVertical(String pos) {
		return pos.charAt(0);
	}

	public boolean isTotallyDestroyed() {
		return totallyDestroyed;
	}

	public void setTotallyDestroyed(boolean totallyDestroyed) {
		this.totallyDestroyed = totallyDestroyed;
	}

	public int getSize() {
		return size;
	}

	public String getShipPositionType() {
		return shipPositionType;
	}

	public void setShipCondition(String pos, String cond) {
		this.shipCondition.replace(pos, cond);
	}
	
	public String getConditionAt(String pos) {
		return this.shipCondition.get(pos);
	}

	public boolean isOutOfBoard(String start, String end) {
		if (getHorizontal(start) < 0 || getHorizontal(start) > 9 || getVertical(start) < 65 || getVertical(start) > 74 || getHorizontal(end) < 0 || getHorizontal(end) > 9 || getVertical(end) < 65 || getVertical(end) > 74) {
			return true;
		}
		return false;
	}
	
	public boolean isCrashed(String start, String end) {
		int start_horizontal = getHorizontal(start);
		int start_vertical = getVertical(start);
		int end_horizontal = getHorizontal(end);
		int end_vertical = getVertical(end);
		
		String positionType = "";
		if (start_horizontal == end_horizontal && Math.abs(end_vertical-start_vertical) + 1 == this.size) {
			positionType = "vertical";
		}
		
		if (start_vertical == end_vertical && Math.abs(end_horizontal-start_horizontal) + 1 == this.size) {			
			positionType = "horizontal";
		}
		
		Enumeration<String> tiles = this.getShipCondition().keys();
		while (tiles.hasMoreElements()) {
			String tile = tiles.nextElement();
			int v_tile = this.getVertical(tile);
			int h_tile = this.getHorizontal(tile);
			
			if (positionType.equals("vertical")) {
				if (h_tile == start_horizontal && 
						((v_tile <= start_vertical && v_tile >= end_vertical) || 
								(v_tile >= start_vertical && v_tile <= end_vertical))) {
					return true;
				}
			}
			else if (positionType.equals("horizontal")) {
				if (v_tile == start_vertical && 
						((h_tile <= start_horizontal && h_tile >= end_horizontal) || 
								(h_tile >= start_horizontal && h_tile <= end_horizontal))) {
					return true;
				}
			}
			
		}
		return false;
	}

	public Hashtable<String, String> getShipCondition() {
		return shipCondition;
	}
}
