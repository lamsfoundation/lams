package org.lamsfoundation.lams.contentrepository;
/**
 * Tried to create a node with an unknown type.
 */
public class NoSuchNodeTypeException extends RepositoryCheckedException {
  /**
	* Constructs a new instance of this class.
	*/
    public NoSuchNodeTypeException() {
    	this("Node Type unknown or not allowed for this action.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public NoSuchNodeTypeException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public NoSuchNodeTypeException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public NoSuchNodeTypeException(Throwable cause) {
		this("Node Type unknown or not allowed for this action.", cause);
	   }
	
	
}
