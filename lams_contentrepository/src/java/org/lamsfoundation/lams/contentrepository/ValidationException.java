package org.lamsfoundation.lams.contentrepository;
/**
 * The node is invalid for some reason. The reason should be given in
 * the exception message.
 */
public class ValidationException extends RepositoryCheckedException {
   /**
	* Constructs a new instance of this class.
	*/
	public ValidationException() {
		this("The node is invalid.");
	}

	/**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
		public ValidationException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public ValidationException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public ValidationException(Throwable cause) {
		this("The node is invalid.", cause);
	   }
	
	

}
