package exceptions;

@SuppressWarnings("serial")
public class UndefinedGroupException extends Exception{
	
	public UndefinedGroupException(String message) {
		super(message);
	}

	public UndefinedGroupException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
