package exceptions;

@SuppressWarnings("serial")
public class MissingGroupDefinitionException extends Exception{
	
	public MissingGroupDefinitionException(String message) {
		super(message);
	}

	public MissingGroupDefinitionException(String message, Throwable cause) {
		super(message,cause);
	}

}	
