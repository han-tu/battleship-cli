package app.testing;

import app.exceptions.ShipNotValidException;
import app.game.Board;
import app.game.Ship;

public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		try {
//			Ship myShip = new Ship(2, "A2", "B3");
//		} catch (ShipNotValidException e) {
//			System.out.println(e.getMessage());
//		}
		Board board = new Board();
		board.setBoardConditionAt("A5", "H");
		board.setBoardConditionAt("B5", "H");
		board.setBoardConditionAt("C5", "H");
		board.setBoardConditionAt("A6", "M");
		System.out.println(board.toString());
		System.out.println("end");
	}

}
