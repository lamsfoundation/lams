package org.lamsfoundation.lams.contentrepository;
/**
 * The current ticket doesn't have sufficient rights for the requested action.
 */
public class AccessDeniedException extends RepositoryCheckedException {
	   /**
	* Constructs a new instance of this class.
	*/
    public AccessDeniedException() {
    	this("The current ticket doesn't have sufficient rights for the requested action.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public AccessDeniedException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public AccessDeniedException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public AccessDeniedException(Throwable cause) {
		this("The current ticket doesn't have sufficient rights for the requested action.", cause);
	   }
	
	
}
