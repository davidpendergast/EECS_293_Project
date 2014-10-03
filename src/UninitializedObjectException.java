//David Pendergast
//dlp75@case.edu
//Error thrown by uninitialized Users and Links when certain methods are called on them.

public class UninitializedObjectException extends Exception {
	public UninitializedObjectException(){
		super();
	}
	
	public UninitializedObjectException(String message){
		super(message);
	}
	
	public UninitializedObjectException(String message, Throwable cause){
		super(message,cause);
	}
	
	public UninitializedObjectException(Throwable cause){
		super(cause);
	}
}
