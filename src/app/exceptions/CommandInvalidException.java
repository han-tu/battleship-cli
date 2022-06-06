package app.exceptions;

@SuppressWarnings("serial")
public class CommandInvalidException extends Exception {
	public CommandInvalidException(String message) {
		super(message);
	}
}
