package app.exceptions;

@SuppressWarnings("serial")
public class ShipNotValidException extends Exception {
	public ShipNotValidException(String message) {
		super(message);
	}
}
