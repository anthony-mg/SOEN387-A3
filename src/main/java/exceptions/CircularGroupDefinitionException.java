package exceptions;

@SuppressWarnings("serial")
public class CircularGroupDefinitionException extends Exception {
	
	public CircularGroupDefinitionException(String message) {
		super(message);
	}

	public CircularGroupDefinitionException(String message, Throwable cause) {
		super(message,cause);
	}
}
	
