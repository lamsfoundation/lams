package org.lamsfoundation.lams.contentrepository;
/**
 * Thrown when value is assigned to a propery of the wrong type, or some 
 * other formatting type problem.
 */
public class ValueFormatException extends RepositoryCheckedException {
	   /**
	* Constructs a new instance of this class.
	*/
    public ValueFormatException() {
    	this("Value assigned to wrong type or other formatting error.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public ValueFormatException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public ValueFormatException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public ValueFormatException(Throwable cause) {
		this("Value assigned to wrong type or other formatting error.", cause);
	   }
	
	
}
