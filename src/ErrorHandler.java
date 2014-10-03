//David Pendergast
//dlp75@case.edu
//ErrorHandler contains static methods used to check for and throw NullPointerExceptions and UninitializedObjectExceptions

public class ErrorHandler {
	public static void checkIfNull(Object obj) throws NullPointerException{
		if(obj == null){
			throw new NullPointerException();
		}
	}
	
	public static void checkIfValid(boolean isValid,String message, Throwable cause) throws UninitializedObjectException{
		if(!isValid){
			throw new UninitializedObjectException(message,cause);
		}
	}
	public static void checkIfValid(boolean isValid, String message) throws UninitializedObjectException{
		if(!isValid){
			throw new UninitializedObjectException(message);
		}
	}
	public static void checkIfValid(boolean isValid, Throwable cause) throws UninitializedObjectException{
		if(!isValid){
			throw new UninitializedObjectException(cause);
		}
	}
	public static void checkIfValid(boolean isValid) throws UninitializedObjectException{
		if(!isValid){
			throw new UninitializedObjectException();
		}
	}

}
