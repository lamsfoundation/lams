package org.lamsfoundation.lams.contentrepository;
/**
 * An item already exists with the given id or path.
 */
public class ItemExistsException extends RepositoryCheckedException {
	   /**
	* Constructs a new instance of this class.
	*/
    public ItemExistsException() {
    	this("An item already exists with the given id or path.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public ItemExistsException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public ItemExistsException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public ItemExistsException(Throwable cause) {
		this("An item already exists with the given id or path.", cause);
	   }
	
	
}
