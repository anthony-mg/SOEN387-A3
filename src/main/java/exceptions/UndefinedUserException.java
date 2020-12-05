package exceptions;

@SuppressWarnings("serial")
public class UndefinedUserException extends Exception{

	public UndefinedUserException(String message) {
		super(message);
	}

	public UndefinedUserException(String message, Throwable cause) {
		super(message, cause);
	}
}
