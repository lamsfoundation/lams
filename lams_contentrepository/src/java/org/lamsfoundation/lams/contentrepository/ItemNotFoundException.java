package org.lamsfoundation.lams.contentrepository;

/**
 * Item requested does not exist.
 */
public class ItemNotFoundException extends RepositoryCheckedException {

  /**
	* Constructs a new instance of this class.
	*/
    public ItemNotFoundException() {
    	this("Item requested does not exist.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public ItemNotFoundException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public ItemNotFoundException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public ItemNotFoundException(Throwable cause) {
		this("Item requested does not exist.", cause);
	   }
	
	
}
