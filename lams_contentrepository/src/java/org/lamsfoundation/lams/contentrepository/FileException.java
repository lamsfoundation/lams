package org.lamsfoundation.lams.contentrepository;
/**
 * Some error was generated reading or writing the files
 * to disk.
 */
public class FileException extends RepositoryCheckedException {
	   /**
	* Constructs a new instance of this class.
	*/
    public FileException() {
    	this("File error occured.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public FileException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public FileException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public FileException(Throwable cause) {
		this("File error occured.", cause);
	   }
	
	
}
