package org.lamsfoundation.lams.contentrepository;
/**
 * Requested workspace not found.
 */
public class WorkspaceNotFoundException extends RepositoryCheckedException {

    /**
	* Constructs a new instance of this class.
	*/
    public WorkspaceNotFoundException() {
    	this("Requested workspace not found.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public WorkspaceNotFoundException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public WorkspaceNotFoundException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public WorkspaceNotFoundException(Throwable cause) {
		this("Requested workspace not found.", cause);
	   }

}
