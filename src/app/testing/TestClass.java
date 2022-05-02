package app.testing;

import app.exceptions.ShipNotValidException;
import app.game.Ship;

public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Ship myShip = new Ship(2, "A2", "B3");
		} catch (ShipNotValidException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("end");
	}

}
