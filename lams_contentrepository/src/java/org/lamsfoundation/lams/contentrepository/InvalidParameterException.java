package org.lamsfoundation.lams.contentrepository;
/**
 * Generic exception thrown whenever a parameter is missing.
 */
public class InvalidParameterException extends RepositoryCheckedException {
	/**
	* Constructs a new instance of this class.
	*/
    public InvalidParameterException() {
    	this("A required parameter is null.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public InvalidParameterException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public InvalidParameterException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public InvalidParameterException(Throwable cause) {
		this("A required parameter is null.", cause);
	   }
	
	
}
