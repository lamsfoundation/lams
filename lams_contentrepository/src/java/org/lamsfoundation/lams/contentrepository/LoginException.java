package org.lamsfoundation.lams.contentrepository;
/**
 * Login failed.
 */
public class LoginException extends RepositoryCheckedException {
	   /**
	* Constructs a new instance of this class.
	*/
    public LoginException() {
    	this("Content Repository Login Failed.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public LoginException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public LoginException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public LoginException(Throwable cause) {
		this("Content Repository Login Failed.", cause);
	   }
	
	
}
